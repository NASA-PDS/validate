// Copyright 2009-2018, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology
// Transfer at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id: LabelValidator.java 16821 2018-06-25 15:01:49Z mcayanan $
//

package gov.nasa.pds.tools.label;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import gov.nasa.pds.tools.label.validate.DefaultDocumentValidator;
import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.tools.label.validate.ExternalValidator;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.LabelUtil;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.TargetExaminer;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.constants.Constants;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.trans.XPathException;

/**
 * This class is responsible for providing utility functions for validating PDS XML Labels.
 *
 * @author pramirez
 *
 */
public class LabelValidator {
  private static final Logger LOG = LoggerFactory.getLogger(LabelValidator.class);
  private Map<String, Boolean> configurations = new HashMap<>();
  private List<URL> userSchemaFiles;
  private List<URL> userSchematronFiles;
  private List<String> userSchematronTransformers;
  private XMLReader cachedParser;
  private ValidatorHandler cachedValidatorHandler;
  private List<String> cachedSchematron;
  private XMLCatalogResolver resolver;
  private Boolean useLabelSchema;
  private Boolean useLabelSchematron;
  private Boolean skipProductValidation;
  private Map<String, String> cachedLabelSchematrons;

  public static final String SCHEMA_CHECK = "gov.nasa.pds.tools.label.SchemaCheck";
  public static final String SCHEMATRON_CHECK = "gov.nasa.pds.tools.label.SchematronCheck";

  private List<ExternalValidator> externalValidators;
  private List<DocumentValidator> documentValidators;
  private CachedEntityResolver cachedEntityResolver;
  private CachedLSResourceResolver cachedLSResolver;
  private SAXParserFactory saxParserFactory;
  private DocumentBuilder docBuilder;
  private SchemaFactory schemaFactory;
  private Schema validatingSchema;
  private SchematronTransformer schematronTransformer;

  private long filesProcessed = 0;
  private double totalTimeElapsed = 0.0;

  /**
   * Returns the number of files processed by the validation function.
   */
  public long getFilesProcessed() {
    return (this.filesProcessed);
  }

  /**
   * Returns the duration it took to run the validation function.
   */
  public double getTotalTimeElapsed() {
    return (this.totalTimeElapsed);
  }

  /**
   * Default constructor.
   *
   * @throws ParserConfigurationException If there was an error setting up the configuration of the
   *         parser that is reposnible for doing the label validation.
   *
   * @throws TransformerConfigurationException If there was an error setting up the Transformer
   *         responsible for doing the transformations of the schematrons.
   */
  public LabelValidator() throws ParserConfigurationException, TransformerConfigurationException {
    this.configurations.put(SCHEMA_CHECK, true);
    this.configurations.put(SCHEMATRON_CHECK, true);
    cachedParser = null;
    cachedValidatorHandler = null;
    cachedSchematron = new ArrayList<>();
    userSchemaFiles = new ArrayList<>();
    userSchematronFiles = new ArrayList<>();
    userSchematronTransformers = new ArrayList<>();
    resolver = null;
    externalValidators = new ArrayList<>();
    documentValidators = new ArrayList<>();
    useLabelSchema = false;
    useLabelSchematron = false;
    cachedLabelSchematrons = new HashMap<>();
    cachedEntityResolver = new CachedEntityResolver();
    cachedLSResolver = new CachedLSResourceResolver();
    validatingSchema = null;

    // Support for XSD 1.1
    schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // We need a SAX parser factory to do the validating parse
    // to create the DOM tree, so we can insert line numbers
    // as user data properties of the DOM nodes.
    saxParserFactory = SAXParserFactory.newInstance();
    saxParserFactory.setNamespaceAware(true);
    saxParserFactory.setXIncludeAware(Utility.supportXincludes());
    // The parser doesn't validate - we use a Validator instead.
    saxParserFactory.setValidating(false);

    // Don't add xml:base attributes to xi:include content, or it messes up
    // PDS4 validation.
    try {
      saxParserFactory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", false);
    } catch (SAXNotRecognizedException e) {
      // should never happen, and can't recover
    } catch (SAXNotSupportedException e) {
      // should never happen, and can't recover
    }

    // We need a document builder to create new documents to insert
    // parsed XML nodes.
    docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

    documentValidators.add(new DefaultDocumentValidator());
    schematronTransformer = new SchematronTransformer();
  }

  /**
   * Pass in a list of schemas to validate against.
   *
   * @param schemaFiles A list of schema URLs.
   *
   */
  public void setSchema(List<URL> schemaFiles) {
    this.userSchemaFiles.addAll(schemaFiles);
    LOG.debug("setSchema:schemaFiles.size(),schemaFiles {},{}", schemaFiles.size(), schemaFiles);
  }

