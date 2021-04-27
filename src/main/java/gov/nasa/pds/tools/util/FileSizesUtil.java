package gov.nasa.pds.tools.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.IOException;

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

      LOG.debug("getExternalFilesize:url {}",url);

      if (url.getProtocol().equals("file")) {
            long fileSize = -1;
            LOG.debug("getExternalFilesize:url,fileSize {}",url,fileSize);
            File file = new File(url.getPath());
            fileSize = file.length();
           LOG.debug("getExternalFilesize:url,fileSize {}",url,fileSize);
            return(fileSize);
      } else {
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
}
