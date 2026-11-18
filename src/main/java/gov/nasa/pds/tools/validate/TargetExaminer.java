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
// $Id$
package gov.nasa.pds.tools.validate;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

/**
 * Class to examine if a Target maches a certain product type, either a bundle or a collection.
 *
 */
public class TargetExaminer {
  private static class Examination {
    boolean isBundle=false, isCollection=false, isDocument=false, isLabel=false;
    ParseFailure parseError;
  }
  private static class ParseFailure {
    String message;
    String systemId;
    int line = -1;
    int column = -1;

    ParseFailure(Exception error) {
      message = error.getMessage();
      for (Throwable cause = error; cause != null; cause = cause.getCause()) {
        if (cause instanceof SAXParseException) {
          SAXParseException saxError = (SAXParseException) cause;
          line = saxError.getLineNumber();
          column = saxError.getColumnNumber();
          systemId = saxError.getSystemId();
          message = saxError.getMessage();
          break;
        } else if (cause instanceof TransformerException) {
          SourceLocator locator = ((TransformerException) cause).getLocator();
          if (locator != null && locator.getLineNumber() > 0) {
            line = locator.getLineNumber();
            column = locator.getColumnNumber();
            systemId = locator.getSystemId();
          }
        }
      }
    }
  }
  final private static Cache<String,Examination> examined = CacheBuilder.newBuilder().softValues().build();
  final private static Logger LOG = LoggerFactory.getLogger(TargetExaminer.class);
  final private static String BUNDLE_NODE_TAG = "Product_Bundle";
  final private static String COLLECTION_NODE_TAG = "Product_Collection";
  final private static String DOCUMENT_NODE_TAG = "Product_Document";

  /**
   * Check if a given url is of Bundle type.
   *
   * @param url the url of file to check.
   * @return true if the given url is of Bundle type.
   */
  public static boolean isTargetBundleType(URL url) { return isTargetBundleType(url, false); }
  public static boolean isTargetBundleType(URL url, boolean ignoreErrors) {
    return TargetExaminer.examine(url, ignoreErrors).isBundle;
  }

  /**
   * Check if a given url is of Collection type.
   *
   * @param url the url of file to check.
   * @return true if the given url is of Collection type.
   */
  public static boolean isTargetCollectionType(URL url) { return isTargetCollectionType(url,false); }
  public static boolean isTargetCollectionType(URL url, boolean ignoreErrors) {
    return TargetExaminer.examine(url, ignoreErrors).isCollection;
  }

