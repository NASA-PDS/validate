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

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.SourceLocation;
import gov.nasa.pds.tools.util.LidVid;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Class that checks that context products referenced in a product label
 * exist in a supplied list of registered context products at the
 * Engineering Node.
 * 
 * @author mcayanan
 *
 */
public class ContextProductReferenceValidationRule extends AbstractValidationRule {
  private final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";
  
  /**
   * XPath to the internal references within a PDS4 data product label.
   * 
   * At this time, we'll only be getting the referenced Context Products. So,
   * we'll look for the following reference_types:
   * 
   * - is_* (is_instrument, is_facility, is_telescope, etc.)
   * - collection_to_context
   * - document_to_* except for document_to_associate
   * - *_to_target
   * - data_to_investigation
   */
  private final String INTERNAL_REF_XPATH =
    "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and starts-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'],'is_')] | " + 
    "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_target')] | " +
    "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and *:reference_type[namespace-uri()='" + PDS4_NS + "'] = 'collection_to_context'] | " + 
    "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and *:reference_type[namespace-uri()='" + PDS4_NS + "'] = 'data_to_investigation'] | " + 
    "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and starts-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'],'document_to_') and " + 
    "*:reference_type[namespace-uri()='" + PDS4_NS + "'] != 'document_to_associate']";
  
  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location) || !Utility.canRead(location)) {
      return false;
    }
    
    if(!getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      return false;
    }
    return true;
  }

  @ValidationTest
  public void checkContextReferences() throws XPathExpressionException {
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
    }
    ValidationTarget target = new ValidationTarget(getTarget());
    Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT,
        Document.class);
    DOMSource source = new DOMSource(label);
    source.setSystemId(uri.toString());
    XPathFactory xpathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
    NodeList references = (NodeList) xpathFactory.newXPath().evaluate(
        INTERNAL_REF_XPATH, source, XPathConstants.NODESET);
    for (int i = 0; i < references.getLength(); i++) {
      NodeList children = references.item(i).getChildNodes();
      for (int j = 0; children != null && j < children.getLength(); j++) {
        if ("lidvid_reference".equalsIgnoreCase(children.item(j).getLocalName()) ||
            "lid_reference".equalsIgnoreCase(children.item(j).getLocalName())) {
          SourceLocation locator = (SourceLocation) children.item(j)
              .getUserData(SourceLocation.class.getName());
          String lidvid = children.item(j).getTextContent();
          if (!getContext().getRegisteredProducts().get("Product_Context")
              .contains(parseIdentifier(lidvid))) {
            getListener().addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.CONTEXT_REFERENCE_NOT_FOUND,
                    "'" + lidvid + "' could not be found in the "
                        + "supplied list of registered context products."),
                target,
                locator.getLineNumber(), 
                -1));
          } else {
            getListener().addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.INFO,
                    ProblemType.CONTEXT_REFERENCE_FOUND,
                    "'" + lidvid + "' was found in the "
                        + "supplied list of registered context products."),
                target,
                locator.getLineNumber(),
                -1));
          }          
        }
      }
    }
  }
  
  private LidVid parseIdentifier(String identifier) {
    if (identifier.indexOf("::") != -1) {
      return new LidVid(identifier.split("::")[0],
          identifier.split("::")[1]);
    } else {
      return new LidVid(identifier.split("::")[0]);
    }
  }
  
}
