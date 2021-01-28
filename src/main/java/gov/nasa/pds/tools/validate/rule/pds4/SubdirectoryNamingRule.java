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

import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a rule that checks for children of a directory
 * using illegal names. These are directories that can only
 * occur in the root directory of a bundle.
 */
public class SubdirectoryNamingRule extends AbstractValidationRule {
  private static final Logger LOG = LoggerFactory.getLogger(SubdirectoryNamingRule.class);
  private static final String[] ILLEGAL_DIRECTORY_NAMES = {
    "browse",
    "calibration",
    "context",
    "data",
    "document",
    "geometry",
    "miscellaneous",
    "spice_kernels",
    "xml_schema"
  };

  private static String illegalNamePatternStr;
  static {
    StringBuilder builder = new StringBuilder();
    for (String s : ILLEGAL_DIRECTORY_NAMES) {
      if (builder.length() > 0) {
        builder.append("|");
      }
      builder.append(s);
    }

    illegalNamePatternStr = builder.toString();
  }

  private static final Pattern ILLEGAL_NAME_PATTERN = Pattern.compile(illegalNamePatternStr);

  @Override
  public boolean isApplicable(String location) {
    URL url;
    try {
      url = new URL(location);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return false;
    }

    if (!Utility.isDir(url)) {
      return false;
    }
    return true;
  }

  /**
   * Checks for illegal subdirectory names.
   */
  @ValidationTest
  public void checkIllegalDirectoryNames() {
    try {
      Crawler crawler = getContext().getCrawler();
      LOG.debug("checkIllegalDirectoryNames:getTarget() {}",getTarget());
      List<Target> targets = crawler.crawl(getTarget(), FalseFileFilter.INSTANCE);
      for (Target target : targets) {
          LOG.debug("checkIllegalDirectoryNames:target,target.isDir() {},{}",target,target.isDir());
          LOG.debug("checkIllegalDirectoryNames:target,FilenameUtils.getName(Utility.removeLastSlash(target.toString())) {},{}",target,FilenameUtils.getName(Utility.removeLastSlash(target.toString())));
          if (target.isDir()) {
              Matcher matcher = ILLEGAL_NAME_PATTERN.matcher(FilenameUtils.getName(Utility.removeLastSlash(target.toString())));
              if (matcher.matches()) {
                  LOG.debug("checkIllegalDirectoryNames:Problem UNALLOWED_BUNDLE_SUBDIRECTORY_NAME found for directory " + target);
                  reportError(PDS4Problems.UNALLOWED_BUNDLE_SUBDIRECTORY_NAME, target.getUrl(), -1, -1);
              }
          }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
    }
  }

}
