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

import gov.nasa.pds.tools.validate.TargetRegistrar;

import java.net.URL;

public abstract class AbstractFindTargets extends AbstractValidationRule {

  @Override
  public boolean isApplicable(String location) {
    return true;
  }

  @ValidationTest
  public void findTargets() {
    TargetRegistrar registrar = getRegistrar();
    TreeWalker walker = new TreeWalker(registrar);
    walker.walkSubtree(getTarget(), null);
  }

  public abstract void handleDirectory(URL dir, TargetRegistrar registrar);

  public abstract void handleFile(URL f, TargetRegistrar registrar);

  private class TreeWalker extends AbstractFileSubtreeWalker<Void> {

    private TargetRegistrar registrar;

    public TreeWalker(TargetRegistrar registrar) {
      this.registrar = registrar;
    }

    @Override
    protected Void handleDirectory(URL dir, Void state) throws Exception {
      AbstractFindTargets.this.handleDirectory(dir, registrar);
      return null;
    }

    @Override
    protected void handleFile(URL f, Void state) throws Exception {
      AbstractFindTargets.this.handleFile(f, registrar);
    }

  }

}
