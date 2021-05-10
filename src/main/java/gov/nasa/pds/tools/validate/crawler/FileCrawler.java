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
import java.lang.StringBuilder;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

  private List<Target> refinedFoundList(Collection<File> collections, URL fileUrl, File directory, boolean getDirectories, String nameToken, boolean ignoreCaseFlag) throws IOException {
    // Given a list of file names found, refine the list (ignoring any files that should be ignore).
    // or the file name matching any specified nameToken value, e.g 'bundle' is in bundle_kaguya_derived.xml file
    LOG.debug("refinedFoundList:directory,fileUrl,getDirectories,nameToken,collections.size() {},{},{},{},{}",directory,fileUrl,getDirectories,nameToken,collections.size());
    LOG.debug("refinedFoundList:fileUrl,nameToken,ignoreCaseFlag {},{},{}",fileUrl,nameToken,ignoreCaseFlag);
    List<Target> results = new ArrayList<Target>();

    for (File file : collections) {
      // Keep the file if it contains a token.
      if (nameToken != null) {
         // Compare differently if the flag ignoreCaseFlag is true
         boolean fileNameContainsTokenFlag = false;
         if (ignoreCaseFlag) {
             fileNameContainsTokenFlag = file.getName().toLowerCase().contains(nameToken.toLowerCase());
         } else {
             fileNameContainsTokenFlag = file.getName().contains(nameToken);
         }

         if (fileNameContainsTokenFlag) {
             LOG.debug("refinedFoundList:ADDING_FILE:directory,file,nameToken {},[{}],[{}]",directory,file.getName(),nameToken);
             results.add(new Target(file.toURI().toURL(), false));
         }
      } else {
          LOG.debug("refinedFoundList:ADDING_FILE:directory,file,nameToken {},[{}],[{}]",directory.getName(),file,nameToken);
          results.add(new Target(file.toURI().toURL(), false));
      }
    }

    //Visit sub-directories if the recurse flag is set
    LOG.debug("refinedFoundList:getDirectories {}",getDirectories);
    if (getDirectories) {
      for (File dir :Arrays.asList(directory.listFiles(directoryFilter))) {
        // Keep the file if it contains a token.
        if (nameToken != null) {
         // Compare differently if the flag ignoreCaseFlag is true
         boolean fileNameContainsTokenFlag = false;
         if (ignoreCaseFlag) {
             fileNameContainsTokenFlag = dir.getName().toLowerCase().contains(nameToken.toLowerCase());
         } else {
             fileNameContainsTokenFlag = dir.getName().contains(nameToken);
         }

         if (fileNameContainsTokenFlag) {
             LOG.debug("refinedFoundList:ADDING_FILE:directory,file,nameToken {},[{}],[{}]",directory,dir.getName(),nameToken);
             results.add(new Target(dir.toURI().toURL(), true));
         }
        } else {
          results.add(new Target(dir.toURI().toURL(), true));
        }
      }
    }
    LOG.debug("refinedFoundList:directory,fileUrl,results.size() {},{},{}",directory,fileUrl,results.size());
    for (Target target : results) {
        LOG.debug("refinedFoundList:target: {}",target);
    }
    LOG.debug("refinedFoundList:this.ignoreList.size(),fileUrl {},{}",this.ignoreList.size(),fileUrl);
    for (Target ignoreItem : this.ignoreList) {
        LOG.debug("refinedFoundList:ignoreItem: {}",ignoreItem);
    }

    // Remove all items from results list if they occur in ignoreList.
    results.removeAll(this.ignoreList);

    for (Target target : results) {
        LOG.debug("refinedFoundList:final:target: {}",target.getUrl());
    }

    LOG.debug("refinedFoundList:fileUrl,this.ignoreList.size(),results.size() {},{},{}",fileUrl,this.ignoreList.size(),results.size());

    return results;
  }

  private List<Target> refinedFoundList(Collection<File> collections, URL fileUrl, File directory, boolean getDirectories, String nameToken) throws IOException {
      // If the last parameter ignoreCaseFlag is not provided, make it 'true'.
      return(this.refinedFoundList(collections, fileUrl, directory, getDirectories, nameToken, true));
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
    LOG.debug("crawl:directory,fileUrl,fileFilter,this.fileFilter {},{},{}",directory,fileUrl,fileFilter,this.fileFilter);
    LOG.debug("REGULAR_CRAWL:crawl:directory,fileUrl {},{}",directory,fileUrl);
    //LOG.debug("crawl:this.fileFilter {}",this.fileFilter);
    if ( !directory.isDirectory() ) {
      LOG.error("Input file is not a directory: " + directory);
      throw new IllegalArgumentException("Input file is not a directory: "
          + directory);
    }
    List<Target> results = new ArrayList<Target>();
    //Find files only first
    LOG.debug("crawl:getDirectories {}",getDirectories);
    LOG.debug("crawl:LISTING_FILES:directory,fileFilter,this.fileFilter {},[{}],[{}]",directory,fileFilter,this.fileFilter);

    Collection<File> collections = FileUtils.listFiles(directory, fileFilter, null);
    results = this.refinedFoundList(collections, fileUrl, directory, getDirectories, null);

    return results;
  }

  /**
   * Crawl a given directory url.
   *
   * @param fileUrl File url.
   * @param extensions The file matching file a list of file extensions.
   * @param getDirectories Flag if True will crawl next sub directory.
   * @param nameToken The substring will be searched for in the file names.  Note that the search will be done in all lower cased if ignoreCaseFlag is true.
   * @param ignoreCaseFlag Flag to ignore case when comparing the file name found with the nameToken.
   *
   * @return A list of files and sub-directories (if found and if
   * getSubDirectories flag is 'true').
   * @throws IOException
   */
  public List<Target> crawl(URL fileUrl, String[] extensions, boolean getDirectories, String nameToken, boolean ignoreCaseFlag) throws IOException {
    File directory = FileUtils.toFile(fileUrl);
    LOG.debug("SPECIAL_CRAWL:crawl:directory,fileUrl,extensions,getDirectories,nameToken {},{},{},{},{}",directory,fileUrl,extensions,getDirectories,nameToken);
    LOG.debug("SPECIAL_CRAWL:crawl:fileUrl,extensions,nameToken,ignoreCaseFlag {},{},{},{}",fileUrl,extensions,nameToken,ignoreCaseFlag);
    if ( !directory.isDirectory() ) {
      LOG.error("Input file is not a directory: " + directory);
      throw new IllegalArgumentException("Input file is not a directory: "
          + directory);
    }
    Collection<File> collections = FileUtils.listFiles(directory, extensions, false);
    List<Target> results = this.refinedFoundList(collections, fileUrl, directory, getDirectories, nameToken, ignoreCaseFlag);
    return results;
  }

  /**
   * Crawl a given directory url.
   *
   * @param fileUrl File url.
   * @param extensions The file matching file a list of file extensions.
   * @param getDirectories Flag if True will crawl next sub directory.
   * @param nameToken The substring will be searched for in the file names.  Note that the search will be done in all lower cased.
   *
   * @return A list of files and sub-directories (if found and if
   * getSubDirectories flag is 'true').
   * @throws IOException
   */
  public List<Target> crawl(URL fileUrl, String[] extensions, boolean getDirectories, String nameToken) throws IOException {
      // If the last parameter ignoreCaseFlag is not provided, make it 'true'.
      LOG.debug("SPECIAL_CRAWL_TOP_LEVEL:fileUrl,extensions,nameToken {},{},{}",fileUrl,extensions,nameToken);
      return(this.crawl(fileUrl, extensions, getDirectories, nameToken, true));
  }
}
