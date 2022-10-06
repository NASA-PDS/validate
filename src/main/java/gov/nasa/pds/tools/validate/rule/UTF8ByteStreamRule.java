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
package gov.nasa.pds.tools.validate.rule;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import gov.nasa.pds.tools.util.Utility;

/**
 * Tests that a file has only legal UTF-8 byte sequences.
 */
public class UTF8ByteStreamRule extends AbstractValidationRule {

  @ValidationTest
  public void testUTF8ByteStream() {
    BufferedInputStream in = null;

    try {
      in = new BufferedInputStream(getTarget().openStream());
      checkUTF8ByteStream(in);
    } catch (IOException e) {
      reportError(GenericProblems.MALFORMED_UTF8_CHARACTER, getTarget(), -1, -1);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ex) {
          // Ignore - no way to recover
        }
      }
    }
  }

  /**
   * Checks that an input stream only has properly formed UTF-8 byte sequences.
   *
   * @param in the input stream
   * @throws IOException if there is an error while reading the input stream or if there is an
   *         invalid byte sequence
   */
  private void checkUTF8ByteStream(BufferedInputStream in) throws IOException {
    int c;

    for (;;) {
      c = in.read();
      if (c < 0) {
        break;
      }

      // If this is an extension character, it is in an invalid spot.
      // Otherwise, if it's the start of a multi-byte sequence, check
      // that we have the right number of extension characters.
      if ((c & 0xC0) == 0x80) {
        // Extension character not at start of multi-byte sequence.
        throw new IOException("Invalid UTF-8 byte sequence");
      }
      if ((c & 0xE0) == 0xC0) {
        checkExtensions(in, 1);
      } else if ((c & 0xF0) == 0xE0) {
        checkExtensions(in, 2);
      } else if ((c & 0xF8) == 0xF0) {
        checkExtensions(in, 3);
      } else if ((c & 0xFC) == 0xF8) {
        checkExtensions(in, 4);
      } else if ((c & 0xFE) == 0xFC) {
        checkExtensions(in, 5);
      } else if (c > 0x7F) {
        throw new IOException("Invalid UTF-8 byte sequence");
      }
    }
  }

  /**
   * Checks that we have the right number of extension characters in a multi-byte sequence.
   *
   * @param in the input stream
   * @param n the number of extension characters expected
   * @throws IOException if an exception occurs while reading the input or if there is an invalid
   *         byte sequence
   */
  private void checkExtensions(BufferedInputStream in, int n) throws IOException {
    for (int i = 0; i < n; ++i) {
      int c = in.read();
      if (c < 0 || (c & 0xC0) != 0x80) {
        throw new IOException("Invalid UTF-8 byte sequence");
      }
    }
  }

  @Override
  public boolean isApplicable(String location) {
    URL url;
    try {
      url = new URL(location);
      boolean isFile = Utility.isDir(url);
      boolean canRead = true;
      try {
        url.openStream().close();
      } catch (IOException io) {
        canRead = false;
      }
      return isFile && canRead;
    } catch (MalformedURLException e) {
      return false;
    }
  }

}
