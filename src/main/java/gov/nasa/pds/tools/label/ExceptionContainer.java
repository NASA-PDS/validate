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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implements an exception handler that retains the exceptions in memory.
 *
 * @author pramirez
 *
 */
public class ExceptionContainer implements ExceptionHandler {
  private List<LabelException> exceptions;

  public ExceptionContainer() {
    this.exceptions = new ArrayList<>();
  }

  public void clear() {
    this.exceptions.clear();
  }

  public List<LabelException> getExceptions() {
    return this.exceptions;
  }

  @Override
  public void addException(LabelException exception) {
    this.exceptions.add(exception);
  }

  public Boolean hasWarning() {
    for (LabelException exception : exceptions) {
      if (exception.getExceptionType() == ExceptionType.WARNING) {
        return true;
      }
    }
    return false;
  }

  public Boolean hasError() {
    for (LabelException exception : exceptions) {
      if (exception.getExceptionType() == ExceptionType.ERROR) {
        return true;
      }
    }
    return false;
  }

  public Boolean hasFatal() {
    for (LabelException exception : exceptions) {
      if (exception.getExceptionType() == ExceptionType.FATAL) {
        return true;
      }
    }
    return false;
  }
}
