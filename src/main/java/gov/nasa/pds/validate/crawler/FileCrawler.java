// Copyright © 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import gov.nasa.pds.tools.validate.Target;

/**
 * Class that crawls a given file url.
 *
 * @author mcayanan
 *
 */
public class FileCrawler extends Crawler {

  public FileCrawler(boolean getDirectories, List<String> fileFilters) {
    super(getDirectories, fileFilters);
  }

  /**
   * Crawl a given directory url.
   *
   * @param fileUrl File url.
   *
   * @return A list of files and sub-directories (if found and if getSubDirectories flag is 'true').
   * @throws IOException
   */
  @Override
  public List<Target> crawl(URL fileUrl) throws IOException {
    File directory = FileUtils.toFile(fileUrl);
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("Input file is not a directory: " + directory);
    }
    List<Target> results = new ArrayList<>();
    // Find files only first
    for (File file : FileUtils.listFiles(directory, fileFilter, null)) {
      results.add(new Target(file.toURI().toURL(), false));
    }
    // Visit sub-directories if the recurse flag is set
    if (getDirectories) {
      for (File dir : Arrays.asList(directory.listFiles(directoryFilter))) {
        results.add(new Target(dir.toURI().toURL(), true));
      }
    }
    return results;
  }
}
