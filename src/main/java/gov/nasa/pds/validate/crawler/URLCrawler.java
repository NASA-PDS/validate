// Copyright © 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
//   conditions and the following disclaimer in the documentation and/or other
//   materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
//   Laboratory, nor the names of its contributors may be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.validate.crawler;

import gov.nasa.pds.tools.validate.Target;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Class to crawl a resource.
 *
 * @author mcayanan
 *
 */
public class URLCrawler extends Crawler {

  /**
   * Constructor.
   *
   * @param getDirectories Flag to indicate whether to retrieve directory
   *  listings.
   * @param fileFilters A list of file filters to use when traversing a
   * directory url.
   */
  public URLCrawler(boolean getDirectories, List<String> fileFilters) {
    super(getDirectories, fileFilters);
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
  public List<Target> crawl(URL url) throws IOException {
    Document doc = Jsoup.connect(url.toString()).get();
    Set<Target> results = new LinkedHashSet<Target>();
    for (Element file : doc.select("a")) {
      String value = file.attr("abs:href");
      // Check if the given url is a subset of the href value. If it is,
      // assume it is a file or a directory we will need to process.
      if (value.contains(url.toString())) {
        //Check if the value has a 3-character extension. If so, it is most likely a file
        if (FilenameUtils.getExtension(value).length() == 3) {
          if (fileFilter.accept(new File(value))) {
            results.add(new Target(new URL(value), false));
          }
        } else {
          //Assume that any href values found that contain a '?' or '#' are
          //links to things other than files and directories. So we can skip
          //over them.
          if (getDirectories &&
              value.indexOf('#') == -1 &&
              value.indexOf('?') == -1) {
            URL absHref = new URL(value);
            String parentUrl = new File(url.getFile()).getParent();
            String parentHref = new File(absHref.getFile()).toString();
            //Check to see if the directory value is a link to the parent
            if (!parentUrl.equalsIgnoreCase(parentHref)) {
              results.add(new Target(absHref, true));
            }
          }
        }
      }
    }
    return new ArrayList<Target>(results);
  }
}
