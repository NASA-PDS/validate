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

import gov.nasa.pds.tools.label.ExceptionType;
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
  public LocationValidator newInstance(ExceptionType logLevel) throws ValidatorException,
  TransformerConfigurationException, ParserConfigurationException {
    if (cachedValidator == null) {
    	cachedValidator = new LocationValidator(logLevel);
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
  
 public void flush() {
     factory = null;
 }

}