  /**
   * Pass in a list of transformed schematrons to validate against.
   *
   * @param schematrons A list of transformed schematrons.
   */
  public void setSchematrons(List<String> schematrons) {
    userSchematronTransformers = schematrons;
    LOG.debug("setSchematrons:schematrons.size(),schematrons {}", schematrons.size());
  }
  public void clear() {
    this.userSchemaFiles.clear();
    this.userSchematronFiles.clear();
    this.userSchematronTransformers.clear();
  }
  /**
   * Pass in a hash map of schematron URLs to its transformed schematron object. This is used when
   * validating a label against it's referenced schematron.
   *
   * @param schematronMap
   */
  public void setLabelSchematrons(Map<String, String> schematronMap) {
    cachedLabelSchematrons = schematronMap;
  }

  /**
   * Pass in a list of schematron files to validate against.
   *
   * @param schematronFiles A list of schematron URLs.
   */
  public void setSchematronFiles(List<URL> schematronFiles) {
    userSchematronFiles.addAll(schematronFiles);
    LOG.debug("setSchematronFiles:schematronFiles.size(),schematronFiles {},{}",
        schematronFiles.size(), schematronFiles);
  }

  /**
   * Pass in a list of Catalog files to use during the validation step.
   *
   * @param catalogFiles
   */
  public void setCatalogs(String[] catalogFiles) {
    resolver = new XMLCatalogResolver();
    resolver.setPreferPublic(true);
    resolver.setCatalogList(catalogFiles);
    useLabelSchematron = true;
    LOG.debug("setCatalogs:catalogFiles {}", (Object[])catalogFiles);
    LOG.debug("setCatalogs:useLabelSchematron explitly set to true");
  }

  public XMLCatalogResolver getCatalogResolver() {
    return this.resolver;
  }

