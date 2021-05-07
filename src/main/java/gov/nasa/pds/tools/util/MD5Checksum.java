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

import java.net.URL;
import java.security.MessageDigest;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that calculates the MD5 checksum of a file.
 *
 * @author mcayanan
 *
 */
public class MD5Checksum {
  private static final Logger LOG = LoggerFactory.getLogger(MD5Checksum.class);
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
    LOG.debug("getMD5Checksum:url,getHex {},{}",url,getHex(b));
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
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        int bytesRead = 0;

        // Use RandomAccessFile to get filesize larger than 2gb
        File aFile = new File(url.toURI());
        RandomAccessFile raf = new RandomAccessFile(aFile, "r");
        raf.seek(0); // Move the pointer to the beginning.

        FileChannel inChannel = raf.getChannel();
        int bufferSize = 10*1024;   // The buffer size does affect the performance when the file is large.  This has been found to be optimal.

        if (bufferSize > inChannel.size()) {
            bufferSize = (int) inChannel.size();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        try {
            // https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.html#read(java.nio.ByteBuffer)
            // According to documentation, the number of bytes read can be 0 or -1 if reached end of stream.
            boolean zeroBytesReadFlag = false;  // Will allow graceful loop exit if the file is empty or reached end of stream.
            do {
                bytesRead = inChannel.read(byteBuffer);
                LOG.debug("createChecksum:url,bytesRead {},{}",url,bytesRead);
                if (bytesRead > 0) {
                    // If desired to do something with byteBuffer, the pointer must be re-positioned to 0.
                    // For example convert from byte to string.  Code left in comment for education.
                    // If the pointer is not repositioned, printing out the buffer will shows an empty array.
                    //byteBuffer.position(0);
                    md5.update(byteBuffer.array(), 0, bytesRead);
                    ((Buffer) byteBuffer).clear();  // This must be done to receive new content and reposition the pointer to the beginning.
                } else {
                    // If the file is empty, the number of bytes read will be zero.  This flag will allow to code to exit an infinite loop.
                    // Also if the channel reached end of stream, the returned value can also be -1.
                    zeroBytesReadFlag = true;
                }
            } while (zeroBytesReadFlag == false);
            raf.close();
            return md5.digest();
        } catch (Exception ex) {
            LOG.error("Cannot create MD5 checksum for url {} with error message: {}",url,ex.getMessage());
            raf.close();
        }
        return md5.digest();
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
