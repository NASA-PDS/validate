// Copyright 2006-2013, by the California Institute of Technology.
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
package gov.nasa.pds.validate.crawler;

import gov.nasa.pds.validate.target.Target;

import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public abstract class Crawler {
  /** A file filter. */
  protected IOFileFilter fileFilter;

  /** A directory filter. */
  protected FileFilter directoryFilter;

  /** Flag to indicate whether to retrieve directory listings. */
  protected boolean getDirectories;

  public Crawler(boolean getDirectories, List<String> fileFilters) {
    this.getDirectories = getDirectories;
    fileFilter = new WildcardOSFilter("*");
    directoryFilter = FileFilterUtils.directoryFileFilter();
    if (fileFilters != null && !(fileFilters.isEmpty())) {
      fileFilter = new WildcardOSFilter(fileFilters);
    }
  }

  public abstract List<Target> crawl(URL url) throws IOException;

}
