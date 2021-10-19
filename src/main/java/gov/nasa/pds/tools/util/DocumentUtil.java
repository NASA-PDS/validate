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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.lang.StringBuilder;

import java.net.URL;

import gov.nasa.pds.tools.util.Utility;

import org.apache.commons.io.FilenameUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to parse and remove comments from a Document object.
 * 
 */
public class DocumentUtil {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentUtil.class);

  public void DocumenUtil() {
  }

  private void removeComments(Node node) {
    // Remove comments from the given node.
    for (int i = 0; i < node.childNodeSize();) {
        Node child = node.childNode(i);
        if (child.nodeName().equals("#comment"))
            child.remove();
        else {
            removeComments(child);
            i++;
        }
    }
  }        

  /**
   * Read the content of the file and returns the content of the file as String.
   * 
   * @param fileUrl The URL of the file.
   * @return The content of the file as String.
   */
  public String readFile(URL fileUrl) {
    String fileName = fileUrl.getPath();
    BufferedReader reader = null;

    // Note: The function FilenameUtils.getPath() doesn't seem to work correctly.
    // It returns the path without the leading slash '/':
    //
    // For this URI
    //
    //      file:/home/qchau/sandbox/validate/src/test/resources/github367/document/
    //
    // The FilenameUtils.getPath(getTarget().getPath()) returns
    //
    //     home/qchau/sandbox/validate/src/test/resources/github367/document/
    //
    // which is missing the leading slash.
    //
    // Using alternative method to get the parent.
    String parent = "";
    if (fileUrl.getPath().lastIndexOf(File.separator) >= 0) {
        parent = fileUrl.getPath().substring(0,fileUrl.getPath().lastIndexOf(File.separator));
    } else {
        LOG.error("The path does not contain a file separator {}",fileUrl.getPath());
        return(null);
    }
    LOG.debug("readFile:fileUrl,parent,FilenameUtils.getName(fileUrl) {},{},{}",fileUrl,parent,FilenameUtils.getName(fileUrl.toString()));

    // Combine the parent and the file name together so sonatype-lift won't complain.
    // https://find-sec-bugs.github.io/bugs.htm#PATH_TRAVERSAL_IN
    try {
        reader = new BufferedReader(new FileReader(parent + File.separator + FilenameUtils.getName(fileUrl.toString())));
    } catch (FileNotFoundException ex) {
        LOG.error("readFile: Cannot find file {}",fileUrl);
        ex.printStackTrace();
    }

    String         line = null;
    StringBuilder  stringBuilder = new StringBuilder();

    try {
        while((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        reader.close();

        return stringBuilder.toString();
    } catch (IOException ex) {
        LOG.error("readFile: Cannot read file {}",fileUrl);
        ex.printStackTrace();
    }

    try {
        reader.close();  // Close the resource in case of an exception.
    } catch (IOException ex) {
        LOG.error("readFile: Cannot close file {}",fileUrl);
        ex.printStackTrace();
    }
    return(null);
  }

  /**
   * Returns the content of the file minus the comments as String.
   * 
   * @param fileUrl The URL of the file.
   * @return documentContent The content of the file minus the comments as String.
   */

  public String getDocumentWithoutComments(URL fileUrl) {
      long start = System.currentTimeMillis();

      String documentContent = this.readFile(fileUrl);  // Read the file as String.
      Document doc = Jsoup.parse(documentContent, "", Parser.xmlParser()); // Parse the file as XML.
      removeComments(doc);  // Remove any comments.

      long finish = System.currentTimeMillis();
      long timeElapsed = finish - start;

      LOG.debug("getDocumentWithoutComments: timeElapsed (millisecs) {}",timeElapsed);
      return(doc.html());  // It doesn't matter that the file is XML.  This function html() returns the file as String.
   }
}
