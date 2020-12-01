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
package gov.nasa.pds.tools.validate.rule;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LocationValidator;
import gov.nasa.pds.tools.label.XMLCatalogResolver;
import gov.nasa.pds.tools.util.ContextProductReference;
import gov.nasa.pds.tools.validate.AdditionalTarget;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.WildcardOSFilter;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.chain.impl.ContextBase;

/**
 * Implements a type-safe context for using validation rules
 * in commands and chains.
 */
public class RuleContext extends ContextBase {

  private static final long serialVersionUID = 1L;
  
  /** The key used to retrieve the top-level validator for getting singleton utilities. */
  public static final String LOCATION_VALIDATOR_KEY = "validation.location-validator";
  
  /** The key used to indicate that declared schema and Schematron files in a label
   * should be validated before the label is validate.
   */
  public static final String FORCE_LABEL_SCHEMA_VALIDATION = "validation.force";
  
  /** The key used to set whether to validate recursively. */
  public static final String RECURSIVE_VALIDATION = "validation.recursive";
  
  /** The key used to set file name filters. */
  public static final String FILE_FILTERS = "validation.file-filters";
  
  /** The key used to retrieve the current validation target from the
   * execution context.
   */
  public static final String TARGET_KEY = "validation.target";

  /** Key used to retrieve additional targets. */
  public static final String ADDITIONAL_TARGET_KEY = "validation.additional-target";

  /** The key used to retrieve the definition listener from the
   * execution context.
   */
  public static final String LISTENER_KEY = "validation.listener";
  /** The key used to retrieve the validation registrar from the execution
   * context.
   */
  public static final String REGISTRAR_KEY = "validation.registrar";

  /** The key used to retrieve the rule manager from the context. */
  public static final String RULE_MANAGER_KEY = "validation.rule-manager";

  public static final String RULE_KEY = "validation.rule";
  
  /** The key used to retrieve the parent target from the context. */
  public static final String PARENT_TARGET_KEY = "validation.parent-target";

  /** The key used to retrieve the crawler. */
  public static final String CRAWLER_KEY = "validation.crawler";
  
  /** The key used to retrieve a hash map containing the checksum values from
   *  a given checksum manifest.
   */
  public static final String CHECKSUM_MANIFEST_KEY = "validation.checksum-manifest";
  
  /** The key used to retrieve the catalog files. */
  public static final String CATALOG_FILES = "validation.catalogs";
  
  /** The key used to retrieve the XMLCatalogResolver object. */
  public static final String CATALOG_RESOLVER = "validation.catalog-resolver";
  
  /** The key used to indicate whether to disable data content validation. */
  public static final String CHECK_DATA_KEY = "validate.check-data";
  
  /** The key used to indicate how many lines or records to skip during 
   *  content validation.
   */
  public static final String SPOT_CHECK_DATA_KEY = "validate.spot-check";
  
  /**
   * Key used to tell the tool to allow unlabeled files in a bundle or collection. 
   */
  public static final String ALLOW_UNLABELED_FILES_KEY = "validate.allow-unlabeled-files";
  
  /**
   * Key used to store a map of registered Product LIDVIDs.
   */
  public static final String REGISTERED_PRODUCTS_KEY = "validate.registered-products";
  
  /** The key used to indicate whether to disable context validation. */
  public static final String CONTEXT_VALIDATION_KEY = "validate.validate-context";
  
  /**
   * Key used to ignore(skip) product level validation.
   */
  public static final String SKIP_PRODUCT_VALIDATION_KEY = "validate.skip-product-validation";
  
  private boolean rootTarget = false;

  private ExceptionType logLevel;

  /**
   * Gets a value from the context in a type-safe manner.
   *
   * @param key the key
   * @param clazz the expected class
   * @return the value, or null if there is no value with that key
   */
  @SuppressWarnings("unchecked")
  public <T> T getContextValue(String key, Class<T> clazz) {
    if (containsKey(key)) {
      return (T) get(key);
    } else {
      return null;
    }
  }