  private List<StreamSource> loadSchemaSources(List<URL> schemas) throws IOException, SAXException {
    List<StreamSource> sources = new ArrayList<>();
    LOG.debug("loadSchemaSources:schemas {}", schemas);
    String externalLocations = "";
    for (URL schema : schemas) {
      LSInput input =
          cachedLSResolver.resolveResource("", "", "", schema.toString(), schema.toString());
      StreamSource streamSource = new StreamSource(input.getByteStream());
      streamSource.setSystemId(schema.toString());
      sources.add(streamSource);
      InputSource inputSource = new InputSource(input.getByteStream());
      inputSource.setSystemId(input.getSystemId());
      try {
        XMLExtractor extractor = new XMLExtractor(inputSource);
        String namespace = extractor.getTargetNamespace();
        if (!namespace.isEmpty()) {
          externalLocations += namespace + " " + schema.toString() + "\n";
        }
        inputSource.getByteStream().reset();
      } catch (Exception e) {
        throw new IOException("Error occurred while getting the " + "targetNamespace for schema '"
            + schema.toString() + "': " + e.getMessage());
      }
    }
    schemaFactory.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
        externalLocations);
    LOG.debug("loadSchemaSources:schemas,externalLocations,sources {}", schemas, externalLocations,
        sources);
    return sources;
  }

  private List<StreamSource> loadSchemaSources(String[] schemaFiles) {
    List<StreamSource> sources = new ArrayList<>();
    LOG.debug("loadSchemaSources:sources.size() {}", sources.size());
    for (String schemaFile : schemaFiles) {
      sources.add(new StreamSource(schemaFile));
      LOG.debug("loadSchemaSources:schemaFile {}", schemaFile);
    }
    return sources;
  }

  public synchronized void validate(ProblemHandler handler, File labelFile) throws SAXException,
      IOException, ParserConfigurationException, TransformerException, MissingLabelSchemaException {
    validate(handler, labelFile.toURI().toURL());
  }

  /**
   * Validates the label against schema and schematron constraints.
   *
   * @param handler a handler to receive errors during the validation
   * @param url label to validate
   *
   * @throws SAXException if there are parsing exceptions
   * @throws IOException if there are I/O errors during the parse
   * @throws ParserConfigurationException if the parser configuration is invalid
   * @throws TransformerException if there is an error during Schematron transformation
   * @throws MissingLabelSchemaException if the label schema cannot be found
   */
  public synchronized void validate(ProblemHandler handler, URL url) throws SAXException,
      IOException, ParserConfigurationException, TransformerException, MissingLabelSchemaException {
    this.parseAndValidate(handler, url);
  }

  public synchronized void validate(ProblemHandler handler, URL url, String labelExtension)
      throws SAXException, IOException, ParserConfigurationException, TransformerException,
      MissingLabelSchemaException {
    this.parseAndValidate(handler, url);

  }

  private Boolean determineSchematronValidationFlag(URL url) {
    // Function return true if the validation against the schematron should be done
    // or not.
    // Note: schematron validation can be time consuming. Only the bundle or
    // collection should be validated against schematron.
    // If the flag skipProductValidation is true, the labels belong to data files
    // should not be done.

    // Removed String nameOnly = Paths.get(url.getPath()).getFileName().toString()
    // due to issue for Windows machine.

    Boolean validateAgainstSchematronFlag;
    if (!this.skipProductValidation) {
      validateAgainstSchematronFlag = true;
    } else {
      // If skip product validation is true, perform schematron validation
      // bundle/collection labels only.
      // Note: To inspect the file is time consuming
      // If the name of the file contains 'bundle' or 'collection' do the schematron
      // validation.
      // 6C.1.3 Reserved File Names
      // The following file names are reserved for specific purposes within PDS
      // archives and may not be used otherwise:
      // bundle*.xml
      // collection*.[xml,csv]

      // Remove time consuming check of reading the file in favor of checking the
      // filename.
      // The file name should be enough to know if it is a bundle or a collection.
      validateAgainstSchematronFlag = false;
      if (TargetExaminer.isTargetBundleType(url)) {
        // File is indeed a bundle.
        validateAgainstSchematronFlag = true;
      }
      // File is not a bundle, check if a collection.
      if (!validateAgainstSchematronFlag && TargetExaminer.isTargetCollectionType(url)) {
        validateAgainstSchematronFlag = true;
      }
    }
    LOG.debug("determineSchematronValidationFlag:url,validateAgainstSchematronFlag {},{}", url,
        validateAgainstSchematronFlag);

    return (validateAgainstSchematronFlag);
  }

  private void checkSchemaSchematronVersions(ProblemHandler handler, URL url) {
    try {
      List<String> specifiedSchema = null, specifiedSchematron = null;
      XMLExtractor sourceXML = new XMLExtractor(url);
      String xmlns = sourceXML.getSchemaLocation();
      specifiedSchematron = sourceXML.getXmlModels();
      if (xmlns != null && 0 < xmlns.strip().length())
        specifiedSchema = Arrays.asList(xmlns.split("\\s"));
      if (specifiedSchema == null || specifiedSchema.size() == 0) {
        handler.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
            ProblemType.SCHEMA_WARNING, "Cannot check versioning because no Schema given."), url));
      } else if (specifiedSchematron == null || specifiedSchematron.size() == 0) {
        handler.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
            ProblemType.SCHEMATRON_WARNING, "Cannot check versioning because no Schematron given."),
            url));
      } else {
        Set<String> schemas = new HashSet<String>();
        Set<String> schematrons = new HashSet<String>();
        for (String schema : specifiedSchema) {
          try {
            URL schemaURL = new URL(schema);
            String schemaName = schemaURL.getFile().toLowerCase();
            if (schemaName.endsWith(".xsd")) {
              schemaName = schemaName.substring(0, schemaName.length() - 4);
              schemas.add(schemaName);
            }
          } catch (MalformedURLException e) {
            // Ignore error since XMLExtractor() was able to read it this should be alright
          }
        }
        for (String model : specifiedSchematron) {
          for (String part : model.split("\\s")) {
            if (part.toLowerCase().startsWith("href")) {
              String modelHREF = part.substring(part.indexOf('"') + 1, part.lastIndexOf('"'));
              try {
                URL schematronURL = new URL(modelHREF);
                String schematronName = schematronURL.getFile().toLowerCase();
                if (schematronName.endsWith(".sch")) {
                  schematronName = schematronName.substring(0, schematronName.length() - 4);
                  schematrons.add(schematronName);
                }
              } catch (MalformedURLException e) {
                // Ignore error since XMLExtractor() was able to it this should be alright
              }
            }
          }
        }
        Set<String> uniqueSchema = new HashSet<String>(schemas);
        Set<String> uniqueSchematron = new HashSet<String>(schematrons);
        uniqueSchema.removeAll(schematrons);
        uniqueSchematron.removeAll(schemas);
        if (0 < uniqueSchema.size() + uniqueSchematron.size()) {
          handler.addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.WARNING, ProblemType.SCHEMA_WARNING,
                  "The schema version(s) " + uniqueSchema.toString().toUpperCase()
                      + " does/do not match the schematron version(s) "
                      + uniqueSchematron.toString().toUpperCase() + "."),
              url));
        }
      }
    } catch (XPathException | XPathExpressionException e) {
      handler.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.WARNING, ProblemType.MISSING_REQUIRED_RESOURCE,
              "Cannot check versioning because XML could not be parsed."),
          url));
    }
  }

  /**
   * Parses and validates a label against the schema and Schematron files, and returns the parsed
   * XML.
   *
   * @param handler an problem handler to receive errors during the validation
   * @param url the URL of the label to validate
   * @return the XML document represented by the label
   * @throws SAXException if there are parsing exceptions
   * @throws IOException if there are I/O errors during the parse
   * @throws ParserConfigurationException if the parser configuration is invalid
   * @throws TransformerException if there is an error during Schematron transformation
   * @throws MissingLabelSchemaException if the label schema cannot be found
   */
  public synchronized Document parseAndValidate(ProblemHandler handler, URL url)
      throws SAXException, IOException, ParserConfigurationException, TransformerException,
      MissingLabelSchemaException {
    List<String> labelSchematronRefs = new ArrayList<>();
    Document xml = null;

    // Printing debug is expensive. Should uncomment by developer only.
    long startTime = System.currentTimeMillis();

    LOG.debug("parseAndValidate:url,performsSchematronValidation() {},{}", url,
        performsSchematronValidation());
    LOG.debug("parseAndValidate:url,useLabelSchematron {},{}", url, useLabelSchematron);
    LOG.debug("parseAndValidate:url,useLabelSchema {},{}", url, useLabelSchema);
    LOG.debug("parseAndValidate:url,performsSchemaValidation() {},{}", url,
        performsSchemaValidation());

    // Are we perfoming schema validation?
    if (performsSchemaValidation()) {
      createParserIfNeeded(handler);
      checkSchemaSchematronVersions(handler, url);

      // Do we need this to clear the cache?

      if (useLabelSchema) {
        LOG.debug("parseAndValidate:#00AA0");
        cachedValidatorHandler = schemaFactory.newSchema().newValidatorHandler();
      } else {
        LOG.debug("parseAndValidate:#00AA1");
        cachedValidatorHandler = validatingSchema.newValidatorHandler();
      }

      // Capture messages in a container
      if (handler != null) {
        LOG.debug("parseAndValidate:#00AA2");
        ErrorHandler eh = new LabelErrorHandler(handler);
        cachedParser.setErrorHandler(eh);
        cachedValidatorHandler.setErrorHandler(eh);

      }
      LOG.debug("parseAndValidate:#00AA3");
      // Finally parse and validate the file
      xml = docBuilder.newDocument();
      cachedParser.setContentHandler(new DocumentCreator(xml));
      cachedParser.parse(Utility.getInputSourceByURL(url));

      // Each version of the Information Model (IM) must be registered so in the end,
      // multiple versions can be reported.
      LabelUtil.setLocation(url.toString());
      String informationModelVersion = LabelUtil.getIMVersion(new DOMSource(xml), url);
      LabelUtil.registerIMVersion(informationModelVersion);

      DOMLocator locator = new DOMLocator(url);
      cachedValidatorHandler.setDocumentLocator(locator);
      if (resolver != null) {
        LOG.debug("parseAndValidate:#00AA4");
        cachedValidatorHandler.setResourceResolver(resolver);
        resolver.setProblemHandler(handler);
      } else {
        LOG.debug("parseAndValidate:#00AA5");
        cachedValidatorHandler.setResourceResolver(cachedLSResolver);
      }

      if (!skipProductValidation) {
        LOG.debug("parseAndValidate:#00AA6");
        walkNode(xml, cachedValidatorHandler, locator);
      }
      LOG.debug("parseAndValidate:#00AA7");

      // If validating against the label supplied schema, check
      // if the xsi:schemalocation attribute was defined in the label.
      // If it is not found, throw an exception.
      if (useLabelSchema) {
        Element root = xml.getDocumentElement();
        if (!root.hasAttribute("xsi:schemaLocation")) {
          throw new MissingLabelSchemaException("No schema(s) specified in the label.");
        }
      }
    } else {
      // No Schema validation will be performed. Just parse the label
      XMLReader reader = saxParserFactory.newSAXParser().getXMLReader();
      xml = docBuilder.newDocument();
      reader.setContentHandler(new DocumentCreator(xml));
      // Capture messages in a container
      if (handler != null) {
        reader.setErrorHandler(new LabelErrorHandler(handler));
      }
      if (resolver != null) {
        reader.setEntityResolver(resolver);
        resolver.setProblemHandler(handler);
      } else if (useLabelSchema) {
        reader.setEntityResolver(cachedEntityResolver);
      }
      reader.parse(new InputSource(url.openStream()));
    }

    // If we get here, then there are no XML parsing errors, so we
    // can parse the XML again, below, and assume the parse will
    // succeed.

    LOG.debug("parseAndValidate:0001:url,useLabelSchematron,cachedSchematron.size() {},{},{}", url,
        useLabelSchematron, cachedSchematron.size());
    // Validate with any schematron files we have
    if (performsSchematronValidation()) {
      // Look for schematron files specified in a label
      if (useLabelSchematron) {
        labelSchematronRefs = getSchematrons(xml.getChildNodes(), url, handler);
      }
      LOG.debug("parseAndValidate:0002:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
          url, useLabelSchematron, cachedSchematron.size());
      if (cachedSchematron.isEmpty()) {
        if (useLabelSchematron) {
          cachedSchematron = loadLabelSchematrons(labelSchematronRefs, url, handler);
        } else if (!userSchematronTransformers.isEmpty()) {
          cachedSchematron = userSchematronTransformers;
          LOG.debug("parseAndValidate:0003:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
              url, useLabelSchematron, cachedSchematron.size());
        } else if (!userSchematronFiles.isEmpty()) {
          List<String> transformers = new ArrayList<>();
          for (URL schematron : userSchematronFiles) {
            transformers.add(schematronTransformer.fetch(schematron, handler));
          }
          cachedSchematron = transformers;
          LOG.debug("parseAndValidate:0004:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
              url, useLabelSchematron, cachedSchematron.size());
        }
        LOG.debug("parseAndValidate:0010:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
            url, useLabelSchematron, cachedSchematron.size());
      } else {
        // If there are cached schematrons....
        LOG.debug(
            "parseAndValidate:0011:url,useLabelSchematron,userSchematronTransformers.isEmpty() {},{},{}",
            url, useLabelSchematron, userSchematronTransformers.isEmpty());
        if (useLabelSchematron) {
          if (!userSchematronTransformers.isEmpty()) {
            cachedSchematron = userSchematronTransformers;
          } else {
            cachedSchematron = loadLabelSchematrons(labelSchematronRefs, url, handler);
          }
        }
        LOG.debug("parseAndValidate:0020:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
            url, useLabelSchematron, cachedSchematron.size());
      }
      LOG.debug("parseAndValidate:0030:url,useLabelSchematron,cachedSchematron.size() {},{},{}",
          url, useLabelSchematron, cachedSchematron.size());

      // Determine if schematron validation should be done or not.
      // Note: schematron validation can be time consuming. Only the bundle or
      // collection should be validated against schematron.
      // If the flag skipProductValidation is true, the labels belong to data files
      // should not be done.
      Boolean validateAgainstSchematronFlag = this.determineSchematronValidationFlag(url);
      LOG.debug("parseAndValidate:url,skipProductValidation,validateAgainstSchematronFlag {},{},{}",
          url, skipProductValidation, validateAgainstSchematronFlag);

      for (String schematron : cachedSchematron) {
        try {
          long singleSchematronStartTime = System.currentTimeMillis();
          if (!validateAgainstSchematronFlag) {
            continue; // Skip the validation if validateAgainstSchematronFlag is not true.
          }
          DOMResult result = new DOMResult();
          DOMSource domSource = new DOMSource(xml);
          LOG.debug("parseAndValidate:VALIDATING_SCHEMATRON_URL:START {} against schematron", url);
          domSource.setSystemId(url.toString());
          // Apply the rules specified in the schematron file
          schematronTransformer.transform(schematron).transform(domSource, result);
          // Output is svrl:schematron-output document
          // Select out svrl:failed-assert nodes and put into problem container
          Document reportDoc = (Document) result.getNode();
          NodeList nodes =
              reportDoc.getElementsByTagNameNS("http://purl.oclc.org/dsdl/svrl", "failed-assert");
          for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            // Add an error for each failed asssert
            handler.addProblem(processFailedAssert(url, node, xml));
          }
          long singleSchematronFinishTime = System.currentTimeMillis();
          long singleSchematronTimeElapsed = singleSchematronFinishTime - singleSchematronStartTime;
          LOG.debug("parseAndValidate:VALIDATING_SCHEMATRON_URL:ELAPSED {} {}", url,
              singleSchematronTimeElapsed);
        } catch (TransformerException te) {
          // should never get here
          handler.addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.FATAL, ProblemType.SCHEMATRON_ERROR, te.getLocalizedMessage()),
              url));        }
      }
      LOG.debug("parseAndValidate:VALIDATING_SCHEMATRON_URL:DONE {} against schematron", url);
    }

    // issue_42: skip product-level validation when the flag is on
    if (!skipProductValidation) {
      if (!externalValidators.isEmpty()) {
        // Perform any other additional checks that were added
        for (ExternalValidator ev : externalValidators) {
          ev.validate(handler, url);
        }
      }

      // Perform any additional checks that were added
      if (!documentValidators.isEmpty()) {

        SAXSource saxSource = new SAXSource(Utility.getInputSourceByURL(url));
        saxSource.setSystemId(url.toString());
        TreeInfo docInfo = LabelParser.parse(saxSource, false);
        for (DocumentValidator dv : documentValidators) {
          dv.validate(handler, docInfo.getRootNode());
        }
      }
    }

    this.filesProcessed += 1;
    long finishTime = System.currentTimeMillis();
    long timeElapsed = finishTime - startTime;
    this.totalTimeElapsed += timeElapsed;
    LOG.debug(
        "parseAndValidate:url,skipProductValidation,this.filesProcessed,timeElapsed,this.totalTimeElapsed/1000.0 {},{},{},{},{}",
        url, skipProductValidation, this.filesProcessed, timeElapsed,
        this.totalTimeElapsed / 1000.0);
    return xml;
  }

  private void createParserIfNeeded(ProblemHandler handler) throws SAXNotRecognizedException,
      SAXNotSupportedException, SAXException, IOException, ParserConfigurationException {
    // Do we have a schema we have loaded previously?
    LOG.debug("createParserIfNeeded:cachedParser,resolver,handler {},{},{}", cachedParser, resolver,
        handler);
    LOG.debug("createParserIfNeeded:#00BB0");
    if (cachedParser == null) {
      LOG.debug("createParserIfNeeded:#00BB1");
      // If catalog is used, allow resources to be loaded for schemas
      // and the document parser
      if (resolver != null) {
        LOG.debug("createParserIfNeeded:#00BB2");
        schemaFactory.setProperty("http://apache.org/xml/properties/internal/entity-resolver",
            resolver);
      }
      LOG.debug("createParserIfNeeded:#00BB3");
      // Allow errors that happen in the schema to be logged there
      if (handler != null) {
        LOG.debug("createParserIfNeeded:#00BB4");
        schemaFactory.setErrorHandler(new LabelErrorHandler(handler));
        cachedLSResolver = new CachedLSResourceResolver(handler);
        schemaFactory.setResourceResolver(cachedLSResolver);
      } else {
        LOG.debug("createParserIfNeeded:#00BB5");
        cachedLSResolver = new CachedLSResourceResolver();
        schemaFactory.setResourceResolver(cachedLSResolver);
      }
      LOG.debug("createParserIfNeeded:#00BB6");
      // Time to load schema that will be used for validation
      if (!userSchemaFiles.isEmpty()) {
        LOG.debug("createParserIfNeeded:#00BB7");
        // User has specified schema files to use
        validatingSchema = schemaFactory
            .newSchema(loadSchemaSources(userSchemaFiles).toArray(new StreamSource[0]));
      } else if (resolver == null) {
        LOG.debug("createParserIfNeeded:#00BB8");
        if (useLabelSchema || !VersionInfo.hasSchemaDir()) {
          LOG.debug("createParserIfNeeded:#00BB9");
          validatingSchema = schemaFactory.newSchema();
        } else {
          LOG.debug("createParserIfNeeded:#00BC0");
          // Load from user specified external directory
          validatingSchema = schemaFactory.newSchema(
              loadSchemaSources(VersionInfo.getSchemasFromDirectory().toArray(new String[0]))
                  .toArray(new StreamSource[0]));
        }
      } else {
        LOG.debug("createParserIfNeeded:#00BC1");
        // We're only going to use the catalog to validate against.
        validatingSchema = schemaFactory.newSchema();
      }

      LOG.debug("createParserIfNeeded:#00BC2");
      cachedParser = saxParserFactory.newSAXParser().getXMLReader();
      cachedValidatorHandler = validatingSchema.newValidatorHandler();
      if (resolver != null) {
        LOG.debug("createParserIfNeeded:#00BC3");
        cachedParser.setEntityResolver(resolver);
        docBuilder.setEntityResolver(resolver);
      } else if (useLabelSchema) {
        LOG.debug("createParserIfNeeded:#00BC4");
        cachedParser.setEntityResolver(cachedEntityResolver);
      }
      LOG.debug("createParserIfNeeded:#00BC5");
      LOG.debug("createParserIfNeeded:cachedParser,cachedValidatorHandler,resolver {},{},{}",
          cachedParser, cachedValidatorHandler, resolver);
    } else {
      // TODO: This code doesn't look right. It says that if we have
      // a cached parser, but we are using the label schema, then
      // create a new parser. It seems like the creation is handled
      // properly at the end of the if-part, above, so we should
      // do nothing if we already have a cached parser.

      // Create a new instance of the DocumentBuilder if validating
      // against a label's schema.
      LOG.debug("createParserIfNeeded:#00BC6");
      if (useLabelSchema) {
        LOG.debug("createParserIfNeeded:#00BC7");
        cachedParser = saxParserFactory.newSAXParser().getXMLReader();
        cachedValidatorHandler = schemaFactory.newSchema().newValidatorHandler();
        cachedParser.setEntityResolver(cachedEntityResolver);
      }
      LOG.debug("createParserIfNeeded:#00BC8");
    }
  }

  public void validate(File labelFile) throws SAXException, IOException,
      ParserConfigurationException, TransformerException, MissingLabelSchemaException {
    validate(null, labelFile);
  }

  /**
   * Walks a DOM subtree starting at the given node, invoking the {@link ContentHandler} callback
   * methods as if the document were being parsed by a SAX parser. Also updates the current location
   * using the {@link DOMLocator} so that error messages generated by the content handler will have
   * the proper source location.
   *
   * @param node the root node of the subtree to walk
   * @param handler the content handler
   * @param locator the locator used to indicate the source location
   * @throws SAXException if there is an exception while walking the tree (which will never happen)
   */
  private void walkNode(Node node, ContentHandler handler, DOMLocator locator) throws SAXException {
    locator.setNode(node);

    if (node instanceof Document) {
      handler.startDocument();
      walkChildren(node, handler, locator);
      locator.setNode(node);
      handler.endDocument();
    } else if (node instanceof Comment) {
      // ignore - comments aren't validated
    } else if (node instanceof CharacterData) {
      CharacterData text = (CharacterData) node;
      char[] chars = text.getNodeValue().toCharArray();
      handler.characters(chars, 0, chars.length);
    } else if (node instanceof ProcessingInstruction) {
      ProcessingInstruction pi = (ProcessingInstruction) node;
      handler.processingInstruction(pi.getTarget(), pi.getData());
    } else if (node instanceof Element) {
      Element e = (Element) node;
      AttributesImpl attributes = new AttributesImpl();

      NamedNodeMap map = e.getAttributes();
      for (int i = 0; i < map.getLength(); ++i) {
        Attr attr = (Attr) map.item(i);
        attributes.addAttribute(attr.getNamespaceURI(), attr.getLocalName(), attr.getNodeName(), "",
            attr.getNodeValue());
      }

      handler.startElement(e.getNamespaceURI(), e.getLocalName(), e.getNodeName(), attributes);
      walkChildren(e, handler, locator);
      locator.setNode(node);
      handler.endElement(e.getNamespaceURI(), e.getLocalName(), e.getNodeName());
    } else {
      System.err.println("Unknown node type in DOM tree: " + node.getClass().getName());
    }
  }

  /**
   * Recursively walks the subtrees for the children of a node.
   *
   * @param node the root node of the subtree to walk
   * @param handler the content handler
   * @param locator the locator used to indicate the source location
   * @throws SAXException if there is an exception while walking the tree (which will never happen)
   */
  private void walkChildren(Node node, ContentHandler handler, DOMLocator locator)
      throws SAXException {
    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); ++i) {
      walkNode(nodes.item(i), handler, locator);
    }
  }

  public List<String> getSchematrons(NodeList nodeList, URL url, ProblemHandler handler) {
    List<String> results = new ArrayList<>();

    LOG.debug("getSchematrons:url {}", url);

    for (int i = 0; i < nodeList.getLength(); i++) {
      if (nodeList.item(i).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
        ProcessingInstruction pi = (ProcessingInstruction) nodeList.item(i);
        if ("xml-model".equalsIgnoreCase(pi.getTarget())) {
          Pattern pattern = Pattern.compile(Constants.SCHEMATRON_SCHEMATYPENS_PATTERN);
          String filteredData = pi.getData().replaceAll("\\s+", " ");
          Matcher matcher = pattern.matcher(filteredData);
          if (matcher.matches()) {
            String value = matcher.group(1).trim();
            URL schematronRef = null;
            URL parent = Utility.getParent(url);
            try {
              schematronRef = new URL(value);
              schematronRef =
                  new URL(Utility.makeAbsolute(parent.toString(), schematronRef.toString()));
            } catch (MalformedURLException ue) {
              // The schematron specification value does not appear to be
              // a URL. Assume a local reference to the schematron and
              // attempt to resolve it.
              try {
                schematronRef = new URL(url, value);
              } catch (MalformedURLException mue) {
                handler.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.SCHEMATRON_ERROR,
                    "Cannot resolve schematron specification '" + value + "': " + mue.getMessage()),
                    url));
                continue;
              }
            }
            results.add(schematronRef.toString());
          }
        }
      }
    }
    LOG.debug("getSchematrons:url,results {},{},{}", url, results, results.size());
    return results;
  }

  private List<String> loadLabelSchematrons(List<String> schematronSources, URL url,
      ProblemHandler handler) {
    List<String> transformers = new ArrayList<>();
    LOG.debug("loadLabelSchematrons:resolver,schematronSources {},{}", resolver, schematronSources);
    for (String source : schematronSources) {
      try {
        String document;

        if (resolver != null) {
          try {
            String absoluteUrl = Utility.makeAbsolute(Utility.getParent(url).toString(), source);
            String resolvedUrl = resolver.resolveSchematron(absoluteUrl);
            LOG.debug("loadLabelSchematrons:resolver,absoluteUrl,resolvedUrl {},{}", resolver,
                absoluteUrl, resolvedUrl);
            if (resolvedUrl == null) {
              throw new Exception("'" + source + "' was not resolvable through the catalog file.");
            }
            source = resolvedUrl;
          } catch (IOException io) {
            throw new Exception("Error while resolving '" + source.toString()
                + "' through the catalog: " + io.getMessage());
          }
        }
        if (!this.cachedLabelSchematrons.containsKey(source)) {
          URL sourceUrl = new URL(source);
          LOG.debug("loadLabelSchematrons:sourceUrl {}", sourceUrl);
          document = schematronTransformer.fetch(sourceUrl);
          cachedLabelSchematrons.put(source, document);          
        }
        document = cachedLabelSchematrons.get(source);
        transformers.add(document);
        LOG.debug("loadLabelSchematrons:transformers.add:source {}", source);
      } catch (TransformerException te) {
        String message = "Schematron '" + source + "' error: " + te.getMessage();
        handler.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.SCHEMATRON_ERROR, message),
            url));
      } catch (Exception e) {
        String message = "Error occurred while loading schematron: " + e.getMessage();
        handler.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.SCHEMATRON_ERROR, message),
            url));
      }
    }
    return transformers;
  }

  /**
   * Process a failed assert message from the schematron report.
   *
   * @param url The url of the xml being validated.
   * @param node The node object containing the failed assert message.
   * @param doc the original document that was being validated, used to obtain line numbers, or null
   *        for no document
   *
   * @return A ValidationProblem object.
   */
  private ValidationProblem processFailedAssert(URL url, Node node, Document doc) {
    Integer lineNumber = -1;
    Integer columnNumber = -1;
    String message = node.getTextContent().trim();
    URL sourceUrl = url;
    ProblemType problemType = ProblemType.SCHEMATRON_ERROR;
    ExceptionType exceptionType = ExceptionType.ERROR;
    if (node.getAttributes().getNamedItem("role") != null) {
      String type = node.getAttributes().getNamedItem("role").getTextContent();
      if ("warn".equalsIgnoreCase(type) || "warning".equalsIgnoreCase(type)) {
        exceptionType = ExceptionType.WARNING;
        problemType = ProblemType.SCHEMATRON_WARNING;
      } else if ("info".equalsIgnoreCase(type)) {
        exceptionType = ExceptionType.INFO;
        problemType = ProblemType.SCHEMATRON_INFO;
      }
    }

    String location = ((Attr) node.getAttributes().getNamedItem("location")).getValue();
    SourceLocation sourceLoc = null;
    try {
      XPath documentPath = new net.sf.saxon.xpath.XPathFactoryImpl().newXPath();
      Node failureNode = (Node) documentPath.evaluate(location, doc, XPathConstants.NODE);
      sourceLoc = (SourceLocation) failureNode.getUserData(SourceLocation.class.getName());
    } catch (XPathExpressionException e) {
      // ignore - will use default line and column number
    }
    if (sourceLoc != null) {
      lineNumber = sourceLoc.getLineNumber();
      columnNumber = sourceLoc.getColumnNumber();
      if (sourceLoc.getUrl() != null) {
        try {
          sourceUrl = new URL(sourceLoc.getUrl());
        } catch (MalformedURLException e) {
          // Ignore. Should not happen!!!
        }
      }
    } else {
      String test = node.getAttributes().getNamedItem("test").getTextContent();
      message = String.format("%s [Context: \"%s\"; Test: \"%s\"]", message, location, test);
    }

    return new ValidationProblem(new ProblemDefinition(exceptionType, problemType, message),
        sourceUrl, lineNumber, columnNumber);

  }

  public Boolean performsSchemaValidation() {
    return getConfiguration(SCHEMA_CHECK);
  }

  public void setSchemaCheck(Boolean value) {
    setSchemaCheck(value, false);
  }

  public void setSchemaCheck(Boolean value, Boolean useLabelSchema) {
    this.setConfiguration(SCHEMA_CHECK, value);
    this.useLabelSchema = useLabelSchema;
  }

  public Boolean performsSchematronValidation() {
    return getConfiguration(SCHEMATRON_CHECK);
  }

  public void setSchematronCheck(Boolean value) {
    setSchematronCheck(value, false);
  }

  public void setSchematronCheck(Boolean value, Boolean useLabelSchematron) {
    this.setConfiguration(SCHEMATRON_CHECK, value);
    this.useLabelSchematron = useLabelSchematron;
    LOG.debug(
        "setSchematronCheck:useLabelSchematron explitly set to value,useLabelSchematron {},{}",
        value, useLabelSchematron);
  }

  public void setSkipProductValidation(Boolean flag) {
    this.skipProductValidation = flag;
  }

  public Boolean getConfiguration(String key) {
    return this.configurations.containsKey(key) ? this.configurations.get(key) : false;
  }

  public void setConfiguration(String key, Boolean value) {
    this.configurations.put(key, value);
  }

  public void addValidator(ExternalValidator validator) {
    this.externalValidators.add(validator);
  }

  public void addValidator(DocumentValidator validator) {
    this.documentValidators.add(validator);
  }

  public void setCachedEntityResolver(CachedEntityResolver resolver) {
    this.cachedEntityResolver = resolver;
  }

  public void setCachedLSResourceResolver(CachedLSResourceResolver resolver) {
    this.cachedLSResolver = resolver;
  }

  /**
   * Implements a source locator for use when walking a DOM tree during XML Schema validation.
   */
  private static class DOMLocator implements Locator {

    private int lineNumber;
    private int columnNumber;
    private String systemId;

    /**
     * Creates a new instance of the locator.
     *
     * @param url the URL of the source document
     */
    public DOMLocator(URL url) {
      this.systemId = url.toString();
    }

    /**
     * Sets the current DOM source node. If the node contains a source location, remember it.
     *
     * @param node the DOM source node
     */
    public void setNode(Node node) {
      SourceLocation location = (SourceLocation) node.getUserData(SourceLocation.class.getName());

      if (location == null) {
        lineNumber = -1;
        columnNumber = -1;
      } else {
        lineNumber = location.getLineNumber();
        columnNumber = location.getColumnNumber();
        if (location.getUrl() != null) {
          systemId = location.getUrl();
        }
      }
    }

    @Override
    public int getColumnNumber() {
      return columnNumber;
    }

    @Override
    public int getLineNumber() {
      return lineNumber;
    }

    @Override
    public String getPublicId() {
      return "";
    }

    @Override
    public String getSystemId() {
      return systemId;
    }

  }

}
