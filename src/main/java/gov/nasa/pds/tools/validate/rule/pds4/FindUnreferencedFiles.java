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

import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implements a validation rule that checks that all files are
 * referenced by some label.
 */
public class FindUnreferencedFiles extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    // This rule is applicable at the top level only.
    return getContext().isRootTarget();
  }

  /**
   * Iterate over unreferenced targets, reporting an error for each.
   */
  @ValidationTest
  public void findUnreferencedTargets() {
    // Only run the test if we are the root target, to avoid duplicate errors.
    if (getContext().isRootTarget() && !getContext().getAllowUnlabeledFiles()) {
      for (String location : getRegistrar().getUnreferencedTargets()) {
        try {
          reportError(PDS4Problems.UNLABELED_FILE, new URL(location), -1, -1);
        } catch (MalformedURLException e) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1, e.getMessage());
        }
      }
    }
  }

}
