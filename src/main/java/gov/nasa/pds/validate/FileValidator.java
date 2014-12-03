// Copyright 2006-2014, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.ValidatorException;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.validate.inventory.reader.InventoryReaderException;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.validate.schema.SchemaValidator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.tinytree.TinyNodeImpl;

import org.xml.sax.SAXException;

/**
 * Class that validates a single file.
 *
 * @author mcayanan
 *
 */
public class FileValidator extends Validator {
  private SchemaValidator schemaValidator;
  private Map<URL, ExceptionContainer> labelSchemaResults;
  private Map<URL, ExceptionContainer> labelSchematronResults;
  private Map<URL, Transformer> labelSchematrons;

  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report object to output the results.
   * @throws ParserConfigurationException
   * @throws ValidatorException
   * @throws TransformerConfigurationException
   */
  public FileValidator(String modelVersion, Report report)
      throws ParserConfigurationException, ValidatorException,
      TransformerConfigurationException {
    super(modelVersion, report);
    schemaValidator = new SchemaValidator(cachedEntityResolver);
    labelSchemaResults = new HashMap<URL, ExceptionContainer>();
    labelSchematronResults = new HashMap<URL, ExceptionContainer>();
    labelSchematrons = new HashMap<URL, Transformer>();
  }

  /**
   * Validate a PDS product file.
   *
   * @param file A PDS product file.
   *
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   * @throws XPathExpressionException
   * @throws TransformerException
   * @throws InventoryReaderException
   *
   */
  public void validate(File file) throws Exception {
    validate(file.toURI().toURL());
  }

  @Override
  public void validate(URL url) throws Exception {
    ExceptionContainer exceptionContainer = new ExceptionContainer();
    if (force) {
      // Validate the label's schema and schematron first before doing
      // label validation
      boolean hasValidSchemas = validateLabelSchemas(url,
          exceptionContainer);

      Map<String, Transformer> labelSchematrons = validateLabelSchematrons(
          url, exceptionContainer);

      if (hasValidSchemas && !labelSchematrons.isEmpty()) {
        labelValidator.setCachedEntityResolver(cachedEntityResolver);
        labelValidator.setLabelSchematrons(labelSchematrons);
        labelValidator.validate(exceptionContainer, url);
        report.record(url.toURI(), exceptionContainer.getExceptions());
      } else {
        // Print any label problems that occurred during schema and schematron
        // validation.
        if (exceptionContainer.getExceptions().size() != 0) {
          report.record(url.toURI(), exceptionContainer.getExceptions());
        }
      }
    } else {
      labelValidator.validate(exceptionContainer, url);
      report.record(url.toURI(), exceptionContainer.getExceptions());
    }
  }

  private boolean validateLabelSchemas(URL label,
      ExceptionContainer labelProblems) {
    boolean passFlag = true;
    List<URL> schemaUrls = new ArrayList<URL>();
    String value = "";
    try {
      XMLExtractor extractor = new XMLExtractor(label);
      value = extractor.getSchemaLocation();
    } catch (Exception e) {
      labelProblems.addException(new LabelException(ExceptionType.FATAL,
          "Error occurred while attempting to find schemas using the XPath '"
          + XMLExtractor.SCHEMA_LOCATION_XPATH + "': " + e.getMessage(),
          label.toString()));
      return false;
    }
    if (value == null || (value != null && value.isEmpty())) {
      labelProblems.addException(new LabelException(ExceptionType.ERROR,
          "No schema(s) found in the label.", label.toString()));
      return false;
    } else {
      StringTokenizer tokenizer = new StringTokenizer(value);
      if ((tokenizer.countTokens() % 2) != 0) {
        labelProblems.addException(new LabelException(ExceptionType.ERROR,
            "schemaLocation value does not appear to have matching sets of "
            + "namespaces to uris: '" + value + "'",
            label.toString()));
        return false;
      } else {
        // While loop that will grab the schema URIs
        while (tokenizer.hasMoreTokens()) {
          // First token assumed to be the namespace
          String token = tokenizer.nextToken();
          // Second token assumed to be the URI
          token = tokenizer.nextToken();
          try {
            schemaUrls.add(new URL(token));
          } catch (MalformedURLException mu) {
            // The schema specification value does not appear to be
            // a URL. Assume a local reference to the schematron and
            // attempt to resolve it.
            try {
              URL parent = label.toURI().resolve(".").toURL();
              schemaUrls.add(new URL(parent, value));
            } catch (MalformedURLException mue) {
              labelProblems.addException(new LabelException(ExceptionType.ERROR,
                  "Cannot resolve schema specification '"
                      + value + "': " + mue.getMessage(),
                      label.toString()));
              passFlag = false;
            } catch (URISyntaxException e) {
              //Ignore
            }
          }
        }
      }
    }
    if (labelProblems.getExceptions().size() != 0) {
      passFlag = false;
    } else {
      for (URL schemaUrl : schemaUrls) {
        try {
          ExceptionContainer container = null;
          if (labelSchemaResults.containsKey(schemaUrl)) {
            container = labelSchemaResults.get(schemaUrl);
            if (container.getExceptions().size() != 0) {
              report.record(schemaUrl.toURI(), container.getExceptions());
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            }
          } else {
            container = schemaValidator.validate(schemaUrl);
            if (container.getExceptions().size() != 0) {
              report.record(schemaUrl.toURI(), container.getExceptions());
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            }
            labelSchemaResults.put(schemaUrl, container);
          }
        } catch (URISyntaxException u) {
          labelProblems.addException(new LabelException(ExceptionType.FATAL,
              "URI syntax exception occurred for schema '"
              + schemaUrl.toString() + "': " + u.getMessage(),
              label.toString()));
        }
      }
    }
    return passFlag;
  }

