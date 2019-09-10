//  Copyright 2009-2018, by the California Institute of Technology.
//  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//  Any commercial use must be negotiated with the Office of Technology
//  Transfer at the California Institute of Technology.
//
//  This software is subject to U. S. export control laws and regulations
//  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//  is subject to U.S. export control laws and regulations, the recipient has
//  the responsibility to obtain export licenses or other export authority as
//  may be required before exporting such information to foreign countries or
//  providing access to foreign nationals.
//
//  $Id$
package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.tools.util.ContextProductReference;
import gov.nasa.pds.tools.util.SettingsManager;
import gov.nasa.pds.tools.validate.ListenerExceptionPropagator;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.ValidateProblemHandler;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationResourceManager;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;
import gov.nasa.pds.tools.validate.rule.RuleContext;
import gov.nasa.pds.tools.validate.rule.ValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationRuleManager;
import gov.nasa.pds.tools.validate.task.BlockingTaskManager;
import gov.nasa.pds.tools.validate.task.TaskManager;
import gov.nasa.pds.tools.validate.task.ValidationTask;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.config.ConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a validator that validates a location (file or directory)
 * against a validation rule set. If no rule set is specified,
 * an appropriate default is chosen.
 */
public class LocationValidator {
	
	private static final Logger LOG = LoggerFactory.getLogger(
	    LocationValidator.class);
	
	private TargetRegistrar targetRegistrar;
	private SettingsManager settingsManager;
	private ValidationRuleManager ruleManager;
	private TaskManager taskManager;
	private LabelValidator labelValidator;
	private RuleContext ruleContext;
	private String validationRule;
	
	/**
	 * Creates a new instance.
	 * @throws ParserConfigurationException if a label validator cannot 
	 * configure its parser
	 * @throws TransformerConfigurationException if a label validator cannot 
	 * configure its transformer
	 */
	public LocationValidator() throws TransformerConfigurationException, 
	ParserConfigurationException {
		settingsManager = SettingsManager.INSTANCE;
		taskManager = new BlockingTaskManager();
		labelValidator = ValidationResourceManager.INSTANCE.getResource(
		    LabelValidator.class);
		ruleContext = new RuleContext();
		
		ConfigParser parser = new ConfigParser();
		URL commandsURL = ClassLoader.getSystemResource("validation-commands.xml");
		try {
			parser.parse(commandsURL);
		} catch (Exception e) {
			System.err.println("Could not parse validation configuration from "
			    + commandsURL + ": " + e);
		}
		Catalog catalog = CatalogFactory.getInstance().getCatalog();
		
		ruleManager = new ValidationRuleManager(catalog);
	}
	
	/**
	 * Validates a location specified by a file or directory.
	 * 
	 * @param f the file or directory to validate
	 */
	public void validate(File f) {
		try {
			URL target = f.toURI().toURL();
			validate(target);
		} catch (MalformedURLException e) {
			// Cannot occur - a file can always be converted to
			// a URI and a URL.
		}
	}
	
	public void validate(URL target) {
		validate(new SimpleProblemHandler(), target);
	}

	/**
	 * Validates a URL location with a given problem handler. This must be
	 * a URL that can be resolved to a file location.
	 * 
	 * @param problemHandler the problem handler
	 * @param url the URL to validate
	 * @throws URISyntaxException 
	 */
	public void validate(ValidateProblemHandler problemHandler, URL url) {
		if (targetRegistrar == null) {
			System.err.println("Configuration error - targetRegistrar not specified"
			    + " in LocationValidator.validate()");
			return;
		}

		ValidationRule rule = getRule(url);
		String location = url.toString();
		if (rule == null) {
			LOG.error("No matching validation rule found for location {}", location);
		} else {
			LOG.info("Using validation style '{}' for location {}", 
			    rule.getCaption(), location);
			if (!rule.isApplicable(location)) {
			  LOG.error("'{}' validation style is not applicable for location {}", 
			      rule.getCaption(), location);
			  return;
			}
			ProblemListener listener = new ListenerExceptionPropagator(
			    problemHandler);
			ValidationTask task = new ValidationTask(listener, ruleContext,
			    targetRegistrar);
			task.setLocation(location);
			task.setRule(rule);
			task.setRuleManager(ruleManager);
			Crawler crawler = CrawlerFactory.newInstance(url);
			ruleContext.setCrawler(crawler);
			ruleContext.setRule(rule);
			taskManager.submit(task);
		}
	}

	/**
	 * Sets the target registrar for the next validation.
	 * 
	 * @param registrar the new target registrar
	 */
	public void setTargetRegistrar(TargetRegistrar registrar) {
		this.targetRegistrar = registrar;
	}
	
	/**
	 * Sets the task manager to use for running the validation tasks.
	 * 
	 * @param manager the new task manager
	 */
	public void setTaskManager(TaskManager manager) {
		this.taskManager = manager;
	}
	
