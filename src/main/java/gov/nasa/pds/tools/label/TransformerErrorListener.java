// Copyright 2009-2018, by the California Institute of Technology.
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

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Listener class to simply throw exceptions when an error occurs when transforming a schematron.
 * This prevents transformer error messages from appearing on the standard out.
 *
 * @author mcayanan
 *
 */
public class TransformerErrorListener implements ErrorListener {
  private ProblemHandler handler;
  private class DefaultSourceLocator implements SourceLocator{

    @Override
    public String getPublicId() {
      return "null";
    }

    @Override
    public String getSystemId() {
      return "null";
    }

    @Override
    public int getLineNumber() {
      return -1;
    }

    @Override
    public int getColumnNumber() {
      return -1;
    }
  }
  final private SourceLocator nullLocator = new DefaultSourceLocator();
  /**
   * Constructor.
   *
   * @param handler A container to hold problems that occur during the transform process.
   */
  public TransformerErrorListener(ProblemHandler handler) {
    this.handler = handler;
  }

  @Override
  public void error(TransformerException exception) throws TransformerException {
    SourceLocator locator = exception.getLocator();
    if (locator == null) {
      locator = this.nullLocator;
    }
    addProblem(ExceptionType.ERROR, ProblemType.SCHEMATRON_ERROR, exception.getMessage(),
        locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
  }

  @Override
  public void fatalError(TransformerException exception) throws TransformerException {
    SourceLocator locator = exception.getLocator();
    if (locator == null) {
      locator = this.nullLocator;
    }
    addProblem(ExceptionType.FATAL, ProblemType.SCHEMATRON_ERROR, exception.getMessage(),
        locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
  }

  @Override
  public void warning(TransformerException exception) throws TransformerException {
    SourceLocator locator = exception.getLocator();
    if (locator == null) {
      locator = this.nullLocator;
    }
    addProblem(ExceptionType.WARNING, ProblemType.SCHEMATRON_WARNING, exception.getMessage(),
        locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
  }

  private void addProblem(ExceptionType severity, ProblemType type, String message, String systemId,
      int lineNumber, int columnNumber) {
    URL url = null;
    try {
      url = new URL(systemId);
    } catch (MalformedURLException mu) {
      // Ignore. Should not happen!!!
    }
    handler.addProblem(new ValidationProblem(new ProblemDefinition(severity, type, message), url,
        lineNumber, columnNumber));
  }
}
