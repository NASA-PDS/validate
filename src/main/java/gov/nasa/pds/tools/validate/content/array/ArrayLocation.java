// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.content.array;

import java.net.URL;

/**
 * Class that holds a specific location in an Array.
 *
 * @author mcayanan
 *
 */
public final class ArrayLocation {
  /** The label associated with the data. */
  private URL label;

  /** The data file associated with the record. */
  private URL dataFile;

  /** The index of the table associated with the record. */
  private int array;

  /** The array location. */
  private int[] location;

  /**
   * Constructor.
   * 
   * @param label The url to the label.
   * @param dataFile The url to the data file.
   * @param array The array index.
   * @param location The location.
   */
  public ArrayLocation(URL label, URL dataFile, int array, int[] location) {
    this.label = label;
    this.dataFile = dataFile;
    this.array = array;
    this.location = location;
  }

  /**
   * 
   * @return the label.
   */
  public URL getLabel() {
    return this.label;
  }

  /**
   * 
   * @return the data file.
   */
  public URL getDataFile() {
    return this.dataFile;
  }

  /**
   * 
   * @return the array index.
   */
  public int getArray() {
    return this.array;
  }

  /**
   * 
   * @return the array location.
   */
  public int[] getLocation() {
    return this.location;
  }

}
