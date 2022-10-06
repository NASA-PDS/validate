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
// $Id: InventoryReaderException.java 8162 2010-11-10 22:05:09Z mcayanan $
package gov.nasa.pds.tools.inventory.reader;

/**
 * Exception class for handling errors when reading a PDS Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryReaderException extends Exception {
  /** Generated serial ID. */
  private static final long serialVersionUID = 4687976349704354553L;

  /**
   * Holds the exception object.
   */
  private Exception exception;

  /** line number where the exception occurred. */
  private int lineNumber;

  /**
   * Constructor.
   *
   * @param exception An exception.
   */
  public InventoryReaderException(Exception exception) {
    super(exception.getMessage());
    this.exception = exception;
    lineNumber = -1;
  }

  /**
   * @return Returns the exception.
   */
  public Exception getException() {
    return exception;
  }

  /**
   * @return Returns the line number associated with the exception. Could be -1 if it was not set.
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Sets the line number.
   *
   * @param line An integer value.
   */
  public void setLineNumber(int line) {
    lineNumber = line;
  }
}
