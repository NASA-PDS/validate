// Copyright 2006-2014, by the California Institute of Technology.
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
package gov.nasa.pds.validate.target;

import java.net.URISyntaxException;
import java.net.URL;

public class Target {
  private URL url;

  private boolean isDir;

  public Target (URL url, boolean isDir) {
    this.url = url;
    this.isDir = isDir;
  }

  public URL getUrl() {
    return url;
  }

  public boolean isDir() {
    return isDir;
  }

  public String toString() {
    try {
      return url.toURI().toString();
    } catch (URISyntaxException e) {
      return url.toString();
    }
  }

  public boolean equals(Object obj) {
    Target otherTarget = (Target) obj;
    if ( (this.url.equals(otherTarget.getUrl())) &&
        (this.isDir == otherTarget.isDir()) ) {
      return true;
    } else {
      return false;
    }
  }

  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (null == url ? 0 : url.hashCode());
    hash = 31 * hash + (isDir ? 0 : 1);
    return hash;
  }

}
