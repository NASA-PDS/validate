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
package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.InputSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.NamespaceUri;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.tiny.TinyNodeImpl;
import net.sf.saxon.xpath.XPathEvaluator;

/**
 * Class to extract data from an XML file.
 */
public class XMLExtractor {
  /** The DOM source. */
  private Source xml = null;

  /** The XPath evaluator object. */
  private XPathEvaluator xpath = null;
  private TreeInfo ti = null;
  public static final String SCHEMA_LOCATION_XPATH = "//*/@xsi:schemaLocation";

  public static final String XML_MODEL_XPATH = "/processing-instruction('xml-model')";

  public static final String DEFAULT_NAMESPACE = "//*/namespace::*[name()='']";

  public static final String TARGET_NAMESPACE = "//*/@targetNamespace";

  /**
   * Constructor.
   *
   * @param xml A parsed XML document.
   *
   * @throws XPathException If an error occurred while parsing the given xml file.
   * @throws XPathExpressionException If an error occurred while setting up the default namespace.
   *
   */
  public XMLExtractor(Source xml) throws XPathExpressionException, XPathException {
    this.xml = xml;
    this.xpath = new XPathEvaluator();
    Configuration configuration = xpath.getConfiguration();

    configuration.setLineNumbering(true);
    configuration.setXIncludeAware(Utility.supportXincludes());
    String definedNamespace = getValueFromDoc("namespace-uri(/*)");
    xpath.getStaticContext().setDefaultElementNamespace(NamespaceUri.of (definedNamespace));
  }

  /**
   * Constructor.
   *
   * @param xmlFile An xml file.
   *
   * @throws XPathException If an error occurred while parsing the given xml file.
   * @throws XPathExpressionException If an error occurred while setting up the default namespace.
   */
  public XMLExtractor(URL url) throws XPathException, XPathExpressionException {
    xpath = new XPathEvaluator();
    Configuration configuration = xpath.getConfiguration();
    configuration.setLineNumbering(true);
    configuration.setXIncludeAware(Utility.supportXincludes());
    ParseOptions options = new ParseOptions();
    options.withErrorHandler(new XMLErrorListener());
    try {
      ti = configuration.buildDocumentTree(new SAXSource(Utility.getInputSourceByURL(url)), options);
      xml = ti.getRootNode();
      String definedNamespace = getValueFromDoc("namespace-uri(/*)");
      xpath.getStaticContext().setDefaultElementNamespace(NamespaceUri.of (definedNamespace));
    } catch (IOException io) {
      throw new XPathException("Error while reading input: " + io.getMessage());
    }
  }

  public XMLExtractor(InputSource source) throws XPathException, XPathExpressionException {
    xpath = new XPathEvaluator();
    Configuration configuration = xpath.getConfiguration();
    configuration.setLineNumbering(true);
    configuration.setXIncludeAware(Utility.supportXincludes());
    ParseOptions options = new ParseOptions();
    options.withErrorHandler(new XMLErrorListener());
    ti = configuration.buildDocumentTree(new SAXSource(source), options);
    xml = ti.getRootNode();
    String definedNamespace = getValueFromDoc("namespace-uri(/*)");
    xpath.getStaticContext().setDefaultElementNamespace(NamespaceUri.of(definedNamespace));
  }

  public XMLExtractor(File file)
      throws XPathException, XPathExpressionException, MalformedURLException {
    this(file.toURI().toURL());
  }

  /**
   * Constructor.
   *
   * @param xmlFile An xml file.
   *
   * @throws XPathException If an error occurred while parsing the given xml file.
   * @throws XPathExpressionException If an error occurred while setting up the default namespace.
   * @throws MalformedURLException
   */
  public XMLExtractor(String url)
      throws XPathException, XPathExpressionException, MalformedURLException {
    this(new URL(url));
  }

  /**
   * Gets the value of the given expression.
   *
   * @param expression An XPath expression.
   *
   * @return The resulting value or null if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   * @throws XPathException
   */
  public String getValueFromDoc(String expression) throws XPathExpressionException, XPathException {
    if (ti == null) {
      ti = xpath.getConfiguration().buildDocumentTree(xml);
    }
    return getValueFromItem(expression, ti); // xpath.setSource(xml));
  }

