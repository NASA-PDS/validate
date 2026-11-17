// Copyright 2021, by the California Institute of Technology.
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
package gov.nasa.pds.tools.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.CrossLabelFileAreaReferenceChecker;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.TargetExaminer;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.RuleContext;

/**
 * Util class to provide additional integrity checks on a Product_Bundle or Product_Collection
 * label.
 *
 * <p>
 * One instance holds the referential integrity state of one validation run. It is created by
 * {@link RuleContext} and shared with child contexts, so separate runs in the same JVM no longer
 * see or reset each other's state.
 *
 * <p>
 * Threading: label validation may run on worker threads and writes to the label identifier cache
 * and the {@link CrossLabelFileAreaReferenceChecker}; both are safe for concurrent use. The rest of
 * the state is only used by the bundle/collection referential pass, which runs on one thread after
 * the labels are done.
 */
public class ReferentialIntegrityUtil {
  private static final Logger LOG = LoggerFactory.getLogger(ReferentialIntegrityUtil.class);

  private final Set<URL> urlsParsedCumulative = new HashSet<>();
  private final Set<String> lidvidsCumulative = new HashSet<>();
  private final Set<String> lidsCumulative = new HashSet<>();
  private final ArrayList<String> lidOrLidVidReferencesCumulative = new ArrayList<>(0);
  private final HashSet<String> lidAndFilenameCombo = new HashSet<String>();
  private final HashMap<String, HashSetReferenceInfo> contextReferencesCumulative =
      new HashMap<>(0); // Collect all references defined in "Context_Area" tag from all labels.
  private final HashMap<String, HashSet<String>> bundleOrCollectionReferenceMap = new HashMap<String, HashSet<String>>(); // Collect
                                                                                            // all
                                                                                            // references
                                                                                            // defined
                                                                                            // in
                                                                                            // "Reference_List"
                                                                                            // tag
                                                                                            // from
                                                                                            // a
                                                                                            // label
                                                                                            // that
                                                                                            // is
                                                                                            // a
                                                                                            // bundle.
  private final HashMap<String, HashSetReferenceInfo> bundleReferenceMap = new HashMap<String, HashSetReferenceInfo>(); // Collect
                                                                                             // all
                                                                                             // references
                                                                                             // defined
                                                                                             // in
                                                                                             // "Context_Area"
                                                                                             // and
                                                                                             // "Reference_List"
                                                                                             // tag
                                                                                             // from
                                                                                             // a
                                                                                             // label
                                                                                             // that
                                                                                             // is
                                                                                             // a
                                                                                             // bundle.
  private final HashMap<String, HashSetReferenceInfo> collectionReferenceMap = new HashMap<>(); // Collect
                                                                                                 // all
                                                                                                 // references
                                                                                                 // defined
                                                                                                 // in
                                                                                                 // "Context_Area"
                                                                                                 // and
                                                                                                 // "Reference_List"
                                                                                                 // tag
                                                                                                 // from
                                                                                                 // a
                                                                                                 // label
                                                                                                 // that
                                                                                                 // is
                                                                                                 // a
                                                                                                 // collection.
  private final ArrayList<URL> lidOrLidVidReferencesCumulativeFileNames = new ArrayList<>(0); // This
                                                                                               // array
                                                                                               // and
                                                                                               // lidOrLidVidReferencesCumulative
                                                                                               // should
                                                                                               // have
                                                                                               // the
                                                                                               // same
                                                                                               // size
  private String bundleBaseID = null;
  private final HashMap<String, String> lidOrLidvidReferenceToLogicalIdentifierMap =
      new HashMap<>(); // A
                       // map
                       // to
                       // allow
                       // getting
                       // a
                       // logical
                       // identifier
                       // from
                       // lid_reference
                       // or
                       // lidvid_reference.
  private final HashMap<String, URL> bundleURLMap = new HashMap<>();

  private URL target = null;
  private ProblemListener problemListener = null;
  private RuleContext ruleContext = null;
  private static final String[] VALID_REFERENCE_TYPES = {"bundle", "collection"};

  // The referenceType determines which label the Reference_List is collected from
  // file name that contains BUNDLE_LABEL_PATTERN or COLLECTION_LABEL_PATTERN.
  private String referenceType = ""; // Possible values are defined in VALID_REFERENCE_TYPES

  private boolean contextReferenceCheck = true; // By default, this class will collect all
                                                       // references and check
                                                       // them from the context area.
  // Used in parsing for "_reference" tags.
  private final String[] tagsList = {LabelUtil.LIDVID_REFERENCE, LabelUtil.LID_REFERENCE};
  private final HashSet<String> reportedErrorsReferenceSet = new HashSet<>();
  private URL parentBundleURL = null;
  // Written by label validation (possibly on worker threads), read by the referential pass.
  private final ConcurrentHashMap<URL, LabelCacheEntry> labelIdentifierCache =
      new ConcurrentHashMap<>();
  private final CrossLabelFileAreaReferenceChecker crossLabelFileAreaReferenceChecker =
      new CrossLabelFileAreaReferenceChecker(this::getCachedLabelIdentifiers);

