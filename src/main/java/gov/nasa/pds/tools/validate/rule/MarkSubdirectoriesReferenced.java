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
package gov.nasa.pds.tools.validate.rule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.filefilter.FalseFileFilter;

import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;

/**
 * Implements a validation rule that marks all subdirectories as referenced.
 */
public class MarkSubdirectoriesReferenced extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    try {
      URL url = new URL(location);
      return Utility.isDir(url);
    } catch (MalformedURLException e) {
      return false;
    }
  }

  @ValidationTest
  public void markSubdirectoriesReferenced() {
    try {
      Crawler crawler = getContext().getCrawler();
      List<Target> targets = crawler.crawl(getTarget(), FalseFileFilter.INSTANCE);
      for (Target t : targets) {
        if (t.isDir()) {
          // Directories reference themselves.
          String location = t.getUrl().toString();
          getRegistrar().addTargetReference(location, location);
        }
      }
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, io.getMessage());
    }
  }

}
