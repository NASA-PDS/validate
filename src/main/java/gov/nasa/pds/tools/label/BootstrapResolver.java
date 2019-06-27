//	Copyright 2009-2010, by the California Institute of Technology.
//	ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//	Any commercial use must be negotiated with the Office of Technology 
//	Transfer at the California Institute of Technology.
//	
//	This software is subject to U. S. export control laws and regulations 
//	(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
//	is subject to U.S. export control laws and regulations, the recipient has 
//	the responsibility to obtain export licenses or other export authority as 
//	may be required before exporting such information to foreign countries or 
//	providing access to foreign nationals.
//	
//	$Id$
//

package gov.nasa.pds.tools.label;

import java.util.Hashtable;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.TransformerException;

import org.xml.sax.InputSource;

/**
 * @author pramirez
 * 
 */
public class BootstrapResolver extends
    org.apache.xml.resolver.helpers.BootstrapResolver {
  /** URI of the W3C XML Schema for OASIS XML Catalog files. */
  public static final String xmlCatalogXSD = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.xsd";

  /** URI of the RELAX NG Grammar for OASIS XML Catalog files. */
  public static final String xmlCatalogRNG = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.rng";

  /** Public identifier for OASIS XML Catalog files. */
  public static final String xmlCatalogPubId = "-//OASIS//DTD XML Catalogs V1.0//EN";

  /** System identifier for OASIS XML Catalog files. */
  public static final String xmlCatalogSysId = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd";

  /** Public identifier for OASIS XML Catalog 1.1 files. */
  public static final String xmlCatalog11PubId = "-//OASIS//DTD XML Catalogs V1.1//EN";

  /** System identifier for OASIS XML Catalog 1.1 files. */
  public static final String xmlCatalog11SysId = "http://www.oasis-open.org/committees/entity/release/1.1/catalog.dtd";

  /**
   * Public identifier for legacy Apache XCatalog files. There is no official
   * system identifier for XCatalog files.
   */
  public static final String xCatalogPubId = "-//DTD XCatalog//EN";

  /** Private hash used for public identifiers. */
  private Hashtable publicMap = new Hashtable();

  /** Private hash used for system identifiers. */
  private Hashtable systemMap = new Hashtable();

  /** Private hash used for URIs. */
  private Hashtable uriMap = new Hashtable();

  public BootstrapResolver() {
    URL url = this.getClass().getResource(
        "/org/apache/xml/resolver/etc/catalog.dtd");
    if (url != null) {
      publicMap.put(xmlCatalogPubId, url.toString());
      systemMap.put(xmlCatalogSysId, url.toString());
    }

    url = this.getClass().getResource(
        "/org/apache/xml/resolver/etc/catalog.rng");
    if (url != null) {
      uriMap.put(xmlCatalogRNG, url.toString());
    }

    url = this.getClass().getResource(
        "/org/apache/xml/resolver/etc/catalog.xsd");
    if (url != null) {
      uriMap.put(xmlCatalogXSD, url.toString());
    }

    url = this.getClass().getResource(
        "/org/apache/xml/resolver/etc/xcatalog.dtd");
    if (url != null) {
      publicMap.put(xCatalogPubId, url.toString());
    }

    url = this.getClass().getResource("/catalog11.dtd");
    if (url != null) {
      publicMap.put(xmlCatalog11PubId, url.toString());
      systemMap.put(xmlCatalog11SysId, url.toString());
    }
  }

  /** SAX resolveEntity API. */
  public InputSource resolveEntity(String publicId, String systemId) {
    String resolved = null;

    if (systemId != null && systemMap.containsKey(systemId)) {
      resolved = (String) systemMap.get(systemId);
    } else if (publicId != null && publicMap.containsKey(publicId)) {
      resolved = (String) publicMap.get(publicId);
    }

    if (resolved != null) {
      try {
        InputSource iSource = new InputSource(resolved);
        iSource.setPublicId(publicId);

        // Ideally this method would not attempt to open the
        // InputStream, but there is a bug (in Xerces, at least)
        // that causes the parser to mistakenly open the wrong
        // system identifier if the returned InputSource does
        // not have a byteStream.
        //
        // It could be argued that we still shouldn't do this here,
        // but since the purpose of calling the entityResolver is
        // almost certainly to open the input stream, it seems to
        // do little harm.
        //
        URL url = new URL(resolved);
        InputStream iStream = url.openStream();
        iSource.setByteStream(iStream);

        return iSource;
      } catch (Exception e) {
        // FIXME: silently fail?
        return null;
      }
    }

    return null;
  }

  /** Transformer resolve API. */
  public Source resolve(String href, String base) throws TransformerException {

    String uri = href;
    int hashPos = href.indexOf("#");
    if (hashPos >= 0) {
      uri = href.substring(0, hashPos);
    }

    String result = null;
    if (href != null && uriMap.containsKey(href)) {
      result = (String) uriMap.get(href);
    }

    if (result == null) {
      try {
        URL url = null;

        if (base == null) {
          url = new URL(uri);
          result = url.toString();
        } else {
          URL baseURL = new URL(base);
          url = (href.length() == 0 ? baseURL : new URL(baseURL, uri));
          result = url.toString();
        }
      } catch (java.net.MalformedURLException mue) {
        // try to make an absolute URI from the current base
        String absBase = makeAbsolute(base);
        if (!absBase.equals(base)) {
          // don't bother if the absBase isn't different!
          return resolve(href, absBase);
        } else {
          throw new TransformerException("Malformed URL " + href + "(base "
              + base + ")", mue);
        }
      }
    }

    SAXSource source = new SAXSource();
    source.setInputSource(new InputSource(result));
    return source;
  }

  /** Attempt to construct an absolute URI */
  private String makeAbsolute(String uri) {
    if (uri == null) {
      uri = "";
    }

    try {
      URL url = new URL(uri);
      return url.toString();
    } catch (MalformedURLException mue) {
      try {
        URL fileURL = makeURL(uri);
        return fileURL.toString();
      } catch (MalformedURLException mue2) {
        // bail
        return uri;
      }
    }
  }
  
  public static URL makeURL(String pathname) throws MalformedURLException {
    if (pathname.startsWith("/")) {
      return new URL("file://" + pathname);
    }

    String userdir = System.getProperty("user.dir");
    userdir = userdir.replace('\\', '/');

    if (userdir.endsWith("/")) {
      return new URL("file:///" + userdir + pathname);
    } else {
      return new URL("file:///" + userdir + "/" + pathname);
    }
  }
  
}
