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
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.validate.ProblemType;

/**
 * Util class to parse and remove comments from a Document object. It will also keep a mapping of
 * document type to ProblemType to allow the retrieval of ProblemType based on document type.
 *
 */
public class DocumentUtil {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentUtil.class);

  // The following two lists are to keep track of a map from document type to
  // ProblemType enum.
  // They are assigned to an empty list to avoid null pointer exception and will
  // be initialized in the initialize() function.

  private ArrayList<String> docTypeList = new ArrayList<>();
  private ArrayList<ProblemType> problemTypeList = new ArrayList<>();
  private boolean classInitialized = false; // Indicate whether if the mapping has been initialized
                                            // or not.

  public DocumentUtil() {
    // Because this class can be instantiated by other classes many times, we only
    // want to call the initialize() function
    // if we will be using the getProblemType() function below.
  }

  private void initialize() {
    // Create a map to go from a document type to a ProblemType.
    // The map consists of two arrays, one to hold the docType and one to hold the
    // enumerated ProblemType.
    // These keys below (of type String) do not have to be exact. When the key is
    // searched for, we will use contains() and ignore case to find the ProblemType
    // Example:
    // The value "postscript" can be found in "encapsulated postscript" and
    // "postscript"

    // Empty the lists in case they have content. This is important if somehow the
    // function initialize() gets call multiple many times.
    this.docTypeList = new ArrayList<>();
    this.problemTypeList = new ArrayList<>();

    // The add() function should be called in the order specified since together
    // they form a mapping mechanism,
    // for example 'ENCAPSULATED' before 'POSTSCRIPT' and 'RICH' before 'TEXT'.

    this.docTypeList.add("ENCAPSULATED");
    this.problemTypeList.add(ProblemType.NON_ENCAPSULATED_POSTSCRIPT_FILE);

    this.docTypeList.add("EXCEL");
    this.problemTypeList.add(ProblemType.NON_MSEXCEL_FILE);

    this.docTypeList.add("GIF");
    this.problemTypeList.add(ProblemType.NON_GIF_FILE);

    this.docTypeList.add("HTML");
    this.problemTypeList.add(ProblemType.NON_HTML_FILE);

    this.docTypeList.add("LATEX");
    this.problemTypeList.add(ProblemType.NON_LATEX_FILE);

    this.docTypeList.add("MPEG");
    this.problemTypeList.add(ProblemType.NON_MP4_FILE);

    this.docTypeList.add("POSTSCRIPT");
    this.problemTypeList.add(ProblemType.NON_POSTSCRIPT_FILE);

    this.docTypeList.add("TIFF");
    this.problemTypeList.add(ProblemType.NON_TIFF_FILE);

    this.docTypeList.add("WORD");
    this.problemTypeList.add(ProblemType.NON_MSWORD_FILE);

    this.classInitialized = true;

    LOG.debug("initialize:this.docTypeList.size {}", this.docTypeList.size());
    LOG.debug("initialize:this.problemTypeList.size {}", this.problemTypeList.size());
  }

  /**
   * Returns the enum ProblemType based on the docType.
   *
   * @param docType The string represent the document type.
   *
   * @return problemType The matching ProblemType based on the document type. Can be null if not
   *         matching ProblemType can be found.
   */

  public ProblemType getProblemType(String docType) {
    ProblemType problemType = null;
    if (!this.classInitialized) {
      // Only initialize this class once of the two lists' content.
      this.initialize();
    }

    // Iterating through docTypeList and check if docType contains singleDocType.
    // Note that everything is changed to lower cases for comparison.
    int ii = 0;
    for (String singleDocType : this.docTypeList) {
      if (docType.toLowerCase().contains(singleDocType.toLowerCase())) {
        problemType = this.problemTypeList.get(ii);
        // Once we have found a matching value, there's no need to continue looping as
        // it will be fetching the wrong ProblemType if we continue.
        break;
      }
      ii++;
    }
    LOG.debug("getProblemType:docType,problemType {},{}", docType, problemType);
    return (problemType);
  }

  private void removeComments(Node node) {
    // Remove comments from the given node.
    for (int i = 0; i < node.childNodeSize();) {
      Node child = node.childNode(i);
      if (child.nodeName().equals("#comment")) {
        child.remove();
      } else {
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
    BufferedReader reader = null;

    // Note: The function FilenameUtils.getPath() doesn't seem to work correctly.
    // It returns the path without the leading slash '/':
    //
    // For this URI
    //
    // file:/home/qchau/sandbox/validate/src/test/resources/github367/document/
    //
    // The FilenameUtils.getPath(getTarget().getPath()) returns
    //
    // home/qchau/sandbox/validate/src/test/resources/github367/document/
    //
    // which is missing the leading slash.
    //
    // Using alternative method to get the parent.
    String parent = "";
    if (fileUrl.getPath().lastIndexOf("/") < 0) {
      LOG.error("The path does not contain a file separator {}", fileUrl.getPath());
      return (null);
    }
    parent = fileUrl.getPath().substring(0, fileUrl.getPath().lastIndexOf("/"));
    LOG.debug("readFile:fileUrl,parent,FilenameUtils.getName(fileUrl) {},{},{}", fileUrl, parent,
        FilenameUtils.getName(fileUrl.toString()));

    // Combine the parent and the file name together so sonatype-lift won't
    // complain.
    // https://find-sec-bugs.github.io/bugs.htm#PATH_TRAVERSAL_IN
    try {
      reader = new BufferedReader(
          new FileReader(parent + File.separator + FilenameUtils.getName(fileUrl.toString())));
    } catch (FileNotFoundException ex) {
      LOG.error("readFile: Cannot find file {}", fileUrl);
      ex.printStackTrace();
    }

    String line = null;
    StringBuilder stringBuilder = new StringBuilder();

    try {
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line + "\n");
      }
      reader.close();

      return stringBuilder.toString();
    } catch (IOException ex) {
      LOG.error("readFile: Cannot read file {}", fileUrl);
      ex.printStackTrace();
    }

    try {
      reader.close(); // Close the resource in case of an exception.
    } catch (IOException ex) {
      LOG.error("readFile: Cannot close file {}", fileUrl);
      ex.printStackTrace();
    }
    return (null);
  }

  /**
   * Returns the content of the file minus the comments as String.
   * 
   * @param fileUrl The URL of the file.
   * @return documentContent The content of the file minus the comments as String.
   */

  public String getDocumentWithoutComments(URL fileUrl) {
    long start = System.currentTimeMillis();

    String documentContent = this.readFile(fileUrl); // Read the file as String.
    Document doc = Jsoup.parse(documentContent, "", Parser.xmlParser()); // Parse the file as XML.
    removeComments(doc); // Remove any comments.

    long finish = System.currentTimeMillis();
    long timeElapsed = finish - start;

    LOG.debug("getDocumentWithoutComments: timeElapsed (millisecs) {}", timeElapsed);
    return (doc.html()); // It doesn't matter that the file is XML. This function html() returns the
                         // file
                         // as String.
  }
}
