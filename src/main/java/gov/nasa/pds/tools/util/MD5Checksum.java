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

package gov.nasa.pds.tools.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

/**
 * A class that calculates the MD5 checksum of a file.
 *
 * @author mcayanan
 *
 */
public class MD5Checksum {

  /** HEX values. */
  private static final String HEXES = "0123456789abcdef";


  /**
   * Gets the MD5 checksum value.
   *
   * @param url The url to the file or resource.
   * @return The MD5 checksum of the given filename.
   *
   * @throws Exception If an error occurred while calculating the checksum.
   */
  public static String getMD5Checksum(URL url) throws Exception {
    byte[] b = createChecksum(url);
    return getHex(b);
  }

  /**
   * Creates the checksum.
   *
   * @param url The url to the file or resource.
   *
   * @return a byte array of the checksum.
   *
   * @throws Exception If an error occurred while calculating the checksum.
   */
  private static byte[] createChecksum(URL url) throws Exception {
    BufferedInputStream input = null;
    try {
      InputStream stream =  Utility.openConnection(url.openConnection());
      input = new BufferedInputStream(stream);
      byte[] buffer = new byte[1024];
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      int bytesRead = 0;
      do {
        bytesRead = input.read(buffer);
        if (bytesRead > 0) {
          md5.update(buffer, 0, bytesRead);
        }
      } while (bytesRead != -1);
      return md5.digest();
    } finally {
      input.close();
    }
  }

  /**
   * Gets the HEX equivalent of the given byte array.
   *
   * @param bytes The bytes to convert.
   *
   * @return The HEX value of the given byte array.
   */
  private static String getHex(byte [] bytes) {
    if (bytes == null) {
      return null;
    }
    final StringBuilder hex = new StringBuilder(2 * bytes.length);
    for (byte b : bytes ) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4))
      .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
  }
}
