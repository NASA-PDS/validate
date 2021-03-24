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

import java.io.File;

import java.net.URI;
import java.net.URL;

import java.util.HashSet;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.tools.validate.ValidationProblem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that keep track of different Information Model (IM) versions found when parsing labels
 * and report a WARNING if multiple versions of the Information Model (IM) are found.
 * 
 */

public class LabelUtil {
  // This class is not meant to be instantiated since its goal is to keep track of different versions of the IM used
  // for the entire run and to report a WARNING if the user desire it.  Since many processes may be accessing these
  // functions and variables, some will be synchronized to protect the data from concurrent access.

  private static final Logger LOG = LoggerFactory.getLogger(LabelUtil.class);
  private static URL contextValue = null;
  private static String location = null;
  private static boolean produceWarningIfMultipleIMVersionProcessedFlag = true;  // By default set to true.  Set to false by developer to test this function.
  private static XPathFactory xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();

  private static HashSet<String> informationModelVersions = new HashSet<>();
  private static Report report              = null;
  private static boolean bundleLabelSetFlag = false;
  private static String bundleLocation      = null;
  private static String launcherURIName     = null;

  /**
   * Reset all list(s) and variables back to their default states.
   * @param  None
   * @return None
   */
  public static synchronized void reset() {
      LabelUtil.informationModelVersions.clear();
      LabelUtil.report             = null;
      LabelUtil.bundleLabelSetFlag = false;
      LabelUtil.bundleLocation     = null;
      LabelUtil.launcherURIName    = null;
  }

  /**
   * Set the name of the executable of validate.
   * @param launcherURIName The name of the executable of validate.
   * @return None
   */
  public static void setLauncherURIName(String launcherURIName) {
      LabelUtil.launcherURIName = launcherURIName;
  }

  /**
   * Get the name of the executable of validate.
   * @param  None
   * @return The name of the executable of validate. 
   */
  public static String getLauncherURIName() {
      return(LabelUtil.launcherURIName);
  }

  /**
   * Set the report to log status of labels or WARNING messages.
   * @param report The Report object.
   * @return None
   */
  public static void setReport(Report report) {
      // Set a report so any mesages can be recorded.
      LabelUtil.report = report; 
  }


  /**
   * Register the Information Model (IM) version.
   * @param informationModelVersion The version of the IM to register, e.g. 1.12.0.0, 1.10.0.0
   * @return None
   */
  public static synchronized void registerIMVersion(String informationModelVersion) {
      LabelUtil.informationModelVersions.add(informationModelVersion);
      LOG.debug("registerIMVersion:informationModelVersion {}",informationModelVersions);
  }

  /**
   * Returns the list of IMs registered so far.
   * @return informationModelVersions the list of IMs registered: {1.12.0.0, 1.10.0.0}
   */
  public static synchronized HashSet<String> getInformationModelVersions() {
     return(LabelUtil.informationModelVersions);
  }

  /**
   * Set the location of the label currently processing.
   * @param location informationModelVersion The version of the IM to register, e.g. 1.12.0.0, 1.10.0.0
   * @return informationModelVersions the list of IMs registerd.
   */
  public static synchronized void setLocation(String location) {
    // Set the location of the label being processed.  If the location is bundle, save it in bundleLocation as well. 
    try { 
        // Create a file object 
        File f = new File(location);
  
        // Get the name only of the given file f.
        String nameOnly = f.getName(); 

        // If the label is a bundle, save its name to bundleLocation so it can be used to report the multiple versions of IM.
        if (nameOnly.startsWith("bundle")) {
            LabelUtil.bundleLabelSetFlag = true;
            LabelUtil.bundleLocation     = location;
        }
        LabelUtil.location = location;
        LOG.debug("location,bundleLocation,bundleLabelSetFlag {},{},{}",location,LabelUtil.bundleLocation,LabelUtil.bundleLabelSetFlag);
    } catch (Exception e) { 
        System.err.println("LabelUtil:setLocation " + e.getMessage()); 
    } 
  }

  /**
   * Get the location of the label currently processing.
   * @return location of the label currently processing.
   */
  public static synchronized String getLocation() {
    return(LabelUtil.location);
  }

