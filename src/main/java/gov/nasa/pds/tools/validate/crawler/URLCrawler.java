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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import gov.nasa.pds.tools.validate.Target;

/**
 * Class to crawl a resource.
 *
 * @author mcayanan
 *
 */
public class URLCrawler extends Crawler {

  public URLCrawler() {
    super();
  }

  @Override
  /**
   * Crawl the given url.
   *
   * @param url The directory url.
   *
   * @return A list of files and directories that were found.
   *
   * @throws IOException
   */
  public List<Target> crawl(URL url, boolean getDirectories, IOFileFilter fileFilter)
      throws IOException {
    try {
      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(null, null, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    } catch (Exception e) {
      throw new IOException(
          "Error while setting SSLSocket connection to TLSv1.2: " + e.getMessage());
    }
    Document doc = Jsoup.connect(url.toString()).get();
    Set<Target> results = new LinkedHashSet<>();
    for (Element file : doc.select("a")) {
      String value = file.attr("abs:href");
      // Check if the given url is a subset of the href value. If it is,
      // assume it is a file or a directory we will need to process.
      if (value.contains(url.toString())) {
        // Check if the value has an extension. If so, it is most likely a file
        if (!FilenameUtils.getExtension(value).isEmpty()) {
          if (fileFilter.accept(new File(value))) {
            results.add(new Target(new URL(value), false));
          }
        } else // Assume that any href values found that contain a '?' or '#' are
        // links to things other than files and directories. So we can skip
        // over them.
        if (getDirectories && value.indexOf('#') == -1 && value.indexOf('?') == -1) {
          URL absHref = new URL(value);
          String parentUrl = new File(url.getFile()).getParent();
          String parentHref = new File(absHref.getFile()).toString();
          // Check to see if the directory value is a link to the parent
          if (!parentUrl.equalsIgnoreCase(parentHref)) {
            results.add(new Target(absHref, true));
          }
        }
      }
    }
    return new ArrayList<>(results);
  }
}