  /**
   * Puts a value into the context in a type-safe manner.
   *
   * @param key the key
   * @param value the value
   */
  public <T> void putContextValue(String key, T value) {
    put(key, value);
  }

  public ExceptionType getLogLevel() { return logLevel; }

  public void setLogLevel(ExceptionType logLevel) {
      this.logLevel = logLevel;
  }

  public URL getTarget() {  
    return getContextValue(TARGET_KEY, URL.class);
  }

  public void setTarget(URL target) throws MalformedURLException,
    URISyntaxException {
    target = target.toURI().normalize().toURL();
    putContextValue(TARGET_KEY, target);
  }

  public AdditionalTarget getExtraTarget() {
    return getContextValue(ADDITIONAL_TARGET_KEY, AdditionalTarget.class);
  }

  public void setExtraTarget(ArrayList<URL> targets) throws MalformedURLException,
    URISyntaxException {
    ArrayList<URL> additionalTarget = new ArrayList<URL>();
    for (URL target : targets) {
        additionalTarget.add(target.toURI().normalize().toURL());
    }
    putContextValue(ADDITIONAL_TARGET_KEY, new AdditionalTarget(additionalTarget));
  }

  public ProblemListener getProblemListener() {
    return getContextValue(LISTENER_KEY, ProblemListener.class);
  }

  public void setProblemListener(ProblemListener listener) {
    putContextValue(LISTENER_KEY, listener);
  }

  public TargetRegistrar getTargetRegistrar() {
    return getContextValue(REGISTRAR_KEY, TargetRegistrar.class);
  }

  public void setTargetRegistrar(TargetRegistrar registrar) {
    putContextValue(REGISTRAR_KEY, registrar);
  }

  /**
   * Gets the rule manager used to find other rules to apply.
   *
   * @return the rule manager
   */
  public ValidationRuleManager getRuleManager() {
    return getContextValue(RULE_MANAGER_KEY, ValidationRuleManager.class);
  }

  public ValidationRule getRule() {
    return getContextValue(RULE_KEY, ValidationRule.class);
  }
  
  /**
   * Sets the rule manager to use for finding new rules.
   *
   * @param ruleManager the rule manager
   */
  public void setRuleManager(ValidationRuleManager ruleManager) {
    putContextValue(RULE_MANAGER_KEY, ruleManager);
  }
  
  public void setRule(ValidationRule rule) {
    putContextValue(RULE_KEY, rule);
  }

  /**
   * Gets the parent target location.
   *
   * @return the parent target location, or null if there is no parent target
   */
  public String getParentTarget() {
    return getContextValue(PARENT_TARGET_KEY, String.class);
  }

  /**
   * Sets the parent target location.
   *
   * @param parent the parent target location
   */
  public void setParentTarget(String parentLocation) {
    putContextValue(PARENT_TARGET_KEY, parentLocation);
  }

  /**
   * Tests whether this is the root target for the validation.
   *
   * @return true, if this context is for the root target
   */
  public boolean isRootTarget() {
    return rootTarget;
  }

  /**
   * Sets whether this is the root target for the validation.
   *
   * @param flag true, if this context is for the root target
   */
  public void setRootTarget(boolean flag) {
    rootTarget = flag;
  }
  
  /**
   * Gets the top-level validator for getting singleton utilities, such
   * as the label validator.
   * 
   * @return the top-level validator
   */
  public LocationValidator getRootValidator() {
  	return getContextValue(LOCATION_VALIDATOR_KEY, LocationValidator.class);
  }
  
  /**
   * Sets the top-level validator.
   * 
   * @param validator the top-level validator
   */
  public void setRootValidator(LocationValidator validator) {
  	putContextValue(LOCATION_VALIDATOR_KEY, validator);
  }
  
  public boolean isRecursive() {
    return getContextValue(RECURSIVE_VALIDATION, Boolean.class);
  }
  
  public void setRecursive(boolean isRecursive) {
  	putContextValue(RECURSIVE_VALIDATION, Boolean.valueOf(isRecursive));
  }
  