  /**
   * Get the Information Model (IM) version of the label (as a DOMSource)
   * @param source The content of context as a DOMSource.
   # @param context The location of the label being parsed from 
   * @return informationModelVersion the version of the IM
   */
  public static String getIMVersion(DOMSource source, URL context) {
      String informationModelVersion = null;
      String INFORMATION_MODEL_VERSION = "information_model_version";
      String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";
      String IDENTIFICATION_AREA = "//*:Identification_Area[namespace-uri()='" + PDS4_NS + "']";
      LOG.debug("getIMVersion:MY_SOURCE[{}]",source);
      try {
          NodeList nodeList = (NodeList) xPathFactory.newXPath().evaluate(IDENTIFICATION_AREA,source,XPathConstants.NODESET);
          for (int i = 0; i < nodeList.getLength(); ++i) {
              NodeList childList = ((Element) nodeList.item(i)).getChildNodes();
              for (int j = 0; j < childList.getLength(); ++j) {
                  Node node = childList.item(j);
                  //LOG.debug("node.getTextContent().trim() {}",node.getTextContent().trim());
                  if (node.getNodeName().equals(INFORMATION_MODEL_VERSION)) {
                      informationModelVersion = node.getTextContent().trim();
                  }
              }
         }
     } catch (XPathExpressionException ex) {
         LOG.error("Cannot extract field " + INFORMATION_MODEL_VERSION + " from context " + context.toString());
     }
     LOG.debug("getIMVersion:context,informationModelVersion {},{}",context,informationModelVersion);
     return(informationModelVersion);
  }

  /**
   * Report a WARNING if the number of unique IM versions are more than one.
   * @param validationRule The rule of the validation, e.g. pds4.label, pds4.bundle.  This value can be null since a rule is is not required within validate module.
   * @return None 
   */
  public static synchronized void reportIfMoreThanOneVersion(String validationRule) {
      // After all the labels have been validated, fetch all the information_model_version encountered.
      HashSet<String> informationModelVersions = LabelUtil.getInformationModelVersions();

      String versions = "";
      int numDifferentVersions = 0;
      for(String str : informationModelVersions) {
          versions += " " + str;
          numDifferentVersions += 1;
      }
      LOG.debug("reportIfMoreThanOneVersion:target,informationModelVersions.size(),MY_VERSIONS {},{},{}",location,informationModelVersions.size(),versions);
      LOG.debug("reportIfMoreThanOneVersion:informationModelVersions {}",informationModelVersions);
      LOG.debug("reportIfMoreThanOneVersion:LabelUtil.produceWarningIfMultipleIMVersionProcessedFlag,numDifferentVersions {},{}",LabelUtil.produceWarningIfMultipleIMVersionProcessedFlag,numDifferentVersions);
      if (versions.equals("")) {
          // This means that the function reportIfMoreThanOneVersion() is called too early and no labels has been parsed yet.
          LOG.warn("reportIfMoreThanOneVersion:There has been no versions added to informationModelVersions set yet.");
          // Clear out all list(s) and reset variables back to their default states.
          LabelUtil.reset();
          return;
      }

      try {
          if (LabelUtil.produceWarningIfMultipleIMVersionProcessedFlag && numDifferentVersions > 1) {
              String message = "";
              URL url = null; // The warning message will be for the entire run so no need to set specific url.
              String forRuleStr = "";
              String forBundleStr = "";
              if (validationRule != null) { 
                  // Some run has no rule so cannot print it.
                  forRuleStr = " for rule " + validationRule;
              }
              if (LabelUtil.bundleLabelSetFlag) {
                  // Print specific bundle name to identify a location name with an actual bundle.
                  forBundleStr = " for bundle " + LabelUtil.bundleLocation;
              }
              message = "Multiple versions (" + Integer.toString(numDifferentVersions) + ") of Information Model (IM) found in this run: " + versions + forRuleStr + forBundleStr;
              LOG.debug(message);

              // Build the ValidationProblem and add it to the report.
              ValidationProblem p1 = new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
                                                                                 ProblemType.GENERAL_INFO, message),url);
              LOG.debug("reportIfMoreThanOneVersion:bundleLabelSetFlag,url {},{}",LabelUtil.bundleLabelSetFlag,url);

              // Append the WARNING message to the report.
              // Note: The value of the url is important as it is a key to the map of the report.  Each url (or label) has status and messages attached to it.
              //LabelUtil.report.record(url.toURI(), p1);
              LabelUtil.report.record(new URI(LabelUtil.launcherURIName), p1);
              //LabelUtil.report.record(url.toURI(), p1);

              // Clear out all list(s) and reset variables back to their default states.
              LabelUtil.reset();
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