  /**
   * Initialize this class to ready for doing referential checks.
   *
   * @param referenceType the referenceType of the target of the check: 'bundle' or 'collection'
   * @param target the URL of the target of the check
   * @param problemListener the ProblemListener of the target of the check
   * @param ruleContext the RuleContext of the target of the check
   * @return None
   */
  public void initialize(String referenceType, URL target, ProblemListener problemListener,
      RuleContext ruleContext) {
    LOG.debug("initialize:referenceType,target [{}],{}", referenceType, target);
    this.setReferenceType(referenceType);
    this.setTarget(target);
    this.setListener(problemListener);
    this.setContext(ruleContext);
  }

  public void cacheLabelIdentifiers(URL url, LabelCacheEntry entry) {
    labelIdentifierCache.put(url, entry);
  }

  public LabelCacheEntry getCachedLabelIdentifiers(URL url) {
    return labelIdentifierCache.get(url);
  }

  /**
   * @return the checker for file areas shared between labels in this validation run
   */
  public CrossLabelFileAreaReferenceChecker getCrossLabelFileAreaReferenceChecker() {
    return crossLabelFileAreaReferenceChecker;
  }

  /**
   * Reset this class to its initial state in case running from regression tests.
   *
   * @return None
   */
  public void reset() {
    // Make sure all composite arrays/maps defined in this class are clear otherwise
    // regression tests will fail,
    // and it will be difficult to figure out why. The code may work when validate
    // runs from the command line
    // but not in regression test.
    this.lidAndFilenameCombo.clear();
    this.lidvidsCumulative.clear();
    this.lidsCumulative.clear();
    this.lidOrLidVidReferencesCumulative.clear();
    this.contextReferencesCumulative.clear();
    this.bundleOrCollectionReferenceMap.clear();
    this.bundleReferenceMap.clear();
    this.collectionReferenceMap.clear();
    this.lidOrLidVidReferencesCumulativeFileNames.clear();
    this.bundleBaseID = null;
    this.lidOrLidvidReferenceToLogicalIdentifierMap.clear();
    this.bundleURLMap.clear();
    this.urlsParsedCumulative.clear();
    this.reportedErrorsReferenceSet.clear();
    this.parentBundleURL = null;
    this.labelIdentifierCache.clear();
    this.crossLabelFileAreaReferenceChecker.reset();
  }

  /**
   * Set the contextReferenceCheck
   *
   * @return None
   */
  public void setContextReferenceCheckFlag(boolean contextReferenceCheck) {
    LOG.debug("setContextReferenceCheckFlag:contextReferenceCheck {}", contextReferenceCheck);
    this.contextReferenceCheck = contextReferenceCheck;
  }

  /**
   * Get the contextReferenceCheck.
   *
   * @return The valule of contextReferenceCheck
   */
  public boolean getContextReferenceCheckFlag() {
    return (this.contextReferenceCheck);
  }

  /**
   * Get the URL of the target of the check.
   *
   * @return the URL of the target of the check
   */
  public URL getTarget() {
    return (this.target);
  }

  /**
   * Set the URL of the target of the check.
   *
   * @return None
   */
  public void setTarget(URL target) {
    LOG.debug("setTarget:target [{}]", target);
    this.target = target;
  }

  /**
   * Get the ProblemListener of the target of the check.
   *
   * @return the ProblemListener of the target of the check
   */
  public ProblemListener getListener() {
    return (this.problemListener);
  }

  /**
   * Set the ProblemListener of the target of the check.
   *
   * @param problemListener The ProblemListener of the target of the check
   * @return None
   */
  public void setListener(ProblemListener problemListener) {
    this.problemListener = problemListener;
  }

  /**
   * Get the RuleContext of the target of the check.
   *
   * @return the RuleContext of the target of the check
   */
  public RuleContext getContext() {
    return (this.ruleContext);
  }

  /**
   * Get the RuleContext of the target of the check.
   *
   * @parem ruleContext the RuleContext of the target of the chec
   * @return None
   */
  public void setContext(RuleContext ruleContext) {
    this.ruleContext = ruleContext;
  }

  /**
   * Get the referenceType of the target of the check.
   *
   * @return the referenceType of the target of the check
   */
  public String getReferenceType() {
    return (this.referenceType);
  }

  /**
   * Set the referenceType of the target of the check.
   *
   * @param referenceType the referenceType of the target of the check: 'bundle' or 'collection'
   * @return None
   */
  public void setReferenceType(String referenceType) {
    LOG.debug(
        "setReferenceType:afor:referenceType,this.referenceType [{}],[{}]",
        referenceType, this.referenceType);
    if (Arrays.asList(this.VALID_REFERENCE_TYPES).contains(referenceType)) {
      this.referenceType = referenceType;
    } else {
      LOG.error("setReferenceType: Unrecognized value for referenceType {}, valid types are {}",
          referenceType, this.VALID_REFERENCE_TYPES);
    }
    LOG.debug(
        "setReferenceType:after:referenceType,this.referenceType [{}],[{}]",
        referenceType, this.referenceType);
  }

  private boolean doesReferenceContainsVersion(String singleLidOrLidvidReference) {
    if (singleLidOrLidvidReference.contains("::")) {
      return (true);
    }
    return (false);
  }

