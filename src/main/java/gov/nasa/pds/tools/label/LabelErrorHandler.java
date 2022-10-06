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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * @author pramirez, mcayanan
 *
 */
public class LabelErrorHandler implements ErrorHandler {
  private ProblemHandler handler;

  public LabelErrorHandler(ProblemHandler handler) {
    this.handler = handler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
   */
  @Override
  public void error(SAXParseException exception) throws SAXException {
    addProblem(ExceptionType.ERROR, ProblemType.SCHEMA_ERROR, exception.getMessage(),
        exception.getSystemId(), exception.getLineNumber(), exception.getColumnNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
   */
  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    addProblem(ExceptionType.ERROR, ProblemType.SCHEMA_ERROR, exception.getMessage(),
        exception.getSystemId(), exception.getLineNumber(), exception.getColumnNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  @Override
  public void warning(SAXParseException exception) throws SAXException {
    addProblem(ExceptionType.WARNING, ProblemType.SCHEMA_WARNING, exception.getMessage(),
        exception.getSystemId(), exception.getLineNumber(), exception.getColumnNumber());
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
