// Copyright 2009-2016, by the California Institute of Technology.
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
//
package gov.nasa.pds.tools.label;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Implements a parse handler for a SAX parser that builds a DOM tree with the parse result and
 * stores the line number information in each created node.
 */
class DocumentCreator implements ContentHandler {

  private Document document;
  private Node currentNode;
  private Locator documentLocator;

  /**
   * Creates a new instance of the handler.
   *
   * @param document the document into which we put the parse result
   * @param exceptionContainer the exception container to hold errors and warnings
   */
  public DocumentCreator(Document document) {
    this.document = document;
    currentNode = document;
  }

  /**
   * Gets the result document.
   *
   * @return the DOM result document
   */
  public Document getDocument() {
    return document;
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    this.documentLocator = locator;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {

    Element element = document.createElementNS(uri, qName);
    currentNode.appendChild(element);
    currentNode = element;
    for (int i = 0; i < attributes.getLength(); ++i) {
      Attr attr = document.createAttributeNS(attributes.getURI(i), attributes.getQName(i));
      attr.setValue(attributes.getValue(i));
      setLocation(attr);
      element.setAttributeNodeNS(attr);
    }
    setLocation(element);
  }

  private void setLocation(Node node) {
    node.setUserData(SourceLocation.class.getName(),
        new SourceLocation(documentLocator.getLineNumber(), documentLocator.getColumnNumber(),
            documentLocator.getSystemId()),
        null);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    currentNode = currentNode.getParentNode();
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    String value = new String(ch, start, length);
    if (!value.trim().isEmpty()) {
      Text text = document.createTextNode(value);
      setLocation(text);
      currentNode.appendChild(text);
    }
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    String value = new String(ch, start, length);
    if (!value.isEmpty()) {
      Text text = document.createTextNode(value);
      setLocation(text);
      currentNode.appendChild(text);
    }
  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
    ProcessingInstruction pi = document.createProcessingInstruction(target, data);
    setLocation(pi);
    currentNode.appendChild(pi);
  }

  @Override
  public void startDocument() throws SAXException {
    // Ignore - don't need to handle.
  }

  @Override
  public void endDocument() throws SAXException {
    // Ignore - don't need to handle.
  }

  @Override
  public void skippedEntity(String name) throws SAXException {
    // Ignore - don't need to handle.
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    // Ignore - don't need to handle.
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {
    // Ignore - don't need to handle.
  }

}
