//  Copyright 2009-2014, by the California Institute of Technology.
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

import gov.nasa.pds.tools.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class that caches entities into memory.
 *
 * @author mcayanan
 *
 */
public class CachedEntityResolver implements EntityResolver {

  /** Hashmap to hold the entities. */
  private Map<String, byte[]> cachedEntities = new HashMap<String, byte[]>();

  /**
   * Constructor.
   */
  public CachedEntityResolver() {
    cachedEntities = new HashMap<String, byte[]>();
  }

  @Override
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {
    byte[] entity = cachedEntities.get(systemId);
    if (entity == null) {
      URL url = new URL(systemId);
      InputStream in = null;
      URLConnection conn = null;
      try {
        conn = url.openConnection();
        in = Utility.openConnection(conn);
        entity = IOUtils.toByteArray(in);
        cachedEntities.put(systemId, entity);
      } catch (IOException io) {
        throw io;
      } finally {
        IOUtils.closeQuietly(in);
        IOUtils.close(conn);
      }
    }
    InputSource inputSource = new InputSource(new ByteArrayInputStream(entity));
    inputSource.setSystemId(systemId);
    return inputSource;
  }

  public void addCachedEntities(Map<String, byte[]> entities) {
    this.cachedEntities.putAll(entities);
  }
}