  /**
   * Gets the value of the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the XPath expression.
   *
   * @return The resulting value or null if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public String getValueFromItem(String expression, Object item) throws XPathExpressionException {
    return xpath.evaluate(expression, item);
  }

  /**
   * Gets a Node object from the given expression.
   *
   * @param expression An XPath expression.
   *
   * @return A Node object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public TinyNodeImpl getNodeFromDoc(String expression)
      throws XPathExpressionException, XPathException {
    return getNodeFromItem(expression, xpath.getConfiguration().buildDocumentTree(xml));
  }

  /**
   * Gets a Node object from the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the XPath expression.
   *
   * @return A Node object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public TinyNodeImpl getNodeFromItem(String expression, Object item)
      throws XPathExpressionException {
    return (TinyNodeImpl) xpath.evaluate(expression, item, XPathConstants.NODE);
  }

  /**
   * Gets the values of the given expression.
   *
   * @param expression An XPath expression.
   *
   * @return The resulting values or an empty list if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<String> getValuesFromDoc(String expression)
      throws XPathExpressionException, XPathException {
    return getValuesFromItem(expression, xpath.getConfiguration().buildDocumentTree(xml));
  }

  /**
   * Gets the values of the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the XPath expression.
   *
   * @return The resulting values or an empty list if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<String> getValuesFromItem(String expression, Object item)
      throws XPathExpressionException {
    List<String> vals = new ArrayList<>();
    List<TinyNodeImpl> nList =
        (List<TinyNodeImpl>) xpath.evaluate(expression, item, XPathConstants.NODESET);
    if (nList != null) {
      for (int i = 0, sz = nList.size(); i < sz; i++) {
        TinyNodeImpl aNode = nList.get(i);
        vals.add(aNode.getStringValue());
      }
    }
    return vals;
  }

  /**
   * Gets the document node of the XML document.
   *
   * @return The Document Node.
   * @throws XPathException
   */
  public Source getDocNode() throws XPathException {
    return xml;
  }

  /**
   * Gets Node objects from the given expression.
   *
   * @param expression An XPath expression.
   *
   * @return A NodeList object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<TinyNodeImpl> getNodesFromDoc(String expression)
      throws XPathExpressionException, XPathException {
    return getNodesFromItem(expression, xpath.getConfiguration().buildDocumentTree(xml));
  }

  /**
   * Gets Node objects from the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the XPath expression.
   *
   * @return A NodeList object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<TinyNodeImpl> getNodesFromItem(String expression, Object item)
      throws XPathExpressionException {
    return (List<TinyNodeImpl>) xpath.evaluate(expression, item, XPathConstants.NODESET);
  }

  /**
   *
   * @return Returns the schemalocation attribute value from the label. If it was not found, a null
   *         value will be returned.
   *
   * @throws XPathExpressionException
   * @throws XPathException
   */
  public String getSchemaLocation() throws XPathExpressionException, XPathException {
    return getValueFromDoc(SCHEMA_LOCATION_XPATH);
  }

  /**
   * @return Returns the schematron specifications in the label.
   * @throws XPathExpressionException
   */
  public List<String> getXmlModels() throws XPathExpressionException, XPathException {
    return getValuesFromDoc(XML_MODEL_XPATH);
  }

  /**
   *
   * @return Returns the 'xmlns' attribute of the document (default namespace)
   * @throws XPathExpressionException
   * @throws XPathException
   */
  public String getDefaultNamespace() throws XPathExpressionException, XPathException {
    return getValueFromDoc(DEFAULT_NAMESPACE);
  }

  /**
   *
   * @return Returns the 'targetNamespace' attribute of the document (default namespace)
   * @throws XPathExpressionException
   * @throws XPathException
   */
  public String getTargetNamespace() throws XPathExpressionException, XPathException {
    return getValueFromDoc(TARGET_NAMESPACE);
  }

  public String getSystemId() {
    return this.xml.getSystemId();
  }
}
