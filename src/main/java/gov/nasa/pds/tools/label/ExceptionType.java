// Copyright 2009-2010, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology
// Transfer at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez, mcayanan
 *
 */
public enum ExceptionType {
  DEBUG(5, "DEBUG"), INFO(4, "INFO"), WARNING(3, "WARNING"), ERROR(2, "ERROR"), FATAL(1,
      "FATAL_ERROR");

  private int value;
  private String name;

  ExceptionType(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public int getValue() {
    return this.value;
  }

  public boolean isDebugApplicable() {
    return getValue() >= DEBUG.getValue();
  }

  public boolean isInfoApplicable() {
    return getValue() >= INFO.getValue();
  }

  public boolean isWarningApplicable() {
    return getValue() >= WARNING.getValue();
  }

  public boolean isErrorApplicable() {
    return getValue() >= ERROR.getValue();
  }

  public String getName() {
    return this.name;
  }
}
