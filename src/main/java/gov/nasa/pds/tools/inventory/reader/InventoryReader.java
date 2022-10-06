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
// $Id: InventoryReader.java 8162 2010-11-10 22:05:09Z mcayanan $
package gov.nasa.pds.tools.inventory.reader;

/**
 * Interface for reading a PDS Inventory File.
 *
 * @author mcayanan
 *
 */
public interface InventoryReader {

  /**
   * Get the next file reference in the Inventory file.
   *
   * @return An object representation of the next file reference in the Inventory file.
   *
   * @throws InventoryReaderException If an error occured while getting the next entry in the
   *         inventory file.
   */
  public abstract InventoryEntry getNext() throws InventoryReaderException;
}
