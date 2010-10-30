// Copyright 2006-2010, by the California Institute of Technology.
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
package gov.nasa.pds.validate;

import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Abstract class to validate a PDS4 product label.
 *
 * @author mcayanan
 *
 */
public abstract class Validator {
    protected Report report;
    protected Schema schema;

    /**
     * Constructor.
     *
     * @param report A Report object to output the results of the validation
     *  run.
     */
    public Validator(Report report) {
        this.report = report;
        this.schema = null;
    }

    /**
     * Sets the schemas to use during validation. By default, the validation
     * comes pre-loaded with schemas to use. This method would only be used
     * in cases where the user wishes to use their own set of schemas for
     * validation.
     *
     * @param schemaFiles A list of schema files.
     *
     * @throws SAXException If a schema is malformed.
     */
    public void setSchema(List<File> schemaFiles) throws SAXException {
        List<StreamSource> schemas = new ArrayList<StreamSource>();
        for (File schema : schemaFiles) {
            schemas.add(new StreamSource(schema));
        }
        SchemaFactory factory = SchemaFactory
          .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.schema = factory.newSchema(schemas.toArray(new StreamSource[0]));
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Validate a PDS product.
     *
     * @param file A PDS product file.
     *
     */
    public abstract void validate(File file);
}
