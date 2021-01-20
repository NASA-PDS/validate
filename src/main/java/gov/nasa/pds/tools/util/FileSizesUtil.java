package gov.nasa.pds.tools.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;

/**
 * A class that calculates the filesize of a file in bytes.
 */
public class FileSizesUtil {

  /**
   * Gets the filesize value.
   *
   * @param url The url to the file or resource.
   * @return The filesize of the given filename.
   *
   * @throws Exception If an error occurred while calculating the checksum.
   */
  public static int getExternalFilesize(URL url) throws Exception {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
  }
}
