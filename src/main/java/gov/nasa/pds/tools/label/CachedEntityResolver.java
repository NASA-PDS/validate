// Copyright 2009-2014, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology
// Transfer at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
//
package gov.nasa.pds.tools.label;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import gov.nasa.pds.tools.util.Utility;

/**
 * Class that caches entities into memory.
 *
 * @author mcayanan
 *
 */
public class CachedEntityResolver implements EntityResolver {

  /** LoadingCache to hold the entities. */
  private LoadingCache<String, byte[]> cachedEntities =
          CacheBuilder.newBuilder().softValues().build(CacheLoader.from(CachedEntityResolver::createEntity));

  /**
   * Constructor.
   */
  public CachedEntityResolver() {
  }

  @Override
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {

    byte[] entity = cachedEntities.getUnchecked(systemId);
    InputSource inputSource = new InputSource(new ByteArrayInputStream(entity));
    inputSource.setSystemId(systemId);
    return inputSource;
  }

  private static byte[] createEntity(String systemId) {
    byte[] entity;
    InputStream in = null;
    URLConnection conn = null;
    try {
      URL url = new URL(systemId);
      conn = url.openConnection();
      in = Utility.openConnection(conn);
      entity = IOUtils.toByteArray(in);
      return entity;
    } catch (IOException io) {
      throw new RuntimeException(io);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.close(conn);
    }
  }

  public void addCachedEntities(Map<String, byte[]> entities) {
    this.cachedEntities.putAll(entities);
  }
}
