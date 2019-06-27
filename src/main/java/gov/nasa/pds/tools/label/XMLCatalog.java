//  Copyright 2009-2017, by the California Institute of Technology.
//  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//  Any commercial use must be negotiated with the Office of Technology 
//  Transfer at the California Institute of Technology.
//  
//  This software is subject to U. S. export control laws and regulations 
//  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
//  is subject to U.S. export control laws and regulations, the recipient has 
//  the responsibility to obtain export licenses or other export authority as 
//  may be required before exporting such information to foreign countries or 
//  providing access to foreign nationals.
//  
//  $Id$
//
package gov.nasa.pds.tools.label;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.DataInputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.readers.CatalogReader;
import org.apache.xml.resolver.helpers.FileURL;


/**
 * Class that extends the Catalog class found in the Apache XML-Commons library.
 *  
 * @author mcayanan
 *
 */
public class XMLCatalog extends Catalog {
  /**
   * Constructs an empty Catalog.
   *
   * <p>The constructor interrogates the relevant system properties
   * using the default (static) CatalogManager
   * and initializes the catalog data structures.</p>
   */
  public XMLCatalog() {
    // nop;
  }

  /**
   * Constructs an empty Catalog with a specific CatalogManager.
   *
   * <p>The constructor interrogates the relevant system properties
   * using the specified Catalog Manager
   * and initializes the catalog data structures.</p>
   */
  public XMLCatalog(CatalogManager manager) {
    catalogManager = manager;
  }

  /**
   * Setup readers.
   */
  /*
  public void setupReaders() {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);
    spf.setValidating(false);

    SAXCatalogReader saxReader = new SAXCatalogReader(spf);

    saxReader.setCatalogParser(null, "XCatalog",
			       "org.apache.xml.resolver.readers.XCatalogReader");

    saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName,
			       "catalog",
			       "gov.nasa.pds.tools.label.OASISXMLCatalogReader");

    addReader("application/xml", saxReader);

    TR9401CatalogReader textReader = new TR9401CatalogReader();
    addReader("text/plain", textReader);
  }
*/

  /**
   * Parse a single catalog file, augmenting internal data structures.
   *
   * @param fileName The filename of the catalog file to process
   *
   * @throws MalformedURLException The fileName cannot be turned into
   * a valid URL.
   * @throws IOException Error reading catalog file.
   */
  protected synchronized void parseCatalogFile(String fileName)
    throws MalformedURLException, IOException, CatalogException {

    CatalogEntry entry;

    // The base-base is the cwd. If the catalog file is specified
    // with a relative path, this assures that it gets resolved
    // properly...
    try {
      // tack on a basename because URLs point to files not dirs
      catalogCwd = FileURL.makeURL("basename");
    } catch (MalformedURLException e) {
      String userdir = System.getProperty("user.dir");
      userdir = userdir.replace('\\', '/');
      catalogManager.debug.message(1, "Malformed URL on cwd", userdir);
      catalogCwd = null;
    }

    // The initial base URI is the location of the catalog file
    try {
      base = new URL(catalogCwd, fixSlashes(fileName));
    } catch (MalformedURLException e) {
      try {
        // The commented out code comes from the parent class implementation.
        // However, this does not work when dealing with Windows paths.
        
        //base = new URL("file:" + fixSlashes(fileName));
        base = new File(fixSlashes(fileName)).toURI().toURL();
      } catch (MalformedURLException e2) {
        catalogManager.debug.message(1, "Malformed URL on catalog filename",
		      fixSlashes(fileName));
        base = null;
      }
    }

    catalogManager.debug.message(2, "Loading catalog", fileName);
    catalogManager.debug.message(4, "Default BASE", base.toString());

    fileName = base.toString();

    DataInputStream inStream = null;
    boolean parsed = false;
    boolean notFound = false;

    for (int count = 0; !parsed && count < readerArr.size(); count++) {
      CatalogReader reader = (CatalogReader) readerArr.get(count);

      try {
        notFound = false;
        inStream = new DataInputStream(base.openStream());
      } catch (FileNotFoundException fnfe) {
        // No catalog; give up!
        notFound = true;
        break;
      }

      try {
        reader.readCatalog(this, inStream);
        parsed = true;
      } catch (CatalogException ce) {
        if (ce.getExceptionType() == CatalogException.PARSE_FAILED) {
          // give up!
          break;
        } else {
          // try again!
        }
      }

      try {
        inStream.close();
      } catch (IOException e) {
        //nop
      }
    }

    if (!parsed) {
      if (notFound) {
        catalogManager.debug.message(3, "Catalog does not exist", fileName);
        throw new IOException("Catalog does not exist: " + fileName);
      } else {
        catalogManager.debug.message(1, "Failed to parse catalog", fileName);
      }
    }
  }

  /**
   * Return the applicable SYSTEM system identifier in this
   * catalog.
   *
   * <p>If a SYSTEM entry exists in the catalog file
   * for the system ID specified, return the mapped value.</p>
   *
   * @param systemId The system ID to locate in the catalog
   *
   * @return The mapped system identifier or null
   */
  protected String resolveLocalSystem(String systemId)
    throws MalformedURLException, IOException {

    String osname = System.getProperty("os.name");
    boolean windows = (osname.indexOf("Windows") >= 0);
    Enumeration en = catalogEntries.elements();
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();
      if (e.getEntryType() == SYSTEM) {
        String systemIdEntry = makeAbsolute(e.getEntryArg(0));
        if (systemIdEntry.equals(systemId) || 
            (windows && systemIdEntry.equalsIgnoreCase(systemId))) {
          return e.getEntryArg(1);
        }
      }
    }

