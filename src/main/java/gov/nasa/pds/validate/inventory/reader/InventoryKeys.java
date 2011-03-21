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
// $Id: InventoryKeys.java -1M 2010-11-01 17:33:08Z (local) $
package gov.nasa.pds.validate.inventory.reader;

public class InventoryKeys {
    /** XPath to the field number of the column containing the file
     * reference.
     */
    public static final String FILE_SPEC_FIELD_NUM_XPATH = "//*[starts-with("
        + "name(),'Table_Record')]/"
        + "Table_Field_File_Specification_Name/field_number";

    /**
     * XPath to the field number of the column containing the LID-VID or LID
     * reference.
     */
    public static final String LIDVID_FIELD_NUM_XPATH =
        "//Table_Record_Inventory_LIDVID/Table_Field_LIDVID/field_number | "
        + "//Table_Record_Inventory_LID/Table_Field_LID/field_number";

    /** XPath to the external table file of a collection. */
    public static final String DATA_FILE_XPATH = "//*[starts-with(name(),"
      + "'File_Area')]/File/file_name";

    /** XPath to grab the Member_Entry tags in a bundle. */
    public static final String MEMBER_ENTRY_XPATH =
      "//*[ends-with(name(),'Member_Entry')]";

    /** The file reference XPath in an Inventory file. */
    public static final String FILE_SPEC_XPATH = "file_specification_name";

    /** The MD5 checksum XPath in an Inventory file. */
    public static final String CHECKSUM_XPATH = "md5_checksum";

    /** The LID-VID or LID XPath for an association. */
    public static final String IDENTITY_REFERENCE_XPATH =
        "lidvid_reference | lid_reference";

    /** XPath that will indicate if a collection is primary. */
    public static final String IS_PRIMARY_COLLECTION_XPATH =
      "//*[starts-with(name(), 'Identification_Area')]/is_primary_collection";
}