  @SuppressWarnings("unchecked")
	public WildcardOSFilter getFileFilters() {
  	return getContextValue(FILE_FILTERS, WildcardOSFilter.class);
  }
  
  public void setFileFilters(List<String> filters) {
  	putContextValue(FILE_FILTERS, new WildcardOSFilter(filters));
  }

  public void setFileFilters(WildcardOSFilter filter) {
    putContextValue(FILE_FILTERS, filter);
  }
  
  /**
   * Tests whether to force validation of schemas and Schematrons defined in a
   * label file.
   * 
   * @return true, if declared schema and Schematron files should be validated
   */
  public boolean isForceLabelSchemaValidation() {
	  return getContextValue(FORCE_LABEL_SCHEMA_VALIDATION, Boolean.class);
  }

	/**
	 * Sets whether to force schema and Schematron validation defined in a label.
	 * 
	 * @param force true, if declared schema and Schematron files should be validated
	 */
	public void setForceLabelSchemaValidation(boolean force) {
		putContextValue(FORCE_LABEL_SCHEMA_VALIDATION, force);
	}

	public Crawler getCrawler() {
	  return getContextValue(CRAWLER_KEY, Crawler.class);
	}
	
	public void setCrawler(Crawler crawler) {
	  putContextValue(CRAWLER_KEY, crawler);
	}
	
	public void setChecksumManifest(Map<URL, String> manifest) {
	  putContextValue(CHECKSUM_MANIFEST_KEY, manifest);
	}
	
	public Map<URL, String> getChecksumManifest() {
	  return (Map<URL, String>) getContextValue(CHECKSUM_MANIFEST_KEY, Map.class);
	}
	
	public void setCatalogs(List<String> catalogs) {
	  putContextValue(CATALOG_FILES, catalogs);
	}
	
	public List<String> getCatalogs() {
	  return (List<String>) getContextValue(CATALOG_FILES, List.class);
	}
	
	public void setCatalogResolver(XMLCatalogResolver catalogResolver) {
	  putContextValue(CATALOG_RESOLVER, catalogResolver);
	}
	
	public XMLCatalogResolver getCatalogResolver() {
	  return (XMLCatalogResolver) getContextValue(CATALOG_RESOLVER, XMLCatalogResolver.class);
	}
	
	public boolean getCheckData() {
	  return getContextValue(CHECK_DATA_KEY, Boolean.class);
	}
	
	public void setCheckData(boolean flag) {
	  putContextValue(CHECK_DATA_KEY, flag);
	}
	
	public int getSpotCheckData() {
	  return getContextValue(SPOT_CHECK_DATA_KEY, Integer.class);
	}
	  
	public void setSpotCheckData(int value) {
	  putContextValue(SPOT_CHECK_DATA_KEY, value);
	}
	
	public boolean getAllowUnlabeledFiles() {
	  return getContextValue(ALLOW_UNLABELED_FILES_KEY, Boolean.class);
	}
	  
	public void setAllowUnlabeledFiles(boolean flag) {
	  putContextValue(ALLOW_UNLABELED_FILES_KEY, flag);
	}
	
	public Map<String, List<ContextProductReference>> getRegisteredProducts() {
	  return getContextValue(REGISTERED_PRODUCTS_KEY, Map.class);
	}
	
	public void setRegisteredProducts(Map<String, List<ContextProductReference>> products) {
	  putContextValue(REGISTERED_PRODUCTS_KEY, products);
	}
	

    public boolean getValidateContext() {
        return getContextValue(CONTEXT_VALIDATION_KEY, Boolean.class);
    }

    public void setValidateContext(boolean flag) {
        putContextValue(CONTEXT_VALIDATION_KEY, flag);
    }

	public boolean getSkipProductValidation() {
	  return getContextValue(SKIP_PRODUCT_VALIDATION_KEY, Boolean.class);
	}
	
	// issue_42: DO NOT check data when --skip-product-validation=true
	public void setSkipProductValidation(boolean flag) {
	  putContextValue(SKIP_PRODUCT_VALIDATION_KEY, flag);
	  if (flag) setCheckData(false);
	}
}
