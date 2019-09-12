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
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

/**
 * Implements the rule that all files that look like labels in a folder
 * must be valid labels.
 */
public class LabelInFolderRule extends AbstractValidationRule {

  private static final String XML_SUFFIX = ".xml";

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location);
  }

  /**
   * Validates each file with a label suffix as a PDS4 label.
   */
  @ValidationTest
  public void validateLabelsInFolder() {
    ValidationRule labelRule = getContext().getRuleManager().findRuleByName("pds4.label");
    
    Crawler crawler = getContext().getCrawler();
    try {
      for (Target t : crawler.crawl(getTarget(), false, getContext().getFileFilters())) {
        try {
          labelRule.execute(getChildContext(t.getUrl()));
        } catch (Exception e) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, t.getUrl(), -1, -1, e.getMessage());
        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
    }
  }

}
