// Copyright © 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
//   conditions and the following disclaimer in the documentation and/or other
//   materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
//   Laboratory, nor the names of its contributors may be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
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

package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.CachedEntityResolver;
import gov.nasa.pds.tools.label.CachedLSResourceResolver;
import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.tools.label.SchematronTransformer;
import gov.nasa.pds.tools.label.ValidatorException;
import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.tools.validate.rule.pds4.SchemaValidator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

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
   * A list of user specified catalogs to use during validation.
   *
   */
  protected List<String> catalogs;

  /**
   * LabelValidator object.
   */
  protected LabelValidator labelValidator;

  /**
   * Flag to force validation against a label's schema and schematron.
   */
  protected boolean force;

  /**
   * Schema validator.
   */
  protected SchemaValidator schemaValidator;

  /**
   * A SchematronTransformer object.
   */
  protected SchematronTransformer schematronTransformer;

  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report object to output the results of the validation
   *  run.
   * @throws ParserConfigurationException
   * @throws ValidatorException
   * @throws TransformerConfigurationException
   */
  public Validator(String modelVersion, Report report)
      throws ParserConfigurationException, ValidatorException,
      TransformerConfigurationException {
    this.report = report;
    this.catalogs = new ArrayList<String>();
    this.labelValidator = new LabelValidator();
    this.force = false;
    schemaValidator = new SchemaValidator();
  }

  /**
   * Sets the schemas to use during validation. By default, the validation
   * comes pre-loaded with schemas to use. This method would only be used
   * in cases where the user wishes to use their own set of schemas for
   * validation.
   *
   * @param schemaFiles A list of schema files.
   * @throws SAXException
   *
   */
  public void setSchemas(List<URL> schemaFiles) throws SAXException {
    labelValidator.setSchema(schemaFiles);
  }

  /**
   * Sets the schematrons to use during validation.
   *
   * @param schematrons A list of schematrons.
   */
  public void setSchematrons(List<Transformer> schematrons) {
    labelValidator.setSchematrons(schematrons);
  }

  public void setCachedEntityResolver(CachedEntityResolver resolver) {
    labelValidator.setCachedEntityResolver(resolver);
  }

  public void setCachedLSResourceResolver(CachedLSResourceResolver resolver) {
    labelValidator.setCachedLSResourceResolver(resolver);
  }

  /**
   * Sets the catalogs to use during validation.
   *
   * @param catalogs A list of catalog files.
   */
  public void setCatalogs(List<String> catalogs) {
    labelValidator.setCatalogs(catalogs.toArray(new String[0]));
  }

  public void setForce(boolean value) {
    labelValidator.setSchemaCheck(true, value);
    labelValidator.setSchematronCheck(true, value);
    force = value;
  }

  public void addValidator(DocumentValidator validator) {
    labelValidator.addValidator(validator);
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
