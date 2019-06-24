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

package gov.nasa.pds.validate.util;

import gov.nasa.pds.validate.target.Target;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Utility class.
 *
 * @author mcayanan
 *
 */
public class Utility {
  /**
   * Removes quotes within a list of strings.
   *
   * @param list A list of strings.
   * @return A list with the quotes removed.
   */
  public static List<String> removeQuotes(List<String> list) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, list.get(i).toString().replace('"', ' ').trim());
    }
    return list;
  }

  public static String toStringNoBraces(JsonObject json) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    StringBuilder string = new StringBuilder(gson.toJson(json));
    string = string.replace(0, 1,"");
    string = string.replace(string.lastIndexOf("}"), string.lastIndexOf("}")+1, "");
    return string.toString().trim();
  }

  public static URL toURL (String target) throws MalformedURLException {
    URL url = null;
    try {
      url = new URL(target);
    } catch (MalformedURLException u) {
      File file = new File(target);
      url = file.toURI().normalize().toURL();
    }
    return url;
  }

  public static Target toTarget(URL target) {
    Target result = null;
    if (target.getProtocol().equalsIgnoreCase("file")) {
      File file = FileUtils.toFile(target);
      if (file.isDirectory()) {
        result = new Target(target, true);
      } else {
        result = new Target(target, false);
      }
    } else if ("".equals(FilenameUtils.getExtension(target.toString()))) {
      result = new Target(target, true);
    } else {
      result = new Target(target, false);
    }
    return result;
  }
}