  private Map<String, Transformer> validateLabelSchematrons(URL label,
      ExceptionContainer labelProblems) {
    boolean passFlag = true;
    Map<String, Transformer> results = new HashMap<String, Transformer>();
    List<TinyNodeImpl> xmlModels = new ArrayList<TinyNodeImpl>();
    try {
      XMLExtractor extractor = new XMLExtractor(label);
      xmlModels = extractor.getNodesFromDoc(XMLExtractor.XML_MODEL_XPATH);
    } catch (Exception e) {
      labelProblems.addException(new LabelException(ExceptionType.FATAL,
          "Error occurred while attempting to find schematrons using the XPath '"
          + XMLExtractor.XML_MODEL_XPATH + "': " + e.getMessage(),
          label.toString()));
      // results should be empty
      return results;
    }
    Pattern pattern = Pattern.compile(
        "href=\\\"([^=]*)\\\"( schematypens=\\\"([^=]*)\\\")?"
        );
    List<URL> schematronRefs = new ArrayList<URL>();
    for (TinyNodeImpl xmlModel : xmlModels) {
      String filteredData = xmlModel.getStringValue().replaceAll("\\s+", " ");
      filteredData = filteredData.trim();
      Matcher matcher = pattern.matcher(filteredData);
      if (matcher.matches()) {
        String value = matcher.group(1).trim();
        URL schematronRef = null;
        try {
          schematronRef = new URL(value);
        } catch (MalformedURLException ue) {
          // The schematron specification value does not appear to be
          // a URL. Assume a local reference to the schematron and
          // attempt to resolve it.
          try {
            URL parent = label.toURI().resolve(".").toURL();
            schematronRef = new URL(parent, value);
          } catch (MalformedURLException mue) {
            labelProblems.addException(new LabelException(ExceptionType.ERROR,
                "Cannot resolve schematron specification '"
                    + value + "': " + mue.getMessage(),
                    label.toString(),
                    label.toString(),
                    new Integer(xmlModel.getLineNumber()),
                    null));
            passFlag = false;
            continue;
          } catch (URISyntaxException e) {
            //Ignore
          }
        }
        schematronRefs.add(schematronRef);
      }
    }
    if (schematronRefs.isEmpty()) {
      labelProblems.addException(new LabelException(ExceptionType.ERROR,
          "No schematrons specified in the label", label.toString()));
    }

    //Now validate the schematrons
    for (URL schematronRef : schematronRefs) {
      try {
        ExceptionContainer container = null;
        if (labelSchematrons.containsKey(schematronRef)) {
          container = labelSchematronResults.get(schematronRef);
          if (container.getExceptions().size() != 0) {
            report.record(schematronRef.toURI(), container.getExceptions());
            if (container.hasError() || container.hasFatal()) {
              passFlag = false;
            }
          } else {
            results.put(schematronRef.toString(),
                labelSchematrons.get(schematronRef));
          }
        } else {
          container = new ExceptionContainer();
          try {
            Transformer transformer = schematronTransformer.transform(
                schematronRef, container);
            labelSchematrons.put(schematronRef, transformer);
            results.put(schematronRef.toString(), transformer);
          } catch (TransformerException te) {
            //Ignore as the listener handles the exceptions and puts it into
            //the container
          } catch (Exception e) {
            container.addException(new LabelException(ExceptionType.FATAL,
                "Error occurred while attempting to read schematron: "
                + e.getMessage(), schematronRef.toString()));
          }
          if (container.getExceptions().size() != 0) {
            report.record(schematronRef.toURI(), container.getExceptions());
            if (container.hasError() || container.hasFatal()) {
              passFlag = false;
            }
          }
          labelSchematronResults.put(schematronRef, container);
        }
      } catch (URISyntaxException u) {
        labelProblems.addException(new LabelException(ExceptionType.FATAL,
            "URI syntax exception occurred for schematron '"
            + schematronRef.toString() + "': " + u.getMessage(),
            label.toString()));
      }
    }
    if (!passFlag) {
      results.clear();
    }
    return results;
  }
}