	private ValidationRule getRule(URL location) {
		String validationType = settingsManager.getString(
		    ValidationSettings.VALIDATION_RULE, null);
		if (validationRule != null) {
			validationType = validationRule;
		}
		ValidationRule rule;
		
		if (validationType == null) {
		  URI uri = null;
		  try {
		    uri = location.toURI();
		  } catch (URISyntaxException e) {
		    //Can't happen
		  }
			rule = ruleManager.findApplicableRule(uri.normalize().toString());
			if (rule == null) {
				System.err.println("No validation type specified and no applicable"
				    + " default rules.");
			}
		} else {
			rule = ruleManager.findRuleByName(validationType);
			if (rule == null) {
				System.err.println("Specified validation type is invalid: "
				    + validationType);
			}
		}
		
		return rule;
	}

	public void setModelVersion(String modelVersion) throws ValidatorException {
		labelValidator.setModelVersion(modelVersion);
	}

	public void setSchema(List<URL> schemaFiles) {
		labelValidator.setSchema(schemaFiles);
	}

	public void setSchematrons(List<Transformer> schematrons) {
		labelValidator.setSchematrons(schematrons);
	}

	public void setCachedEntityResolver(CachedEntityResolver resolver) {
		labelValidator.setCachedEntityResolver(resolver);
	}

	public void setCachedLSResourceResolver(CachedLSResourceResolver resolver) {
		labelValidator.setCachedLSResourceResolver(resolver);
	}

	public void setCatalogs(List<String> catalogFiles) {
		labelValidator.setCatalogs(catalogFiles.toArray(
		    new String[catalogFiles.size()]));
		ruleContext.setCatalogs(catalogFiles);
		ruleContext.setCatalogResolver(labelValidator.getCatalogResolver());
	}

	public void setSchemaCheck(boolean value, boolean useLabelSchema) {
		labelValidator.setSchemaCheck(value, useLabelSchema);
	}

	public void setSchematronCheck(Boolean value, Boolean useLabelSchematron) {
		labelValidator.setSchematronCheck(value, useLabelSchematron);
	}

	public void addValidator(DocumentValidator validator) {
		labelValidator.addValidator(validator);
	}
	
	public void setLabelSchematrons(Map<String, Transformer> labelSchematrons) {
		labelValidator.setLabelSchematrons(labelSchematrons);
	}

	public void setForce(boolean force) {
    labelValidator.setSchemaCheck(true, force);
    labelValidator.setSchematronCheck(true, force);
    ruleContext.setForceLabelSchemaValidation(force);
	}

	public void setFileFilters(List<String> regExps) {
		ruleContext.setFileFilters(regExps);
	}

	public void setRecurse(boolean traverse) {
		ruleContext.setRecursive(traverse);
	}
	
	public void setChecksumManifest(Map<URL, String> checksums) {
	  ruleContext.setChecksumManifest(checksums);
	}

	public void  setCheckData(boolean flag) {
	  ruleContext.setCheckData(flag);
	}
	
	public void setRegisteredProducts(Map<String, List<ContextProductReference>> products) {
	  ruleContext.setRegisteredProducts(products);
	}
	
	
	/**
	 * Gets a singleton label validator.
	 * 
	 * @return the label validator
	 */
	public LabelValidator getLabelValidator() {
		return labelValidator;
	}
	
	/**
	 * Forces a validation rule to use for the target location.
	 * 
	 * @param ruleName the name of the rule
	 */
	public void setRule(String ruleName) {
		this.validationRule = ruleName;
	}
	
	public void setSpotCheckData(int value) {
	  ruleContext.setSpotCheckData(value);
	}
	
	public void setAllowUnlabeledFiles(boolean flag) {
	  ruleContext.setAllowUnlabeledFiles(flag);
	}
	
	/**
	 * Implements a simple problem handler that prints problems to the 
	 * standout error output.
	 * @author merose, mcayanan
	 *
	 */
	private class SimpleProblemHandler implements ValidateProblemHandler {

		@Override
		public void addProblem(ValidationProblem problem) {
			StringBuilder buf = new StringBuilder();
			buf.append(problem.getMessage());
			if (problem.getTarget()!=null) {
				buf.append(": ");
				buf.append(problem.getTarget().getLocation());
			}
			if (problem.getLineNumber() > 0) {
				buf.append(", line ");
				buf.append(Integer.toString(problem.getLineNumber()));
			}
			if (problem.getColumnNumber() > 0) {
				buf.append(", column ");
				buf.append(Integer.toString(problem.getColumnNumber()));
			}
			System.err.println(buf.toString());
		}

    @Override
    public void addLocation(String location) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void printHeader(String title) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void record(String location) {
      // TODO Auto-generated method stub
      
    }
	}

}
