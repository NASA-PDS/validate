package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that calculates the filesize of a file in bytes.
 */
public class FileSizesUtil {

  private static final Logger LOG = LoggerFactory.getLogger(FileSizesUtil.class);

  /**
   * Gets the filesize value.
   *
   * @param url The url to the file or resource.
   * @return The filesize of the given filename.
   *
   * @throws Exception If an error occurred while calculating the checksum.
   */
  public static long getExternalFilesize(URL url) throws Exception {
    // If the protocol is "file" use special trick to get the file size.
    // Normal usage of URLConnection fail if the file is too big.

    LOG.debug("getExternalFilesize:url {}", url);

    if (url.getProtocol().equals("file")) {
      long fileSize = -1;
      LOG.debug("getExternalFilesize:url,fileSize {},{}", url, fileSize);
      LOG.debug("getExternalFilesize:afor:path [{}]", url.getPath());
      // For some strange reason, the File class does not handle well with the "%20"
      // in the name. It needs to be an actual space.
      String actualPath = URLDecoder.decode(url.getPath(), "UTF-8"); // Use the URLDecoder.decode()
                                                                     // to convert
                                                                     // from %20 to " ".
      LOG.debug("getExternalFilesize:after:path [{}]", actualPath);
      File file = new File(actualPath);
      if (!file.exists()) {
        LOG.error("getExternalFilesize:Cannot find file [{}]", actualPath);
      } else {
        LOG.debug("getExternalFilesize:File exist [{}]", actualPath);
      }
      fileSize = file.length();
      LOG.debug("getExternalFilesize:url,fileSize {},{}", url, fileSize);
      return (fileSize);
    }
    URLConnection conn = null;
    try {
      conn = url.openConnection();
      if (conn instanceof HttpURLConnection) {
        ((HttpURLConnection) conn).setRequestMethod("HEAD");
      }
      conn.getInputStream();
      return conn.getContentLength();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn instanceof HttpURLConnection) {
        ((HttpURLConnection) conn).disconnect();
      }
    }
  }
}
