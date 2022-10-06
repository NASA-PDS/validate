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

import java.net.URL;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to smart read a table by counting the number of headers before the "Table_" tag.
 *
 */

public class TableUtil {
  private static final Logger LOG = LoggerFactory.getLogger(TableUtil.class);
  private DocumentUtil documentUtil = new DocumentUtil();

  public void DocumenUtil() {}

  /**
   * Returns the number of "<Header>" tags in fileUrl before a "<Table_" tag.
   * 
   * @param fileUrl The URL of the label.
   * @return headersBeforeTable The number of headers before a table tag.
   */

  public int countHeadersBeforeTable(URL fileUrl) {
    // Given a url to a label, count the number of "<Header>" tags that occur before
    // any "Table_" tags.
    // The reason is there can be "<Header>" tags for arrays and such in the file.
    long start = System.currentTimeMillis();
    int headersBeforeTable = 0;

    // It is important to remove all the comments from the file before doing any
    // processing
    // since the tags we are looking for could be inside the comments.
    String cleanDocument = this.documentUtil.getDocumentWithoutComments(fileUrl);

    Scanner scanner = new Scanner(cleanDocument);

    String currLine = null;
    int lineNumber = 1;
    boolean foundTableTag = false;

    // Count the "Header" tags until reached the "Table_" tag.
    while (scanner.hasNextLine() && !foundTableTag) {
      currLine = scanner.nextLine();
      if (currLine.contains("<Header>")) {
        LOG.debug("countHeadersBeforeTable: currLine [" + currLine + "] lineNumber {}", lineNumber);
        headersBeforeTable += 1;
      }
      // Any "Table_" tag will do. Possible list: {Table_Character,Table_Delimited,
      // Table_Binary}
      if (currLine.contains("<Table_")) {
        LOG.debug("countHeadersBeforeTable: currLine [" + currLine + "] lineNumber {}", lineNumber);
        foundTableTag = true; // Set to true to allow while loop to exit.
      }
      lineNumber++;
    }
    scanner.close();

    long finish = System.currentTimeMillis();
    long timeElapsed = finish - start;
    LOG.debug("countHeadersBeforeTable: timeElapsed (millisecs) {}", timeElapsed);
    return (headersBeforeTable);
  }
}
