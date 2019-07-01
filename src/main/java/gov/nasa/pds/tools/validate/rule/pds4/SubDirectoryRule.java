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
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Implements a rule that iterates over subdirectories, validating
 * each as a PDS4 folder.
 */
public class SubDirectoryRule extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location) && getContext().isRecursive();
  }

  @ValidationTest
  public void testCollectionDirectories() {
    FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.isDirectory();
        }
    };
    ValidationRule collectionRule = getContext().getRuleManager().findRuleByName("pds4.folder");

    if (collectionRule != null) {
      try {
        Crawler crawler = getContext().getCrawler();
        List<Target> dirs = crawler.crawl(getContext().getTarget(), FalseFileFilter.INSTANCE);
        for (Target dir : dirs) {
          try {
            if (dir.isDir()) {
              collectionRule.execute(getChildContext(dir.getUrl()));
            }
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
