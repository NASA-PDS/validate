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
// $Id$
package gov.nasa.pds.validate.XPath;

/**
 * Interface containing XPaths used to extract core information
 * from a PDS4 data product label.
 *
 * @author mcayanan
 *
 */
public interface CoreXPaths {
  /**
   * XPath to the product class.
   */
  public final static String PRODUCT_CLASS =
      "//*[starts-with(name(),'Identification_Area')]/product_class";

  /**
   * XPath to the logical identifier.
   */
  public final static String LOGICAL_IDENTIFIER =
      "//*[starts-with(name(),'Identification_Area')]/logical_identifier";

  /**
   * XPath to the version id.
   */
  public final static String VERSION_ID =
      "//*[starts-with(name(),'Identification_Area')]/version_id";

  /**
   * XPath to determine the field delimiter being used in the inventory table.
   */
  public static final String FIELD_DELIMITER = "//Inventory/field_delimiter";

  /**
   * XPath to determine the field location of the member status field in the
   * inventory table.
   */
  public static final String MEMBER_STATUS_FIELD_NUMBER =
    "//Inventory/Record_Delimited/Field_Delimited[name='Member_Status']/field_number";

  /**
   * XPath to determine the field location of the LID-LIDVID field in the
   * inventory table.
   */
  public static final String LIDVID_LID_FIELD_NUMBER =
    "//Inventory/Record_Delimited/Field_Delimited[data_type='ASCII_LIDVID_LID']/field_number";

  /** XPath to the external table file of a collection. */
  public static final String DATA_FILE = "//*[starts-with(name(),"
    + "'File_Area')]/File/file_name";

  /** XPath to grab the Member_Entry tags in a bundle. */
  public final static String BUNDLE_MEMBER_ENTRY = "//Bundle_Member_Entry";

  /** The MD5 checksum XPath in an Inventory file. */
  public static final String CHECKSUM = "md5_checksum";

  /** The member status XPath in an Inventory file. */
  public static final String MEMBER_STATUS = "member_status";

  /** The LID-VID or LID XPath for an association. */
  public static final String IDENTITY_REFERENCE =
      "lidvid_reference | lid_reference";

}