  private void performReporting(String singleLidOrLidvidReference, boolean referenceIsLidvid,
      int indexToFilenames) {
    try {
      String message = "";
      URL url =
          this.lidOrLidVidReferencesCumulativeFileNames.get(indexToFilenames); // The
                                                                                                   // warning
                                                                                                   // message
                                                                                                   // will
                                                                                                   // be
                                                                                                   // for
                                                                                                   // this
                                                                                                   // label.
      if (referenceIsLidvid) {
        message = "A LIDVID reference " + singleLidOrLidvidReference
            + " is referencing a logical identifier for a product not found in this "
            + this.getReferenceType();
      } else {
        message = "A LID reference " + singleLidOrLidvidReference
            + " is referencing a logical identifier for a product not found in this "
            + this.getReferenceType();
      }
      LOG.debug("performReporting:" + message);

      ValidationProblem p1 = new ValidationProblem(
          new ProblemDefinition(ExceptionType.WARNING, ProblemType.REFERENCE_NOT_FOUND, message),
          url);
      // Append the WARNING message to the report.
      getListener().addProblem(p1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean bruteForceCheckForNonExistLogicalReferences(
      String singleLidOrLidvidReference) {
    // Given a LID or LIDVID reference, check to see if it is in
    // logicalIdentifiersCumulative.
    // Because the reference may not contain the version, we must do a brute-force
    // check since the logicalIdentifiersCumulative contains a version number.
    return lidvidsCumulative.contains(singleLidOrLidvidReference) || lidsCumulative.contains(singleLidOrLidvidReference);
  }

  /**
   * Report a WARNING if any LID or LIDVID references does not resolve to at least one element in
   * the list of logical identifiers.
   *
   * @param validationRule The rule of the validation, e.g. pds4.label, pds4.bundle. This value can
   *        be null since a rule is not required within validate module.
   * @return None
   */
  public void reportLidOrLidvidReferenceToNonExistLogicalReferences() {
    // After all the local_identifier and lid_reference or lidliv_reference tags are
    // collected, they can be check if they are pointing to local identifier
    // collected.

    LOG.debug(
        "reportLidOrLidvidReferenceToNonExistLogicalReferences:this.lidOrLidVidReferencesCumulative.size() {}",
        this.lidOrLidVidReferencesCumulativeFileNames.size());
    try {
      int indexToFilenames = 0;
      for (String singleLidOrLidvidReference : this.lidOrLidVidReferencesCumulative) {
        long t0 = System.currentTimeMillis();
        LOG.debug(
            "reportLidOrLidvidReferenceToNonExistLogicalReferences:VALIDATING_REFERENCE:singleLidOrLidvidReference,filename {},{}",
            singleLidOrLidvidReference,
            this.lidOrLidVidReferencesCumulativeFileNames
                .get(indexToFilenames));
        // It is possible that the reference does not contain a version, we must check
        // for existence differently.
        if (!this.doesReferenceContainsVersion(singleLidOrLidvidReference)) {
          if (!this
              .bruteForceCheckForNonExistLogicalReferences(singleLidOrLidvidReference)) {

            // We also need to check if the product is actually a product in the bundle.
            // We should not throw a WARNING if the product does not belong to the bundle.
            String filename = this.lidOrLidVidReferencesCumulativeFileNames
                .get(indexToFilenames).toString();
            String logicalIdentifierPerLidReference =
                this.lidOrLidvidReferenceToLogicalIdentifierMap
                    .get(singleLidOrLidvidReference);

            LOG.debug(
                "reportLidOrLidvidReferenceToNonExistLogicalReferences:REFERENCE_WITH_VERSION:filename,singleLidOrLidvidReference,logicalIdentifierPerLidReference {},{},{}",
                filename, singleLidOrLidvidReference, logicalIdentifierPerLidReference);

            boolean productBelongToBundleFlag = this
                .isIdentiferMatchingBundleBaseID(logicalIdentifierPerLidReference);
            boolean referenceBelongToBundleFlag = this
                .isIdentiferMatchingBundleBaseID(singleLidOrLidvidReference);

            // Only throw a WARNING if the product does belong to this bundle.
            if (productBelongToBundleFlag) {
              if (referenceBelongToBundleFlag) {
                LOG.debug(
                    "reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_IS_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",
                    filename, logicalIdentifierPerLidReference);
                this.performReporting(singleLidOrLidvidReference, false,
                    indexToFilenames);
              }
            } else {
              LOG.debug(
                  "reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_NOT_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",
                  filename, logicalIdentifierPerLidReference);
            }
          } else {
            LOG.debug(
                "reportLidOrLidvidReferenceToNonExistLogicalReferences:LID_REFERENCE:singleLidOrLidvidReference {} is in logicalIdentifiersCumulative",
                singleLidOrLidvidReference);
          }
        } else if (!this.lidvidsCumulative
            .contains(singleLidOrLidvidReference)) {

          // We also need to check if the product is actually a product in the bundle.
          // We should not throw a WARNING if the product does not belong to the bundle.
          String filename = this.lidOrLidVidReferencesCumulativeFileNames
              .get(indexToFilenames).toString();
          String logicalIdentifierPerLidReference =
              this.lidOrLidvidReferenceToLogicalIdentifierMap
                  .get(singleLidOrLidvidReference);

          LOG.debug(
              "reportLidOrLidvidReferenceToNonExistLogicalReferences:REFERENCE_WITHOUT_VERSION:filename,singleLidOrLidvidReference,logicalIdentifierPerLidReference {},{},{}",
              filename, singleLidOrLidvidReference, logicalIdentifierPerLidReference);

          boolean productBelongToBundleFlag = this
              .isIdentiferMatchingBundleBaseID(logicalIdentifierPerLidReference);

          // Only throw a WARNING if the product does belong to this bundle.
          if (productBelongToBundleFlag) {
            LOG.debug(
                "reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_IS_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",
                filename, logicalIdentifierPerLidReference);
            this.performReporting(singleLidOrLidvidReference, true,
                indexToFilenames);
          } else {
            LOG.debug(
                "reportLidOrLidvidReferenceToNonExistLogicalReferences:PRODUCT_NOT_IN_BUNDLE:filename,logicalIdentifierPerLidReference {},{}",
                filename, logicalIdentifierPerLidReference);
          }
        } else {
          LOG.debug(
              "reportLidOrLidvidReferenceToNonExistLogicalReferences:LIDVID_REFERENCE:singleLidOrLidvidReference {} is in logicalIdentifiersCumulative",
              singleLidOrLidvidReference);
        }
        indexToFilenames += 1;
        if (isDebugLogLevel()) {
          long elapsed = System.currentTimeMillis() - t0;
          System.out.println(
                  "DEBUG  [" + ProblemType.TIMING_METRICS.getKey() + "]  " + System.currentTimeMillis()
                          + " :: " + singleLidOrLidvidReference + " :: " + "bundleReferentialIntegrity" + " in " + (elapsed) + " ms");
        }

      } // end for loop
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isIdentiferMatchingBundleBaseID(String singleLogicalIdentifier) {
    // Given a logical identifier, check if it contains the bundle base identifier.
    // If the bundle base identifier is urn:nasa:pds:kaguya_grs_spectra
    // then urn:nasa:pds:kaguya_grs_spectra:document:kgrs_calibrated_spectra does
    // contain the bundle base identifier.
    boolean identifierMatchBundleBaseIDFlag = false;
    if (singleLogicalIdentifier != null) {
      if ((this.bundleBaseID != null)
          && singleLogicalIdentifier.startsWith(this.bundleBaseID)) {
        identifierMatchBundleBaseIDFlag = true;
      }
    }

    LOG.debug(
        "isIdentiferMatchingBundleBaseID:singleLogicalIdentifier,this.bundleBaseID,identifierMatchBundleBaseIDFlag {},{},{}",
        singleLogicalIdentifier, this.bundleBaseID,
        identifierMatchBundleBaseIDFlag);
    return (identifierMatchBundleBaseIDFlag);
  }

  private String getBundleBaseID(List<String> logicalIdentifiers,
      String bundleFilename) {
    // Given a list of logical identifier from a bundle, fetch the bundle base ID
    // urn:nasa:pds:kaguya_grs_spectra:document:kgrs_calibrated_spectra -->
    // urn:nasa:pds:kaguya_grs_spectra
    // urn:nasa:pds:kaguya_grs_spectra --> urn:nasa:pds:kaguya_grs_spectra
    // Note that a bundle should only have one logical_identifier so we pick the
    // first element and print a warning if there are more than one.
    String bundleBaseID = null;
    if (logicalIdentifiers.size() >= 1) {
      if (logicalIdentifiers.size() == 1) {
        // Split the logical id and fetch the first 4 to form the bundleBaseID
        String[] splittedTokens = logicalIdentifiers.get(0).split(":");
        if (splittedTokens.length >= 4) {
          bundleBaseID = splittedTokens[0] + ":" + splittedTokens[1] + ":" + splittedTokens[2] + ":"
              + splittedTokens[3];
        } else {
          LOG.error(
              "getBundleBaseID: Expecting at least 4 tokens from parsing logical identifier {}",
              logicalIdentifiers.get(0));
        }
      } else {
        LOG.warn(
            "getBundleBaseID: Expecting only 1 logical identifier but received {} from list of identifiers from bundle file {}",
            logicalIdentifiers.size(), bundleFilename);
      }
    }
    return (bundleBaseID);
  }

  private String getParentIdFromBundleLogical(String bundleLogicalId) {
    // Given a bundle urn:nasa:pds:kaguya_grs_spectra::1.1, returns
    // the parent id: urn:nasa:pds:kaguya_grs_spectra
    String parentId = null;

    String[] tokens = {};
    if (bundleLogicalId != null) {
      tokens = bundleLogicalId.split(":");
    }
    if (tokens.length >= 4) {
      parentId = tokens[0] + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3];
    } else {
      LOG.error("getParentIdFromBundleLogical: Expecting at least 4 tokens from {}",
          bundleLogicalId);
    }

    return (parentId);
  }

  private void addUniqueReferencesToMap(HashMap<String, HashSetReferenceInfo> hashMap,
      List<String> contextLidOrLidVidReferences, URL url, String parentId) {
    // Given a list of references, add unique references to provided hashMap.
    // The key to hashMap is the logical identifier stored in parentId variable.
    LOG.debug("addUniqueReferencesToMap:contextLidOrLidVidReferences.size {},{}",
        contextLidOrLidVidReferences.size(), url);
    LOG.debug("addUniqueReferencesToMap:parentId,contextLidOrLidVidReferences.size {},{},{}",
        parentId, contextLidOrLidVidReferences.size(), url);
    LOG.debug(
        "addUniqueReferencesToMap:referenceType,url,contextLidOrLidVidReferences.size {},{},{}",
        this.getReferenceType(), url, contextLidOrLidVidReferences.size());

    int numReferencesAdded = 0;
    for (String singleReference : contextLidOrLidVidReferences) {
      // Check if parentId exist yet in hashMap. If yes, add the new singleReference
      // to the list of references for parentId
      if (hashMap.keySet().contains(parentId)) {
        HashSetReferenceInfo setOfReferences = hashMap.get(parentId);
        if (setOfReferences != null && !setOfReferences.doesReferenceExist(singleReference)) {
          // Add the new reference if setOfReferences does not already contains it.
          setOfReferences.addReference(singleReference, url);
          numReferencesAdded += 1;
          LOG.debug(
              "addUniqueReferencesToMap:ADDING_REFERENCE_TO_PARENT_EXISTING_REFERENCE_NEW:referenceType,parentId,singleReference {},{},{}",
              this.getReferenceType(), parentId, singleReference);
        } else {
          LOG.debug(
              "addUniqueReferencesToMap:ADDING_REFERENCE_TO_PARENT_EXISTING_REFERENCE_EXISTING:referenceType,parentId,singleReference {},{},{}",
              this.getReferenceType(), parentId, singleReference);
        }
      } else {
        HashSetReferenceInfo setOfReferences = new HashSetReferenceInfo();
        setOfReferences.addReference(singleReference, url);
        numReferencesAdded += 1;
        hashMap.put(parentId, setOfReferences);
        LOG.debug(
            "addUniqueReferencesToMap:ADDING_REFERENCE_TO_PARENT_NEW_REFERENCE_NEW:referenceType,parentId,singleReference {},{},{}",
            this.getReferenceType(), parentId, singleReference);
      }
      LOG.debug("addUniqueReferencesToMap:parentId,singleReference {},{}", parentId,
          singleReference);
    }
    LOG.debug("addUniqueReferencesToMap:referenceType,parentId,url,numReferencesAdded {},{},{},{}",
        this.getReferenceType(), parentId, url, numReferencesAdded);
  }

  private void collectAllContextReferences(List<String> allContextAreaRefs,
      List<String> logicalIdentifiers, List<String> lidOrLidVidReferences,
      boolean labelIsBundleFlag, boolean labelIsCollectionFlag, URL url) {
    if ((logicalIdentifiers == null) || logicalIdentifiers.isEmpty()) {
      return;
    }
    if (labelIsCollectionFlag || labelIsBundleFlag) {
      if (labelIsBundleFlag) {
        this.addUniqueReferencesToMap(this.bundleReferenceMap,
            allContextAreaRefs, url, logicalIdentifiers.get(0));
      } else {
        this.addUniqueReferencesToMap(
            this.collectionReferenceMap, allContextAreaRefs, url,
            logicalIdentifiers.get(0));
      }
    } else {
      this.addUniqueReferencesToMap(
          this.contextReferencesCumulative, allContextAreaRefs, url,
          logicalIdentifiers.get(0));
    }
    LOG.debug("collectAllContextReferences:url,contextReferencesCumulative {},{},{}", url,
        contextReferencesCumulative, contextReferencesCumulative.size());
  }

  private void collectAllContextReferences(DOMSource domSource,
      List<String> logicalIdentifiers, List<String> lidOrLidVidReferences,
      boolean labelIsBundleFlag, boolean labelIsCollectionFlag, URL url) {
    // https://github.com/NASA-PDS/validate/issues/69 As a user, I want to validate
    // that all context objects specified in observational products are referenced
    // in the parent bundle/collection Reference_List
    // Collect all the context references defined for each label under the
    // "Context_Area" tag.

    ArrayList<String> allContextLidOrLidVidReferencesPerLabel = new ArrayList<>();
    ArrayList<String> contextLidOrLidVidReferences = new ArrayList<>();

    // Pass reportCarriageReturns=false here because getLidVidReferences() already
    // checks all Internal_Reference elements (including context area ones) with the
    // comprehensive INTERNAL_REFERENCE_AREA XPath. Reporting here too would cause
    // duplicate error.validation.invalid_field_value errors for the same tag.
    contextLidOrLidVidReferences = LabelUtil.getIdentifiersCommon(domSource, url,
        this.tagsList, LabelUtil.CONTEXT_AREA_INVESTIGATION_AREA_REFERENCE,
        false);
    allContextLidOrLidVidReferencesPerLabel.addAll(contextLidOrLidVidReferences);

    contextLidOrLidVidReferences =
        LabelUtil.getIdentifiersCommon(domSource, url, this.tagsList,
            LabelUtil.CONTEXT_AREA_OBSERVATION_SYSTEM_COMPONENT_REFERENCE, false);
    allContextLidOrLidVidReferencesPerLabel.addAll(contextLidOrLidVidReferences);

    contextLidOrLidVidReferences = LabelUtil.getIdentifiersCommon(domSource, url,
        this.tagsList, LabelUtil.CONTEXT_AREA_TARGET_IDENTIFICATION_REFERENCE,
        false);
    allContextLidOrLidVidReferencesPerLabel.addAll(contextLidOrLidVidReferences);

    // If the label is a bundle or collection, all identifiers in Context_Area can
    // be collected in the appropriate map.

    if (labelIsCollectionFlag || labelIsBundleFlag) {
      if ((logicalIdentifiers != null) && !logicalIdentifiers.isEmpty()) {
        // Collection the references for either a bundle or a collection. Put in them in
        // the appropriate map.
        if (labelIsBundleFlag) {
          // Add all references listed in Context_Area
          this.addUniqueReferencesToMap(
              this.bundleReferenceMap, allContextLidOrLidVidReferencesPerLabel,
              url, logicalIdentifiers.get(0));
          // Add all references listed in Reference_List
          // this.addUniqueReferencesToMap(this.bundleReferenceMap,
          // lidOrLidVidReferences,url,logicalIdentifiers.get(0));
          // The [lid/lidvid] references in the Reference_List are not context references
          // so they are not added.
          // They were added erroneously previously.
        } else if (labelIsCollectionFlag) {
          // Add all references listed in Context_Area
          this.addUniqueReferencesToMap(
              this.collectionReferenceMap,
              allContextLidOrLidVidReferencesPerLabel, url, logicalIdentifiers.get(0));
          // Add all references listed in Reference_List
          // this.addUniqueReferencesToMap(this.collectionReferenceMap,
          // lidOrLidVidReferences,url,logicalIdentifiers.get(0));
          // The [lid/lidvid] references in the Reference_List are not context references
          // so they are not added.
          // They were added erroneously previously.
          LOG.debug(
              "collectAllContextReferences:NON_CONTEXT_REFERENCES_NOT_ADDED:lidOrLidVidReferences {},{}",
              lidOrLidVidReferences, lidOrLidVidReferences.size());
        } else {
          LOG.error("This function does not support referenceType {}",
              this.getReferenceType());
        }
      }
    } else {
      // Regular label, all references listed in label (lidOrLidVidReferences) gets
      // stored in contextReferencesCumulative.
      // The parent ID in the label is the logical indentifier.
      this.addUniqueReferencesToMap(
          this.contextReferencesCumulative,
          allContextLidOrLidVidReferencesPerLabel, url, logicalIdentifiers.get(0));
    }

    LOG.debug("collectAllContextReferences:url,contextReferencesCumulative {},{},{}", url,
        contextReferencesCumulative, contextReferencesCumulative.size());
  }

  private void crawlParentForBundleLabel(URL crawlTarget) {
    // Given a crawl target, crawl the parent target for any Bundle labels.
    URL parentURL = Utility.getParent(crawlTarget);
    URL url = null;

    List<Target> children = new ArrayList<>();
    try {
      if (getContext().getCrawler() != null) {
        children = getContext().getCrawler().crawl(parentURL, false, getContext().getFileFilters()); // Get also the directories.
      } else {
        LOG.warn("crawlParentForBundleLabel:getContext().getCrawler() is null for URL {}",
            crawlTarget);
      }
      LOG.debug("crawlParentForBundleLabel:crawlTarget,children.size() {},{}", crawlTarget,
          children.size());
      for (Target child : children) {
        LOG.debug("crawlParentForBundleLabel:FilenameUtils.getName(child.toString()) {}",
            FilenameUtils.getName(child.toString()));
        url = child.getUrl();
        if (url.toString().endsWith("." + getContext().getLabelExtension())) {

          // Check to see if the label is collection or a bundle (instead of regular
          // label).
          if (TargetExaminer.isTargetBundleType (child.getUrl())) {
            // Save the URL of the bundle to be used to report the error.
            this.parentBundleURL = url;
            LOG.debug("crawlParentForBundleLabel:BUNDLE_LABEL_FOUND_TRUE:parentBundleURL,url {},{}",
                this.parentBundleURL, url);
          } else {
            LOG.debug(
                "crawlParentForBundleLabel:BUNDLE_LABEL_FOUND_FALSE:parentBundleURL,url {},{}",
                this.parentBundleURL, url);
          }
        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, io.getMessage());
    } catch (Exception ex) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, ex.getMessage());
    }

    return;
  }

  public void additionalReferentialIntegrityChecks(URL crawlTarget) {
    this.additionalReferentialIntegrityChecks(crawlTarget, null);
  }

  /**
   * Perform additional referential integrity check beside the normal check. For all references in
   * all labels, check if they refer to a logical identifier that is valid and is in this
   * bundle/collection.
   *
   * @param crawlTarget The URL of the target to validate for.
   * @return None
   */
  public void additionalReferentialIntegrityChecks(URL crawlTarget, URL bundleURL) {
    URL url = null;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    } catch (javax.xml.parsers.ParserConfigurationException ex) {
      LOG.error("additionalReferentialIntegrityChecks:Exception found with message {}",
          ex.getMessage());
      return;
    }
    DocumentBuilder db = null;
    Document xml = null;
    DOMSource domSource = null;

    boolean labelIsCollectionFlag = false;
    boolean labelIsBundleFlag = false;
    List<Target> children = new ArrayList<>();
    String parentId = null;

    try {
      if (getContext().getCrawler() != null) {
        children = getContext().getCrawler().crawl(crawlTarget, true, getContext().getFileFilters()); // Get also the directories.
      } else {
        LOG.warn("additionalReferentialIntegrityChecks:getContext().getCrawler() is null");
      }
      LOG.debug("additionalReferentialIntegrityChecks:crawlTarget {}", crawlTarget);
      LOG.debug(
          "additionalReferentialIntegrityChecks:crawlTarget,children.size():afor_reduced: {},{}",
          crawlTarget, children.size());

      // Because a collection is one directory below the bundle,
      // crawl the parent directory for bundle label to collect the bundle name so a
      // message can be attached to the parent bundle.
      if (bundleURL != null) {
        this.parentBundleURL = bundleURL;
      } else {
        this.crawlParentForBundleLabel(crawlTarget);
      }

      db = dbf.newDocumentBuilder();

      for (Target child : children) {
        LOG.debug("additionalReferentialIntegrityChecks:FilenameUtils.getName(child.toString()) {}",
            FilenameUtils.getName(child.toString()));

        // Regardless of what kinds of file it is, parse it to find all the
        // local_identifier and lid_reference or lidvid_reference tags.
        url = child.getUrl();

        // Check this URL has been parsed before. If yes, skip this file.
        if (this.urlsParsedCumulative.contains(url)) {
          LOG.info("SKIPPING_URL_TRUE:referenceType,url {},{}",
              this.getReferenceType(), url);
          continue;
        }

        if (url.toString().endsWith("." + getContext().getLabelExtension()) && TargetExaminer.isTargetALabel(url)) {
         LOG.info("SKIPPING_URL_FALSE:referenceType,url {},{}",
              this.getReferenceType(), url);
          labelIsCollectionFlag = false;
          labelIsBundleFlag = false;

          // Check to see if the label is collection or a bundle (instead of regular
          // label).
          if (TargetExaminer.isTargetBundleType (child.getUrl())) {
            labelIsBundleFlag = true;
            // Save the URL of the bundle to be used to report the error.
            this.parentBundleURL = url;
          }
          if (TargetExaminer.isTargetCollectionType (child.getUrl())) {
            labelIsCollectionFlag = true;
          }
          LabelCacheEntry cached = this.getCachedLabelIdentifiers(url);
          List<String> lidOrLidVidReferences;
          List<String> logicalIdentifiers;
          if (cached != null) {
            logicalIdentifiers = cached.getLogicalIdentifiers();
            lidOrLidVidReferences = cached.getLidOrLidVidReferences();
            // Replay any suppressed lid_reference \n errors from the cache without re-parsing.
            LabelUtil.replayCarriageReturnErrors(cached.getSuppressedLidVidErrors(), url);
          } else {
            xml = db.parse(url.openStream());
            domSource = new DOMSource(xml);
            // Note that the function getLidVidReferences() collects all Internal_Reference
            // elements in the PDS4 core namespace (including those in Reference_List,
            // Context_Area, discipline LDD areas, and any other location in the label).
            // so the lidOrLidVidReferencesCumulative will be a cumulative collection of all
            // references collected in lidOrLidVidReferences for each label.
            lidOrLidVidReferences = LabelUtil.getLidVidReferences(domSource, url);
            logicalIdentifiers = LabelUtil.getLogicalIdentifiers(domSource, url);
          }
          LOG.debug("additionalReferentialIntegrityChecks:url,lidOrLidVidReferences {},{}", url,
              lidOrLidVidReferences.size());
          LOG.debug("additionalReferentialIntegrityChecks:url,logicalIdentifiers {},{}", url,
              logicalIdentifiers.size());

          if ((logicalIdentifiers != null) && !logicalIdentifiers.isEmpty()) {
            this.lidvidsCumulative.addAll(logicalIdentifiers);
            lidsCumulative.addAll(removeVersionIds(logicalIdentifiers));

            // If the label is a bundle, parse the logical identifier for the base ID.
            if (labelIsBundleFlag) {
              this.bundleBaseID =
                  this.getBundleBaseID(logicalIdentifiers, child.toString());
              parentId =
                  this.getParentIdFromBundleLogical(logicalIdentifiers.get(0));
              this.bundleURLMap.put(parentId, url); // Save the bundle URL so
                                                                        // any
                                                                        // warning/error can be
                                                                        // reported
                                                                        // for that bundle.
            }
          }

          if ((lidOrLidVidReferences != null) && !lidOrLidVidReferences.isEmpty()) {
            String urlstr = url.toString();
            for (int ii = 0; ii < lidOrLidVidReferences.size(); ii++) {
              LOG.debug(
                  "additionalReferentialIntegrityChecks:ii,url,lidOrLidVidReferences.get(ii) {},{},[{}]",
                  ii, url, lidOrLidVidReferences.get(ii));
              // Do not add duplicate references by checking for existence of the combination
              // reference id in this.lidOrLidVidReferencesCumulative
              // and the file name has not already been added already in
              // this.lidOrLidVidReferencesCumulativeFileNames list.
              // Note that because the reference id can be the same, the combination of the id
              // plus the file name will make it unique.
              if (!this
                  .lidAndFilenameCombo.contains(lidOrLidVidReferences.get(ii) + urlstr)) {

                this.lidOrLidVidReferencesCumulative
                    .add(lidOrLidVidReferences.get(ii));
                this.lidOrLidVidReferencesCumulativeFileNames.add(url); // Save
                                                                                            // the
                                                                                            // file
                                                                                            // name
                                                                                            // as
                                                                                            // well
                                                                                            // so it
                                                                                            // can
                                                                                            // be
                                                                                            // referred
                                                                                            // to.
                this.lidAndFilenameCombo.add(lidOrLidVidReferences.get(ii) + urlstr);
                LOG.debug("additionalReferentialIntegrityChecks:ADDING_REFERENCE {}",
                    lidOrLidVidReferences.get(ii), lidOrLidVidReferencesCumulative.size());
              }

              // Every lid_reference or lidvid_reference is connected to a logical identifier.
              // Save that in a Map so a logical identifier can be retrieved from a
              // lid_reference or lidvid_reference as key.
              if ((logicalIdentifiers != null) && !logicalIdentifiers.isEmpty()) {
                this.lidOrLidvidReferenceToLogicalIdentifierMap
                    .put(lidOrLidVidReferences.get(ii), logicalIdentifiers.get(0));
              } else {
                LOG.error("Expecting the logicalIdentifiers array to be non-empty for label {}",
                    url);
              }
            } // end for loop
          }

          // https://github.com/NASA-PDS/validate/issues/69 As a user, I want to validate
          // that all context objects specified in observational products are referenced
          // in the parent bundle/collection Reference_List
          // Collect all the context references defined for each label under the
          // "Context_Area" tag.
          if (this.contextReferenceCheck) {
            if (cached != null) {
              this.collectAllContextReferences(cached.getContextAreaRefs(),
                  logicalIdentifiers, lidOrLidVidReferences, labelIsBundleFlag,
                  labelIsCollectionFlag, url);
            } else {
              this.collectAllContextReferences(domSource, logicalIdentifiers,
                  lidOrLidVidReferences, labelIsBundleFlag, labelIsCollectionFlag, url);
            }
          }

        } else {
          LOG.debug("additionalReferentialIntegrityChecks:NON_XML:url {}", url);
          if (Utility.isDir(url.toString())) {
            // If the url is a directory, make a recursive call to this same function.
            this.additionalReferentialIntegrityChecks(url);
          }

        }

        // Add this url so we won't be parsing it again.
        this.urlsParsedCumulative.add(url);

      } // end for (Target child : children)
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, io.getMessage());
    } catch (Exception ex) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, ex.getMessage());
    }

