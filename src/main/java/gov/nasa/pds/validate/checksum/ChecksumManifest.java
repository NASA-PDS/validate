// Copyright 2006-2015, by the California Institute of Technology.
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

package gov.nasa.pds.validate.checksum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;

/**
 * Class that reads a checksum manifest file.
 *
 * @author mcayanan
 *
 */
public class ChecksumManifest {
  private URL baseUrl;

  public ChecksumManifest(String baseUrl)
      throws MalformedURLException {
    if (!baseUrl.endsWith("/")) {
      this.baseUrl = new URL(baseUrl + "/");
    } else {
      this.baseUrl = new URL(baseUrl);
    }
  }

  /**
   * Reads a checksum manifest file.
   *
   * @param manifest The checksum manifest.
   *
   * @return A hash map of file paths(absolute or relative) to checksum values.
   *
   * @throws IOException If there was an error reading the checksum manifest.
   */
  public HashMap<URL, String> read(URL manifest)
  throws IOException {
    HashMap<URL, String> checksums = new HashMap<URL, String>();
    LineNumberReader reader = new LineNumberReader(new BufferedReader(
        new InputStreamReader(manifest.openStream())));
    String line = "";
    try {
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.equals("")) {
          continue;
        }
        String[] tokens = line.split("\\s{1,2}", 2);
        URL url = new URL(baseUrl, FilenameUtils.separatorsToUnix(tokens[1]));
        checksums.put(url, tokens[0]);
      }
    } catch (ArrayIndexOutOfBoundsException ae) {
      throw new IOException("line " + reader.getLineNumber()
          + ": Could not tokenize '" + line + "': " + ae.getMessage());
    } finally {
      reader.close();
    }
    return checksums;
  }
}
