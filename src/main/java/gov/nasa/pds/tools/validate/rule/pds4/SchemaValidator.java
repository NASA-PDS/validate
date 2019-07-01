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
// $Id: SchemaValidator.java 14717 2016-03-24 20:52:00Z mrose $
package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.label.CachedLSResourceResolver;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelErrorHandler;
import gov.nasa.pds.tools.validate.ProblemContainer;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
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
   * Constructor.
   *
   */
  public SchemaValidator() {
    // Support for XSD 1.1
    schemaFactory = SchemaFactory
        .newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
    schemaFactory.setResourceResolver(new CachedLSResourceResolver());
  }

  /**
   * Validate the given schema.
   *
   * @param schema URL of the schema.
   *
   * @return An ExceptionContainer that contains any problems
   * that were found during validation.
   */
  public ProblemContainer validate(StreamSource schema) {
    ProblemContainer container = new ProblemContainer();
    schemaFactory.setErrorHandler(new LabelErrorHandler(container));
    CachedLSResourceResolver resolver =
        (CachedLSResourceResolver) schemaFactory.getResourceResolver();
    resolver.setProblemHandler(container);
    try {
      schemaFactory.newSchema(schema);
    } catch (SAXException se) {
      if ( !(se instanceof SAXParseException) ) {
        URL url = null;
        try {
          url = new URL(schema.toString());
        } catch (MalformedURLException u) {
          //Ignore. Should not happen!!!
        }
        ValidationProblem problem = new ValidationProblem(
            new ProblemDefinition(ExceptionType.FATAL,
                ProblemType.SCHEMA_ERROR,
                se.getMessage()), 
            url);
        container.addProblem(problem);
      }
    }
    return container;
  }

  public void setExternalLocations(String locations)
      throws SAXNotRecognizedException, SAXNotSupportedException {
    schemaFactory.setProperty(
        "http://apache.org/xml/properties/schema/external-schemaLocation",
         locations);
  }

  public CachedLSResourceResolver getCachedLSResolver() {
    return (CachedLSResourceResolver) schemaFactory.getResourceResolver();
  }
}
