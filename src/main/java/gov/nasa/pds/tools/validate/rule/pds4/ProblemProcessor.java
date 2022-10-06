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
package gov.nasa.pds.tools.validate.rule.pds4;

import java.net.URL;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Implements a ProblemHandler for XML parsing and Schematron errors.
 */
public class ProblemProcessor implements ProblemHandler {
  private ProblemListener listener;
  private URL target;

  /**
   * Constructor.
   * 
   * @param listener problem listener.
   * @param target The URL of the target label.
   */
  public ProblemProcessor(ProblemListener listener, URL target) {
    this.listener = listener;
    this.target = target;
  }

  /**
   * 
   * @return The target label.
   */
  public URL getTarget() {
    return this.target;
  }

  /**
   * Add a problem.
   * 
   * @param problem The validation problem to add.
   */
  @Override
  public void addProblem(ValidationProblem problem) {
    problem.setSource(target.toString());
    listener.addProblem(problem);
  }
}
