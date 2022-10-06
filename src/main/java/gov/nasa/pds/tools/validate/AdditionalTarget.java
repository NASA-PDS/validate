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

package gov.nasa.pds.tools.validate;

import java.net.URL;
import java.util.ArrayList;

/**
 * Object representation of additional target input. In use when the --alternate_file_paths is
 * specified from command line.
 */
public class AdditionalTarget {

  private ArrayList<URL> extraTargetList = new ArrayList<>();

  public AdditionalTarget(ArrayList<URL> urlList) {
    this.extraTargetList = urlList;
  }

  public ArrayList<URL> getExtraTargetList() {
    return (this.extraTargetList);
  }

  /*-->
    public boolean equals(Object obj) {
  AdditionalTarget otherTarget = (AdditionalTarget) obj;
  if (this.extraTargetList.equals(otherTarget.getExtraTargetList())) {
    return true;
  } else {
    return false;
  }
    }
  
    public int hashCode() {
  int hash = 7;
  hash = 31 * hash + (null == extraTargetList ? 0 : extraTargetList.hashCode());
  return hash;
    }
  <---*/
}
