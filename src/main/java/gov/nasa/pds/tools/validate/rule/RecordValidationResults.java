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
package gov.nasa.pds.tools.validate.rule;

import gov.nasa.pds.tools.validate.ListenerExceptionPropagator;

/**
 * Class intended to record the validation results. This allows us to
 * continually flush the results from cache memory. Useful especially when
 * validating a million product bundle.
 * 
 * @author mcayanan
 *
 */
public class RecordValidationResults extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    return true;
  }

  @ValidationTest
  public void record() {
    // For now, we will record validation results only if using the 
    // ListenerExceptionPropagator class. 
    if (getListener() instanceof ListenerExceptionPropagator) {
      ListenerExceptionPropagator lp = 
          (ListenerExceptionPropagator) getListener();
      lp.record(getTarget().toString());
    }
  }
}
