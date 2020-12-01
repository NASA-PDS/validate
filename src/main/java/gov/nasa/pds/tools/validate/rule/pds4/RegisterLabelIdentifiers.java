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

import gov.nasa.pds.tools.validate.Identifier;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers identifiers defined within a label, and verifies that
 * the same identifier is not registered twice.
 */
public class RegisterLabelIdentifiers extends AbstractValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(RegisterLabelIdentifiers.class);
  private static final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";

  private static final String IDENTIFIERS_PATH
      = "//*:Identification_Area[namespace-uri()='" + PDS4_NS + "']"
      + "/*:logical_identifier[namespace-uri()='" + PDS4_NS + "']";
  
  private static final String VERSION_ID_PATH = 
      "//*:Identification_Area[namespace-uri()='" + PDS4_NS + "']"
      + "/*:version_id[namespace-uri()='" + PDS4_NS + "']";

  private XPathFactory xPathFactory;

  /**
   * Creates a new instance.
   */
  public RegisterLabelIdentifiers() {
    xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
  }

  @Override
  public boolean isApplicable(String location) {
    // The rule is applicable if a label has been parsed.
    return getContext().containsKey(PDS4Context.LABEL_DOCUMENT);
  }

  /**
   * Tests that label identifiers are uniquely defined.
   *
   * @throws XPathExpressionException if there is an error processing the XPath to
   *   the label logical identifier
   */
  @ValidationTest
  public void registerIdentifiers() throws XPathExpressionException {
    // We have a reference to the current target, since it is a label.
    getRegistrar().setTargetIsLabel(getTarget().toString(), true);

    Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class);
    DOMSource source = new DOMSource(label);

    NodeList identifiers = (NodeList) xPathFactory.newXPath().evaluate(IDENTIFIERS_PATH, source, XPathConstants.NODESET);
    String lid = "";
    for (int i=0; i < identifiers.getLength(); ++i) {
      Node name = identifiers.item(i);
      lid = name.getTextContent();
    }
    NodeList versions = (NodeList) xPathFactory.newXPath().evaluate(VERSION_ID_PATH, source, XPathConstants.NODESET);
    String vid = "";
    for (int i=0; i < versions.getLength(); ++i) {
      Node name = versions.item(i);
      vid = name.getTextContent();
    }
    LOG.debug("RegisterLabelIdentifiers:registerIdentifiers:getTarget(),lid,vid {},{},{}",getTarget(),lid,vid);
    registerIdentifier(new Identifier(lid, vid));
  }

  private void registerIdentifier(Identifier identifier) {
    TargetRegistrar registrar = getRegistrar();
    URI target = null;
    try {
      target = getTarget().toURI();
    } catch (URISyntaxException e) {
      //Should never happen
    }
    LOG.debug("RegisterLabelIdentifiers:registerIdentifier:getTarget(),identifier {},{}",getTarget(),identifier);
    if (registrar.getTargetForIdentifier(identifier) == null) {
      registrar.setTargetIdentifier(target.normalize().toString(), identifier);
    } else {
      String message = String.format("Identifier %s already defined (old location: %s)",
                identifier.toString(), registrar.getTargetForIdentifier(identifier));
      reportError(PDS4Problems.DUPLICATE_LOGICAL_IDENTIFIER, getTarget(), -1, -1, message);
    }
  }
}
