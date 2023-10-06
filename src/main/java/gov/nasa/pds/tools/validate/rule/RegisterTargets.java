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

import java.net.URISyntaxException;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.TargetType;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.WildcardOSFilter;

/**
 * Implements a rule that inserts this target into the target registry, if not already present, and
 * also adds all of its child targets.
 */
public class RegisterTargets extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    return true;
  }

  @ValidationTest
  public void registerTargets() {
    TargetRegistrar registrar = getRegistrar();

    String targetLocation = getTarget().toString();
    String parentLocation = getParentTarget();
    TargetType type = Utility.getTargetType(getTarget());

    if (registrar.getRoot() == null || !registrar.hasTarget(targetLocation)) {
      registrar.addTarget(parentLocation, type, targetLocation);
    }

    if (Utility.isDir(getTarget())) {
      try {
        Crawler crawler = getContext().getCrawler();
        WildcardOSFilter fileFilter = getContext().getFileFilters();
        for (Target child : crawler.crawl(getTarget(), getContext().isRecursive(), fileFilter)) {
          try {
            String childLocation = child.getUrl().toURI().normalize().toString();
            TargetType childType = Utility.getTargetType(child.getUrl());
            registrar.addTarget(targetLocation, childType, childLocation);
          } catch (URISyntaxException e) {
            reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1,
                e.getMessage());
          }
        }
      } catch (Exception e) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1,
            e.getMessage());
      }
    }
  }
}
