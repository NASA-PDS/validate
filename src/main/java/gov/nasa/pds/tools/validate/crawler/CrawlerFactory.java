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

import java.net.URL;

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
   *
   * @return The appropriate Crawler object.
   */
  public static Crawler newInstance(URL url) {
    if ("file".equalsIgnoreCase(url.getProtocol())) {
      return new FileCrawler();
    }
    return new URLCrawler();
  }
}
