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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.LabelUtil;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.Identifier;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

/**
 * Validation rule that performs referential integrity checking on
 * a Product_Bundle product label.
 * 
 * @author mcayanan
 *
 */
public class BundleReferentialIntegrityRule extends AbstractValidationRule {
  private static final Logger LOG = LoggerFactory.getLogger(BundleReferentialIntegrityRule.class);

  private static final Pattern BUNDLE_LABEL_PATTERN = 
      Pattern.compile(".*bundle.*\\.xml", Pattern.CASE_INSENSITIVE);
  
  private static final String PRODUCT_CLASS =
      "//*[starts-with(name(),'Identification_Area')]/product_class";
  
  /** XPath to grab the Member_Entry tags in a bundle. */
  private static final String BUNDLE_MEMBER_ENTRY = "//Bundle_Member_Entry";
  
  /** The member status XPath in an Inventory file. */
  private static final String MEMBER_STATUS = "member_status";

  /** The LID-VID or LID XPath for an association. */
  private static final String IDENTITY_REFERENCE =
      "lidvid_reference | lid_reference";
  
  /**
   * XPath to the logical identifier.
   */
  private static final String LOGICAL_IDENTIFIER =
      "//*[starts-with(name(),'Identification_Area')]/logical_identifier";

  /**
   * XPath to the version id.
   */
  private static final String VERSION_ID =
      "//*[starts-with(name(),'Identification_Area')]/version_id";

  private double totalTimeElapsed = 0.0;
  private ArrayList<String> logicalIdentifiersCumulative = new ArrayList<String>(0);
  private ArrayList<String> lidOrLidVidReferencesCumulative = new ArrayList<String>(0);
  private ArrayList<URL> lidOrLidVidReferencesCumulativeFileNames = new ArrayList<URL>(0); // This array and lidOrLidVidReferencesCumulative should have the same size 
  private String bundleBaseID = null; 
  private HashMap<String, String> lidOrLidvidReferenceToLogicalIdentifierMap =  new HashMap<String, String>(); // A map to allow getting a logical identifier from lid_reference or lidvid_reference.
  
  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean doesReferenceContainsVersion(String singleLidOrLidvidReference) {
      if (singleLidOrLidvidReference.contains("::")) {
          return(true);
      } else {
          return(false);
      }
  }

  private void performReporting(String singleLidOrLidvidReference, boolean referenceIsLidvid, int indexToFilenames) {
      try {
          String message = "";
          URL url = this.lidOrLidVidReferencesCumulativeFileNames.get(indexToFilenames); // The warning message will be for this label.
          if (referenceIsLidvid) {
              message = "A LIDVID reference " + singleLidOrLidvidReference + " is referencing a logical identifier for a product not found in this bundle.";
          } else {
              message = "A LID reference " + singleLidOrLidvidReference + " is referencing a logical identifier for a product not found in this bundle.";
          }
          LOG.debug(message);

          // Build the ValidationProblem and add it to the report.
          ValidationProblem p1 = new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
                                                                                 ProblemType.GENERAL_INFO, message),url);
          // Append the WARNING message to the report.
          getListener().addProblem(p1);

      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  private boolean bruteForceCheckForNonExistLogicalReferences(String singleLidOrLidvidReference) {
      // Given a LID or LIDVID reference, check to see if it is in logicalIdentifiersCumulative.
      // Because the reference may not contain the version, we must do a brute-force check since the logicalIdentifiersCumulative contains a version number.
      boolean referenceIsValid = false;

      for (String singleLogicalIdentifier : this.logicalIdentifiersCumulative) {
          LOG.debug("bruteForceCheckForNonExistLogicalReferences:singleLidOrLidvidReference,singleLogicalIdentifier {},{}",singleLidOrLidvidReference,singleLogicalIdentifier);
          if (singleLogicalIdentifier.contains(singleLidOrLidvidReference)) {
              referenceIsValid = true;
              LOG.debug("bruteForceCheckForNonExistLogicalReferences:singleLidOrLidvidReference,singleLogicalIdentifier,REFERENCE_IS_VALID {},{}",singleLidOrLidvidReference,singleLogicalIdentifier);
              break;
          }
      }
      return(referenceIsValid);
  }


