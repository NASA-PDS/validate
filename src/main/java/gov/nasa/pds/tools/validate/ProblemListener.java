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

import java.util.Collection;
import gov.nasa.pds.tools.label.ExceptionType;

/**
 * Defines an interface for receiving problems during the validation.
 */
public interface ProblemListener extends ProblemHandler {
  /**
   * Adds a location to the listener.
   *
   * @param location the location of the label being validated.
   */
  void addLocation(String location);

  /**
   * Gets the number of error problems encountered.
   *
   * @return the error count
   */
  int getErrorCount();

  /**
   * Gets the number of warning problems encountered.
   *
   * @return the warning count
   */
  int getWarningCount();

  /**
   * Gets the number of informational problems encountered.
   *
   * @return the info count
   */
  int getInfoCount();

  /**
   * Tests whether a target has had problems reported.
   *
   * @param location the target location
   * @param includeChildren true, if problems for child locations should be included
   * @return true, if problems have been reported
   */
  boolean hasProblems(String location, boolean includeChildren);

  /**
   * Gets the maximum severity of problems related to a location.
   *
   * @param location the target location
   * @param includeChildren true, if problems in children of the target should be included
   * @return the maximum problem severity
   */
  ExceptionType getSeverity(String location, boolean includeChildren);

  /**
   * Gets a collection of problems for a given location.
   *
   * @param location the target location
   * @param includeChildren true, if problems for child locations should be included
   * @return a collection of problems
   */
  Collection<ValidationProblem> getProblemsForLocation(String location, boolean includeChildren);

}