  public static boolean isTargetDocumentType (URL url) { return isTargetDocumentType(url,false); }
  public static boolean isTargetDocumentType (URL url, boolean ignoreErrors) {
    return TargetExaminer.examine(url, ignoreErrors).isDocument;
  }
  private static Examination examine (URL url, boolean ignoreErrors) {
    String key = url.toString();
    try {
      return TargetExaminer.examined.get(key, () -> doExamine(url, ignoreErrors));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private static Examination doExamine(URL url, boolean ignoreErrors) {
    Examination patient = new Examination();
    TargetExaminer.tagMatches(url, patient, ignoreErrors);
    return patient;
  }

  /**
   * Check if content of url contains a node that matches tagCheck.
   *
   * @param url the url of file to check.
   * @param tagCheck the tag of the node to check.
   * @return true if the given url contains a node that matches tagCheck.
   */
  private static void tagMatches(URL url, Examination patient, boolean ignoreErrors) {
    if (!"file".equalsIgnoreCase(url.getProtocol())) {
      LOG.error("Provided URL is not a a file: {}", url.toString());
      return;     
    }
    if (!FileUtils.toFile(url).exists()) {
      LOG.error("Provided file does not exist: {}", FileUtils.toFile(url));
      return;
    }

    if (!FileUtils.toFile(url).isFile()) {
      LOG.error("Provided file is not a regular file: {}", FileUtils.toFile(url));
      return;
    }
    try {
      InputSource source = Utility.getInputSourceByURL(url);
      SAXSource saxSource = new SAXSource(source);
      saxSource.setSystemId(url.toString());
      TreeInfo docInfo = LabelParser.parse(saxSource, ignoreErrors); // Parses a label.
      LOG.debug("tagMatches:docInfo {},{}", docInfo, docInfo.getClass());
      List<TinyNodeImpl> xmlModels = new ArrayList<>();
      try {
        XMLExtractor extractor = new XMLExtractor(docInfo.getRootNode());
        xmlModels = extractor.getNodesFromDoc("logical_identifier");
        patient.isLabel = true;
        xmlModels = extractor.getNodesFromDoc(TargetExaminer.BUNDLE_NODE_TAG);
        patient.isBundle = xmlModels.size() > 0;
        xmlModels = extractor.getNodesFromDoc(TargetExaminer.COLLECTION_NODE_TAG);
        patient.isCollection = xmlModels.size() > 0;
        xmlModels = extractor.getNodesFromDoc(TargetExaminer.DOCUMENT_NODE_TAG);
        patient.isDocument = xmlModels.size() > 0;
      } catch (Exception e) {
        if (!ignoreErrors) {
          LOG.error("Exception encountered in tagMatches:url {},{}", url, e.getMessage());
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      String path = url.getPath().toLowerCase(Locale.ROOT);
      if (path.endsWith(".xml") || path.endsWith(".lblx")) {
        patient.parseError = new ParseFailure(e);
      }
      if (!ignoreErrors) {
        LOG.error("Exception encountered in tagMatches:url {},{}", url, e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Return the content of the field within a node.
   *
   * @param url the url of file to check.
   * @param nodeCheck the tag of the node to check.
   * @param fieldCheck the tag of the field within node to check.
   * @param fieldCheck2 additional tag of the field within node to check.
   * @return the content of the field within a node as string.
   */
  public static ArrayList<String> getTargetContent(URL url, String nodeCheck, String fieldCheck,
      String fieldCheck2) {
    // Given a target URL, read the content and return the content of the given
    // fieldCheck(s) in a given nodeCheck.

    LOG.debug("getTargetContent:url,nodeCheck,fieldCheck {},{},{},{}", url, nodeCheck, fieldCheck,
        fieldCheck2);
    ArrayList<String> fieldContent = new ArrayList<>();

    try {
      InputSource source = Utility.getInputSourceByURL(url);
      SAXSource saxSource = new SAXSource(source);
      saxSource.setSystemId(url.toString());
      TreeInfo docInfo = LabelParser.parse(saxSource, false); // Parses a label.
      LOG.debug("getTargetContent:docInfo {},{}", docInfo, docInfo.getClass());
      List<TinyNodeImpl> xmlModels = new ArrayList<>();
      try {
        XMLExtractor extractor = new XMLExtractor(docInfo.getRootNode());
        xmlModels = extractor.getNodesFromDoc(nodeCheck);
        LOG.debug("getTargetContent:url,nodeCheck,xmlModels.size() {},{},{}", url, nodeCheck,
            xmlModels.size());
        if (xmlModels.size() > 0) {
          for (TinyNodeImpl xmlModel : xmlModels) {
            // It is possible that the tag we look for does not exist, function
            // getValueFromItem will return an empty string.
            if (!extractor.getValueFromItem(fieldCheck, xmlModel).equals("")) {
              fieldContent.add(extractor.getValueFromItem(fieldCheck, xmlModel));
            }
            LOG.debug("getTargetContent:url,fieldCheck,getValueFromItem {},{},[{}]", url,
                fieldCheck, extractor.getValueFromItem(fieldCheck, xmlModel));
            if (fieldCheck2 != null) {
              if (!extractor.getValueFromItem(fieldCheck2, xmlModel).equals("")) {
                fieldContent.add(extractor.getValueFromItem(fieldCheck2, xmlModel));
              }
            }
            // We only need one value.
            break;
          }
        }
      } catch (Exception e) {
        LOG.error("Exception encountered in getTargetContent:url {},{}", url, e.getMessage());
        e.printStackTrace();
      }
    } catch (Exception e) {
      LOG.error("Exception encountered in getTargetContent:url {},{}", url, e.getMessage());
      e.printStackTrace();
    }

    LOG.debug("getTargetContent:url,nodeCheck,fieldCheck,fieldContent {},{},{},{}", url, nodeCheck,
        fieldCheck, fieldContent);

    return (fieldContent);
  }
  public static boolean isTargetALabel(URL url) {
    return TargetExaminer.examine(url, true).isLabel;
  }

  private static ValidationProblem malformedXmlProblem(URL url, ParseFailure parseError) {
    if (parseError == null) {
      return null;
    }
    if (!"file".equalsIgnoreCase(url.getProtocol()) || !FileUtils.toFile(url).isFile()) {
      return null;
    }
    String filename = FileUtils.toFile(url).getName().toLowerCase(Locale.ROOT);
    boolean isProduct = filename.equals("bundle.xml") || filename.equals("collection.xml")
        || filename.startsWith("bundle_") || filename.startsWith("collection_");
    XMLInputFactory factory = XMLInputFactory.newFactory();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    try (InputStream input = url.openStream()) {
      XMLStreamReader reader = factory.createXMLStreamReader(input);
      try {
        while (reader.hasNext()) {
          if (reader.next() == XMLStreamConstants.START_ELEMENT) {
            isProduct = "http://pds.nasa.gov/pds4/pds/v1".equals(reader.getNamespaceURI())
                && reader.getLocalName().startsWith("Product_");
            break;
          }
        }
      } finally {
        reader.close();
      }
    } catch (Exception e) {
      LOG.debug("Unable to inspect XML diagnostic for {}", url, e);
    }
    boolean isError = isProduct || filename.endsWith(".lblx");
    ExceptionType severity = isError ? ExceptionType.ERROR : ExceptionType.WARNING;
    ProblemType type = isError ? ProblemType.BAD_LABEL_FILE_ERROR : ProblemType.BAD_LABEL_FILE_WARN;
    String message = "The label file cannot be parsed: " + parseError.message;
    if (parseError.systemId != null && !parseError.systemId.equals(url.toExternalForm())) {
      message += " (source: " + parseError.systemId + ")";
    }
    return new ValidationProblem(new ProblemDefinition(severity, type, message), url,
        parseError.line, parseError.column);
  }

  /**
   * Modifies the input array and returns it so the same call can be used
   * as a function or as a procedure.
   * @param targets
   * @return
   */
  public static List<Target> removeNonLabels(List<Target> targets, @Nullable ProblemListener listener) {
    ArrayList<Target> nons = new ArrayList<Target>();
    for (Target t : targets) {
      if (!isTargetALabel (t.getUrl())) {
        nons.add (t);
        if (listener != null) {
          String path = t.getUrl().getPath().toLowerCase(Locale.ROOT);
          ValidationProblem diagnostic = path.endsWith(".xml") || path.endsWith(".lblx")
              ? malformedXmlProblem(t.getUrl(), examine(t.getUrl(), true).parseError) : null;
          if (diagnostic != null) {
            listener.addProblem(diagnostic);
            continue;
          }
          ExceptionType et = t.getUrl().getPath().endsWith(".lblx") ? ExceptionType.ERROR : ExceptionType.WARNING;
          ProblemType pt = et == ExceptionType.ERROR ? ProblemType.BAD_LABEL_FILE_ERROR : ProblemType.BAD_LABEL_FILE_WARN;
          String msg = "The label file cannot be parsed or understood using PDS4 schema" + (et == ExceptionType.ERROR ? "." : " but it may be a data file being as a label.");
          listener.addProblem(new ValidationProblem(new ProblemDefinition(et,pt,msg), t.getUrl()));            
          }
        }
      }
    for (Target t : nons) {
      targets.remove (t);
    }
    return targets;
  }
}
