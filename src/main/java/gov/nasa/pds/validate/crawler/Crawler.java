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

import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import gov.nasa.pds.tools.validate.Target;

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
