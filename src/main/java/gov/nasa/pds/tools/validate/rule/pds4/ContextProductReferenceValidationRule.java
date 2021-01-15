// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.validate.rule.pds4;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.SourceLocation;
import gov.nasa.pds.tools.util.ContextProductReference;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Class that checks that context products referenced in a product label exist
 * in a supplied list of registered context products at the Engineering Node.
 * 
 * @author mcayanan
 *
 */
public class ContextProductReferenceValidationRule extends AbstractValidationRule {
    private static final Logger LOG = LoggerFactory.getLogger(ContextProductReferenceValidationRule.class);

    private final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";

    /**
     * XPath to the internal references within a PDS4 data product label.
     * 
     * At this time, we'll only be getting the referenced Context Products. So,
     * we'll look for the following reference_types:
     * 
     * - is_* (is_instrument, is_facility, is_telescope, etc.)
     * - *_to_<context> (e.g. data_to_investigation, data_to_instrument, etc.)
     */
    private final String INTERNAL_REF_XPATH =
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and starts-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'],'is_')] | " + 
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_target')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_investigation')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_instrument')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_instrument_host')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_other')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_facility')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_telescope')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_airborne')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_laboratory')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_observatory')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_target')] | " +
      "//*:Internal_Reference[namespace-uri()='" + PDS4_NS + "' and ends-with(*:reference_type[namespace-uri()='" + PDS4_NS + "'], '_to_context')]";

    @Override
    public boolean isApplicable(String location) {
      if (Utility.isDir(location) || !Utility.canRead(location)) {
        return false;
      }

      if(!getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
        return false;
      }
      
      // issue_42: Add capability to ignore product-level validation
      if (getContext().getSkipProductValidation()) {
          return false;
      }
      return true;
    }

    private boolean performVersionIdSearch(List<ContextProductReference> rgProds, ContextProductReference lidvidObj) {
        // The list of registered products only contain the latest version as of 01/08/2021.
        // If the exact lidvid cannot be found in the list of registered products, do an additional check on the
        // version id (vid).  As long as the registered version is greater than the provided
        // version, the provided lidvid is considered good.
        //
        // Provided lidvid:   urn:nasa:pds:context:investigation:mission.insight::1.0
        // Registered lidvid: urn:nasa:pds:context:investigation:mission.insight::2.0
        //
        // Parameters:
        // List<ContextProductReference> rgProds   = a list of all registered products parsed from registered_context_products.json file.
        // ContextProductReference       lidvidObj = the lidvid to check against the list registered products.

        boolean contextReferenceFoundFlag = false;
        LOG.debug("performVersionIdSearch:lidvidObj,rgProds.size() {},{}",lidvidObj,rgProds.size());

        // If is possible for older version of lidvid to not have the version id.  Must first perform a check.
        // If no version is provided, return immediately.
        if (lidvidObj.hasVersion()) {
            double providedVersionId = Double.parseDouble(lidvidObj.getVersion());
            String providedLogicalId = lidvidObj.getLid();

            ContextProductReference contextProductReference = null;
            int refIndex = 0;
            while (!contextReferenceFoundFlag && refIndex < rgProds.size()) { 
                contextProductReference = rgProds.get(refIndex);
                //LOG.debug("performVersionIdSearch:refIndex,providedLogicalId,providedVersionId,contextProductReference.getVersion() {},{}",refIndex,providedLogicalId,providedVersionId,contextProductReference.getVersion());
                if (contextProductReference.getLid().equals(providedLogicalId)) {
                    // If the provided logical id is the same and the provided version id is greater or equal to registered,
                    // the reference is good.
                    if (contextProductReference.hasVersion()  && (Double.parseDouble(contextProductReference.getVersion()) >= providedVersionId)) {
                        contextReferenceFoundFlag = true;
                    }
                }
                refIndex += 1;
            
            }
        }
        return(contextReferenceFoundFlag);
    }

    @ValidationTest
    public void checkContextReferences() throws XPathExpressionException {
        LOG.debug("checkContextReferences: getContext().getValidateContext() {}",getContext().getValidateContext());
        if (getContext().getValidateContext()) {            
            URI uri = null;
            try {
                uri = getTarget().toURI();
            } catch (URISyntaxException e) {
                // Should never happen
            }
            ValidationTarget target = new ValidationTarget(getTarget());
            Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class);
            DOMSource source = new DOMSource(label);
            source.setSystemId(uri.toString());
            XPathFactory xpathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
            NodeList references = (NodeList) xpathFactory.newXPath().evaluate(INTERNAL_REF_XPATH, source,
                    XPathConstants.NODESET);
    
            List<ContextProductReference> rgProds = getContext().getRegisteredProducts().get("Product_Context");
            LOG.debug("checkContextReferences: uri,references.getLength() {},{}",uri,references.getLength());
            LOG.debug("checkContextReferences: uri,rgProds () {},{}",uri,rgProds);
            for (int i = 0; i < references.getLength(); i++) {
    
                // get name and type from parent
                Node topNode = references.item(i).getParentNode();
                NodeList nodesOfParent = topNode.getChildNodes();
    
                List<String> names = new ArrayList<String>();
                List<String> types = new ArrayList<String>();
                
                SourceLocation locator = null;
                ContextProductReference rgp = null;
    
                // System.out.println("Nubmber of nodes parent: " +
                // nodesOfParent.getLength());
LOG.debug("checkContextReferences: uri,nodesOfParent.getLength() {},{}",uri,nodesOfParent.getLength());
                for (int n = 0; n < nodesOfParent.getLength(); n++) {
                    Node parantN = nodesOfParent.item(n);
                    if (parantN.getNodeName().equals("name")) {
                        names.add(parantN.getTextContent());
                    }
                    if (parantN.getNodeName().equals("type")) {
                        types.add(parantN.getTextContent());
                    }
                }
                // check LIDVID and name and type
                NodeList children = references.item(i).getChildNodes();
                for (int j = 0; children != null && j < children.getLength(); j++) {
                    //LOG.debug("checkContextReferences: uri,j,children.item(j).getLocalName() {},{},{}",uri,j,children.item(j).getLocalName());
                    if ("lidvid_reference".equalsIgnoreCase(children.item(j).getLocalName())
                            || "lid_reference".equalsIgnoreCase(children.item(j).getLocalName())) {
    
                        locator = (SourceLocation) children.item(j)
                                .getUserData(SourceLocation.class.getName());
                        String lidvid = children.item(j).getTextContent();
                        //LOG.debug("checkContextReferences: uri,j,lidvid {},{},{}",uri,j,lidvid);
                        //System.out.println("lidvid: " + lidvid);
    
                        // check name/type is available
                        ContextProductReference lidvidObj = parseIdentifier(lidvid, types, names);
    
                        //int rpJsonSize = rgProds.size();
                        //System.out.println("rpJsonSize: " + rpJsonSize);
                        //LOG.debug("checkContextReferences: uri,j,lidvid,lidvidObj {},{},{},{}",uri,j,lidvid,lidvidObj);
                        try {
                        if (!rgProds.contains(lidvidObj)) {
                          // The list of registered products only contain the latest version as of 01/08/2021.
                          // If the exact lidvid cannot be found in the list of registered products, do an additional check on the
                          // version id (vid).  As long as the registered version is greater than the provided
                          // version, the provided lidvid is considered good.
                          //
                          // Provided lidvid:   urn:nasa:pds:context:investigation:mission.insight::1.0
                          // Registered lidvid: urn:nasa:pds:context:investigation:mission.insight::2.0

                          boolean contextReferenceFoundFlag = performVersionIdSearch(rgProds,lidvidObj);

                          LOG.debug("checkContextReferences: lidvidObj,contextReferenceFoundFlag {},{}",lidvidObj,contextReferenceFoundFlag);

                          // Finally if the version id indeed not found, then report the error.
                          if (!contextReferenceFoundFlag) {
                            getListener().addProblem(new ValidationProblem(
                                    new ProblemDefinition(ExceptionType.ERROR, ProblemType.CONTEXT_REFERENCE_NOT_FOUND,
                                            "'Context product not found: " + lidvid),
                                    target, locator.getLineNumber(), -1));
                           }
                        } else {
                            // Found LidVid
                            //LOG.debug("checkContextReferences: " + "'Context product indeed found: " + lidvid); 
                            getListener().addProblem(new ValidationProblem(
                                    new ProblemDefinition(ExceptionType.INFO, ProblemType.CONTEXT_REFERENCE_FOUND,
                                            "Context product found: '" + lidvid + "'"),
                                    target, locator.getLineNumber(), -1));
    
                            //now lets check name and type
                            List<String> rgpNames = null;
                            List<String> rgpTypes = null;
                            if (names != null && types != null) {
                                rgp = rgProds.get(rgProds.indexOf(lidvidObj));
                                rgpNames = rgp.getNames();
                                rgpTypes = rgp.getTypes();
                                // check the name
                                for (String name : names) {
                                    if (rgpNames.stream().anyMatch(name::equalsIgnoreCase)) {
//                                    if (name.equalsIgnoreCase(rgp.getName())) {
                                        // Check name and type case sensitive
                                        if (!rgpNames.contains(name)) {
//                                        if (!name.equals(rgp.getName())) {                                   
                                            getListener().addProblem(new ValidationProblem(
                                                    new ProblemDefinition(ExceptionType.INFO,
                                                            ProblemType.CONTEXT_REFERENCE_FOUND_CASE_MISMATCH,
                                                            "Context reference name case mismatch. Value: '" + name + "'"
                                                                    + " Expected one of: '" + rgp.getNames() + "'"),
                                                    target, locator.getLineNumber(), -1));
                                        }
                                    } else {
                                        getListener().addProblem(new ValidationProblem(
                                                new ProblemDefinition(ExceptionType.INFO,
                                                        ProblemType.CONTEXT_REFERENCE_FOUND_MISMATCH,
                                                        "Context reference name mismatch. Value: '" + name + "'"
                                                                + " Expected one of: '" + rgp.getNames() + "'"),
                                                target, locator.getLineNumber(), -1));
                                    }
                                }
                                // check the type
                                for (String type : types) {
                                    if (rgpTypes.stream().anyMatch(type::equalsIgnoreCase)) {
//                                    if (type.equalsIgnoreCase(rgp.getType())) {
                                        if (!rgpTypes.contains(type)) {
//                                        if (!type.equals(rgp.getType())) {
                                            getListener()
                                            .addProblem(new ValidationProblem(
                                                    new ProblemDefinition(ExceptionType.INFO,
                                                            ProblemType.CONTEXT_REFERENCE_FOUND_CASE_MISMATCH,
                                                            "Context reference type case mismatch. Value: '" + type + "'"
                                                                    + " Expected one of: '" + rgp.getTypes() + "'"),
                                                    target, locator.getLineNumber(), -1));
                                        }
                                    } else if (!topNode.getLocalName().equals("Observing_System_Component")) { // TODO For now, we are punting on Observing System Component
                                        getListener().addProblem(new ValidationProblem(
                                                new ProblemDefinition(ExceptionType.INFO,
                                                        ProblemType.CONTEXT_REFERENCE_FOUND_MISMATCH,
                                                        "Context reference type mismatch. Value: '" + type + "'"
                                                        + " Expected one of: '" + rgp.getTypes() + "'"),
                                                target, locator.getLineNumber(), -1));
                                    }
                                }
                            }
                        }  // if rgProds.contains
                        } catch (Exception e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                    }
                }  // for loop j
            }  // for loop i
        }
    }

    private ContextProductReference parseIdentifier(String identifier, List<String> types, List<String> names) {
        if (identifier.indexOf("::") != -1) {
            return new ContextProductReference(identifier.split("::")[0], identifier.split("::")[1], types, names);
        } else {
            return new ContextProductReference(identifier.split("::")[0], null, types, names);
        }
    }

}
