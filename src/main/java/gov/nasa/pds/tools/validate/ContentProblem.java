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

import java.net.URL;

/**
 * An object representation of problems relating to data content validation.
 * 
 * @author mcayanan
 *
 */
public class ContentProblem extends ValidationProblem {
  /** url to the label. */
  protected URL label;

  /**
   * Constructor.
   * 
   * @param defn An object representation of a problem.
   * @param source url of the data file associated with this message.
   * @param label url of the label file.
   */
  public ContentProblem(ProblemDefinition defn, URL source, URL label) {
    super(defn, source);
    this.label = label;
  }
  
  /**
   * 
   * @return Returns the label.
   */
  public URL getLabel() {
    return this.label;
  }
}