    // If there's a REWRITE_SYSTEM entry in this catalog, use it
    en = catalogEntries.elements();
    String startString = null;
    String prefix = null;
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == REWRITE_SYSTEM) {
        String p = (String) e.getEntryArg(0);
        p = makeAbsolute(p);
        if (p.length() <= systemId.length()
            && p.equals(systemId.substring(0, p.length()))) {
          // Is this the longest prefix?
          if (startString == null
              || p.length() > startString.length()) {
            startString = p;
            prefix = e.getEntryArg(1);
          }
        }
      }
    }

    if (prefix != null) {
      // return the systemId with the new prefix
      return prefix + systemId.substring(startString.length());
    }

    // If there's a SYSTEM_SUFFIX entry in this catalog, use it
    en = catalogEntries.elements();
    String suffixString = null;
    String suffixURI = null;
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == SYSTEM_SUFFIX) {
        String p = (String) e.getEntryArg(0);
        if (p.length() <= systemId.length()
            && systemId.endsWith(p)) {
          // Is this the longest prefix?
          if (suffixString == null
              || p.length() > suffixString.length()) {
            suffixString = p;
            suffixURI = e.getEntryArg(1);
          }
        }
      }
    }

    if (suffixURI != null) {
      // return the systemId for the suffix
      return suffixURI;
    }

    // If there's a DELEGATE_SYSTEM entry in this catalog, use it
    en = catalogEntries.elements();
    Vector delCats = new Vector();
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == DELEGATE_SYSTEM) {
        String p = (String) e.getEntryArg(0);
        p = makeAbsolute(p);
        if (p.length() <= systemId.length()
            && p.equals(systemId.substring(0, p.length()))) {
          // delegate this match to the other catalog

          delCats.addElement(e.getEntryArg(1));
        }
      }
    }

    if (delCats.size() > 0) {
      Enumeration enCats = delCats.elements();

      if (catalogManager.debug.getDebug() > 1) {
        catalogManager.debug.message(2, "Switching to delegated catalog(s):");
        while (enCats.hasMoreElements()) {
          String delegatedCatalog = (String) enCats.nextElement();
          catalogManager.debug.message(2, "\t" + delegatedCatalog);
        }
      }

      Catalog dcat = newCatalog();

      enCats = delCats.elements();
      while (enCats.hasMoreElements()) {
        String delegatedCatalog = (String) enCats.nextElement();
        dcat.parseCatalog(delegatedCatalog);
      }

      return dcat.resolveSystem(systemId);
    }

    return null;
  }

  /**
   * Return the applicable URI in this catalog.
   *
   * <p>If a URI entry exists in the catalog file
   * for the URI specified, return the mapped value.</p>
   *
   * @param uri The URI to locate in the catalog
   *
   * @return The mapped URI or null
   */
  protected String resolveLocalURI(String uri)
    throws MalformedURLException, IOException {
    Enumeration en = catalogEntries.elements();
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();
      if (e.getEntryType() == URI) {
        String uriEntry = makeAbsolute(e.getEntryArg(0));
        if (uriEntry.equals(uri)) {
          return e.getEntryArg(1);
        }
      }
    }

    // If there's a REWRITE_URI entry in this catalog, use it
    en = catalogEntries.elements();
    String startString = null;
    String prefix = null;
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == REWRITE_URI) {
      	String p = (String) e.getEntryArg(0);
      	p = makeAbsolute(p);
      	if (p.length() <= uri.length()
      	    && p.equals(uri.substring(0, p.length()))) {
      	  // Is this the longest prefix?
      	  if (startString == null
      	      || p.length() > startString.length()) {
      	    startString = p;
      	    prefix = e.getEntryArg(1);
      	  }
      	}
      }
    }

    if (prefix != null) {
      // return the uri with the new prefix
      return prefix + uri.substring(startString.length());
    }

    // If there's a URI_SUFFIX entry in this catalog, use it
    en = catalogEntries.elements();
    String suffixString = null;
    String suffixURI = null;
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == URI_SUFFIX) {
        String p = (String) e.getEntryArg(0);
        if (p.length() <= uri.length()
            && uri.endsWith(p)) {
          // Is this the longest prefix?
          if (suffixString == null
              || p.length() > suffixString.length()) {
            suffixString = p;
            suffixURI = e.getEntryArg(1);
          }
        }
      }
    }

    if (suffixURI != null) {
      // return the uri for the suffix
      return suffixURI;
    }

    // If there's a DELEGATE_URI entry in this catalog, use it
    en = catalogEntries.elements();
    Vector delCats = new Vector();
    while (en.hasMoreElements()) {
      CatalogEntry e = (CatalogEntry) en.nextElement();

      if (e.getEntryType() == DELEGATE_URI) {
        String p = (String) e.getEntryArg(0);
        p = makeAbsolute(p);
        if (p.length() <= uri.length()
            && p.equals(uri.substring(0, p.length()))) {
          // delegate this match to the other catalog

          delCats.addElement(e.getEntryArg(1));
        }
      }
    }

    if (delCats.size() > 0) {
      Enumeration enCats = delCats.elements();

      if (catalogManager.debug.getDebug() > 1) {
        catalogManager.debug.message(2, "Switching to delegated catalog(s):");
        while (enCats.hasMoreElements()) {
          String delegatedCatalog = (String) enCats.nextElement();
          catalogManager.debug.message(2, "\t" + delegatedCatalog);
        }
      }

      Catalog dcat = newCatalog();

      enCats = delCats.elements();
      while (enCats.hasMoreElements()) {
        String delegatedCatalog = (String) enCats.nextElement();
        dcat.parseCatalog(delegatedCatalog);
      }

      return dcat.resolveURI(uri);
    }

    return null;
  }
}

