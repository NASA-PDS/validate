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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.inventory.reader.InventoryEntry;
import gov.nasa.pds.tools.inventory.reader.InventoryReaderException;
import gov.nasa.pds.tools.inventory.reader.InventoryTableReader;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.ReferentialIntegrityUtil;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.Identifier;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Validation rule that performs referential integrity checking on a Product_Collection product
 * label.
 *
 * @author mcayanan
 *
 */
public class CollectionReferentialIntegrityRule extends AbstractValidationRule {
  private static final Logger LOG =
      LoggerFactory.getLogger(CollectionReferentialIntegrityRule.class);

  private static final String PRODUCT_CLASS =
      "//*[starts-with(name(),'Identification_Area')]/product_class";

  private static final String LOGICAL_IDENTIFIER =
      "//*[starts-with(name(),'Identification_Area')]/logical_identifier";

  private String lid = null;
  private double totalTimeElapsed = 0.0;

  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location)) {
      return true;
    }
    return false;
  }

  @ValidationTest
  public void collectionReferentialIntegrityRule() {
    LOG.debug("collectionReferentialIntegrityRule:START: ");
    // Use the targets registered during RegisterTargets step to identify
    // collections
    if (getContext().isLastDirectory()) {
      Map<String, ValidationTarget> collections = getRegistrar().getCollections();
      LOG.debug("collectionReferentialIntegrityRule:getTarget() {}, num collections {}",
          getTarget(), collections.keySet().size());

      for (Map.Entry<String, ValidationTarget> collection : collections.entrySet()) {
        Target collectionTarget = new Target(collection.getValue().getUrl(), false);
        try {
          XMLExtractor extractor = new XMLExtractor(collectionTarget.getUrl());
          if ("Product_Collection".equals(extractor.getValueFromDoc(PRODUCT_CLASS))) {
            getListener().addLocation(collectionTarget.getUrl().toString());
            this.lid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
            getCollectionMembers(collectionTarget.getUrl());
          }
        } catch (Exception e) {
          // Ignore. This isn't a valid Collection label, so let's skip it.
        }
      }

      // https://github.com/NASA-PDS/validate/issues/69
      // As a user, I want to validate that all context objects specified in
      // observational products are referenced in the parent bundle/collection
      // Reference_List
      //
      // For every references in the Context_Area, check if it also occur in the
      // bundle/collection Reference_List,
      // i.e: All context objects specified in observational are referenced in the
      // parent bundle/collection Reference_List
      //
      ReferentialIntegrityUtil.initialize("collection", getTarget(), getListener(), getContext());
      ReferentialIntegrityUtil.additionalReferentialIntegrityChecks(getTarget());
    }
  }

  private void getCollectionMembers(URL collection) {
    LOG.info("getCollectionMembers: BEGIN_PROCESSING_COLLECTION:collection {}", collection);
    long startTime = System.currentTimeMillis();
    try {
      int numOfCollectionMembers = 0;

      InventoryTableReader reader = new InventoryTableReader(collection);

      Map<String, Set<Identifier>> identifersByLid = new HashMap<>();
      for (Identifier idEntry : getRegistrar().getIdentifierDefinitions().keySet()) {
        indexEntry(identifersByLid, idEntry);
      }

      for (InventoryEntry entry = new InventoryEntry(); entry != null;) {
        if (!entry.isEmpty()) {
          numOfCollectionMembers++;
          long t0 = System.currentTimeMillis();
          validateEntry(collection, numOfCollectionMembers, entry, identifersByLid);
          if (isDebugLogLevel()) {
            long elapsed = System.currentTimeMillis() - t0;
            System.out.println(
                    "DEBUG  [" + ProblemType.TIMING_METRICS.getKey() + "]  " + System.currentTimeMillis()
                            + " :: " + entry.getIdentifier() + " :: " + "referentialIntegrity" + " in " + (elapsed) + " ms");
          }
        }
        entry = reader.getNext();
      }
      long records = reader.getNumRecords();
      LOG.debug("getCollectionMembers: collection,numOfCollectionMembers,records {},{},{}",
          collection, numOfCollectionMembers, records);

      if (numOfCollectionMembers > 0 && records > 0 && numOfCollectionMembers != records) {
        String message = "Number of records read is not equal "
            + "to the defined number of records in the collection (expected " + records + ", got "
            + numOfCollectionMembers + ").";
        getListener().addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.RECORDS_MISMATCH, message),
            collection));
      }
      long finishTime = System.currentTimeMillis();
      long timeElapsed = finishTime - startTime;
      totalTimeElapsed += timeElapsed;

      LOG.info(
          "getCollectionMembers: END_PROCESSING_COLLECTION:collection,totalTimeElapsed,numOfCollectionMembers {},{},{}",
          collection, totalTimeElapsed, numOfCollectionMembers);
    } catch (InventoryReaderException e) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, collection, -1, -1, e.getMessage());
    }
  }

  private static void indexEntry(Map<String, Set<Identifier>> identifersByLid, Identifier idEntry) {
    identifersByLid.computeIfAbsent(idEntry.getLid(), x -> new HashSet<>()).add(idEntry);
  }

  private void validateEntry(URL collection, int numOfCollectionMembers, InventoryEntry entry, Map<String, Set<Identifier>> identifersByLid) {
    String identifier = entry.getIdentifier();

    LOG.debug("getCollectionMembers: numOfCollectionMembers {}", numOfCollectionMembers);
    LOG.debug("getCollectionMembers: identifier: {}", identifier);
    LOG.debug("getCollectionMembers: getTarget(): {}", getTarget());
    LOG.debug("getCollectionMembers: collection: {}", collection);
    if (!identifier.equals("")) {
      // Check for a LID or LIDVID
      Identifier id = parseIdentifier(identifier);
      LOG.debug("getCollectionMembers: id {}", id);
      LOG.debug("getCollectionMembers: id,id.hasVersion(),id.getVersion() {},{},{}", id,
          id.hasVersion(), id.getVersion());
      LOG.debug("getCollectionMembers: id,id.hasVersion(),collection {},{},{}", id,
          id.hasVersion(), collection);

      // https://github.com/NASA-PDS/validate/issues/230
      // New requirement: The 'P' entry must be a LIDVID (logical identifier and
      // version id separated by '::').
      // Report as error if not a LIDVID.
      if ("P".equalsIgnoreCase(entry.getMemberStatus())) {
        // If identifier has no version, it is not a LIDVID and should be flagged as an
        // error with the new type ProblemType.MISSING_VERSION.
        if (!id.hasVersion()) {
          getListener().addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.ERROR, ProblemType.MISSING_VERSION,
                  "The primary member '" + id + "' should include the version number"),
                  collection));
        }
      }


      List<Identifier> matchingMembers = identifersByLid.getOrDefault(id.getLid(), new HashSet<>()).stream().
              filter(id::nearNeighbor).collect(Collectors.toList());
      Map<Identifier, String> idMap = getRegistrar().getIdentifierDefinitions();


      LOG.debug("getCollectionMembers: id,matchingMembers.size() {},{}", id,
          matchingMembers.size());
      LOG.debug(
          "getCollectionMembers: id,matchingMembers.isEmpty(),entry.getMemberStatus() {},{},{}",
          id, matchingMembers.isEmpty(), entry.getMemberStatus());
      if (matchingMembers.isEmpty() && "P".equalsIgnoreCase(entry.getMemberStatus())) {
        getListener()
            .addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
                ProblemType.MEMBER_NOT_FOUND, "The member '" + id + "' could not be found in "
                    + "any product within the given target."),
                    collection));
      } else if (matchingMembers.size() == 1) {
        super.verifyLidPrefix(id.getLid(), this.lid, entry.getMemberStatus(), collection);
        getListener()
            .addProblem(
                new ValidationProblem(
                    new ProblemDefinition(ExceptionType.INFO, ProblemType.MEMBER_FOUND,
                        "The member '" + id + "' is identified in "
                            + "the following product: " + idMap.get(matchingMembers.get(0))),
                        collection));
        // LOG.debug("getCollectionMembers: id {} SUCCESS",id);
        // LOG.debug("getCollectionMembers: id {} SUCCESS: {}",id,"The member '" + id +
        // "' is identified in "
        // + "the following product: "
        // + matchingMembers.get(0).getValue());
      } else if (matchingMembers.size() > 1) {
        super.verifyLidPrefix(id.getLid(), this.lid, entry.getMemberStatus(), collection);
        ExceptionType exceptionType = ExceptionType.ERROR;
        if (!id.hasVersion()) {
          Map<String, List<String>> matchingIds = findMatchingIds(matchingMembers);
          boolean foundDuplicates = false;
          for (String matchingId : matchingIds.keySet()) {
            if (matchingIds.get(matchingId).size() > 1) {
              getListener().addProblem(new ValidationProblem(
                  new ProblemDefinition(exceptionType, ProblemType.DUPLICATE_VERSIONS,
                      "The member '" + id + "' is identified "
                          + "in multiple products, but with the same " + "version id '"
                          + matchingId.split("::")[1] + "': "
                          + matchingIds.get(matchingId).toString()),
                      collection));
              foundDuplicates = true;
            }
          }
          if (!foundDuplicates) {
            List<String> targets = new ArrayList<>();
            for (Identifier m : matchingMembers) {
              targets.add(idMap.get(m));
            }
            getListener()
                .addProblem(
                    new ValidationProblem(
                        new ProblemDefinition(ExceptionType.INFO,
                            ProblemType.DUPLICATE_MEMBERS_INFO,
                            "The member '" + id + "' is identified "
                                + "in multiple products: " + targets.toString()),
                            collection));
          }
        } else {
          List<String> targets = new ArrayList<>();
          for (Identifier m : matchingMembers) {
            targets.add(idMap.get(m));
          }
          getListener().addProblem(new ValidationProblem(new ProblemDefinition(exceptionType,
              ProblemType.DUPLICATE_MEMBERS, "The member '" + id + "' is identified "
                  + "in multiple products: " + targets.toString()),
                  collection));
        }
      }
      getRegistrar().addIdentifierReference(collection.toString(), id);
      indexEntry(identifersByLid, id);

    }
  }

  private Identifier parseIdentifier(String identifier) {
    // Even though the below identifier contains errors (blanks around '::' and too
    // many digits)
    // and may be caught by other validation rules, this function need to remove any
    // leading or trailing blanks from
    // tokens after the split() function:
    //
    // [urn:nasa:pds:cocirs_c2h4abund:data_derived:c2h4_abund_profiles :: 1.2.3.4]`

    if (identifier.indexOf("::") != -1) {
      return new Identifier(identifier.split("::")[0].trim(), identifier.split("::")[1].trim());
    }
    return new Identifier(identifier.split("::")[0].trim());
  }

  private Map<String, List<String>> findMatchingIds(List<Identifier> products) {
    Map<Identifier, String> idMap = getRegistrar().getIdentifierDefinitions();

    Map<String, List<String>> results = new HashMap<>();
    for (Identifier product : products) {
      if (results.get(product.toString()) != null) {
        List<String> targets = results.get(product.toString());
        targets.add(idMap.get(product));
      } else {
        List<String> targets = new ArrayList<>();
        targets.add(idMap.get(product));
        results.put(product.toString(), targets);
      }
    }
    return results;
  }

}
