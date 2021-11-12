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

import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that sanitizes the directory and file name by using the decode function to replacing '%20' with ' '.
 * 
 */

public class FilenameUtility {
  // This class is not meant to be instantiated.  The public functions can be called directly.
  private static final Logger LOG = LoggerFactory.getLogger(FilenameUtility.class);

  /**
   * Returns the file name replacing the '%20' with ' ' character.
   * @param  filename The filename of the file
   * @return decodedName The decoded filename of the file
   */
  public static String decodeSpace(String filename) {
    // For some strange reason, the File class does not handle well with the "%20" in the name.  It needs to be an actual space.
    String decodedName = null;
    try {
        decodedName = URLDecoder.decode(filename, "UTF-8");  // Use the URLDecoder.decode() to convert from '%20' to ' '.
        LOG.debug("decodeSpace:filename,decodedName [{}],[{}]",filename,decodedName);
    } catch (Exception ex) {
        LOG.error("Cannot convert '%20' to ' ' for filename {}",filename);
        ex.printStackTrace();
    }
    return(decodedName);
  }

  /** 
   * Returns the parent directory of the given filename
   * @param  fileUrl The URL of the file name
   * @return parent The parent directory of the given filename
   */
  public static String getParent(URL fileUrl) {
    // Using alternative method to get the parent since the other utility does not work correctly:  The leading character after the 'file:' is inadvertently removed. 
    String parent = null;
    if (fileUrl != null && fileUrl.getPath().lastIndexOf(File.separator) >= 0) {
        parent = fileUrl.getPath().substring(0,fileUrl.getPath().lastIndexOf(File.separator));
        LOG.debug("getParent:fileUrl,parent,FilenameUtils.getName(fileUrl) {},{},{}",fileUrl,parent,FilenameUtils.getName(fileUrl.toString()));
    } else {
        LOG.error("The path is null or does not contain a file separator {}",fileUrl);
        return(null);
    }
    LOG.debug("getParent:fileUrl,parent {},{}",fileUrl,parent);
    return(parent);
  }
}
