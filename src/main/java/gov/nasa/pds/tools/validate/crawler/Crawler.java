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
package gov.nasa.pds.tools.validate.crawler;

import gov.nasa.pds.tools.validate.Target;

import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public abstract class Crawler {
  /** A file filter. */
  protected IOFileFilter fileFilter;

  /** A directory filter. */
  protected FileFilter directoryFilter;

  public Crawler() {
    fileFilter = new WildcardOSFilter("*");
    directoryFilter = FileFilterUtils.directoryFileFilter();
  }

  public Crawler(IOFileFilter fileFilter) {
    this.fileFilter = fileFilter;
    directoryFilter = FileFilterUtils.directoryFileFilter();
  }
  
  public void setFileFilter(List<String> fileFilters) {
    this.fileFilter = new WildcardOSFilter(fileFilters);
  }
  
  public void setFileFilter(IOFileFilter fileFilter) {
    this.fileFilter = fileFilter;
  }

  public List<Target> crawl(URL url) throws IOException {
    return crawl(url, true, this.fileFilter);
  }
  
  public List<Target> crawl(URL url, IOFileFilter fileFilter) throws IOException {
    return crawl(url, true, fileFilter);
  }
  
  public List<Target> crawl(URL url, boolean getDirectories) throws IOException {
    return crawl(url, getDirectories, this.fileFilter);
  }
  
  public abstract List<Target> crawl(URL url, boolean getDirectories, IOFileFilter fileFilter) throws IOException;
  
}
