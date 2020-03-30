//	Copyright 2009-2018, by the California Institute of Technology.
//	ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//	Any commercial use must be negotiated with the Office of Technology
//	Transfer at the California Institute of Technology.
//
//	This software is subject to U. S. export control laws and regulations
//	(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//	is subject to U.S. export control laws and regulations, the recipient has
//	the responsibility to obtain export licenses or other export authority as
//	may be required before exporting such information to foreign countries or
//	providing access to foreign nationals.
//
//	$Id: LabelValidator.java 16821 2018-06-25 15:01:49Z mcayanan $
//

package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.label.validate.DefaultDocumentValidator;
import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.tools.label.validate.ExternalValidator;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemContainer;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
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
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.om.DocumentInfo;

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

/**
 * This class is responsible for providing utility functions for validating PDS
 * XML Labels.
 *
 * @author pramirez
 *
 */
public class LabelValidator {
  private Map<String, Boolean> configurations = new HashMap<String, Boolean>();
  private List<URL> userSchemaFiles;
  private List<URL> userSchematronFiles;
  private List<Transformer> userSchematronTransformers;
  private XMLReader cachedParser;
  private ValidatorHandler cachedValidatorHandler;
  private List<Transformer> cachedSchematron;
  private XMLCatalogResolver resolver;
  private Boolean useLabelSchema;
  private Boolean useLabelSchematron;
  private Boolean skipProductValidation;
  private Map<String, Transformer> cachedLabelSchematrons;

  public static final String SCHEMA_CHECK = 
      "gov.nasa.pds.tools.label.SchemaCheck";
  public static final String SCHEMATRON_CHECK = 
      "gov.nasa.pds.tools.label.SchematronCheck";

  private List<ExternalValidator> externalValidators;
  private List<DocumentValidator> documentValidators;
  private CachedEntityResolver cachedEntityResolver;
  private CachedLSResourceResolver cachedLSResolver;
  private SAXParserFactory saxParserFactory;
  private DocumentBuilder docBuilder;
  private SchemaFactory schemaFactory;
  private Schema validatingSchema;
  private SchematronTransformer schematronTransformer;
  private XPathFactory xPathFactory;

  /**
   * Default constructor.
   *
   * @throws ParserConfigurationException If there was an error setting up
   * the configuration of the parser that is reposnible for doing the
   * label validation.
   *
   * @throws TransformerConfigurationException If there was an error setting
   * up the Transformer responsible for doing the transformations of the
   * schematrons.
   */
  public LabelValidator() throws ParserConfigurationException,
  TransformerConfigurationException {
    this.configurations.put(SCHEMA_CHECK, true);
    this.configurations.put(SCHEMATRON_CHECK, true);
    cachedParser = null;
    cachedValidatorHandler = null;
    cachedSchematron = new ArrayList<Transformer>();
    userSchemaFiles = null;
    userSchematronFiles = null;
    userSchematronTransformers = new ArrayList<Transformer>();
    resolver = null;
    externalValidators = new ArrayList<ExternalValidator>();
    documentValidators = new ArrayList<DocumentValidator>();
    useLabelSchema = false;
    useLabelSchematron = false;
    cachedLabelSchematrons = new HashMap<String, Transformer>();
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
      saxParserFactory.setFeature(
          "http://apache.org/xml/features/xinclude/fixup-base-uris", false);
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

    xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
  }

  /**
   * Pass in a list of schemas to validate against.
   *
   * @param schemaFiles A list of schema URLs.
   *
   */
  public void setSchema(List<URL> schemaFiles) {
    this.userSchemaFiles = schemaFiles;
  }

  /**
   * Pass in a list of transformed schematrons to validate
   * against.
   *
   * @param schematrons A list of transformed schematrons.
   */
  public void setSchematrons(List<Transformer> schematrons) {
    userSchematronTransformers = schematrons;
  }

  /**
   * Pass in a hash map of schematron URLs to its transformed
   * schematron object. This is used when validating a label
   * against it's referenced schematron.
   *
   * @param schematronMap
   */
  public void setLabelSchematrons(
      Map<String, Transformer> schematronMap) {
    cachedLabelSchematrons = schematronMap;
  }

