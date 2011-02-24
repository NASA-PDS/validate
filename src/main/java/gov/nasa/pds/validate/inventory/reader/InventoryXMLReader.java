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
// $Id: InventoryXMLReader.java 8360 2011-01-11 19:26:28Z mcayanan $
package gov.nasa.pds.validate.inventory.reader;

import gov.nasa.pds.validate.util.XMLExtractor;

import java.io.File;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.tinytree.TinyElementImpl;

import org.apache.commons.io.FilenameUtils;

/**
 * Class that supports the reading of an XML version of the
 * PDS Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryXMLReader implements InventoryReader {
    /** The directory path of the Inventory file. */
    private String parentDirectory;

    /** An index to keep track of the number of inventory entries. */
    private int index;

    /** A list of nodes containing the inventory entries. */
    private List<TinyElementImpl> memberEntries;

    /** The XML Extractor. */
    private XMLExtractor extractor;

    /**
     * Constructor.
     *
     * @param file A PDS Inventory file.
     *
     * @throws InventoryReaderException If an error occurred while reading
     * the Inventory file.
     */
    public InventoryXMLReader(File file)
    throws InventoryReaderException {
        index = 0;
        parentDirectory = file.getParent();
        extractor = new XMLExtractor();
        try {
            extractor.parse(file);
            memberEntries = extractor.getNodesFromDoc(
                    InventoryKeys.MEMBER_ENTRY_XPATH);
        } catch (Exception e) {
            throw new InventoryReaderException(e);
        }
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
        if (index >= memberEntries.size()) {
            return null;
        }

        TinyElementImpl entry = memberEntries.get(index++);
        File file = null;
        String checksum = "";
        String lidvid = "";
        try {
            file = new File(
                    FilenameUtils.separatorsToSystem(
                    extractor.getValueFromItem(
                            InventoryKeys.FILE_SPEC_XPATH, entry)));
            checksum = extractor.getValueFromItem(InventoryKeys.CHECKSUM_XPATH,
                    entry);
            lidvid = extractor.getValueFromItem(
                    InventoryKeys.IDENTITY_REFERENCE_XPATH, entry);
        } catch (XPathExpressionException x) {
            throw new InventoryReaderException(x);
        }
        if (!file.isAbsolute()) {
            file = new File(parentDirectory, file.toString());
        }
        return new InventoryEntry(file, checksum, lidvid);
    }
}
