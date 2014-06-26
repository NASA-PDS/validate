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
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.validate.inventory.reader.InventoryReaderException;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
   * @throws ParserConfigurationException 
   */
  public FileValidator(String modelVersion, Report report)
      throws ParserConfigurationException {
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
   * @throws TransformerException
   * @throws InventoryReaderException
   *
   */
  public void validate(File file) throws Exception {
    validate(file.toURI().toURL());
  }

  @Override
  public void validate(URL url) throws Exception {
    ExceptionContainer exceptionContainer = new ExceptionContainer();
    labelValidator.validate(exceptionContainer, url);
    report.record(url.toURI(), exceptionContainer.getExceptions());

  }
}
