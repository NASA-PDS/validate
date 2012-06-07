// Copyright 2006-2010, by the California Institute of Technology.
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
// $Id: InventoryTableReader.java 8360 2011-01-11 19:26:28Z mcayanan $
package gov.nasa.pds.validate.inventory.reader;

import gov.nasa.pds.validate.util.XMLExtractor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.commons.io.FilenameUtils;

/**
 * Class that supports reading of a table-version of the PDS
 * Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryTableReader implements InventoryReader {
  /** The field location of the identifier (LID-VID or LID). */
  private int identifierFieldLocation;

  /** The field length of the identifier (LID-VID or LID). */
  private int identifierFieldLength;

  /** The field number of the file reference. */
  private int filenameFieldLocation;

  /** The field length of the file reference. */
  private int filenameFieldLength;

  /** The field number of the checksum. */
  private int checksumFieldLocation;

  /** Reads the external data file of the Inventory file. */
  private LineNumberReader reader;

  /** The directory path of the inventory file. */
  private String parentDirectory;

  /** The data file being read. */
  private File dataFile;

  /**
   * Constructor.
   *
   * @param file A PDS Inventory file.
   *
   * @throws InventoryReaderException If an error occurred while reading
   * the Inventory file.
   */
  public InventoryTableReader(File file)
  throws InventoryReaderException {
    filenameFieldLocation = 0;
    filenameFieldLength = 0;
    checksumFieldLocation = 0;
    identifierFieldLocation = 0;
    identifierFieldLength = 0;
    dataFile = null;
    parentDirectory = file.getParent();
    XMLExtractor extractor = new XMLExtractor();
    try {
      extractor.parse(file);
      String dataFileName = extractor.getValueFromDoc(
        InventoryKeys.DATA_FILE_XPATH);
      if (dataFileName.isEmpty()) {
        throw new Exception("Could not find a data file using the "
            + "following XPath: " + InventoryKeys.DATA_FILE_XPATH);
      }
      dataFile = new File(FilenameUtils.separatorsToSystem(dataFileName));
      if (!dataFile.isAbsolute()) {
          dataFile = new File(file.getParent(), dataFile.toString());
      }
      reader = new LineNumberReader(new FileReader(dataFile));
      String value = "";
      value = extractor.getValueFromDoc(
          InventoryKeys.FILE_SPEC_FIELD_LOCATION_XPATH);
      if (!value.isEmpty()) {
        filenameFieldLocation = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing file: " + file + ". XPath "
            + "expression returned no result: "
            + InventoryKeys.FILE_SPEC_FIELD_LOCATION_XPATH);
      }
      value = extractor.getValueFromDoc(
          InventoryKeys.FILE_SPEC_FIELD_LENGTH_XPATH);
      if (!value.isEmpty()) {
        filenameFieldLength = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing file: " + file + ". XPath "
            + "expression returned no result: "
            + InventoryKeys.FILE_SPEC_FIELD_LENGTH_XPATH);
      }
      value = extractor.getValueFromDoc(
          InventoryKeys.IDENTIFIER_FIELD_LOCATION_XPATH);
      if (!value.isEmpty()) {
        identifierFieldLocation = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing file: " + file + ". XPath "
            + "expression returned no result: "
            + InventoryKeys.IDENTIFIER_FIELD_LOCATION_XPATH);
      }
      value = extractor.getValueFromDoc(
          InventoryKeys.IDENTIFIER_FIELD_LENGTH_XPATH);
      if (!value.isEmpty()) {
        identifierFieldLength = Integer.parseInt(value);
      } else {
        throw new Exception("Problems parsing file: " + file + ". XPath "
            + "expression returned no result: "
            + InventoryKeys.IDENTIFIER_FIELD_LENGTH_XPATH);
      }
    } catch (Exception e) {
      throw new InventoryReaderException(e);
    }
  }

  /**
   * Constructor.
   *
   * @param file A PDS Inventory file
   *
   * @throws InventoryReaderException If an error occurred while reading
   * the Inventory file.
   */
  public InventoryTableReader(String file)
  throws InventoryReaderException {
    this(new File(file));
  }

  /**
   * Gets the data file that is being read.
   *
   * @return the data file.
   */
  public File getDataFile() {
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
      } else if (line.isEmpty()) {
        return new InventoryEntry();
      }
    } catch (IOException i) {
        throw new InventoryReaderException(i);
    }
    File file = null;
    String identifier = "";
    if (filenameFieldLocation != 0 && filenameFieldLength != 0) {
      try {
        file = new File(FilenameUtils.separatorsToSystem(line.substring(
            filenameFieldLocation - 1,
            (filenameFieldLocation - 1) + filenameFieldLength).trim()));
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(
            new IndexOutOfBoundsException("Could not parse a value "
                + "from the file name specification field in the file: "
                + dataFile));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
      if (!file.isAbsolute()) {
        file = new File(parentDirectory, file.toString());
      }
    }
    if (identifierFieldLocation != 0 && identifierFieldLength != 0) {
      try {
      identifier = line.substring(identifierFieldLocation - 1,
          (identifierFieldLocation - 1) + identifierFieldLength).trim();
      } catch (IndexOutOfBoundsException ae) {
        InventoryReaderException ir = new InventoryReaderException(
            new IndexOutOfBoundsException("Could not parse a value "
                + "from the identifier (LID or LIDVID) field in the file: "
                + dataFile));
        ir.setLineNumber(reader.getLineNumber());
        throw ir;
      }
    }
    return new InventoryEntry(file, "", identifier);
  }
}
