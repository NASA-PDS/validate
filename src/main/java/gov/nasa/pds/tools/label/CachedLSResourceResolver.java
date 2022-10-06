// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.label;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Class to resolve resources.
 *
 * @author mcayanan
 *
 */
public class CachedLSResourceResolver implements LSResourceResolver {
  /**
   * Input Source implementation class.
   *
   * @author mcayanan
   *
   */
  private class LSInputImpl implements LSInput {
    private Reader characterStream;
    private InputStream byteStream;
    private String stringData;
    private String systemId;
    private String publicId;
    private String baseURI;
    private String encoding;
    private boolean certifiedText;

    @Override
    public String getBaseURI() {
      return baseURI;
    }

    @Override
    public InputStream getByteStream() {
      return byteStream;
    }

    @Override
    public boolean getCertifiedText() {
      return certifiedText;
    }

    @Override
    public Reader getCharacterStream() {
      return characterStream;
    }

    @Override
    public String getEncoding() {
      return encoding;
    }

    @Override
    public String getPublicId() {
      return publicId;
    }

    @Override
    public String getStringData() {
      return stringData;
    }

    @Override
    public String getSystemId() {
      return systemId;
    }

    @Override
    public void setBaseURI(String uri) {
      this.baseURI = uri;
    }

    @Override
    public void setByteStream(InputStream input) {
      this.byteStream = input;
    }

    @Override
    public void setCertifiedText(boolean flag) {
      this.certifiedText = flag;
    }

    @Override
    public void setCharacterStream(Reader reader) {
      this.characterStream = reader;
    }

    @Override
    public void setEncoding(String encoding) {
      this.encoding = encoding;
    }

    @Override
    public void setPublicId(String publicId) {
      this.publicId = publicId;
    }

    @Override
    public void setStringData(String data) {
      this.stringData = data;
    }

    @Override
    public void setSystemId(String systemId) {
      this.systemId = systemId;
    }
  }

  /** Hashmap to hold the entities. */
  private Map<String, byte[]> cachedEntities = new HashMap<>();

  private ProblemHandler handler;

  /**
   * Constructor.
   */
  public CachedLSResourceResolver() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param container A container to hold messages.
   */
  public CachedLSResourceResolver(ProblemHandler handler) {
    cachedEntities = new HashMap<>();
    this.handler = handler;
  }

  @Override
  public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId,
      String baseURI) {
    if (systemId == null) {
      return null;
    }
    byte[] entity = cachedEntities.get(systemId);
    LSInputImpl input = new LSInputImpl();
    if (entity == null) {
      InputStream in = null;
      URLConnection conn = null;
      URL url = null;
      try {
        URL base = new URL(baseURI);
        // If we have a jar URL, we need to resolve this differently
        if ("jar".equals(base.getProtocol())) {
          URL jarBase = new URL(base.getPath());
          base = jarBase.toURI().getPath().endsWith("/") ? jarBase.toURI().resolve("..").toURL()
              : jarBase.toURI().resolve(".").toURL();
          if (systemId.startsWith("jar")) {
            URL s = new URL(systemId);
            URL systemPath = new URL(s.getPath());
            url = base.toURI().resolve(systemPath.toURI()).toURL();
          } else {
            url = new URL(base, systemId);
          }
          if (url.toString().contains("!")) {
            url = new URL("jar:" + url.toString());
          }
        } else {
          base = base.toURI().getPath().endsWith("/") ? base.toURI().resolve("..").toURL()
              : base.toURI().resolve(".").toURL();
          url = new URL(base, systemId);
        }
        conn = url.openConnection();
        in = Utility.openConnection(conn);
        entity = IOUtils.toByteArray(in);
        cachedEntities.put(systemId, entity);
      } catch (Exception e) {
        if (handler != null) {
          URL u = null;
          try {
            u = new URL(systemId);
          } catch (MalformedURLException mu) {
            u = url;
          }
          handler.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.FATAL,
              ProblemType.LABEL_UNRESOLVABLE_RESOURCE, e.getMessage()), u));
        } else {
          e.printStackTrace();
        }
      } finally {
        IOUtils.closeQuietly(in);
        IOUtils.close(conn);
      }
    }
    input.setPublicId(publicId);
    input.setSystemId(systemId);
    input.setBaseURI(baseURI);
    if (entity != null) {
      input.setByteStream(new ByteArrayInputStream(entity));
      input.setCharacterStream(new InputStreamReader(new ByteArrayInputStream(entity)));
      return input;
    }
    return null;
  }

  public Map<String, byte[]> getCachedEntities() {
    return cachedEntities;
  }

  public void addCachedEntities(Map<String, byte[]> entities) {
    this.cachedEntities.putAll(entities);
  }

  public void setProblemHandler(ProblemHandler handler) {
    this.handler = handler;
  }

  public ProblemHandler getProblemHandler() {
    return this.handler;
  }
}
