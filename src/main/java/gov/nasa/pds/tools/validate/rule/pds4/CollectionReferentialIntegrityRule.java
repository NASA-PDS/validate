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
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import gov.nasa.pds.tools.inventory.reader.InventoryEntry;
import gov.nasa.pds.tools.inventory.reader.InventoryReaderException;
import gov.nasa.pds.tools.inventory.reader.InventoryTableReader;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.Identifier;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Validation rule that performs referential integrity checking
 * on a Product_Collection product label.
 * 
 * @author mcayanan
 *
 */
public class CollectionReferentialIntegrityRule extends AbstractValidationRule {
  private static final Pattern COLLECTION_LABEL_PATTERN = 
      Pattern.compile("(.*_)*collection(_.*)*\\.xml", Pattern.CASE_INSENSITIVE);
  
  private static final String PRODUCT_CLASS =
      "//*[starts-with(name(),'Identification_Area')]/product_class";
  
  private static final String LOGICAL_IDENTIFIER =
      "//*[starts-with(name(),'Identification_Area')]/logical_identifier";
  
  private String lid = null;
  
  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location)) {
      return true;
    } else {
      return false;
    }
  }

  @ValidationTest
  public void collectionReferentialIntegrityRule() {
    Crawler crawler = getContext().getCrawler();
    try {
      IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
      List<Target> children = crawler.crawl(getTarget(), regexFileFilter);
      // Check for collection(_.*)?\.xml file.
      for (int i = 0; i < children.size(); i++) {
        Target child = children.get(i);
        if (child.isDir()) {
          children.addAll(crawler.crawl(child.getUrl(), regexFileFilter));
        } else {
          try {
            XMLExtractor extractor = new XMLExtractor(child.getUrl());
            if("Product_Collection".equals(
                extractor.getValueFromDoc(PRODUCT_CLASS))) {
              getListener().addLocation(child.getUrl().toString());
              this.lid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
              getCollectionMembers(child.getUrl());
            }
          } catch (Exception e) {
            //Ignore. This isn't a valid Collection label, so let's skip it.
          } 
        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, 
          io.getMessage());
    }
  }
  
  private void getCollectionMembers(URL collection) {
    try {
      InventoryTableReader reader = new InventoryTableReader(collection);
      for (InventoryEntry entry = new InventoryEntry(); entry != null;) {
        if (!entry.isEmpty()) {
          String identifier = entry.getIdentifier();
          if (!identifier.equals("")) {
            //Check for a LID or LIDVID
            Identifier id = parseIdentifier(identifier);
            List<Map.Entry<Identifier, String>> matchingMembers = 
                new ArrayList<Map.Entry<Identifier, String>>();
            for (Map.Entry<Identifier, String> idEntry : 
              getRegistrar().getIdentifierDefinitions().entrySet()) {
              if (id.equals(idEntry.getKey())) {
                matchingMembers.add(idEntry);
              }
            }
            if (matchingMembers.isEmpty() && 
                "P".equalsIgnoreCase(entry.getMemberStatus())) {
              getListener().addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.WARNING,
                      ProblemType.MEMBER_NOT_FOUND,
                      "The member '" + id + "' could not be found in "
                          + "any product within the given target."), 
                      collection));
            } else if (matchingMembers.size() == 1) {
              verifyLidPrefix(id.getLid(), this.lid, entry.getMemberStatus(),
            	      collection);
              getListener().addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.INFO,
                      ProblemType.MEMBER_FOUND,
                      "The member '" + id + "' is identified in "
                          + "the following product: "
                          + matchingMembers.get(0).getValue()), 
                      collection));
            } else if (matchingMembers.size() > 1) {
              verifyLidPrefix(id.getLid(), this.lid, entry.getMemberStatus(),
            	      collection);
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
                                + "': "
                                + matchingIds.get(matchingId).toString()),
                       collection));
                    foundDuplicates = true;
                  }
                }
                if (!foundDuplicates) {
                  List<String> targets = new ArrayList<String>();
                  for (Map.Entry<Identifier, String> m : matchingMembers) {
                    targets.add(m.getValue());
                  }
                  getListener().addProblem(new ValidationProblem(
                      new ProblemDefinition(ExceptionType.INFO,
                          ProblemType.DUPLICATE_MEMBERS_INFO,
                          "The member '" + id + "' is identified "
                              + "in multiple proudcts: " + targets.toString()),
                      collection));
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
                    collection));
              }
            }
            getRegistrar().addIdentifierReference(collection.toString(), id);
          }
        }
        entry = reader.getNext();
      }
    } catch (InventoryReaderException e) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, collection, -1, -1,
          e.getMessage());
    }
  }
  
  private Identifier parseIdentifier(String identifier) {
    if (identifier.indexOf("::") != -1) {
      return new Identifier(identifier.split("::")[0],
          identifier.split("::")[1]);
    } else {
      return new Identifier(identifier.split("::")[0]);
    }
  }
  
  private Map<String, List<String>> findMatchingIds(
      List<Map.Entry<Identifier, String>> products) {
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
