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
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a rule that iterates over subdirectories, treating each
 * as a collection within a bundle, and applying the PDS4 collection
 * rules for each.
 */
public class CollectionInBundleRule extends AbstractValidationRule {
    private static final Logger LOG = LoggerFactory.getLogger(CollectionInBundleRule.class);

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location);
  }

  @ValidationTest
  public void testCollectionDirectories() {
    ValidationRule collectionRule = getContext().getRuleManager().findRuleByName("pds4.collection");

    if (collectionRule != null) {
      try {
        Crawler crawler = getContext().getCrawler();
        List<Target> dirs = crawler.crawl(getContext().getTarget(), FalseFileFilter.INSTANCE);
        
        int count = 0;
        for (Target dir :dirs) {
          if (++count == dirs.size())
              getContext().setLastDirectoryFlag(true);

          try {
            collectionRule.execute(getChildContext(dir.getUrl()));
          } catch (Exception e) {
            reportError(GenericProblems.UNCAUGHT_EXCEPTION, dir.getUrl(), -1, -1, e.getMessage());
          }
        }
      } catch (IOException io) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
      }
    }
  }

}
