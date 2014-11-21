// Copyright 2006-2014, by the California Institute of Technology.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelErrorHandler;
import gov.nasa.pds.tools.label.LabelException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

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
    // Fix validate label method to always set the
    schemaFactory.setErrorHandler(new LabelErrorHandler(container));
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
