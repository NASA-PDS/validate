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
// $Id: InventoryTableReader.java 10921 2012-09-10 22:11:40Z mcayanan $
package gov.nasa.pds.tools.inventory.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import gov.nasa.pds.tools.util.XMLExtractor;

/**
 * Class that supports reading of a table-version of the PDS Inventory file.
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

  private long numRecords = -1;

  /**
   * XPath to determine the field delimiter being used in the inventory table.
   */
  public static final String FIELD_DELIMITER = "//Inventory/field_delimiter";

  /**
   * XPath to determine the field location of the member status field in the inventory table.
   */
  public static final String MEMBER_STATUS_FIELD_NUMBER =
      "//Inventory/Record_Delimited/Field_Delimited[name='Member_Status' or name='Member Status']/field_number";

  /**
   * XPath to determine the field location of the LID-LIDVID field in the inventory table.
   */
  public static final String LIDVID_LID_FIELD_NUMBER =
      "//Inventory/Record_Delimited/Field_Delimited[data_type='ASCII_LIDVID_LID']/field_number";

  /** XPath to the external table file of a collection. */
  public static final String DATA_FILE = "//*[starts-with(name()," + "'File_Area')]/File/file_name";

  /**
   * Constructor.
   *
   * @param url The URL to the PDS Inventory file.
   *
   * @throws InventoryReaderException If an error occurred while reading the Inventory file.
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  public InventoryTableReader(URL url) throws InventoryReaderException {
    memberStatusFieldNumber = 0;
    identifierFieldNumber = 0;
    dataFile = null;
    try {
      try {
        parent = url.toURI().getPath().endsWith("/") ? url.toURI().resolve("..").toURL()
            : url.toURI().resolve(".").toURL();
      } catch (Exception e) {
        throw new Exception("Problem occurred while trying to get the parent " + " URL of '"
            + url.toString() + "': " + e.getMessage());
      }
      XMLExtractor extractor = new XMLExtractor(url);
      String dataFileName = extractor.getValueFromDoc(DATA_FILE);
      if (dataFileName.equals("")) {
        throw new Exception(
            "Could not retrieve a data file name using " + "the following XPath: " + DATA_FILE);
      }
      dataFile = new URL(parent, dataFileName);

      this.numRecords = Long.parseLong(extractor.getValueFromDoc("//Inventory/records"));
      reader =
          new LineNumberReader(new BufferedReader(new InputStreamReader(dataFile.openStream())));
      String value = "";
      // Extract the field numbers defined in the inventory table section
      // in order to determine the metadata in the data file.
      value = extractor.getValueFromDoc(MEMBER_STATUS_FIELD_NUMBER);
      if (value.isEmpty()) {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: " + MEMBER_STATUS_FIELD_NUMBER);
      }
      memberStatusFieldNumber = Integer.parseInt(value);
      value = extractor.getValueFromDoc(LIDVID_LID_FIELD_NUMBER);
      if (value.isEmpty()) {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: " + LIDVID_LID_FIELD_NUMBER);
      }
      identifierFieldNumber = Integer.parseInt(value);
      value = extractor.getValueFromDoc(FIELD_DELIMITER);
      if (value.isEmpty()) {
        throw new Exception("Problems parsing url '" + url.toString() + "'. XPath "
            + "expression returned no result: " + FIELD_DELIMITER);
      }
      fieldDelimiter = InventoryKeys.fieldDelimiters.get(value.toLowerCase());
      if (fieldDelimiter == null) {
        throw new Exception("Field delimiter value is not a valid value: " + value);
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
   * Returns the records number in the PDS Inventory file.
   * 
   */
  public long getNumRecords() {
    return this.numRecords;
  }

  /**
   * Gets the next product file reference in the PDS Inventory file.
   *
   * @return A class representation of the next product file reference in the PDS inventory file. If
   *         the end-of-file has been reached, a null value will be returned.
   *
   * @throws InventoryReaderException If an error occurred while reading the Inventory file.
   *
   */
  @Override
  public InventoryEntry getNext() throws InventoryReaderException {
    String line = "";
    try {
      line = reader.readLine();
      if (line == null) {
        reader.close();
        return null;
      }
      if (line.trim().equals("")) {
        return new InventoryEntry();
      }
    } catch (IOException i) {
      throw new InventoryReaderException(i);
    }
    if (fieldDelimiter == null) {
      throw new InventoryReaderException(new Exception("Field delimiter is not set."));
    }
    String identifier = "";
    String memberStatus = "";
    String fields[] = line.split(fieldDelimiter);
    if (memberStatusFieldNumber != 0) {
      try {
        memberStatus = fields[memberStatusFieldNumber - 1].trim();
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(new IndexOutOfBoundsException(
            "Could not retrieve the member " + "status after parsing the line in the file '"
                + dataFile + "': " + Arrays.asList(fields)));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
    }
    if (identifierFieldNumber != 0) {
      try {
        identifier = fields[identifierFieldNumber - 1].trim();
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(new IndexOutOfBoundsException(
            "Could not retrieve the " + "LIDVID-LID value after parsing the line in the file '"
                + dataFile + "': " + Arrays.asList(fields)));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
    }
    return new InventoryEntry(identifier, memberStatus);
  }
}