  /**
   * Pass in a list of schematron files to validate against.
   *
   * @param schematronFiles A list of schematron URLs.
   */
  public void setSchematronFiles(List<URL> schematronFiles) {
    userSchematronFiles = schematronFiles;
  }

  /**
   * Pass in a list of Catalog files to use during the validation
   * step.
   *
   * @param catalogFiles
   */
  public void setCatalogs(String[] catalogFiles) {  
    resolver = new XMLCatalogResolver();
    resolver.setPreferPublic(true);
    resolver.setCatalogList(catalogFiles);
    useLabelSchematron = true;
  }
  
  public XMLCatalogResolver getCatalogResolver() {
    return this.resolver;
  }

  private List<StreamSource> loadSchemaSources(List<URL> schemas)
      throws IOException, SAXException {
    List<StreamSource> sources = new ArrayList<StreamSource>();
    String externalLocations = "";
    for (URL schema : schemas) {
      LSInput input = cachedLSResolver.resolveResource("", "", "",
          schema.toString(), schema.toString());
      StreamSource streamSource = new StreamSource(
          input.getByteStream());
      streamSource.setSystemId(schema.toString());
      sources.add(streamSource);
      InputSource inputSource = new InputSource(
          input.getByteStream());
      inputSource.setSystemId(input.getSystemId());
      try {
      XMLExtractor extractor = new XMLExtractor(inputSource);
      String namespace = extractor.getTargetNamespace();
      if (!namespace.isEmpty()) {
        externalLocations += namespace + " " + schema.toString() + "\n";
      }
      inputSource.getByteStream().reset();
      } catch (Exception e) {
        throw new IOException("Error occurred while getting the "
            + "targetNamespace for schema '" + schema.toString() + "': "
            + e.getMessage());
      }
    }
    schemaFactory.setProperty(
        "http://apache.org/xml/properties/schema/external-schemaLocation",
        externalLocations);
    return sources;
  }

  private List<StreamSource> loadSchemaSources(String[] schemaFiles) {
    List<StreamSource> sources = new ArrayList<StreamSource>();
    for (String schemaFile : schemaFiles) {
      sources.add(new StreamSource(schemaFile));
    }
    return sources;
  }

  public synchronized void validate(ProblemHandler handler, File labelFile)
  throws SAXException, IOException, ParserConfigurationException,
  TransformerException, MissingLabelSchemaException {
    validate(handler, labelFile.toURI().toURL());
  }

  /**
   * Validates the label against schema and schematron constraints.
   *
   * @param handler
   *          a handler to receive errors during the validation
   * @param url
   *          label to validate
   *
   * @throws SAXException if there are parsing exceptions
   * @throws IOException if there are I/O errors during the parse
   * @throws ParserConfigurationException if the parser configuration is 
   * invalid
   * @throws TransformerException if there is an error during Schematron 
   * transformation
   * @throws MissingLabelSchemaException if the label schema cannot be found
   */
  public synchronized void validate(ProblemHandler handler, URL url)
      throws SAXException, IOException, ParserConfigurationException,
      TransformerException, MissingLabelSchemaException {

    parseAndValidate(handler, url);

  }

