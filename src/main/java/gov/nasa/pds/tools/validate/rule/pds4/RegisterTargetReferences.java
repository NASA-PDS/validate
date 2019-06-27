// Copyright 2006-2017, by the California Institute of Technology.
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

import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Registers file references from the label, as well as an implied
 * reference to the label itself.
 */
public class RegisterTargetReferences extends AbstractValidationRule {

  private static final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";

  private static final String FILE_NAMES_PATH
      = "//*:File[namespace-uri()='" + PDS4_NS + "']"
      + "/*:file_name[namespace-uri()='" + PDS4_NS + "']";

  private static final String DOCUMENT_FILE_NAMES_PATH
      = "//*:Document_File[namespace-uri()='" + PDS4_NS + "']"
      + "/*:file_name[namespace-uri()='" + PDS4_NS + "']";

  private XPathFactory xPathFactory;

  private static final Pattern LABEL_PATTERN = Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE);
  
  /**
   * Creates a new instance.
   */
  public RegisterTargetReferences() {
      xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
  }

  @Override
  public boolean isApplicable(String location) {
    Matcher matcher = LABEL_PATTERN.matcher(FilenameUtils.getName(location));
    // The rule is applicable if a label has been parsed.
    return getContext().containsKey(PDS4Context.LABEL_DOCUMENT) || matcher.matches();
  }

  @ValidationTest
  public void registerFileReferences() throws XPathExpressionException {
    // We have a reference to the current target, since it is a label.
    URL target = getTarget();
    getRegistrar().setTargetIsLabel(target.toString(), true);
    
    if (!getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      return;
    }
    
    Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class);
    DOMSource source = new DOMSource(label);

    NodeList fileNames = (NodeList) xPathFactory.newXPath().evaluate(FILE_NAMES_PATH, source, XPathConstants.NODESET);
    for (int i=0; i < fileNames.getLength(); ++i) {
        Node name = fileNames.item(i);
        try {
          URL url = new URL(Utility.getParent(getTarget()), name.getTextContent());
          registerReference(url);
        } catch (MalformedURLException e) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, e.getMessage());
          return;
        }
    }
  }

  @ValidationTest
  public void registerDocumentFileReferences() throws XPathExpressionException {
      // We have a reference to the current target, since it is a label.
    URL target = getTarget();
    
    getRegistrar().setTargetIsLabel(target.toString(), true);
    
    if (!getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      return;
    }
    
    Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class);
    DOMSource source = new DOMSource(label);

    NodeList fileNames = (NodeList) xPathFactory.newXPath().evaluate(DOCUMENT_FILE_NAMES_PATH, source, XPathConstants.NODESET);
    for (int i=0; i < fileNames.getLength(); ++i) {
      Node name = fileNames.item(i);
      Node directory = getSiblingNode(name, "directory_path_name");
      if (directory == null) {
        try {
          URL referencedUrl = new URL(Utility.getParent(target), name.getTextContent());
          registerReference(referencedUrl);
        } catch (MalformedURLException mu) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, mu.getMessage());
          return;
        }
      } else {
        try {
          URL documentDir = new URL(Utility.getParent(target), directory.getTextContent() + "/");
          URL referencedFile = new URL(documentDir, name.getTextContent());
          registerReference(referencedFile);
        } catch (MalformedURLException mu) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, mu.getMessage());
          return;
        }
      }
    }
  }

  private Node getSiblingNode(Node child, String nodeName) {
    NodeList siblings = child.getParentNode().getChildNodes();
    for (int i=0; i < siblings.getLength(); ++i) {
      Node sibling = siblings.item(i);
      if (sibling!=child && sibling.getNodeName().equals(nodeName)) {
        return sibling;
      }
    }

    return null;
  }

  private void registerReference(URL referencedUrl) {
    getRegistrar().addTargetReference(getTarget().toString(), referencedUrl.toString());
  }
}
