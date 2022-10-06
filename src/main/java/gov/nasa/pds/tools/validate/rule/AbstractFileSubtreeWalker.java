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

import java.net.URL;
import java.util.List;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;

/**
 * Implements a class that walks a file system subtree and performs an operation on each file or
 * directory found.
 *
 * @param <T> the type used by the concrete subclass to keep track of state
 */
public abstract class AbstractFileSubtreeWalker<T> {

  /**
   * Walks a subtree starting from a specified file or directory.
   *
   * @param f a file or directory
   * @param state the state managed by the caller
   */
  public void walkSubtree(URL url, T state) {
    if (!Utility.isDir(url)) {
      try {
        handleFile(url, state);
      } catch (Exception e) {
        // TODO Log the exception
        e.printStackTrace();
      }
    } else {
      T childState;
      try {
        childState = handleDirectory(url, state);
        Crawler crawler = CrawlerFactory.newInstance(url);
        List<Target> children = crawler.crawl(url);
        for (Target child : children) {
          walkSubtree(child.getUrl(), childState);
        }
      } catch (Exception e) {
        // TODO log the exception
        e.printStackTrace();
      }
    }
  }

  /**
   * Handles encountering a directory. Subclasses should implement the processing for directories in
   * this method.
   *
   * @param dir the directory encountered
   * @param state the current walking state
   * @throws Exception if there is an error processing the directory
   * @return the new state for walking children of this directory
   */
  protected abstract T handleDirectory(URL dir, T state) throws Exception;

  /**
   * Handles encountering a file. Subclasses should implement the processing for files in this
   * method.
   *
   * @param f the file encountered
   * @param state the current walking state
   * @throws Exception if there is an error processing the file
   */
  protected abstract void handleFile(URL f, T state) throws Exception;

}
