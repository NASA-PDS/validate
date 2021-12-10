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
package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.label.ExceptionHandler;
import gov.nasa.pds.tools.label.LabelException;

import java.net.URL;

/**
 * Implements an exception handler for XML parsing and Schematron
 * errors.
 */
public class ExceptionProcessor implements ExceptionHandler {
  private URL target;

  public ExceptionProcessor(ProblemListener listener, URL target) {
    this.target = target;
  }
  
  public URL getTarget() {
    return this.target;
  }
  
  public void addException(LabelException exception) {
    exception.setSource(target.toString());
  }
}
