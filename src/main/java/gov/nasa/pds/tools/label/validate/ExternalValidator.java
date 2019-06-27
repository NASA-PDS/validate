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

import java.io.File;
import java.net.URL;

import gov.nasa.pds.tools.validate.ProblemHandler;

public interface ExternalValidator {
  /**
   * Method signature for checking to see if a label is valid.
   *
   * @param handler A problem handler.
   * @param labelFile PDS4 label file.
   *
   * @return flag indicating whether or not the step in validation was passed.
   */
  public boolean validate(ProblemHandler handler, File labelFile);

  /**
   * Method signature for checking to see if a label is valid.
   *
   * @param handler A problem handler.
   * @param url URL of the PDS4 label.
   *
   * @return flag indicating whether or not the step in validation was passed.
   */
  public boolean validate(ProblemHandler handler, URL url);
}
