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
package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import gov.nasa.pds.tools.validate.TargetExaminer;
import gov.nasa.pds.tools.validate.TargetType;
import gov.nasa.pds.tools.validate.ValidationTarget;

/**
 * Utility class.
 *
 * @author mcayanan
 *
 */
public class Utility {
  private static final Logger LOG = LoggerFactory.getLogger(Utility.class);

  // A static cache of the ValidationTargets.
  // There is no need to re-evaluate and/or create these
  // as validation proceeds, as they are static things like
  // a file or a URL.
  public static HashMap<String, ValidationTarget> cachedTargets;
  static {
    cachedTargets = new HashMap<>();
  }

  // Implementation is needed since pds.nasa.gov currently uses SNI
  // which is not supported in Java 6, but is supported in Java 7.
  static {
    javax.net.ssl.HttpsURLConnection
        .setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
          @Override
          public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
            if (hostname.equals("pds.nasa.gov") && hostname.equals(sslSession.getPeerHost())) {
              return true;
            }
            return false;
          }
        });
  }

  /**
   * Returns a ValidationTarget for the specified target URL.
   *
   * If a cached target already exists in the cache, then that is returned, otherwise a new
   * ValidationTarget is returned.
   *
   */
  public static ValidationTarget getValidationTarget(URL target) {
    if (target == null) {
      // for backwards-compatability with previous code, supporting the null case.
      // This seems to be null in the additional context products case.
      return new ValidationTarget(null);
    }
    ValidationTarget valTarget = cachedTargets.get(target.toString());
    if (valTarget == null) {
      valTarget = new ValidationTarget(target);
      cachedTargets.put(target.toString(), valTarget);
    }
    return valTarget;
  }
  public static ValidationTarget getValidationTarget(URL source, URL label) {
    if (source == null) {
      // for backwards-compatability with previous code, supporting the null case.
      // This seems to be null in the additional context products case.
      return new ValidationTarget(null);
    }
    ValidationTarget valTarget = cachedTargets.get(source.toString());
    if (valTarget == null) {
      valTarget = new ValidationTarget(source, label);
      cachedTargets.put(source.toString(), valTarget);
    }
    return valTarget;
  }

  /**
   * Method that opens a connection. Supports redirects.
   *
   * @param conn URL Connection
   *
   * @return input stream.
   * @throws IOException If an error occurred while opening the stream.
   */
  public static InputStream openConnection(URLConnection conn) throws IOException {
    boolean redir;
    int redirects = 0;
    InputStream in = null;
    do {
      if (conn instanceof HttpURLConnection) {
        ((HttpURLConnection) conn).setInstanceFollowRedirects(false);
      }
      // This code can go away when we move to Java 8
      if (conn instanceof HttpsURLConnection) {
        try {
          SSLContext context = SSLContext.getInstance("TLSv1.2");
          context.init(null, null, new java.security.SecureRandom());
          HttpsURLConnection test = (HttpsURLConnection) conn;
          SSLSocketFactory sf = test.getSSLSocketFactory();
          SSLSocketFactory d = HttpsURLConnection.getDefaultSSLSocketFactory();
          ((HttpsURLConnection) conn).setSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
          throw new IOException(e.getMessage());
        }
      }

      // We want to open the input stream before getting headers
      // because getHeaderField() et al swallow IOExceptions.
      try {
        in = conn.getInputStream();
        redir = false;
        if (conn instanceof HttpURLConnection) {
          HttpURLConnection http = (HttpURLConnection) conn;
          int stat = http.getResponseCode();
          if (stat >= 300 && stat <= 307 && stat != 306
              && stat != HttpURLConnection.HTTP_NOT_MODIFIED) {
            URL base = http.getURL();
            String loc = http.getHeaderField("Location");
            URL target = null;
            if (loc != null) {
              target = new URL(base, loc);
            }
            http.disconnect();
            // Redirection should be allowed only for HTTP and HTTPS
            // and should be limited to 5 redirections at most.
            if (target == null
                || !(target.getProtocol().equals("http") || target.getProtocol().equals("https"))
                || redirects >= 5) {
              throw new SecurityException("illegal URL redirect");
            }
            redir = true;
            conn = target.openConnection();
            redirects++;
          }
        }
      } catch (IOException io) {
        throw new IOException(io.getMessage());
      }
    } while (redir);
    return in;
  }

  public static InputSource getInputSourceByURL(URL url) throws IOException {
    InputSource inputSource = new InputSource(Utility.openConnection(url.openConnection()));
    URI uri = null;
    try {
      uri = url.toURI();
    } catch (URISyntaxException e) {
      // Ignore. Shouldn't happen!
    }
    inputSource.setSystemId(uri.toString());
    return inputSource;
  }

  public static List<URL> toURL(List<String> targets) throws MalformedURLException {
    List<URL> results = new ArrayList<>();
    for (String t : targets) {
      results.add(toURL(t));
    }
    return results;
  }

  public static URL toURL(String target) throws MalformedURLException {
    URL url = null;
    try {
      url = new URL(target);
    } catch (MalformedURLException u) {
      File file = new File(target);
      url = file.toURI().normalize().toURL();
    }
    return url;
  }

  public static boolean isDir(String url) {
    try {
      return isDir(new URL(url));
    } catch (MalformedURLException e) {
      return false;
    }
  }

  public static boolean isDir(URL url) {
    File file = FileUtils.toFile(url);
    LOG.debug("isDir.file: {}, url:{}", file, url);
    LOG.debug("isDir.extension length: {}, url: {}",
        FilenameUtils.getExtension(url.toString()).length(), url);

    if (file == null || !file.exists()) {
      return false;
    }

    return file.isDirectory();
  }

  public static URL getParent(URL url) {
    try {
      return url.toURI().getPath().endsWith("/") ? url.toURI().resolve("..").toURL()
          : url.toURI().resolve(".").toURL();
    } catch (Exception e) {
      return null;
    }
  }

  public static boolean canRead(URL url) {
    try {
      url.openStream().close();
      return true;
    } catch (IOException io) {
      return false;
    }
  }

  public static boolean canRead(String url) {
    try {
      return canRead(new URL(url));
    } catch (MalformedURLException e) {
      return false;
    }
  }

  public static String removeLastSlash(String url) {
    if (url.endsWith("/")) {
      return url.substring(0, url.lastIndexOf("/"));
    }
    return url;
  }

  /**
   * Convenience method for disabling xinclude support throughout the core library.
   * 
   * @return
   */
  public static boolean supportXincludes() {
    return false;
  }

  /**
   * Replace backslashes with forward slashes. (URLs always use forward slashes.)
   *
   * @param sysid The input system identifier.
   * @return The same system identifier with backslashes turned into forward slashes.
   */
  public static String fixSlashes(String sysid) {
    return sysid.replace('\\', '/');
  }

  /**
   * Construct an absolute URI from a relative one, using the current base URI.
   *
   * @param sysid The (possibly relative) system identifier
   * @return The system identifier made absolute with respect to the current {@link #base}.
   * @throws MalformedURLException
   */
  public static String makeAbsolute(String base, String sysid) throws MalformedURLException {
    URL local = null;

    sysid = fixSlashes(sysid);

    try {
      local = new URL(new URL(base), sysid);
      return local.toString();
    } catch (MalformedURLException e) {
      throw new MalformedURLException("Malformed URL on system identifier: " + sysid);
    }
  }

  public static TargetType getTargetType(URL url) {
    if (isDir(url)) {
      return TargetType.DIRECTORY;
    }
    if (TargetExaminer.isTargetBundleType (url, true)) {
      return TargetType.BUNDLE;
    }
    if (TargetExaminer.isTargetCollectionType (url, true)) {
      return TargetType.COLLECTION;
    }
    return TargetType.FILE;
  }
  public static TargetType getTargetType(URL source, URL label) {
    int extIndex = source.getPath().lastIndexOf(".");
    if (isDir(source)) {
      return TargetType.DIRECTORY;
    }
    if (label != null 
        && -1 < extIndex
        && FileUtils.toFile(label).getPath().toLowerCase().endsWith(FileUtils.toFile(source).getPath().substring(extIndex).toLowerCase())) {
      if (TargetExaminer.isTargetBundleType (source, true)) {
        return TargetType.BUNDLE;
      }
      if (TargetExaminer.isTargetCollectionType (source, true)) {
        return TargetType.COLLECTION;
      }
    }
    return TargetType.FILE;
  }
}
