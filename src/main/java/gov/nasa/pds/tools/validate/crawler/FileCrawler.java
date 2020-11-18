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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that crawls a given file url.
 *
 * @author mcayanan
 *
 */
public class FileCrawler extends Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(FileCrawler.class);

    public FileCrawler() {
      super();
    }

  /**
   * Crawl a given directory url.
   *
   * @param fileUrl File url.
   *
   * @return A list of files and sub-directories (if found and if
   * getSubDirectories flag is 'true').
   * @throws IOException
   */
  public List<Target> crawl(URL fileUrl, boolean getDirectories, IOFileFilter fileFilter) throws IOException {
    File directory = FileUtils.toFile(fileUrl);
    //LOG.debug("crawl:directory,fileUrl,fileFilter,this.fileFilter {},{},{}",directory,fileUrl,fileFilter,this.fileFilter);
    //LOG.debug("crawl:this.fileFilter {}",this.fileFilter);
    if ( !directory.isDirectory() ) {
      LOG.error("Input file is not a directory: " + directory);
      throw new IllegalArgumentException("Input file is not a directory: "
          + directory);
    }
    List<Target> results = new ArrayList<Target>();
    //Find files only first
    for (File file : FileUtils.listFiles(directory, fileFilter, null)) {
      results.add(new Target(file.toURI().toURL(), false));
    }
    //Visit sub-directories if the recurse flag is set
    if (getDirectories) {
      for (File dir :Arrays.asList(directory.listFiles(directoryFilter))) {
        results.add(new Target(dir.toURI().toURL(), true));
      }
    }
    //LOG.debug("crawl:directory,fileUrl,results.size() {},{},{}",directory,fileUrl,results.size());
    for (Target target : results) {
        //LOG.debug("crawl:target: {}",target);
    }
    //LOG.debug("crawl:this.ignoreList.size(),fileUrl {},{}",this.ignoreList.size(),fileUrl);
    for (Target ignoreItem : this.ignoreList) {
        //LOG.debug("crawl:ignoreItem: {}",ignoreItem);
    }

    // Remove all items from results list if they occur in ignoreList.
    results.removeAll(this.ignoreList);

    for (Target target : results) {
        //LOG.debug("crawl:final:target: {}",target.getUrl());
    }

    //LOG.debug("crawl:fileUrl,this.ignoreList.size(),results.size() {},{},{}",fileUrl,this.ignoreList.size(),results.size());

    return results;
  }
}