  /**
   * Parses and validates a label against the schema and Schematron files,
   * and returns the parsed XML.
   *
   * @param handler an problem handler to receive errors during the 
   * validation
   * @param url the URL of the label to validate
   * @return the XML document represented by the label
   * @throws SAXException if there are parsing exceptions
   * @throws IOException if there are I/O errors during the parse
   * @throws ParserConfigurationException if the parser configuration is invalid
   * @throws TransformerException if there is an error during Schematron transformation
   * @throws MissingLabelSchemaException if the label schema cannot be found
   */
  public synchronized Document parseAndValidate(ProblemHandler handler, URL url)
      throws SAXException, IOException, ParserConfigurationException,
      TransformerException, MissingLabelSchemaException {
    List<String> labelSchematronRefs = new ArrayList<String>();
    Document xml = null;
  
    // Are we perfoming schema validation?
    if (performsSchemaValidation()) {
      createParserIfNeeded(handler);

      // Do we need this to clear the cache?
     
      if (useLabelSchema) {
        cachedValidatorHandler = schemaFactory.newSchema().newValidatorHandler();
      } else {
        cachedValidatorHandler = validatingSchema.newValidatorHandler();
      }    
      
      // Capture messages in a container
      if (handler != null) {
        ErrorHandler eh = new LabelErrorHandler(handler);
        cachedParser.setErrorHandler(eh);
        cachedValidatorHandler.setErrorHandler(eh);

      }
      // Finally parse and validate the file
      xml = docBuilder.newDocument();
      cachedParser.setContentHandler(new DocumentCreator(xml));
      cachedParser.parse(Utility.openConnection(url));

      DOMLocator locator = new DOMLocator(url);
      cachedValidatorHandler.setDocumentLocator(locator);
      if (resolver != null) {
        cachedValidatorHandler.setResourceResolver(resolver);
        resolver.setProblemHandler(handler);
      } else {
        cachedValidatorHandler.setResourceResolver(cachedLSResolver);
      }
      
      if (!skipProductValidation) {
    	  walkNode(xml, cachedValidatorHandler, locator);
      }

      // If validating against the label supplied schema, check
      // if the xsi:schemalocation attribute was defined in the label.
      // If it is not found, throw an exception.
      if (useLabelSchema) {
        Element root = xml.getDocumentElement();
        if (!root.hasAttribute("xsi:schemaLocation")) {
          throw new MissingLabelSchemaException(
              "No schema(s) specified in the label.");
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

    // Validate with any schematron files we have
    if (performsSchematronValidation()) {
      // Look for schematron files specified in a label
      if (useLabelSchematron) {
        labelSchematronRefs = getSchematrons(xml.getChildNodes(), url,
            handler);
      }
      if (cachedSchematron.isEmpty()) {
        if (useLabelSchematron) {
          cachedSchematron = loadLabelSchematrons(labelSchematronRefs, url,
              handler);
        } else {
          if (!userSchematronTransformers.isEmpty()) {
            cachedSchematron = userSchematronTransformers;
          } else if (userSchematronFiles != null) {
            List<Transformer> transformers = new ArrayList<Transformer>();
            for (URL schematron : userSchematronFiles) {
              StreamSource source = new StreamSource(schematron.toString());
              source.setSystemId(schematron.toString());
              Transformer transformer = schematronTransformer.transform(
                  source, handler);
              transformers.add(transformer);
            }
            cachedSchematron = transformers;
          }
        }
      } else {
        // If there are cached schematrons....
        if (useLabelSchematron) {
          if (!userSchematronTransformers.isEmpty()) {
            cachedSchematron = userSchematronTransformers;
          } else {
            cachedSchematron = loadLabelSchematrons(labelSchematronRefs, url,
              handler);
          }
        }
      }

      for (Transformer schematron : cachedSchematron) {
        DOMResult result = new DOMResult();
        DOMSource domSource = new DOMSource(xml);
        domSource.setSystemId(url.toString());
        // Apply the rules specified in the schematron file
        schematron.transform(domSource, result);
        // Output is svrl:schematron-output document
        // Select out svrl:failed-assert nodes and put into problem container
        Document reportDoc = (Document) result.getNode();
        NodeList nodes = reportDoc.getElementsByTagNameNS(
            "http://purl.oclc.org/dsdl/svrl", "failed-assert");
        for (int i = 0; i < nodes.getLength(); i++) {
          Node node = nodes.item(i);
          // Add an error for each failed asssert
          handler.addProblem(processFailedAssert(url, node, xml));
        }
      }
    }
    
    // issue_42: skip product-level validation when the flag is on
    if (!skipProductValidation) {
      if (!externalValidators.isEmpty()) {
        // Perform any other additional checks that were added
        for(ExternalValidator ev : externalValidators) {
          ev.validate(handler, url);
        }
      }

      // Perform any additional checks that were added
      if (!documentValidators.isEmpty()) {
        SAXSource saxSource = new SAXSource(Utility.openConnection(url));
        saxSource.setSystemId(url.toString());
        DocumentInfo docInfo = LabelParser.parse(saxSource);
        for (DocumentValidator dv : documentValidators) {
          dv.validate(handler, docInfo);
        }
      }
    }

    return xml;
  }

  private void createParserIfNeeded(ProblemHandler handler)
      throws SAXNotRecognizedException, SAXNotSupportedException, SAXException,
      IOException, ParserConfigurationException {
    // Do we have a schema we have loaded previously?
    if (cachedParser == null) {
      // If catalog is used, allow resources to be loaded for schemas
      // and the document parser
      if (resolver != null) {
        schemaFactory.setProperty(
            "http://apache.org/xml/properties/internal/entity-resolver",
            resolver);
      }
      // Allow errors that happen in the schema to be logged there
      if (handler != null) {
        schemaFactory.setErrorHandler(new LabelErrorHandler(handler));
        cachedLSResolver = new CachedLSResourceResolver(handler);
        schemaFactory.setResourceResolver(cachedLSResolver);
      } else {
        cachedLSResolver = new CachedLSResourceResolver();
        schemaFactory.setResourceResolver(cachedLSResolver);
      }
      // Time to load schema that will be used for validation
      if (userSchemaFiles != null) {
        // User has specified schema files to use
        validatingSchema = schemaFactory.newSchema(loadSchemaSources(
            userSchemaFiles).toArray(new StreamSource[0]));
      } else if (resolver == null) {
        if (useLabelSchema) {
          validatingSchema = schemaFactory.newSchema();
        } else {
          // Load from user specified external directory
          validatingSchema = schemaFactory.newSchema(loadSchemaSources(
              VersionInfo.getSchemasFromDirectory().toArray(new String[0]))
              .toArray(new StreamSource[0]));
        }
      } else {
        // We're only going to use the catalog to validate against.
        validatingSchema = schemaFactory.newSchema();
      }

      cachedParser = saxParserFactory.newSAXParser().getXMLReader();
      cachedValidatorHandler = validatingSchema.newValidatorHandler();
      if (resolver != null) {
        cachedParser.setEntityResolver(resolver);
        docBuilder.setEntityResolver(resolver);
      } else if (useLabelSchema) {
        cachedParser.setEntityResolver(cachedEntityResolver);
      }
    } else {
      //TODO: This code doesn't look right. It says that if we have
      //  a cached parser, but we are using the label schema, then
      //  create a new parser. It seems like the creation is handled
      //  properly at the end of the if-part, above, so we should
      //  do nothing if we already have a cached parser.

      // Create a new instance of the DocumentBuilder if validating
      // against a label's schema.
      if (useLabelSchema) {
      	cachedParser = saxParserFactory.newSAXParser().getXMLReader();
      	cachedValidatorHandler = schemaFactory.newSchema().newValidatorHandler();
      	cachedParser.setEntityResolver(cachedEntityResolver);
      }
    }
  }

  public void validate(File labelFile) throws SAXException, IOException,
      ParserConfigurationException, TransformerException,
      MissingLabelSchemaException {
    validate(null, labelFile);
  }

  /**
   * Walks a DOM subtree starting at the given node, invoking the
   * {@link ContentHandler} callback methods as if the document
   * were being parsed by a SAX parser. Also updates the current
   * location using the {@link DOMLocator} so that error messages
   * generated by the content handler will have the proper source
   * location.
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
      for (int i=0; i < map.getLength(); ++i) {
        Attr attr = (Attr) map.item(i);
        attributes.addAttribute(attr.getNamespaceURI(), attr.getLocalName(), attr.getNodeName(), "", attr.getNodeValue());
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
  private void walkChildren(Node node, ContentHandler handler, DOMLocator locator) throws SAXException {
    NodeList nodes = node.getChildNodes();
    for (int i=0; i < nodes.getLength(); ++i) {
      walkNode(nodes.item(i), handler, locator);
    }
  }

  public List<String> getSchematrons(NodeList nodeList, URL url,
      ProblemHandler handler) {
    List<String> results = new ArrayList<String>();

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
              schematronRef = new URL(Utility.makeAbsolute(parent.toString(), schematronRef.toString()));
            } catch (MalformedURLException ue) {
              // The schematron specification value does not appear to be
              // a URL. Assume a local reference to the schematron and
              // attempt to resolve it.
              try {
                schematronRef = new URL(url, value);
              } catch (MalformedURLException mue) {
                handler.addProblem(
                    new ValidationProblem(
                        new ProblemDefinition(
                            ExceptionType.ERROR,
                            ProblemType.SCHEMATRON_ERROR,
                            "Cannot resolve schematron specification '"
                                + value + "': " + mue.getMessage()),
                        url));
                continue;
              }
            }
            results.add(schematronRef.toString());
          }
        }
      }
    }
    return results;
  }

  private List<Transformer> loadLabelSchematrons(List<String> schematronSources,
      URL url, ProblemHandler handler) {
    List<Transformer> transformers = new ArrayList<Transformer>();
    for (String source : schematronSources) {
      try {
        if (resolver != null) {
          try {
            String absoluteUrl = Utility.makeAbsolute(Utility.getParent(url).toString(), source);  
            String resolvedUrl = resolver.resolveSchematron(absoluteUrl);
            if (resolvedUrl == null) {
              throw new Exception("'" + source + "' was not resolvable through the catalog file.");
            } else {
              source = resolvedUrl;
            }
          } catch (IOException io) {
            throw new Exception("Error while resolving '" + source.toString()
              + "' through the catalog: " + io.getMessage());
          }
        }
        Transformer transformer = cachedLabelSchematrons.get(source);
        if (transformer != null) {
          transformers.add(transformer);
        } else {
          URL sourceUrl = new URL(source);
          try {
            transformer = schematronTransformer.transform(sourceUrl);
            cachedLabelSchematrons.put(source, transformer);
          } catch (TransformerException te) {
            throw new Exception("Schematron '" + source + "' error: "
                + te.getMessage());
          }
        }
      } catch (Exception e) {
        String message = "Error occurred while loading schematron: "
            + e.getMessage();
        handler.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR,
                ProblemType.SCHEMATRON_ERROR,
                message), 
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
   * @param doc the original document that was being validated, used to obtain line numbers, or null for no document
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
      String type = node.getAttributes().getNamedItem("role")
      .getTextContent();
      if ("warn".equalsIgnoreCase(type) ||
          "warning".equalsIgnoreCase(type)) {
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
      XPath documentPath = xPathFactory.newXPath();
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
    
    return new ValidationProblem(
        new ProblemDefinition(
            exceptionType, 
            problemType,
            message),
       sourceUrl,
       lineNumber,
       columnNumber
    );

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
  }
  
  public void setSkipProductValidation(Boolean flag) {
      this.skipProductValidation = flag;
  }

  public Boolean getConfiguration(String key) {
    return this.configurations.containsKey(key) ? this.configurations.get(key)
        : false;
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

  public static void main(String[] args) throws Exception {
    LabelValidator lv = new LabelValidator();
    lv.setCatalogs(new String[]{args[1]});
    ProblemContainer container = new ProblemContainer();
    lv.validate(container, new File(args[0]));
    for (ValidationProblem problem : container.getProblems()) {
      System.out.println(problem.getMessage());
    }
  }

  /**
   * Implements a source locator for use when walking a DOM tree
   * during XML Schema validation.
   */
  private static class DOMLocator implements Locator {

    private URL url;
    private int lineNumber;
    private int columnNumber;
    private String systemId;

    /**
     * Creates a new instance of the locator.
     *
     * @param url the URL of the source document
     */
    public DOMLocator(URL url) {
      this.url = url;
      this.systemId = url.toString();
    }

    /**
     * Sets the current DOM source node. If the node contains
     * a source location, remember it.
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
