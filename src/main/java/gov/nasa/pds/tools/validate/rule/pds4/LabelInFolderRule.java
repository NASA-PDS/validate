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

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.AdditionalTarget;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the rule that all files that look like labels in a folder
 * must be valid labels.
 */
public class LabelInFolderRule extends AbstractValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(LabelInFolderRule.class);
  private static final String XML_SUFFIX = ".xml";
  private static final long THREAD_TIMEOUT = 100; // HOURS
  private double totalTimeElapsed = 0.0;

  private ExecutorService validateThreadExecutor;
  List<Future<?>> futures = new ArrayList<Future<?>>();

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location);
  }

  private boolean verifyTargetValid(URL target) {
      // Verify that the target is valid by checking that it is a directory and does exist.
      boolean targetIsValidFlag = false;
      URI uri = null;
      try {
          uri = target.toURI();
          if (Utility.isDir(target) && (new File(uri)).exists()) {
              targetIsValidFlag = true;
          }
      } catch (URISyntaxException e) {
          LOG.error("URI Syntax Error: " + e.getMessage());
      }

      return(targetIsValidFlag);
  }

  private List<Target> recursiveCrawl(URL target, Crawler crawler) {
      // Perform recursive crawl to the leaf node for files only.
      List<Target> allTargets = new ArrayList<Target>();
      List<Target> targetList = new ArrayList<Target>();
      try {
          targetList = crawler.crawl(target, true, getContext().getFileFilters());
      } catch (IOException io) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
      }
      //LOG.debug("recursiveCrawl:target,targetList,targetList.size() {},{},{}",target,targetList,targetList.size());
      for (Target t : targetList) {
          URL url = null;
          url = t.getUrl();
          //LOG.debug("recursiveCrawl:target,url,Utility.isDir(url) {},{},{}",target,url,Utility.isDir(url));
          // If the target is a directory, call this function recursively until the leaf node.
          if (Utility.isDir(url.toString())) {
              allTargets.addAll(recursiveCrawl(url, crawler));
          } else {
              allTargets.add(t);
              //LOG.debug("recursiveCrawl:ADD_FILE:target,url,t {},{},{}",target,url,t);
          }
      }
      return(allTargets);
  }

  /**
   * Perform the validation of files in a directory (each with a file with a label suffix as a PDS4 label).
   * @param target
   *     The location of the directory of files (labels) to validate. 
   * @param  getDirectories
   *     Boolean flag to either crawler recursively or not.
   */
  private void doValidateLabelsInFolder(URL target, boolean getDirectories) {
      // issue_51: https://github.com/NASA-PDS/validate/issues/51: Provide the capability to specify multiple locations for pds4.bundle validation
      // This function is a re-fractor of validateLabelsInFolder() to receive an input and a flag to crawl recursively or not.
      validateThreadExecutor = Executors.newFixedThreadPool(1);

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
      // The target is already provided no need to call getTarget()
      long startTime = System.currentTimeMillis();
      LOG.info("doValidateLabelsInFolder:BEGIN_PROCESSING_FOLDER:target,labelRuleTmp {},{}",target,labelRuleTmp);
      //LOG.debug("doValidateLabelsInFolder:target,getDirectories {},{}",target,getDirectories);
      try {
        int targetCount = 0;
        // Crawl recursively or not depending on value of getDirectories.
        // Previously, it was always false.
        List<Target> targetList = new ArrayList<Target>();
        if (getDirectories) {
            targetList = this.recursiveCrawl(target,crawler);
        } else {
            targetList = crawler.crawl(target, false, getContext().getFileFilters());
        }
        //LOG.debug("doValidateLabelsInFolder:target,getDirectories,targetList {},{},{}",target,getDirectories,targetList);
        
        if (targetList.size()>0) {
           getListener().addProblem(
                new ValidationProblem(
                  new ProblemDefinition(ExceptionType.DEBUG,
                    ProblemType.GENERAL_INFO, "Targets need to be validated: " + targetList.size()), target));
        //System.out.println("LabelInFolderRule.....Targets need to be validated: " + targetList.size()); 
        }   
        for (Target t : targetList) {
        	Future<?> f = validateThreadExecutor.submit(new Runnable() {
            public void run() {
              try {
                LOG.debug("doValidateLabelsInFolder:t.getUrl() {}",t.getUrl());
                labelRule.execute(getChildContext(t.getUrl()));
              } catch (Exception e) {
                reportError(GenericProblems.UNCAUGHT_EXCEPTION, t.getUrl(), -1, -1, e.getMessage());
                e.printStackTrace();
              }
            }
          });
          futures.add(f);
          
          targetCount++;
        } // end for

        try {
          // Wait for threads to complete
          for(Future<?> future : futures)
              future.get();

          validateThreadExecutor.shutdown();
        } catch (Exception e) {
          e.printStackTrace();
        }
        getListener().addProblem(
                new ValidationProblem(
                  new ProblemDefinition(ExceptionType.DEBUG,
                    ProblemType.GENERAL_INFO, "Targets completed: " + targetCount), target));
      } catch (IOException io) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, io.getMessage());
      }
      long finishTime = System.currentTimeMillis();
      long timeElapsed = finishTime - startTime;
      this.totalTimeElapsed += timeElapsed;
      LOG.info("doValidateLabelsInFolder:END_PROCESSING_FOLDER:target,timeElapsed,this.totalTimeElapsed {},{} ms",target,timeElapsed,this.totalTimeElapsed);
  }


  /**
   * Validates each file with a label suffix as a PDS4 label.
   */
  @ValidationTest
  public void validateLabelsInFolder() {
      //LOG.info("validateLabelsInFolder:BEGIN_PROCESSING_FOLDER");

      boolean getDirectories = false;
      // Do the validation on default target.
      URL target = getTarget();
      this.doValidateLabelsInFolder(target,getDirectories);

      // Do the validation on any additional targets provided. 
      AdditionalTarget additionalTarget = getExtraTarget();
      if (additionalTarget == null) {
          LOG.debug("validateLabelsInFolder:additionalTarget is null.  Nothing to do.");
      } else {
          //LOG.debug("validateLabelsInFolder:additionalTarget.getExtraTargetList() {}",additionalTarget.getExtraTargetList());
          ArrayList<URL> additionalFolders = additionalTarget.getExtraTargetList();
          //LOG.debug("validateLabelsInFolder:additionalFolders.size() {}",additionalFolders.size());
          //LOG.debug("validateLabelsInFolder:additionalFolders {}",additionalFolders);
          getDirectories = true;
          for (URL additionalFolder : additionalFolders) {
              // The additionalFolder need to be verified that it is a directory and does exist.
              boolean targetIsValidFlag = this.verifyTargetValid(additionalFolder);
              if (targetIsValidFlag)  {
                  this.doValidateLabelsInFolder(additionalFolder,getDirectories);
              } else {
                  getListener().addProblem(
                       new ValidationProblem(
                         new ProblemDefinition(ExceptionType.ERROR,
                           ProblemType.GENERAL_INFO, "Additional path is either not a directory or does not exist"), additionalFolder));
              }
          }
      }
  }

}
