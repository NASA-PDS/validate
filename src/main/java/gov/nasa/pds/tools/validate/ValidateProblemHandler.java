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
package gov.nasa.pds.tools.validate;

/**
 * Defines an interface for handling exceptions during label validation.
 */
public interface ValidateProblemHandler extends ProblemHandler {
  /**
   * Adds a location to the listener.
   *
   * @param location the location of the label being validated.
   */
  void addLocation(String location);

  /**
   * Adds a header to the report.
   * 
   * @param title The name of the section to add.
   */
  void printHeader(String title);

  /**
   * Records the validation results.
   * 
   * @param location The target file.
   */
  void record(String location);
}
