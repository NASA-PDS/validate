// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.label.CachedEntityResolver;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.tools.label.MissingLabelSchemaException;
import gov.nasa.pds.tools.label.SchematronTransformer;
import gov.nasa.pds.tools.label.XMLCatalogResolver;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemContainer;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationResourceManager;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import net.sf.saxon.tree.tiny.TinyNodeImpl;
import net.sf.saxon.trans.XPathException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Implements a validation chain that validates PDS4 bundles. It is applicable
 * if there is a bundle label in the root directory.
 */
public class LabelValidationRule extends AbstractValidationRule {

	private static final Pattern LABEL_PATTERN = Pattern.compile(".*\\.xml", 
	    Pattern.CASE_INSENSITIVE);

	private static final String XML_SUFFIX = ".xml";
	
	private SchemaValidator schemaValidator;
	private SchematronTransformer schematronTransformer;
  private Map<URL, ProblemContainer> labelSchemaResults;
  private Map<URL, ProblemContainer> labelSchematronResults;
  private Map<URL, Transformer> labelSchematrons;
  private XMLExtractor extractor;

  private static Object lock = new Object();

  public LabelValidationRule() throws TransformerConfigurationException {
    schemaValidator = new SchemaValidator();
    schematronTransformer = new SchematronTransformer();
    labelSchemaResults = new HashMap<URL, ProblemContainer>();
    labelSchematronResults = new HashMap<URL, ProblemContainer>();
    labelSchematrons = new HashMap<URL, Transformer>();
    extractor = null;
  }
  
	@Override
	public boolean isApplicable(String location) {
    if (Utility.isDir(location)) {
      return false;
    } else {
      return true;
    }
	}

	/**
	 * Implements a rule that checks the label file extension.
	 */
	@ValidationTest
	public void checkLabelExtension() {
	  if (!FilenameUtils.getName(getTarget().toString()).endsWith(XML_SUFFIX)) {
		  reportError(PDS4Problems.INVALID_LABEL_EXTENSION, getTarget(), -1, -1);
    }
	}

	/**
	 * Parses the label and records any errors resulting from the parse,
	 * including schema and schematron errors.
	 */
	@ValidationTest
	public void validateLabel() {
      long t0 = System.currentTimeMillis();
      URL target = getTarget();
      String targetFileName = target.toString().substring(target.toString().lastIndexOf("/") + 1);
      ProblemProcessor processor = new ProblemProcessor(
              getListener(), target);

      LabelValidator validator =
              ValidationResourceManager.INSTANCE.getResource(LabelValidator.class);
      try {
        Document document = null;
        boolean pass = true;
        ProblemContainer problemContainer = new ProblemContainer();
        if (getContext().getCatalogResolver() != null ||
                getContext().isForceLabelSchemaValidation()) {
          boolean hasValidSchemas = false;
          Map<String, Transformer> labelSchematrons = null;
          synchronized (lock) {
            // Validate the label's schema and schematron first before doing
            // label validation
            hasValidSchemas = validateLabelSchemas(target,
                    problemContainer, getContext().getCatalogResolver());

            labelSchematrons = validateLabelSchematrons(
                    target, problemContainer, getContext().getCatalogResolver());
          }

            if (hasValidSchemas && !labelSchematrons.isEmpty()) {
              CachedEntityResolver resolver = new CachedEntityResolver();
              resolver.addCachedEntities(schemaValidator.getCachedLSResolver()
                      .getCachedEntities());
              validator.setCachedEntityResolver(resolver);
              validator.setCachedLSResourceResolver(
                      schemaValidator.getCachedLSResolver());
              validator.setLabelSchematrons(labelSchematrons);
              if (getContext().isForceLabelSchemaValidation()) {
                try {
                  schemaValidator.setExternalLocations(
                          getExtractor(target).getSchemaLocation());
                } catch (Exception ignore) {
                  //Should not throw an exception
                }
              }
            } else {
              // Print any label problems that occurred during schema and schematron
              // validation.
              if (problemContainer.getProblems().size() != 0) {
                for (ValidationProblem problem : problemContainer.getProblems()) {
                  problem.setSource(target.toString());
                  getListener().addProblem(problem);
                }
              }
              pass = false;
            }
        }
        if (pass) {
          getListener().addLocation(target.toString());
          document = validator.parseAndValidate(processor, target);
        }
        if (document != null) {
          getContext().put(PDS4Context.LABEL_DOCUMENT, document);
        }
      } catch (SAXException | IOException | ParserConfigurationException
              | TransformerException | MissingLabelSchemaException e) {
        if (e instanceof XPathException) {
          XPathException xe = (XPathException) e;
          if (!xe.hasBeenReported()) {
            reportError(GenericProblems.UNCAUGHT_EXCEPTION, target, -1, -1,
                    e.getMessage());
          }
        } else {
          // Don't need to report SAXParseException messages as they have already
          // been reported by the LabelValidator's error handler
          if (!(e instanceof SAXParseException)) {
            reportError(GenericProblems.UNCAUGHT_EXCEPTION, target, -1, -1,
                    e.getMessage());
          }
        }
      }
      long t1 = System.currentTimeMillis();

      if (isInfoLogLevel()) {
        System.out.println(System.currentTimeMillis() + " :: " + targetFileName + " :: validateLabel() in " + (t1 - t0) + " ms");
      }
	}
	