  /**
   * Report a WARNING if any LID or LIDVID references does not resolve to at least one element in the list of logical identifiers.
   * @param validationRule The rule of the validation, e.g. pds4.label, pds4.bundle.  This value can be null since a rule is is not required within validate module.
   * @return None
   */
  private void reportLidOrLidvidReferenceToNonExistLogicalReferences() {
      // After all the local_identifier and lid_reference or lidliv_reference tags are collected, they can be check if they are pointing to local identifier collected.

      try {
        int indexToFilenames = 0;
        for (String singleLidOrLidvidReference : this.lidOrLidVidReferencesCumulative) {
             LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:VALIDATING_REFERENCE:singleLidOrLidvidReference,filename {},{}",singleLidOrLidvidReference,this.lidOrLidVidReferencesCumulativeFileNames.get(indexToFilenames));
            // It is possible that the reference does not contain a version, we must check for existence differently.
            if (!this.doesReferenceContainsVersion(singleLidOrLidvidReference)) {
                if (!this.bruteForceCheckForNonExistLogicalReferences(singleLidOrLidvidReference)) {

                    // We also need to check if the product is actually a product in the bundle.
                    // We should not throw a WARNING if the product does not belong to the bundle.
                    String filename = this.lidOrLidVidReferencesCumulativeFileNames.get(indexToFilenames).toString(); 
                    String logicalIdentifierPerLidReference = this.lidOrLidvidReferenceToLogicalIdentifierMap.get(singleLidOrLidvidReference);

                    LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);

                    boolean productBelongToBundleFlag = this.isIdentiferMatchingBundleBaseID(logicalIdentifierPerLidReference);

                    // Only throw a WARNING if the product does belong to this bundle.
                    if (productBelongToBundleFlag == true) {
                        LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_IS_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);
                        this.performReporting(singleLidOrLidvidReference, false, indexToFilenames);
                    } else {
                        LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_NOT_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);
                    }
                } else {
                    LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:LID_REFERENCE:singleLidOrLidvidReference {} is in logicalIdentifiersCumulative",singleLidOrLidvidReference);
                }
            } else {
                if (!this.logicalIdentifiersCumulative.contains(singleLidOrLidvidReference)) {

                    // We also need to check if the product is actually a product in the bundle.
                    // We should not throw a WARNING if the product does not belong to the bundle.
                    String filename = this.lidOrLidVidReferencesCumulativeFileNames.get(indexToFilenames).toString();
                    String logicalIdentifierPerLidReference = this.lidOrLidvidReferenceToLogicalIdentifierMap.get(singleLidOrLidvidReference);

                    LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);


                    //boolean productBelongToBundleFlag = this.isIdentiferMatchingBundleBaseID(logicalIdentifierPerFilename);
                    boolean productBelongToBundleFlag = this.isIdentiferMatchingBundleBaseID(logicalIdentifierPerLidReference);

                    // Only throw a WARNING if the product does belong to this bundle.
                    if (productBelongToBundleFlag == true) {
                        LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_IS_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);
                        this.performReporting(singleLidOrLidvidReference, true, indexToFilenames);
                    } else {
                        LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_NOT_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",filename,logicalIdentifierPerLidReference);
                    }
                } else {
                    LOG.debug("reportLidOrLidvidReferenceToNonExistLogicalReferences:LIDVID_REFERENCE:singleLidOrLidvidReference {} is in logicalIdentifiersCumulative",singleLidOrLidvidReference);
                }
            }
            indexToFilenames += 1;
        } // end for loop
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  private boolean hasReferenceIDAndFilenameComboAdded(String singleLidorLidVidReference, URL filename) {
      boolean referenceIDAndFilenameComboAddedFlag = false;
      // Build the combo of reference and filename together from input parameters.
      // Remove the use of the slash '/' to avoid confusion.   We are merely looking at the combination of the lid_reference (or lidvid_reference) plus filename as strings for comparison.
      String referenceIDAndFilenameComboValue = singleLidorLidVidReference + filename.toString();
      for (int ii=0; ii < this.lidOrLidVidReferencesCumulative.size(); ii++) {
          // Build the combo of reference and filename together from each value in this.lidOrLidVidReferencesCumulative.get and this.lidOrLidVidReferencesCumulativeFileNames.get.
          // Remove the use of the slash '/' to avoid confusion.   We are merely looking at the combination of the lid_reference (or lidvid_reference) plus filename as strings for comparison.
          String singleComboValue = this.lidOrLidVidReferencesCumulative.get(ii) + this.lidOrLidVidReferencesCumulativeFileNames.get(ii);
          LOG.debug("hasReferenceIDAndFilenameComboAdded:referenceIDAndFilenameComboValue,singleComboValue {},{}",referenceIDAndFilenameComboValue,singleComboValue);
          if (referenceIDAndFilenameComboValue.equals(singleComboValue)) {
              // If there is a compare, we have found our answer and will break out of loop.
              referenceIDAndFilenameComboAddedFlag = true;
              break;
          }
      }
      return(referenceIDAndFilenameComboAddedFlag);
  }

  private boolean isIdentiferMatchingBundleBaseID(String singleLogicalIdentifier) {
      // Given a logical identifier, check if it contains the bundle base identifier.
      // If the bundle base identifier is urn:nasa:pds:kaguya_grs_spectra
      // then urn:nasa:pds:kaguya_grs_spectra:document:kgrs_calibrated_spectra does contain the bundle base identifier. 
      boolean identifierMatchBundleBaseIDFlag = false;
      if (singleLogicalIdentifier != null) {
          if ((this.bundleBaseID != null) && singleLogicalIdentifier.contains(this.bundleBaseID)) {
              identifierMatchBundleBaseIDFlag = true;
          }
      }

      LOG.debug("isIdentiferMatchingBundleBaseID:singleLogicalIdentifier,this.bundleBaseID,identifierMatchBundleBaseIDFlag {},{},{}",singleLogicalIdentifier,this.bundleBaseID,identifierMatchBundleBaseIDFlag);
      return(identifierMatchBundleBaseIDFlag);
  }

  private String getBundleBaseID(ArrayList<String> logicalIdentifiers, String bundleFilename) {
      // Given a list of logical identifier from a bundle, fetch the bundle base ID
      // urn:nasa:pds:kaguya_grs_spectra:document:kgrs_calibrated_spectra --> urn:nasa:pds:kaguya_grs_spectra
      // urn:nasa:pds:kaguya_grs_spectra                                  --> urn:nasa:pds:kaguya_grs_spectra
      // Note that a bundle should only have one logical_identifier so we pick the first element and print a warning if there are more than one.
      String bundleBaseID = null;
      if (logicalIdentifiers.size() >= 1) {
          if (logicalIdentifiers.size() == 1) {
              // Split the logical id and fetch the first 4 to form the bundleBaseID
              String [] splittedTokens = logicalIdentifiers.get(0).split(":");
              if (splittedTokens.length >= 4) {
                  bundleBaseID = splittedTokens[0] + ":" + splittedTokens[1] + ":" + splittedTokens[2] + ":" + splittedTokens[3];
              } else {
                  LOG.error("getBundleBaseID: Expecting at least 4 tokens from parsing logical identifier {}",logicalIdentifiers.get(0));
              }
          } else {
              LOG.warn("getBundleBaseID: Expecting only one logical identifier but received {} from list of identifiers from bundle file {}",bundleFilename);
          }
      }
      return(bundleBaseID);
  }

  private void bundleAdditionalReferentialIntegrityChecks(URL crawlTarget) {
    // Perform additional referential integrity check beside the normal check.
    // For all references in all labels, check if they refer to a logical identifier that is valid and is in this bundle.

    URL url = null;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    Document xml = null;
    DOMSource domSource = null;

    try {
      List<Target> children = getContext().getCrawler().crawl(crawlTarget,true);  // Get also the directories.
      LOG.debug("bundleAdditionalReferentialIntegrityChecks:crawlTarget {}",crawlTarget);
      LOG.debug("bundleAdditionalReferentialIntegrityChecks:crawlTarget,children.size():afor_reduced: {},{}",crawlTarget,children.size());

      db = dbf.newDocumentBuilder();

      for (Target child : children) {
        LOG.debug("bundleAdditionalReferentialIntegrityChecks:FilenameUtils.getName(child.toString()) {}",FilenameUtils.getName(child.toString()));

        // Regardless of what kinds of file it is, parse it to find all the local_identifier and lid_reference or lidvid_reference tags.
        url = child.getUrl();

        if (url.toString().endsWith(".xml")) {
            xml = db.parse(url.openStream());
            domSource = new DOMSource(xml);
            ArrayList<String> lidOrLidVidReferences = LabelUtil.getLidVidReferences(domSource,url);
            ArrayList<String> logicalIdentifiers    = LabelUtil.getLogicalIdentifiers(domSource,url);

            LOG.debug("bundleAdditionalReferentialIntegrityChecks:url,lidOrLidVidReferences {}",url,lidOrLidVidReferences.size());
            LOG.debug("bundleAdditionalReferentialIntegrityChecks:url,logicalIdentifiers {}",url,logicalIdentifiers.size());

            if ((logicalIdentifiers != null) && !logicalIdentifiers.isEmpty()) {
                this.logicalIdentifiersCumulative.addAll(logicalIdentifiers);

                // If the label is a bundle, parse the logical identifier for the base ID.
                Matcher matcher = BUNDLE_LABEL_PATTERN.matcher(
                FilenameUtils.getName(child.toString()));
                if (matcher.matches()) {
                    this.bundleBaseID = this.getBundleBaseID(logicalIdentifiers, child.toString());
                }
            }

            if ((lidOrLidVidReferences != null) && !lidOrLidVidReferences.isEmpty()) {
                for (int ii=0; ii < lidOrLidVidReferences.size(); ii++) {
                    // Do not add duplicate references by checking for existence of the combination reference id in this.lidOrLidVidReferencesCumulative
                    // and the file name has not already been added already in this.lidOrLidVidReferencesCumulativeFileNames list.
                    // Note that because the reference id can be the same, the combination of the id plus the file name will make it unique. 
                    if (this.hasReferenceIDAndFilenameComboAdded(lidOrLidVidReferences.get(ii),url) == false) {

                        this.lidOrLidVidReferencesCumulative.add(lidOrLidVidReferences.get(ii));
                        this.lidOrLidVidReferencesCumulativeFileNames.add(url);  // Save the file name as well so it can be referred to.

                        LOG.debug("bundleAdditionalReferentialIntegrityChecks:ADDING_REFERENCE {}",lidOrLidVidReferences.get(ii),lidOrLidVidReferencesCumulative.size());
                    }

                    // Every lid_reference or lidvid_reference is connected to a logical identifier.
                    // Save that in a Map so a logical identifier can be retrieved from a lid_reference or lidvid_reference as key.
                    if ((logicalIdentifiers != null) && !logicalIdentifiers.isEmpty()) {
                        this.lidOrLidvidReferenceToLogicalIdentifierMap.put(lidOrLidVidReferences.get(ii), logicalIdentifiers.get(0));
                    } else {
                        LOG.error("Expecting the logicalIdentifiers array to be non-empty for label {}",url);
                    }
                }
            }
        } else {
            LOG.debug("bundleAdditionalReferentialIntegrityChecks:NON_XML:url {}",url);
            if (Utility.isDir(url.toString())) {
                // If the url is a directory, make a recursive call to this same function.
                this.bundleAdditionalReferentialIntegrityChecks(url);
            }

        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1,
          io.getMessage());
    } catch (Exception ex) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1,
          ex.getMessage());
    }

    LOG.debug("bundleAdditionalReferentialIntegrityChecks:logicalIdentifiersCumulative.size() {}",this.logicalIdentifiersCumulative.size());
    LOG.debug("bundleAdditionalReferentialIntegrityChecksbundleReferentialIntegrityRule:lidOrLidVidReferencesCumulative.size() {}",this.lidOrLidVidReferencesCumulative.size());
    LOG.debug("bundleAdditionalReferentialIntegrityChecksbundleReferentialIntegrityRule:lidOrLidVidReferencesCumulativeFilenames.size() {}",this.lidOrLidVidReferencesCumulativeFileNames.size());
  }

  @ValidationTest
  public void bundleReferentialIntegrityRule() {

    try {
      List<Target> children = getContext().getCrawler().crawl(getTarget());
      LOG.debug("bundleReferentialIntegrityRule:getTarget() {}",getTarget());
      LOG.debug("bundleReferentialIntegrityRule:children.size():afor_reduced: {}",children.size());

      // Check for bundle(.*)?\.xml file.
      for (Target child : children) {
        Matcher matcher = BUNDLE_LABEL_PATTERN.matcher(
            FilenameUtils.getName(child.toString()));
        //LOG.debug("bundleReferentialIntegrityRule:child.toString() {}",child.toString());
        LOG.debug("bundleReferentialIntegrityRule:FilenameUtils.getName(child.toString()) {}",FilenameUtils.getName(child.toString()));
        if (matcher.matches()) {
          try {
            XMLExtractor extractor = new XMLExtractor(child.getUrl());
            if("Product_Bundle".equals(
                extractor.getValueFromDoc(PRODUCT_CLASS))) {
              String lid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
              String vid = extractor.getValueFromDoc(VERSION_ID);
              // For bundles, set a reference to itself.
              getRegistrar().addIdentifierReference(child.getUrl().toString(), 
                  new Identifier(lid, vid));
              getListener().addLocation(child.getUrl().toString());
              getBundleMembers(child.getUrl());
              break;
            }
          } catch (Exception e) {
            //Ignore. This isn't a valid Bundle label, so let's skip it.
          }
        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1,
          io.getMessage());
    } catch (Exception ex) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1,
          ex.getMessage());
    }


    // https://github.com/NASA-PDS/validate/issues/308
    // As a user, I want to check that all Internal References are valid references to other PDS4 products within the current validating bundle
    //
    // Report the integrity of the references if they point to any local identifiers in this bundle.
    //

    this.bundleAdditionalReferentialIntegrityChecks(getTarget());
    this.reportLidOrLidvidReferenceToNonExistLogicalReferences();
  }
  
  private void getBundleMembers(URL bundle) {
    LOG.info("getBundleMembers:BEGIN_PROCESSING_BUNDLE:bundle {}",bundle);
    long startTime = System.currentTimeMillis();
    try {
      XMLExtractor extractor = new XMLExtractor(bundle);
      List<TinyNodeImpl> nodes = extractor.getNodesFromDoc(
          BUNDLE_MEMBER_ENTRY);
      for (TinyNodeImpl node : nodes) {
        String reference = extractor.getValueFromItem(
            IDENTITY_REFERENCE,
            node);
        String memberStatus = extractor.getValueFromItem(
            MEMBER_STATUS,
            node);
        Identifier id = parseIdentifier(reference);
        LOG.debug("getBundleMembers:reference,memberStatus,id {},{},{}",reference,memberStatus,id);
        List<Map.Entry<Identifier, String>> matchingMembers = 
            new ArrayList<Map.Entry<Identifier, String>>();
        for (Map.Entry<Identifier, String> idEntry : 
          getRegistrar().getIdentifierDefinitions().entrySet()) {
          LOG.debug("getBundleMembers:reference,memberStatus,id,idEntry.getKey()) {},{},{},{}",reference,memberStatus,id,idEntry.getKey());
          if (id.equals(idEntry.getKey())) {
            matchingMembers.add(idEntry);
            LOG.debug("getBundleMembers:ADDING_IDENTRY:reference,memberStatus,id,idEntry.getKey()) {},{},{},{}",reference,memberStatus,id,idEntry.getKey());
          }
        }
        if (matchingMembers.isEmpty() && 
            "Primary".equalsIgnoreCase(memberStatus)) {
          LOG.debug("getBundleMembers:MATCHING_MEMBER_ID_IS_EMPTY {}",id);
          getListener().addProblem(new ValidationProblem(new ProblemDefinition(
              ExceptionType.WARNING,
              ProblemType.MEMBER_NOT_FOUND,
              "The member '" + id + "' could not be found in "
                  + "any product within the given target."), 
              bundle));
        } else if (matchingMembers.size() == 1) {
          String parentLid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
          super.verifyLidPrefix(id.getLid(), parentLid, memberStatus, bundle);
          getListener().addProblem(new ValidationProblem(new ProblemDefinition(
              ExceptionType.INFO,
              ProblemType.MEMBER_FOUND,
              "The member '" + id + "' is identified in "
                  + "the following product: "
                  + matchingMembers.get(0).getValue()), 
              bundle));
        } else if (matchingMembers.size() > 1) {
          String parentLid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
          super.verifyLidPrefix(id.getLid(), parentLid, memberStatus, bundle);
          ExceptionType exceptionType = ExceptionType.ERROR;
          if (!id.hasVersion()) {
            Map<String, List<String>> matchingIds = 
                findMatchingIds(matchingMembers);
            boolean foundDuplicates = false;
            for (String matchingId : matchingIds.keySet()) {
              if (matchingIds.get(matchingId).size() > 1) {
                getListener().addProblem(new ValidationProblem(
                    new ProblemDefinition(exceptionType,
                        ProblemType.DUPLICATE_VERSIONS,
                        "The member '" + id + "' is identified "
                            + "in multiple products, but with the same "
                            + "version id '" + matchingId.split("::")[1]
                            + "': " + matchingIds.get(matchingId).toString()),
                    bundle));
                foundDuplicates = true;
              }
            }
            if (!foundDuplicates) {
              List<String> targets = new ArrayList<String>();
              for (Map.Entry<Identifier, String> m : matchingMembers) {
                targets.add(m.getValue());
              }
              getListener().addProblem(new ValidationProblem(
                  new ProblemDefinition(
                      ExceptionType.INFO,
                      ProblemType.DUPLICATE_MEMBERS_INFO,
                      "The member '" + id + "' is identified "
                          + "in multiple proudcts: " + targets.toString()),
                  bundle));
            }
          } else {
            List<String> targets = new ArrayList<String>();
            for (Map.Entry<Identifier, String> m : matchingMembers) {
              targets.add(m.getValue());
            }
            getListener().addProblem(new ValidationProblem(
                new ProblemDefinition(exceptionType,
                    ProblemType.DUPLICATE_MEMBERS,
                    "The member '" + id + "' is identified "
                        + "in multiple proudcts: " + targets.toString()),
                bundle));                
          }
        }
        getRegistrar().addIdentifierReference(bundle.toString(), id);
      }
    } catch (Exception e) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, bundle, -1, -1, 
          e.getMessage());
    }
    long finishTime = System.currentTimeMillis();
    long timeElapsed = finishTime - startTime;
    totalTimeElapsed += timeElapsed;

    LOG.info("getBundleMembers:END_PROCESSING_BUNDLE:bundle,totalTimeElapsed {},{}",bundle,totalTimeElapsed/1000.0);
  }
  
  private Identifier parseIdentifier(String identifier) {
    if (identifier.indexOf("::") != -1) {
      return new Identifier(identifier.split("::")[0],
          identifier.split("::")[1]);
    } else {
      return new Identifier(identifier.split("::")[0]);
    }
  }
  
  private Map<String, List<String>> findMatchingIds(List<Map.Entry<Identifier, 
      String>> products) {
    Map<String, List<String>> results = new HashMap<String, List<String>>();
    for (Map.Entry<Identifier, String> product : products) {
      if (results.get(product.getKey().toString()) != null) {
        List<String> targets = results.get(product.getKey().toString());
        targets.add(product.getValue());
      } else {
        List<String> targets = new ArrayList<String>();
        targets.add(product.getValue());
        results.put(product.getKey().toString(), targets);
      }
    }
    return results;
  }
  
}
