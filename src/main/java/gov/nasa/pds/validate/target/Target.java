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

package gov.nasa.pds.validate.target;

import java.net.URISyntaxException;
import java.net.URL;

public class Target {
  private URL url;

  private boolean isDir;

  public Target (URL url, boolean isDir) {
    this.url = url;
    this.isDir = isDir;
  }

  public URL getUrl() {
    return url;
  }

  public boolean isDir() {
    return isDir;
  }

  public String toString() {
    try {
      return url.toURI().toString();
    } catch (URISyntaxException e) {
      return url.toString();
    }
  }

  public boolean equals(Object obj) {
    Target otherTarget = (Target) obj;
    if ( (this.url.equals(otherTarget.getUrl())) &&
        (this.isDir == otherTarget.isDir()) ) {
      return true;
    } else {
      return false;
    }
  }

  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (null == url ? 0 : url.hashCode());
    hash = 31 * hash + (isDir ? 0 : 1);
    return hash;
  }

}
