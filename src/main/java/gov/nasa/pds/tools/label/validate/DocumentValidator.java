//  Copyright 2009-2018, by the California Institute of Technology.
//  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//  Any commercial use must be negotiated with the Office of Technology
//  Transfer at the California Institute of Technology.
//
//  This software is subject to U. S. export control laws and regulations
//  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//  is subject to U.S. export control laws and regulations, the recipient has
//  the responsibility to obtain export licenses or other export authority as
//  may be required before exporting such information to foreign countries or
//  providing access to foreign nationals.
//
//  $Id$
//
package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.validate.ProblemHandler;
import net.sf.saxon.om.DocumentInfo;

public interface DocumentValidator {

  /**
   * Method signature for checking to see if a label is valid.
   *
   * @param handler A problem handler.
   * @param xml An object representation of a parsed xml document.
   *
   * @return flag indicating whether or not the step in validation was passed.
   */
  public boolean validate(ProblemHandler handler, DocumentInfo xml);
}
