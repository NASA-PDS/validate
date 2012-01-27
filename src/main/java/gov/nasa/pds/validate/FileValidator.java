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

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * Class that validates a single file.
 *
 * @author mcayanan
 *
 */
public class FileValidator extends Validator {
  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report object to output the results.
   */
  public FileValidator(String modelVersion, Report report) {
    super(modelVersion, report);
  }

  /**
   * Validate a PDS product file.
   *
   * @param file A PDS product file.
   *
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   * @throws XPathExpressionException
   *
   */
  public void validate(File file) throws SAXException, IOException,
  ParserConfigurationException, XPathExpressionException {
    ExceptionContainer exceptionContainer = new ExceptionContainer();
    LabelValidator lv = new LabelValidator();
    lv.setModelVersion(modelVersion);
    if (!schemas.isEmpty()) {
      lv.setSchema(schemas.toArray(new String[0]));
    }
    if (!catalogs.isEmpty()) {
      lv.setCatalogs(catalogs.toArray(new String[0]));
    }
    lv.validate(exceptionContainer, file);
    report.record(file.toURI(), exceptionContainer.getExceptions());
  }
}
