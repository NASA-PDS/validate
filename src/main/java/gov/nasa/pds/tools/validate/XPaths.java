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
// $Id$
package gov.nasa.pds.tools.validate;

/**
 * Class containing some of the common XPaths used in the library.
 * 
 * @author mcayanan
 *
 */
public class XPaths {
  /** PDS4 namespace. */
  public static final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";

  /** XPath to Table_Binary elements. */
  public static final String TABLE_BINARY
  = "//*:Table_Binary[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to Table_Character elements. */
  public static final String TABLE_CHARACTER
      = "//*:Table_Character[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to Table_Delimited elements. */
  public static final String TABLE_DELIMITED = 
      "//*:Table_Delimited[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the Manifest_SIP_Deep_Archive element. */
  public static final String MANIFEST_SIP_DEEP_ARCHIVE = 
      "//*:Manifest_SIP_Deep_Archive[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the Transfer_Manifest element. */
  public static final String TRANSFER_MANIFEST = 
      "//*:Transfer_Manifest[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the Inventory element. */
  public static final String INVENTORY = 
      "//*:Inventory[namespace-uri()='" + PDS4_NS + "']";

  /** XPath to find tables in a label. */
  public static final String TABLE_TYPES = TABLE_BINARY + " | "
      + TABLE_CHARACTER + " | " + TABLE_DELIMITED + " | "
      + MANIFEST_SIP_DEEP_ARCHIVE + " | "
      + TRANSFER_MANIFEST + " | " + INVENTORY;
  
  /** XPaths to find the different File_Area elements related to tables. */
  public static final String FILE_AREA_OBSERVATIONAL = 
      "//*:File_Area_Observational[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the File_Area_SIP_Deep_Archive element. */
  public static final String FILE_AREA_SIP_DEEP_ARCHIVE = 
      "//*:File_Area_SIP_Deep_Archive[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the File_Area_Transfer_Manifest element. */
  public static final String FILE_AREA_TRANSFER_MANIFEST = 
      "//*:File_Area_Transfer_Manifest[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the File_Area_Inventory element. */
  public static final String FILE_AREA_INVENTORY = 
      "//*:File_Area_Inventory[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the File_Area_Browse element. */
  public static final String FILE_AREA_BROWSE = 
      "//*:File_Area_Browse[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the File_Area_Browse element. */
  public static final String FILE_AREA_ANCILLARY = 
      "//*:File_Area_Ancillary[namespace-uri()='" + PDS4_NS + "']";
  
  /** XPath to the different File_Area elements for tables. */
  public static final String TABLE_FILE_AREAS = 
      FILE_AREA_OBSERVATIONAL + " | " + FILE_AREA_SIP_DEEP_ARCHIVE
      + " | " + FILE_AREA_TRANSFER_MANIFEST + " | "
      + FILE_AREA_INVENTORY + " | " + FILE_AREA_BROWSE + " | " + FILE_AREA_ANCILLARY;
  
  /** Xpath to the Array objects. */
  public static final String ARRAYS = "//*[starts-with(name(), 'Array')]";
  
  /** XPath to the different File_Area elements for tables. */
  public static final String ARRAY_FILE_AREAS = 
      FILE_AREA_OBSERVATIONAL + " | " + FILE_AREA_BROWSE;
}
