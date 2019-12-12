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
import gov.nasa.pds.tools.validate.rule.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Implements the rule that all files that look like labels in a folder
 * must be valid labels.
 */
public class LabelInFolderRule extends AbstractValidationRule {

  private static final String XML_SUFFIX = ".xml";

  private ExecutorService validateThreadExecutor = Executors.newFixedThreadPool(1);

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location);
  }

  /**
   * Validates each file with a label suffix as a PDS4 label.
   */
  @ValidationTest
  public void validateLabelsInFolder() {
      ValidationRule labelRuleTmp = null;

      // issue_124:
      if (!getContext().getCheckData()) {
        labelRuleTmp = getContext().getRuleManager().findRuleByName("pds4.label.skip.content");
      }
      else {
        labelRuleTmp = getContext().getRuleManager().findRuleByName("pds4.label");
      }
      final ValidationRule labelRule = labelRuleTmp;

      Crawler crawler = getContext().getCrawler();
      try {
        int targetCount = 0;
        for (Target t : crawler.crawl(getTarget(), false, getContext().getFileFilters())) {

          validateThreadExecutor.execute(new Runnable() {
            public void run() {
              //System.out.println("\nVALIDATING : " + t.getUrl());

              try {
                labelRule.execute(getChildContext(t.getUrl()));
              } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
                reportError(GenericProblems.UNCAUGHT_EXCEPTION, t.getUrl(), -1, -1, e.getMessage());
              }
            }
          });

          targetCount++;

        } // end for

        try {
          validateThreadExecutor.shutdown();
          validateThreadExecutor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

//        System.out.println("COMPLETED " + targetCount + " targets.");

      } catch (IOException io) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
      }
  }

}
