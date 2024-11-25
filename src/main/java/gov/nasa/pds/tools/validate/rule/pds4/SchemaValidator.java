// Copyright © 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.validate.rule.pds4;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelErrorHandler;
import gov.nasa.pds.tools.label.XMLCatalogResolver;
import gov.nasa.pds.tools.validate.ProblemContainer;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Class to validate schemas.
 *
 * @author mcayanan
 *
 */
public class SchemaValidator {
  private static final Logger LOG = LoggerFactory.getLogger(SchemaValidator.class);
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
    schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try {
      schemaFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      schemaFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      schemaFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
      LOG.error("Error setting SchemaFactory features to prevent XXE", e);
    }
    schemaFactory.setResourceResolver(new XMLCatalogResolver());
  }

  /**
   * Validate the given schema.
   *
   * @param schema URL of the schema.
   *
   * @return An ExceptionContainer that contains any problems that were found during validation.
   * @throws SAXNotSupportedException
   * @throws SAXNotRecognizedException
   */
  public ProblemContainer validate(StreamSource schema) {
    ProblemContainer container = new ProblemContainer();
    try {
      schemaFactory.setErrorHandler(new LabelErrorHandler(container));

      XMLCatalogResolver resolver = (XMLCatalogResolver) schemaFactory.getResourceResolver();
      resolver.setProblemHandler(container);

      schemaFactory.newSchema(schema);
    } catch (SAXException se) {
      if (!(se instanceof SAXParseException)) {
        URL schemaUrl = null;
        try {
          schemaUrl = new URL(schema.toString());
        } catch (MalformedURLException e) {
          // Ignore. Should not happen!!!
        }
        ValidationProblem problem = new ValidationProblem(
            new ProblemDefinition(ExceptionType.FATAL, ProblemType.SCHEMA_ERROR, se.getMessage()),
            schemaUrl);
        container.addProblem(problem);
      }
    }
    return container;
  }

  public void setExternalLocations(String locations)
      throws SAXNotRecognizedException, SAXNotSupportedException {
    schemaFactory.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
        locations);
    LOG.debug("setExternalLocations:locations {}", locations);
  }

  public XMLCatalogResolver getCachedLSResolver() {
    LOG.debug("getCachedLSResolver:");
    return (XMLCatalogResolver) schemaFactory.getResourceResolver();
  }

  public void setCatalogResolver(XMLCatalogResolver resolver) {
    LOG.debug("setCatalogResolver:resolver {}", resolver);
    if (resolver != null) {
      schemaFactory.setResourceResolver(resolver);
    }
  }
}
