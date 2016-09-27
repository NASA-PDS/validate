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
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.LocationValidator;
import gov.nasa.pds.tools.label.ValidatorException;
import gov.nasa.pds.tools.label.validate.DocumentValidator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

/**
 * Factory class that will create the appropriate Validator object.
 *
 * @author mcayanan
 *
 */
public class ValidatorFactory {
  /** Holds the factory object. */
  private static ValidatorFactory factory = null;

  private LocationValidator cachedValidator;
  
  /** A list of DocumentValidator objects. */
  private List<DocumentValidator> documentValidators;

  /** The model version to use when performing validation. */
  private String modelVersion;

  /** Private constructor. */
  private ValidatorFactory() {
    documentValidators = new ArrayList<DocumentValidator>();
    modelVersion = "";
  }

  /** Gets an instance of the factory.
   *
   */
  public static synchronized ValidatorFactory getInstance() {
    if (factory == null) {
      factory = new ValidatorFactory();
    }
    return factory;
  }

  /**
   * Returns a Validator object.
   *
   * @param target The target URL.
   *
   * @return a Validator object based on the inputs given.
   *
   * @throws ParserConfigurationException Parser configuration error occurred.
   * @throws ValidatorException Validator error occurred.
   * @throws TransformerConfigurationException Transformer configuration error occurred.
   */
  public LocationValidator newInstance(URL target) throws ValidatorException,
  TransformerConfigurationException, ParserConfigurationException {
    Validator validator = null;
    if (cachedValidator == null) {
    	cachedValidator = new LocationValidator();
    	cachedValidator.setModelVersion(modelVersion);
    	for(DocumentValidator dv : documentValidators) {
    		cachedValidator.addValidator(dv);
    	}
    }
    return cachedValidator;
  }

  /**
   *
   * @param validators A list of DocumentValidators
   */
  public void setDocumentValidators(List<DocumentValidator> validators) {
    this.documentValidators = validators;
  }

  /**
   *
   * @param modelVersion The model version.
   */
  public void setModelVersion(String modelVersion) {
    this.modelVersion = modelVersion;
  }

}
