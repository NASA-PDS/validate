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

import java.net.URL;
import java.util.List;

/**
 * Factory class to instantiate the different Crawler objects.
 *
 * @author mcayanan
 *
 */
public class CrawlerFactory {

  /**
   * Creates the appropriate Crawler object based on the given inputs.
   *
   * @param url The url.
   * @param getDirectories If the crawler should retrieve directory listings.
   * @param fileFilters File filters to use when retrieving file listings.
   *
   * @return The appropriate Crawler object.
   */
  public static Crawler newInstance(URL url, boolean getDirectories,
      List<String> fileFilters) {
    if ("file".equalsIgnoreCase(url.getProtocol())) {
      return new FileCrawler(getDirectories, fileFilters);
    } else {
      return new URLCrawler(getDirectories, fileFilters);
    }
  }
}