    LOG.debug(
        "additionalReferentialIntegrityChecks:referenceType,crawlTarget,logicalIdentifiersCumulative.size() {},{},{}",
        this.referenceType, crawlTarget,
        this.lidvidsCumulative.size());
    LOG.debug(
        "additionalReferentialIntegrityChecks:referenceType,crawlTarget,lidOrLidVidReferencesCumulative.size() {},{},{}",
        this.referenceType, crawlTarget,
        this.lidOrLidVidReferencesCumulative.size());
    LOG.debug(
        "additionalReferentialIntegrityChecks:referenceType,crawlTarget,lidOrLidVidReferencesCumulativeFilenames.size() {},{},{}",
        this.referenceType, crawlTarget,
        this.lidOrLidVidReferencesCumulativeFileNames.size());
    LOG.debug(
        "additionalReferentialIntegrityChecks:referenceType,crawlTarget,bundleOrCollectionReferenceMap {},{},{},{}",
        this.referenceType, crawlTarget, bundleOrCollectionReferenceMap,
        bundleOrCollectionReferenceMap.size());
  }

  private static Collection<String> removeVersionIds(Collection<String> logicalIdentifiers) {
    Collection<String> result = new HashSet<String>();
    for (String lidOrLidVid : logicalIdentifiers) {
      if (lidOrLidVid.contains("::")) {
        result.add(lidOrLidVid.split("::")[0]);
      }
      else {
        result.add(lidOrLidVid);
      }
    }
    return result;
  }

  /**
   * Reports an error to the validation listener.
   *
   * @param defn the problem definition
   * @param targetFile the validation target file containing the problem
   * @param lineNumber the line number, or -1 if no line number applies
   * @param columnNumber the column number, or -1 if no column number applies
   */
  protected void reportError(ProblemDefinition defn, URL targetUrl, int lineNumber,
      int columnNumber) {
    ValidationProblem problem = new ValidationProblem(defn, ValidationTarget.build(targetUrl),
        lineNumber, columnNumber, defn.getMessage());
    problemListener.addProblem(problem);
  }

  /**
   * Reports an error to the validation listener with a custom message.
   *
   * @param defn the problem definition
   * @param target the validation target containing the problem
   * @param lineNumber the line number, or -1 if no line number applies
   * @param columnNumber the column number, or -1 if no column number applies
   * @param message the error message to report
   */
  protected void reportError(ProblemDefinition defn, URL target, int lineNumber,
      int columnNumber, String message) {
    ValidationProblem problem = new ValidationProblem(defn, ValidationTarget.build(target),
        lineNumber, columnNumber, message);
    problemListener.addProblem(problem);
  }

  protected boolean isDebugLogLevel() {
    return getContext().getLogLevel().isDebugApplicable();
  }
}