  private boolean validateLabelSchemas(URL label,
      ProblemContainer labelProblems, XMLCatalogResolver resolver) {
    boolean passFlag = true;
    List<StreamSource> sources = new ArrayList<StreamSource>();
    Map<String, URL> schemaLocations = new LinkedHashMap<String, URL>();
    this.schemaValidator.setCatalogResolver(resolver);

    try {
      schemaLocations = getSchemaLocations(label);  
    } catch (Exception e) {
      labelProblems.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.FATAL,
              ProblemType.SCHEMA_ERROR,
              e.getMessage()), 
          label));
      return false;
    }
    
    // Loop through all the schemaLocations from the label and resolve them
    for (Map.Entry<String, URL> schemaLocation : schemaLocations.entrySet()) {
      URL schemaUrl = schemaLocation.getValue();
      ProblemContainer container = new ProblemContainer();
      boolean resolvableUrl = true;
      if (resolver != null) {
        String resolvedUrl = null;
        try {
          resolvedUrl = resolver.resolveSchema(schemaLocation.getKey(), 
              schemaUrl.toString(), label.toString());

          if (resolvedUrl != null) {
            schemaUrl = new URL(resolvedUrl);
          } else {
            labelProblems.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.CATALOG_UNRESOLVABLE_SCHEMA,
                    "Could not resolve schema '"
                    + schemaLocation.getValue().toString()
                    + "' through the catalog"), 
                label));
            resolvableUrl = false;
          }
        } catch (IOException io) {
          labelProblems.addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.ERROR,
                  ProblemType.CATALOG_UNRESOLVABLE_SCHEMA,
                  "Error while trying to resolve schema '"
                  + schemaLocation.getValue().toString()
                  + "' through the catalog: " + io.getMessage()), 
              label));
          resolvableUrl = false;
        }
      }
      
      // If we found the schema, let's read it into memory
      if (resolvableUrl) {
        schemaValidator.getCachedLSResolver().setProblemHandler(container);
        LSInput input = schemaValidator.getCachedLSResolver()
            .resolveResource("", "", "", schemaUrl.toString(), schemaUrl.toString());
        boolean addSource = true;
        if (container.getProblems().size() != 0) {
          try {
            for (ValidationProblem le : container.getProblems()) {
              le.setSource(label.toURI().toString());
              getListener().addProblem(le);
            }
            if (container.hasError() || container.hasFatal()) {
              passFlag = false;
              addSource = false;
            }
          } catch (URISyntaxException u) {
            labelProblems.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.FATAL,
                    ProblemType.SCHEMA_ERROR,
                    "URI syntax exception occurred for schema '"
                        + schemaUrl.toString() + "': " + u.getMessage()),
                label));
          }
        }
        
        // Load the sources
        if (addSource) {
          StreamSource streamSource = new StreamSource(
              input.getByteStream());
          streamSource.setSystemId(schemaUrl.toString());
          sources.add(streamSource);
        }
      } else {
        passFlag = false;
      }
    }
    
    if (passFlag) {
      // Now let's loop through the schemas themselves, and validate them
      for (StreamSource source : sources) {
        try {
          URL schemaUrl = null;
          try {
            schemaUrl = new URL(source.getSystemId());
          } catch(MalformedURLException ignore) {
            //Should never throw an exception
          }
          ProblemContainer container = new ProblemContainer();
          if (labelSchemaResults.containsKey(schemaUrl)) {
            container = labelSchemaResults.get(schemaUrl);
            if (container.getProblems().size() != 0) {
              for (ValidationProblem le : container.getProblems()) {
                le.setSource(label.toURI().toString());
                getListener().addProblem(le);
              }
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            }
          } else {
            try {
              // Before validating we need to load all schemas
              
              // Validate the schema source
              container = schemaValidator.validate(source);
            } catch (Exception e) {
              container.addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.ERROR,
                      ProblemType.SCHEMA_ERROR,
                      "Error reading schema: " + e.getMessage()),
                  schemaUrl));
            }
            if (container.getProblems().size() != 0) {
              for (ValidationProblem le : container.getProblems()) {
                le.setSource(label.toURI().toString());
                getListener().addProblem(le);
              }
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            }
            labelSchemaResults.put(schemaUrl, container);
          }
        } catch (URISyntaxException u) {
          labelProblems.addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.FATAL,
                  ProblemType.SCHEMA_ERROR,
                  "URI syntax exception occurred for schema '"
                      + source.getSystemId() + "': " + u.getMessage()),
              label));
        }
      }
    }
    return passFlag;
  }

  private Map<String, Transformer> validateLabelSchematrons(URL label,
      ProblemContainer labelProblems, XMLCatalogResolver resolver) {
    boolean passFlag = true;
    Map<String, Transformer> results = new HashMap<String, Transformer>();
    List<URL> schematronRefs = new ArrayList<URL>();
    try {
      schematronRefs = getSchematrons(label, labelProblems);
      if (labelProblems.getProblems().size() != 0) {
        for (ValidationProblem le : labelProblems.getProblems()) {
          getListener().addProblem(le);
        }
        passFlag = false;
      }
    } catch (Exception e) {
      labelProblems.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR,
              ProblemType.SCHEMATRON_ERROR,
              e.getMessage()), 
          label));
      passFlag = false;
    }
    
    //Now validate the schematrons
    for (URL schematronRef : schematronRefs) {
      try {
        ProblemContainer container = null;
        boolean resolvableUrl = true;
        if (resolver != null) {
          String resolvedUrl = null;
          try {
            String absoluteUrl = Utility.makeAbsolute(
                Utility.getParent(label).toString(),
                schematronRef.toString());
            resolvedUrl = resolver.resolveSchematron(absoluteUrl);
            if (resolvedUrl != null) {
              schematronRef = new URL(resolvedUrl);
            } else {
              labelProblems.addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.ERROR,
                      ProblemType.CATALOG_UNRESOLVABLE_SCHEMATRON,
                      "Could not resolve schematron '"
                      + schematronRef.toString() + "' through the catalog."), 
                      label));
              resolvableUrl = false;
            }
          } catch (IOException io) {
            labelProblems.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.CATALOG_UNRESOLVABLE_SCHEMATRON,
                    "Error while trying to resolve schematron '"
                    + schematronRef.toString() + "' through the catalog: "
                    + io.getMessage()), 
                    label));
            resolvableUrl = false;
          }
        }
        if (resolvableUrl) {
          if (labelSchematrons.containsKey(schematronRef)) {
            container = labelSchematronResults.get(schematronRef);
            if (container.getProblems().size() != 0) {
              for (ValidationProblem le : container.getProblems()) {
                le.setSource(label.toURI().toString());
                getListener().addProblem(le);
              }
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            } else {
              results.put(schematronRef.toString(),
                  labelSchematrons.get(schematronRef));
            }
          } else {
            container = new ProblemContainer();
            try {
              Transformer transformer = schematronTransformer.transform(
                  schematronRef, container);
              labelSchematrons.put(schematronRef, transformer);
              results.put(schematronRef.toString(), transformer);
            } catch (TransformerException te) {
              //Ignore as the listener handles the exceptions and puts it into
              //the container
            } catch (Exception e) {
              container.addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.FATAL,
                      ProblemType.SCHEMATRON_ERROR,
                      "Error occurred while attempting to read schematron: "
                          + e.getMessage()),
                  schematronRef));
            }
            if (container.getProblems().size() != 0) {
              for (ValidationProblem le : container.getProblems()) {
                le.setSource(label.toURI().toString());
                getListener().addProblem(le);
              }
              if (container.hasError() || container.hasFatal()) {
                passFlag = false;
              }
            }
            labelSchematronResults.put(schematronRef, container);
          }
        } else {
          passFlag = false;
        }
      } catch (URISyntaxException u) {
        labelProblems.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.FATAL,
                ProblemType.SCHEMATRON_ERROR,
                "URI syntax exception occurred for schematron '"
                    + schematronRef.toString() + "': " + u.getMessage()),
            label));
      }
    }
    if (!passFlag) {
      results.clear();
    }
    return results;
  }
  
  private Map<String, URL> getSchemaLocations(URL label) throws Exception {
    Map<String, URL> schemaLocations = new LinkedHashMap<String, URL>();
    String value = "";
    try {
      XMLExtractor extractor = getExtractor(label);
      value = extractor.getSchemaLocation();
    } catch (Exception e) {
      throw new Exception(
          "Error occurred while attempting to find schemas using the XPath '"
          + XMLExtractor.SCHEMA_LOCATION_XPATH + "': " + e.getMessage());
    }
    if (value == null || value.isEmpty()) {
      throw new Exception("No schema(s) found in the label.");
    } else {
      StringTokenizer tokenizer = new StringTokenizer(value);
      if ((tokenizer.countTokens() % 2) != 0) {
        throw new Exception(
            "schemaLocation value does not appear to have matching sets of "
            + "namespaces to uris: '" + schemaLocations + "'");
      } else {
        // While loop that will grab the schema URIs
        while (tokenizer.hasMoreTokens()) {
          // First token assumed to be the namespace
          String namespace = tokenizer.nextToken();
          // Second token assumed to be the URI
          String uri = tokenizer.nextToken();
          URL schemaUrl = null;
          try {
            schemaUrl = new URL(uri);
          } catch (MalformedURLException mu) {
            // The schema specification value does not appear to be
            // a URL. Assume a local reference to the schematron and
            // attempt to resolve it.
            try {
              URL parent = label.toURI().resolve(".").toURL();
              schemaUrl = new URL(parent, uri);
            } catch (MalformedURLException mue) {
              throw new Exception(
                  "Cannot resolve schema specification '"
                      + uri + "': " + mue.getMessage());
            } catch (URISyntaxException e) {
              //Ignore
            }
          }
          schemaLocations.put(namespace, schemaUrl);
        }
      }
    }
    return schemaLocations;
  }
  
  private List<URL> getSchematrons(URL label, ProblemContainer labelProblems)
      throws Exception {
    List<URL> schematronRefs = new ArrayList<URL>();
    List<TinyNodeImpl> xmlModels = new ArrayList<TinyNodeImpl>();
    try {
      XMLExtractor extractor = getExtractor(label);
      xmlModels = extractor.getNodesFromDoc(XMLExtractor.XML_MODEL_XPATH);
    } catch (Exception e) {
      throw new Exception(
          "Error occurred while attempting to find schematrons using "
              + "the XPath '" + XMLExtractor.XML_MODEL_XPATH + "': "
              + e.getMessage());
    }
    Pattern pattern = Pattern.compile(
        "href=\\\"([^=]*)\\\"( schematypens=\\\"([^=]*)\\\")?"
        );
    for (TinyNodeImpl xmlModel : xmlModels) {
      String filteredData = xmlModel.getStringValue().replaceAll("\\s+", " ");
      filteredData = filteredData.trim();
      Matcher matcher = pattern.matcher(filteredData);
      if (matcher.matches()) {
        String value = matcher.group(1).trim();
        URL schematronRef = null;
        URL parent = Utility.getParent(label);
        try {
          schematronRef = new URL(value);
          schematronRef = new URL(Utility.makeAbsolute(parent.toString(), 
              schematronRef.toString()));
        } catch (MalformedURLException ue) {
          // The schematron specification value does not appear to be
          // a URL. Assume a local reference to the schematron and
          // attempt to resolve it.
          try {
            schematronRef = new URL(parent, value);
          } catch (MalformedURLException mue) {
            labelProblems.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.SCHEMATRON_ERROR,
                    "Cannot resolve schematron specification '"
                    + value + "': " + mue.getMessage()),
                    label,
                    xmlModel.getLineNumber(),
                    -1));
            continue;
          }
        }
        schematronRefs.add(schematronRef);
      }
    }
    if (schematronRefs.isEmpty()) {
      labelProblems.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.WARNING,
              ProblemType.MISSING_SCHEMATRON_SPEC,
              "No schematrons specified in the label"),
          label));
    } 
    return schematronRefs;
  }
  
  private XMLExtractor getExtractor(URL label)
      throws XPathException, XPathExpressionException {
    if (extractor == null || 
        !(label.toString().equals(extractor.getSystemId())) ) {
      extractor = new XMLExtractor(label);
    }
    return extractor;
  }
}
