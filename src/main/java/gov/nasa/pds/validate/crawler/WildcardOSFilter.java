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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;

/**
 * Filters files using supplied wildcard(s). Based on the Apache WildcardFilter class in the Commons
 * IO package. Difference is that in this class, it uses the
 * org.apache.commons.io.FilenameUtils.wildcardMatchOnSystem() for its matching rules, which means
 * that pattern matching using this class is OS dependent (case-insensitive on Windows and
 * case-sensitive on Unix, Linux, MAC)
 *
 * @author mcayanan
 * @version $Revision$
 *
 */
public class WildcardOSFilter extends AbstractFileFilter {

  /** A list of wildcard patterns. */
  private List<String> wildcards = null;

  /**
   * Constructor for a single wildcard.
   *
   * @param wc a single filter to set
   */
  public WildcardOSFilter(String wc) {
    if (wc == null) {
      throw new NullPointerException();
    }

    this.wildcards = new ArrayList<>();
    this.wildcards.add(wc);
  }

  /**
   * Returns list of filters that were set.
   *
   * @return a list of filters
   */
  public List<String> getWildcards() {
    return wildcards;
  }

  /**
   * Constructor for a list of wildcards.
   *
   * @param wc a list of filters to set.
   */
  public WildcardOSFilter(List<String> wc) {
    if (wc == null) {
      throw new NullPointerException();
    }

    this.wildcards = new ArrayList<>();
    this.wildcards.addAll(wc);
  }

  /**
   * Checks to see if the filename matches one of the wildcards. Matching is case-insensitive for
   * Windows and case-sensitive for Unix.
   *
   * @param file the file to check.
   *
   * @return true if the filename matches one of the wildcards.
   */

  @Override
  public boolean accept(File file) {
    if (file == null) {
      throw new NullPointerException("No file specified");
    }
    for (Iterator<String> i = wildcards.iterator(); i.hasNext();) {
      if (FilenameUtils.wildcardMatchOnSystem(file.getName(), i.next())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks to see if the filename matches one of the wildcards. Matching is case-insensitive for
   * Windows and case-sensitive for Unix.
   *
   * @param dir the directory to check.
   * @param name the file name within the directory to check.
   *
   * @return true if the filename matches one of the wildcards, false otherwise.
   */
  @Override
  public boolean accept(File dir, String name) {
    return accept(new File(dir, name));
  }

}
