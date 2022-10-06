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

import java.net.MalformedURLException;
import java.net.URL;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.rule.AbstractValidationChain;

/**
 * Implements a validation chain that validates PDS4 bundles.
 *
 */
public class BundleValidationRule extends AbstractValidationChain {

  @Override
  public boolean isApplicable(String location) {
    URL url;
    try {
      url = new URL(location);
    } catch (MalformedURLException e) {
      return false;
    }

    if (!Utility.isDir(url)) {
      return false;
    }
    return true;
  }

}
