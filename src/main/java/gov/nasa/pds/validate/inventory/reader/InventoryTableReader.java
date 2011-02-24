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
    /** The field number of the LID-VID. */
    private int lidvidFieldLocation;

    /** The field number of the file reference. */
    private int filenameFieldLocation;

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
        checksumFieldLocation = 0;
        lidvidFieldLocation = 0;
        dataFile = null;
        parentDirectory = file.getParent();
        XMLExtractor extractor = new XMLExtractor();
        try {
            extractor.parse(file);
            String value = extractor.getValueFromDoc(
                InventoryKeys.DATA_FILE_XPATH);
            if (value.isEmpty()) {
              throw new Exception("Could not find a data file using the "
                  + "following XPath: " + InventoryKeys.DATA_FILE_XPATH);
            }
            dataFile = new File(FilenameUtils.separatorsToSystem(value));
            if (!dataFile.isAbsolute()) {
                dataFile = new File(file.getParent(), dataFile.toString());
            }
            reader = new LineNumberReader(new FileReader(dataFile));
            filenameFieldLocation = Integer.parseInt(
                    extractor.getValueFromDoc(
                            InventoryKeys.FILE_SPEC_FIELD_NUM_XPATH));
            lidvidFieldLocation = Integer.parseInt(
                    extractor.getValueFromDoc(
                            InventoryKeys.LIDVID_FIELD_NUM_XPATH));
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
        String lidvid = null;
        String[] tokens = line.split(",");
        if (filenameFieldLocation != 0) {
            try {
              file = new File(FilenameUtils.separatorsToSystem(
                    tokens[filenameFieldLocation - 1].trim()));
            } catch (ArrayIndexOutOfBoundsException ae) {
               InventoryReaderException ir = new InventoryReaderException(
                   new ArrayIndexOutOfBoundsException(
                   "Could not parse a value from the file name field in the file: " + dataFile));
               ir.setLineNumber(reader.getLineNumber());
               throw ir;
            }
            if (!file.isAbsolute()) {
                file = new File(parentDirectory, file.toString());
            }
        }
        if (lidvidFieldLocation != 0) {
          try {
            lidvid = tokens[lidvidFieldLocation - 1].trim();
          } catch (ArrayIndexOutOfBoundsException ae) {
            InventoryReaderException ir = new InventoryReaderException(
                new ArrayIndexOutOfBoundsException(
                "Could not parse a value from the lidvid field in the file: " + dataFile));
            ir.setLineNumber(reader.getLineNumber());
            throw ir;
          }
        }
        return new InventoryEntry(file, "", lidvid);
    }
}
