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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.validate.Target;

/**
 * Utility class to handle file(s) pruning of a given list related to logical identifier plus
 * version (lidvid).
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
   *
   * @param lidvidsMap a HashMap of logical identifier and a list of all versions.
   * @return a HashMap containing a map of each logical identifier and its latest version.
   */
  public static HashMap<String, String> getLatestVersion(
      HashMap<String, ArrayList<String>> lidvidsMap) {
    // Function returns the elements in lidvidsMap with the largest version.
    // Each key in lidvidsMap is a logical identifier and must be processed in its
    // own loop.

    HashMap<String, String> largestVersion = new HashMap<>(); // A HashMap array containing a map of
                                                              // logical identifier and its latest
                                                              // version.

    LOG.debug("getLatestVersion:lidvidsMap.size {}", lidvidsMap.size());

    // Loop through each logical_identifier in lidvidsMap and find the largest
    // version for that logical_identifier.
    for (String lid : lidvidsMap.keySet()) {
      ArrayList<String> vidsList = lidvidsMap.get(lid); // The value in HashMap is an array of
                                                        // version_id list.
      LOG.debug("getLatestVersion:vidsList,vidsList.size {},{}", vidsList, vidsList.size());
      // From the way the HashMap is build for lidvidsMap, there is a guaranteed that
      // at least one element will be in vidsList

      // Due to the fact that the natural character order will see "9.0" is larger
      // than "10.0", we have to convert the character strings to floats.
      ArrayList<Float> floatVidsList = new ArrayList<>();
      for (String vid : vidsList) {
        floatVidsList.add(new Float(Float.parseFloat(vid)));
      }

      Collections.sort(floatVidsList); // Sort by float number order:
                                       // [1.2,1.3,2.03,2.1,2.5,9.0,10.0]

      String lastElement = Float.toString(floatVidsList.get(floatVidsList.size() - 1)); // The last
                                                                                        // element
                                                                                        // is
                                                                                        // "10.0"
                                                                                        // convert
                                                                                        // it from
                                                                                        // float to
                                                                                        // string
                                                                                        // for
                                                                                        // parsing.
      String[] lastVersionTokens = lastElement.split("\\.");

      LOG.debug("getLatestVersion:lastElement {}", String.join(", ", lastElement));
      LOG.debug("getLatestVersion:lastVersionTokens {}", String.join(", ", lastVersionTokens));

      if (lastVersionTokens.length < 2) {
        LOG.error("Cannot split lastElement on dot . {}", lastElement);
        return (largestVersion);
      }
      String lastMajorVersion = lastVersionTokens[0];
      ArrayList<Integer> minorVersionList = new ArrayList<>(); // Will store a list of integers of
                                                               // the
                                                               // minor versions [3,1,5] of ["2.03",
                                                               // "2.1",
                                                               // "2.5]"

      for (String vid : vidsList) {
        if (vid.contains(lastVersionTokens[0] + ".")) { // Add any thing that has "2." since they
                                                        // have the
                                                        // latest major version.
          // lastVidsList.add(vid);
          String[] versionTokens = vid.split("\\.");
          minorVersionList.add(Integer.parseInt(versionTokens[1])); // Save the minor version as
                                                                    // integer: 5
                                                                    // for 2.5, 3 for 2.03
          LOG.debug("getLatestVersion:ADDING_MINOR_VERSION {}", Integer.parseInt(versionTokens[1]));
        }
      }
      // LOG.debug("getLatestVersion:lastVidsList {}",lastVidsList);

      // Sort minorVersionList by ascending to get to the largest integer number in
      // this order [1,5].
      Collections.sort(minorVersionList);
      Integer largestMinorVersion = minorVersionList.get(minorVersionList.size() - 1);

      LOG.debug("getLatestVersion:lastMajorVersion {}", lastMajorVersion);
      LOG.debug("getLatestVersion:minorVersionList {}", minorVersionList);
      LOG.debug("getLatestVersion:minorVersionList(last) {}",
          minorVersionList.get(minorVersionList.size() - 1));

      for (String vid : vidsList) {
        // The largest version is the one with "2.5"
        LOG.debug("getLatestVersion:inspecting vid for largestMinorVersion {},{}", vid,
            largestMinorVersion);
        // It is important to look for lastMajorVersion AND largestMinorVersion in vid,
        // e.g look for "2" and "5" in vid. Both versions must be checked.
        if (vid.contains(lastMajorVersion) && vid.contains(Integer.toString(largestMinorVersion))) {
          largestVersion.put(lid, vid); // The type of largestVersion is now a HashMap, we put lid
                                        // key with
                                        // value vid.
        }
      }
      LOG.debug("getLatestVersion:largestVersion {}", largestVersion);
    } // end for (String lid : lidvidsMap.keySet()) {

    LOG.debug("largestVersion.size {}", largestVersion.size());
    for (String lid : largestVersion.keySet()) {
      LOG.debug("getLatestVersion:lid,largestValue {},{}", lid, largestVersion.get(lid));
    }
    LOG.debug("getLatestVersion:lidvidsMap.size {}", lidvidsMap.size());

    // The return type of largestVersion is HashMap with the key being the
    // logical_identifier and the value the largest version.
    //
    // urn:nasa:pds:insight_seis:data_seed,10.0
    // urn:nasa:pds:insight_seis:data_laf,4.0
    // urn:nasa:pds:insight_seis:data_table,10.0
    //
    return (largestVersion);
  }

  /**
   * Reduce a list of target to only the one with the latest version.
   *
   * @param children a list of Target(s). * @return a list of target containing the latest version.
   */
  public static List<Target> reduceToLatestTargetOnly(List<Target> children) {
    // Given a list of Target, return the target with the largest version.
    // This function only work on bundle and collection targets.

    List<Target> reducedKids = new ArrayList<>(); // Start out with zero elements.

    List<String> lidsList = new ArrayList<>();
    List<String> vidsList = new ArrayList<>();
    HashMap<String, ArrayList<String>> vidsMap = new HashMap<>(); // Hold a HashMap of a
                                                                  // logical_identifier to
                                                                  // a list of versions
    HashMap<String, Target> vidsTable = new HashMap<>(); // A table of target using the string
                                                         // version
                                                         // of vid as key, e.g. "03", "4"

    for (int i = 0; i < children.size(); i++) {
      Target child = children.get(i);
      try {
        XMLExtractor extractor = new XMLExtractor(child.getUrl());
        if (("Product_Collection".equals(extractor.getValueFromDoc(PRODUCT_CLASS)))
            || ("Product_Bundle".equals(extractor.getValueFromDoc(PRODUCT_CLASS)))) {
          String lid = extractor.getValueFromDoc(LOGICAL_IDENTIFIER);
          String vid = extractor.getValueFromDoc(VERSION_ID);
          lidsList.add(lid);
          vidsList.add(vid);
          // Only add the child to vidsTable if the value of (logical_identifier + "::" +
          // version_id) has not been added already
          // since we only want to have unique keys.
          if (!vidsTable.containsKey(lid + "::" + vid)) {
            vidsTable.put(lid + "::" + vid, child); // Use the logical_identifier + version as the
                                                    // key, e.g.
                                                    // urn:nasa:pds:insight_seis:data_laf::4.0
          }

          // Check to see if the logical_identifier (lid) has already been added to
          // vidsMap.
          if (!vidsMap.containsKey(lid)) {
            // Create a new ArrayList<String> and add the vid to it.
            ArrayList<String> versionsArray = new ArrayList<>();
            versionsArray.add(vid);
            vidsMap.put(lid, versionsArray);
          } else {
            // The key logical_identifier (lid) is in vidsMap, get the versionsArray and add
            // the new vid to it.
            vidsMap.get(lid).add(vid); // Get the versionsArray and add the new version_id (vid) to
                                       // it in
                                       // one step.
          }

        }
      } catch (Exception e) {
        // Ignore. This isn't a valid Bundle/Collection label so skip it.
        LOG.debug("Target not a Bundle/Collection, skipping {}", child.getUrl());
      }
    }

    int ii = 0;
    for (String lid : lidsList) {
      LOG.debug("reduceToLatestTargetOnly:lid {}", lid);
      LOG.debug("reduceToLatestTargetOnly:lid,vid {},{}", lid, vidsList.get(ii));
      ii += 1;
    }

    // From vidsList, sort by ascending order so the ordering will be, e.g. [ "1.5",
    // "1.6", "1.7", "1.10"] and retrieve the largest version "1.10"
    // Change the parameter to be a HashMap instead of a list since multiple
    // collections are possible.
    HashMap<String, String> largestVersion = LidVid.getLatestVersion(vidsMap);

    LOG.debug("reduceToLatestTargetOnly:largestVersion.size() {}", largestVersion.size());

    for (String vid : vidsList) {
      LOG.debug("reduceToLatestTargetOnly:vid {}", vid);
    }

    // The target with the largest version is the desired one to return.
    // Because largestVersion can be null if the list of targets is empty, it must
    // be checked.
    if (largestVersion != null && largestVersion.size() > 0) {
      String fullyQualifiedLogicalIdentifierVersion = null; // Also known as lidvid.
      for (String singleLogicalIdentifier : largestVersion.keySet()) {
        // To build the key to vidsTable, it is necessary to add the logical identifier
        // to "::" and then the version_id
        fullyQualifiedLogicalIdentifierVersion =
            singleLogicalIdentifier + "::" + largestVersion.get(singleLogicalIdentifier);
        if (vidsTable.containsKey(fullyQualifiedLogicalIdentifierVersion)) {
          // Get the actual file name from vidsTable using the key
          // fullyQualifiedLogicalIdentifierVersion.
          reducedKids.add(vidsTable.get(fullyQualifiedLogicalIdentifierVersion));
          LOG.debug(
              "reduceToLatestTargetOnly:fullyQualifiedLogicalIdentifierVersion,vidsTable.get() {},{}",
              fullyQualifiedLogicalIdentifierVersion,
              vidsTable.get(fullyQualifiedLogicalIdentifierVersion));
        }
      }
    }

    LOG.debug("reduceToLatestTargetOnly:reducedKids {}", reducedKids);

    return (reducedKids);
  }
}
