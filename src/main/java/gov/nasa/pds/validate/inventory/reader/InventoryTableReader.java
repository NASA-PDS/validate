// Copyright 2006-2014, by the California Institute of Technology.
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
// $Id: InventoryTableReader.java 10921 2012-09-10 22:11:40Z mcayanan $
package gov.nasa.pds.validate.inventory.reader;

import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.validate.XPath.CoreXPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * Class that supports reading of a table-version of the PDS
 * Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryTableReader implements InventoryReader {
  /** The field location of the identifier (LID-VID or LID). */
  private int identifierFieldNumber;

  /** The field location of the member status. */
  private int memberStatusFieldNumber;

  /** The field delimiter being used in the inventory table. */
  private String fieldDelimiter;

  /** Reads the external data file of the Inventory file. */
  private LineNumberReader reader;

  /** The directory path of the inventory file. */
  private URL parent;

  /** The data file being read. */
  private URL dataFile;

  /**
   * Constructor.
   *
   * @param url The URL to the PDS Inventory file.
   *
   * @throws InventoryReaderException If an error occurred while reading
   * the Inventory file.
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  public InventoryTableReader(URL url)
  throws InventoryReaderException {
    memberStatusFieldNumber = 0;
    identifierFieldNumber = 0;
    dataFile = null;
    try {
      try {
        parent = url.toURI().getPath().endsWith("/") ?
          url.toURI().resolve("..").toURL() :
            url.toURI().resolve(".").toURL();
      } catch (Exception e) {
        throw new Exception("Problem occurred while trying to get the parent "
            + " URL of '" + url.toString() + "': " + e.getMessage());
      }
      XMLExtractor extractor = new XMLExtractor(url);
      String dataFileName = extractor.getValueFromDoc(
          CoreXPaths.DATA_FILE);
      if (dataFileName.equals("")) {
        throw new Exception("Could not retrieve a data file name using "
            + "the following XPath: " + CoreXPaths.DATA_FILE);
      }
      dataFile = new URL(parent, dataFileName);

      reader = new LineNumberReader(new BufferedReader(
          new InputStreamReader(dataFile.openStream())));
      String value = "";
      // Extract the field numbers defined in the inventory table section
      // in order to determine the metadata in the data file.
      value = extractor.getValueFromDoc(
          CoreXPaths.MEMBER_STATUS_FIELD_NUMBER);
      if (!value.isEmpty()) {
        memberStatusFieldNumber = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: "
            + CoreXPaths.MEMBER_STATUS_FIELD_NUMBER);
      }
      value = extractor.getValueFromDoc(
          CoreXPaths.LIDVID_LID_FIELD_NUMBER);
      if (!value.isEmpty()) {
        identifierFieldNumber = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: "
            + CoreXPaths.LIDVID_LID_FIELD_NUMBER);
      }
      value = extractor.getValueFromDoc(CoreXPaths.FIELD_DELIMITER);
      if (!value.isEmpty()) {
        fieldDelimiter = InventoryKeys.fieldDelimiters.get(
            value.toLowerCase());
        if (fieldDelimiter == null) {
          throw new Exception("Field delimiter value is not a valid value: "
              + value);
        }
      } else {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: "
            + CoreXPaths.FIELD_DELIMITER);
      }
    } catch (Exception e) {
      throw new InventoryReaderException(e);
    }
  }

  /**
   * Gets the data file that is being read.
   *
   * @return the data file.
   */
  public URL getDataFile() {
    return dataFile;
  }

  /**
   * Gets the line number that was just read.
   *
   * @return the line number.
   */
  public int getLineNumber() {
    return reader.getLineNumber();
  }

  /**
   * Gets the next product file reference in the PDS Inventory file.
   *
   * @return A class representation of the next product file reference
   * in the PDS inventory file. If the end-of-file has been reached,
   * a null value will be returned.
   *
   * @throws InventoryReaderException If an error occurred while reading
   * the Inventory file.
   *
   */
  public InventoryEntry getNext() throws InventoryReaderException {
    String line = "";
    try {
      line = reader.readLine();
      if (line == null) {
        reader.close();
        return null;
      } else if (line.trim().equals("")) {
        return new InventoryEntry();
      }
    } catch (IOException i) {
      throw new InventoryReaderException(i);
    }
    if (fieldDelimiter == null) {
      throw new InventoryReaderException(
          new Exception("Field delimiter is not set."));
    }
    String identifier = "";
    String memberStatus = "";
    String fields[] = line.split(fieldDelimiter);
    if (memberStatusFieldNumber != 0) {
      try {
        memberStatus = fields[memberStatusFieldNumber-1].trim();
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(
            new IndexOutOfBoundsException("Could not retrieve the member "
                + "status after parsing the line in the file '" + dataFile
                + "': " + Arrays.asList(fields)));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
    }
    if (identifierFieldNumber != 0) {
      try {
      identifier = fields[identifierFieldNumber-1].trim();
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(
            new IndexOutOfBoundsException("Could not retrieve the "
                + "LIDVID-LID value after parsing the line in the file '"
                + dataFile + "': " + Arrays.asList(fields)));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
    }
    return new InventoryEntry(identifier, memberStatus);
  }
}
