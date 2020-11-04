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
package gov.nasa.pds.tools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.Target;

/**
 * Utility class to handle file(s) pruning of a given list related to logical identifier plus version (lidvid).
 *
 */
public class LidVid {
    private static final Logger LOG = LoggerFactory.getLogger(LidVid.class);
  
    private static final String PRODUCT_CLASS =
        "//*[starts-with(name(),'Identification_Area')]/product_class";
  
    private static final String LOGICAL_IDENTIFIER =
        "//*[starts-with(name(),'Identification_Area')]/logical_identifier";

    private static final String VERSION_ID =
        "//*[starts-with(name(),'Identification_Area')]/version_id";
  
    /**
     * Find the latest version of lidvid(s).
     * @param vidsList a list of string of lidvid(s).
     * @return a string containing the latest version.
     */
    public static String getLatestVersion(List<String> vidsList) {
      // Function returns the element in vidsList with the largest version.

      String largestVersion = null;
      if (vidsList.size() == 0) {
          return(largestVersion);
      }

      Collections.sort(vidsList);  // Sort by natural character order: ["1.2","1.3","2.03", "2.1","2.5"]

      String lastElement = vidsList.get(vidsList.size() - 1);   // The last element is "2.5"
      //ArrayList<String> lastVidsList = new ArrayList<String>();
      String[] lastVersionTokens = lastElement.split("\\.");

      LOG.debug("getLatestVersion:lastElement {}",String.join(", ", lastElement));
      LOG.debug("getLatestVersion:lastVersionTokens {}",String.join(", ",lastVersionTokens));

      if (lastVersionTokens.length < 2) {
          LOG.error("Cannot split lastElement on dot . {}",lastElement);
          return(largestVersion);
      } else {
          String lastMajorVersion = lastVersionTokens[0];
          ArrayList<Integer> minorVersionList = new ArrayList<Integer>();    // Will store a list of integers of the minor versions [3,1,5] of ["2.03", "2.1", "2.5]"

          for (String vid :  vidsList) {
              if (vid.contains(lastVersionTokens[0] + ".")) {  // Add any thing that has "2." since they have the latest major version.
                  //lastVidsList.add(vid);
                  String[] versionTokens = vid.split("\\.");
                  minorVersionList.add(Integer.parseInt(versionTokens[1]));  // Save the minor version as integer: 5 for 2.5, 3 for 2.03
                  LOG.debug("getLatestVersion:ADDING_MINOR_VERSION {}",Integer.parseInt(versionTokens[1]));
              }
          }
          //LOG.debug("getLatestVersion:lastVidsList {}",lastVidsList);

          // Sort minorVersionList by ascending to get to the largest integer number in this order [1,5].
          Collections.sort(minorVersionList);
          Integer largestMinorVersion = minorVersionList.get(minorVersionList.size() - 1);

          LOG.debug("getLatestVersion:lastMajorVersion {}",lastMajorVersion);
          LOG.debug("getLatestVersion:minorVersionList {}",minorVersionList);
          LOG.debug("getLatestVersion:minorVersionList(last) {}",minorVersionList.get(minorVersionList.size() - 1));

          for (String vid :  vidsList) {
              // The largest version is the one with "2.5"
              LOG.debug("getLatestVersion:inspecting vid for largestMinorVersion {},{}",vid,largestMinorVersion);
              // It is important to look for lastMajorVersion AND largestMinorVersion in vid, e.g look for "2" and "5" in vid.  Both versions must be checked.
              if (vid.contains(lastMajorVersion) &&
                  vid.contains(Integer.toString(largestMinorVersion))) {
                  largestVersion = vid;
              }
          }
          LOG.debug("getLatestVersion:largestVersion {}",largestVersion);
      } 

      return(largestVersion);
  }

    /**
     * Reduce a list of target to only the one with the latest version.
     * @param children a list of Target(s).  * @return a list of target containing the latest version.
     */
    public static List<Target> reduceToLatestTargetOnly(List<Target> children) {
      // Given a list of Target, return the target with the largest version.
      // This function only work on bundle and collection targets.

      List<Target> reducedKids = new ArrayList<>();  // Start out with zero elements.

      List<String> lidsList = new ArrayList<>();
      List<String> vidsList = new ArrayList<>();
      HashMap<String,Target> vidsTable = new HashMap<String,Target>();  // A table of target using the string version of vid as key, e.g. "03", "4"

      for (int i = 0; i < children.size(); i++) {
        Target child = children.get(i);
          try {
            XMLExtractor extractor = new XMLExtractor(child.getUrl());
            if (("Product_Collection".equals(extractor.getValueFromDoc(PRODUCT_CLASS))) ||
                ("Product_Bundle".    equals(extractor.getValueFromDoc(PRODUCT_CLASS))))  {
              String lid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
              String vid = extractor.getValueFromDoc(VERSION_ID);
              lidsList.add(lid);
              vidsList.add(vid);
              vidsTable.put(vid,child);
            }
          } catch (Exception e) {
            //Ignore. This isn't a valid Bundle/Collection label so skip it.
            LOG.debug("Target not a Bundle/Collection, skipping {}",child.getUrl());
          }
      }

      for (String lid : lidsList) { 
          LOG.debug("reduceToLatestTargetOnly:lid {}",lid);
      }

      // From vidsList, sort by ascending order so the ordering will be, e.g. [ "1.5", "1.6", "1.7", "1.10"] and retrieve the largest version "1.10"
      String largestVersion = LidVid.getLatestVersion(vidsList);

      LOG.debug("reduceToLatestTargetOnly:largestVersion {}",largestVersion);

      for (String vid : vidsList) { 
          LOG.debug("reduceToLatestTargetOnly:vid {}",vid);
      }

      // The target with the largest version is the desired one to return.
      // Because largestVersion can be null if the list of targets is empty, it must be checked.
      if (largestVersion != null) {
          reducedKids.add(vidsTable.get(largestVersion));
          LOG.debug("reduceToLatestTargetOnly:vidsTable.get(largestVersion) {}",vidsTable.get(largestVersion));
      }

      LOG.debug("reduceToLatestTargetOnly:reducedKids {}",reducedKids);

      return(reducedKids);
    }
}
