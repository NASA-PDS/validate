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

import gov.nasa.pds.tools.label.ValidatorException;
import gov.nasa.pds.validate.inventory.reader.InventoryReaderException;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * Abstract class to validate a PDS4 product label.
 *
 * @author mcayanan
 *
 */
public abstract class Validator {
  /** An object representation of a report to capture the results of
   *  validation. */
  protected Report report;

  /**
   * A list of user specified schemas to validate against.
   *
   */
  protected List<String> schemas;

  /**
   * A list of user specified schematrons to validate against.
   *
   */
  protected List<String> schematrons;

  /**
   * A list of user specified catalogs to use during validation.
   *
   */
  protected List<String> catalogs;

  /**
   * The model version to validate against.
   *
   */
  protected String modelVersion;

  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report object to output the results of the validation
   *  run.
   */
  public Validator(String modelVersion, Report report) {
    this.report = report;
    this.schemas = new ArrayList<String>();
    this.schematrons = new ArrayList<String>();
    this.catalogs = new ArrayList<String>();
    this.modelVersion = modelVersion;
  }

  /**
   * Sets the schemas to use during validation. By default, the validation
   * comes pre-loaded with schemas to use. This method would only be used
   * in cases where the user wishes to use their own set of schemas for
   * validation.
   *
   * @param schemaFiles A list of schema files.
   *
   */
  public void setSchemas(List<String> schemaFiles) {
    this.schemas.addAll(schemaFiles);
  }

  /**
   * Sets the schematrons to use during validation.
   *
   * @param schematronFiles A list of schematron files.
   */
  public void setSchematrons(List<String> schematronFiles) {
    this.schematrons.addAll(schematronFiles);
  }

  /**
   * Sets the catalogs to use during validation.
   *
   * @param catalogs A list of catalog files.
   */
  public void setCatalogs(List<String> catalogs) {
    this.catalogs.addAll(catalogs);
  }

  /**
   * Validate a PDS product.
   *
   * @param file A PDS product file.
   * @throws ValidatorException
   *
   */
  public abstract void validate(File file) throws Exception;

  public abstract void validate(URL url) throws Exception;
}
