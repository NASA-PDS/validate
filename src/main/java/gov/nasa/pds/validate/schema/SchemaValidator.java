// Copyright 2006-2015, by the California Institute of Technology.
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
package gov.nasa.pds.validate.schema;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelErrorHandler;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.util.Utility;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class to validate schemas.
 *
 * @author mcayanan
 *
 */
public class SchemaValidator {
  /**
   * Schema factory.
   */
  private SchemaFactory schemaFactory;

  /**
   * An entity resolver.
   */
  private EntityResolver resolver;

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

  /**
   * Class that resolves import statements found within a schema file.
   *
   * @author mcayanan
   *
   */
  private class SchemaResourceResolver implements LSResourceResolver {

    /** Hashmap to hold the entities. */
    private Map<String, byte[]> cachedEntities = new HashMap<String, byte[]>();

    private ExceptionContainer container;

    /**
     * Constructor.
     */
    public SchemaResourceResolver(ExceptionContainer container) {
      cachedEntities = new HashMap<String, byte[]>();
      this.container = container;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI,
        String publicId, String systemId, String baseURI) {

      byte[] entity = cachedEntities.get(systemId);
      LSInputImpl input = new LSInputImpl();
      if (entity == null) {
        InputStream in = null;
        URLConnection conn = null;
        try {
          URL base = new URL(baseURI);
          base = base.toURI().getPath().endsWith("/") ?
              base.toURI().resolve("..").toURL() :
                base.toURI().resolve(".").toURL();
          URL url = new URL(base, systemId);
          conn = url.openConnection();
          in = Utility.openConnection(conn);
          entity = IOUtils.toByteArray(in);
          cachedEntities.put(systemId, entity);
        } catch (Exception e) {
          container.addException(new LabelException(ExceptionType.FATAL,
              e.getMessage(), systemId, systemId, null, null));
        } finally {
          IOUtils.closeQuietly(in);
          IOUtils.close(conn);
        }
      }
      input.setPublicId(publicId);
      input.setSystemId(systemId);
      input.setBaseURI(baseURI);
      input.setCharacterStream(
          new InputStreamReader(new ByteArrayInputStream(entity)));

      return input;
    }
  }



  /**
   * Constructor.
   *
   * @param resolver An entity resolver.
   */
  public SchemaValidator(EntityResolver resolver) {
    // Support for XSD 1.1
    schemaFactory = SchemaFactory
        .newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
    this.resolver = resolver;
  }

  /**
   * Validate the given schema.
   *
   * @param schema URL of the schema.
   *
   * @return An ExceptionContainer that contains any problems
   * that were found during validation.
   */
  public ExceptionContainer validate(URL schema) {
    ExceptionContainer container = new ExceptionContainer();
    schemaFactory.setErrorHandler(new LabelErrorHandler(container));
    schemaFactory.setResourceResolver(new SchemaResourceResolver(container));
    try {
      InputSource inputSource = resolver.resolveEntity("", schema.toString());
      StreamSource source = new StreamSource(inputSource.getByteStream());
      source.setSystemId(schema.toString());
      schemaFactory.newSchema(source);
    } catch (SAXException se) {
      if ( !(se instanceof SAXParseException) ) {
        LabelException le = new LabelException(ExceptionType.FATAL,
            se.getMessage(), schema.toString(), schema.toString(),
            null, null);
        container.addException(le);
      }
    } catch (IOException io) {
      if (io instanceof FileNotFoundException) {
        LabelException le = new LabelException(ExceptionType.FATAL,
          "Cannot read schema as URL cannot be found: " + io.getMessage(),
          schema.toString(), schema.toString(), null, null);
        container.addException(le);
      } else {
        LabelException le = new LabelException(ExceptionType.FATAL,
            "Exception occurred while reading schema: " + io.getMessage(),
            schema.toString(), schema.toString(), null, null);
        container.addException(le);
      }
    }
    return container;
  }
}
