// Copyright 2006-2010, by the California Institute of Technology.
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
package gov.nasa.pds.validate.util;

import gov.nasa.pds.tools.util.VersionInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.Configuration;
import net.sf.saxon.event.ParseOptions;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tinytree.TinyElementImpl;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.xpath.XPathEvaluator;

import org.xml.sax.InputSource;

/**
 * Class to extract data from an XML file.
 */
public class XMLExtractor {

  /** The DOM source. */
  private DocumentInfo xml = null;

  /** The XPath evaluator object. */
  private XPathEvaluator xpath = null;

  /** Default namespace uri. */
  private static String defaultNamespaceUri =
    VersionInfo.getPDSDefaultNamespace(VersionInfo.getDefaultModelVersion());

  /** Namespace Context. */
  private static PDSNamespaceContext namespaceContext = null;

  /**
   * Constructor.
   *
   */
  public XMLExtractor() {
    xpath = new XPathEvaluator();
    xpath.getStaticContext().setDefaultElementNamespace(
        defaultNamespaceUri);
    if (namespaceContext != null) {
      xpath.getStaticContext().setNamespaceContext(namespaceContext);
    }
  }

  /**
   * Parse the given file.
   *
   * @param src An XML file.
   *
   * @throws XPathException If an error occurred while parsing the XML file.
   */
  public void parse(File src) throws XPathException {
    String uri = src.toURI().toString();
    Configuration configuration = xpath.getConfiguration();
    configuration.setLineNumbering(true);
    ParseOptions options = new ParseOptions();
    options.setErrorListener(new XMLErrorListener());
    xml = configuration.buildDocument(new SAXSource(new InputSource(uri)),
        options);
  }

  /**
   * Parse the given file.
   *
   * @param src An XML file.
   *
   * @throws XPathException If an error occurred while parsing the XML file.
   */
  public void parse(String src) throws XPathException {
    parse(new File(src));
  }

  /**
   * Sets the default namespace URI.
   *
   * @param uri A URI.
   */
  public static void setDefaultNamespace(String uri) {
    //xpath.getStaticContext().setDefaultElementNamespace(uri);
    defaultNamespaceUri = uri;
  }

  /**
   * Sets the Namespace Context to support handling of namespaces
   * in XML documents.
   *
   * @param context The NamespaceContext object.
   */
  public static void setNamespaceContext(PDSNamespaceContext context) {
    //xpath.getStaticContext().setNamespaceContext(context);
    namespaceContext = context;
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
  public String getValueFromDoc(String expression)
  throws XPathExpressionException, XPathException {
    return getValueFromItem(expression, xpath.setSource(xml));
  }

  /**
   * Gets the value of the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the
   * XPath expression.
   *
   * @return The resulting value or null if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public String getValueFromItem(String expression, Object item)
  throws XPathExpressionException {
    try {
      return xpath.evaluate(expression, item);
    } catch (XPathExpressionException x) {
      throw new XPathExpressionException("Bad xpath expression: "
          + expression);
    }
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
  public TinyElementImpl getNodeFromDoc(String expression)
  throws XPathExpressionException {
    return getNodeFromItem(expression, xml);
  }

  /**
   * Gets a Node object from the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the
   * XPath expression.
   *
   * @return A Node object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public TinyElementImpl getNodeFromItem(String expression, Object item)
  throws XPathExpressionException {
    try {
      return (TinyElementImpl) xpath.evaluate(expression, item,
          XPathConstants.NODE);
    } catch (XPathExpressionException x) {
      throw new XPathExpressionException("Bad xpath expression: "
          + expression);
    }
  }

  /**
   * Gets the values of the given expression.
   *
   * @param expression An XPath expression.
   *
   * @return The resulting values or null if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<String> getValuesFromDoc(String expression)
  throws XPathExpressionException {
    return getValuesFromItem(expression, xml);
  }

  /**
   * Gets the values of the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the
   * XPath expression.
   *
   * @return The resulting values or null if nothing was found.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<String> getValuesFromItem(String expression, Object item)
  throws XPathExpressionException {
    List<String> vals = new ArrayList<String>();
    List<TinyElementImpl> nList = null;
    try {
      nList = (List<TinyElementImpl>) xpath.evaluate(
        expression, item, XPathConstants.NODESET);
    } catch (XPathExpressionException x) {
      throw new XPathExpressionException("Bad xpath expression: "
          + expression);
    }
    if (nList != null) {
      for (int i = 0, sz = nList.size(); i < sz; i++) {
        TinyElementImpl aNode = nList.get(i);
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
  public DocumentInfo getDocNode() throws XPathException {
    return xpath.setSource(xml).getDocumentRoot();
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
  public List<TinyElementImpl> getNodesFromDoc(String expression)
  throws XPathExpressionException {
    return getNodesFromItem(expression, xml);
  }

  /**
   * Gets Node objects from the given expression.
   *
   * @param expression An XPath expression.
   * @param item The starting point from which to evaluate the
   * XPath expression.
   *
   * @return A NodeList object.
   *
   * @throws XPathExpressionException If the given expression was malformed.
   */
  public List<TinyElementImpl> getNodesFromItem(String expression, Object item)
  throws XPathExpressionException {
    try {
      return (List<TinyElementImpl>) xpath.evaluate(
            expression, item, XPathConstants.NODESET);
    } catch (XPathExpressionException x) {
      throw new XPathExpressionException("Bad xpath expression: "
          + expression);
    }
  }
}
