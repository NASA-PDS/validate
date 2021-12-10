// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.rule.pds3;

import gov.nasa.pds.tools.constants.Constants;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.ValidationSettings;
import gov.nasa.pds.tools.util.SettingsManager;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Standard;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import gov.nasa.pds.web.ui.containers.StatusContainer;
import gov.nasa.pds.web.ui.containers.dataSet.Bucket;
import gov.nasa.pds.web.ui.containers.dataSet.ValidationResults;
import gov.nasa.pds.web.ui.containers.dataSet.ValidationResults.SimpleProblem;
import gov.nasa.pds.web.ui.utils.DataSetValidator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a validation rule for PDS3 volumes.
 */
public class VolumeValidationRule extends AbstractValidationRule implements Observer {
	
	private static final Logger LOG = LoggerFactory.getLogger(VolumeValidationRule.class);

	private static final Pattern VOLUME_README_PATTERN = Pattern.compile("aareadme.txt", Pattern.CASE_INSENSITIVE);

	/** The process ID for the validation process. This is an arbitrary
	 * constant.
	 */
	private static final String PROC_ID = "12345";
	
	/**
	 * Status container to hold current state of the validation process
	 */
	protected final StatusContainer status = new StatusContainer();

	private Map<Object, MessageFormat> messageTemplates = new HashMap<Object, MessageFormat>();
	
	public VolumeValidationRule() {
		Properties properties = new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("resources.properties");
			properties.load(in);
		} catch (IOException e) {
			// Cannot occur, unless there is a build problem.
			LOG.error("Cannot load properties resource - error messages will by shown by key");
		}
		
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			MessageFormat fmt = new MessageFormat((String) entry.getValue());
			messageTemplates.put(entry.getKey(), fmt);
		}
	}
	
	@Override
	public boolean isApplicable(String location) {
   URL url;
    try {
      url = new URL(location);
    } catch (MalformedURLException e) {
      return false;
    }

    if (!Utility.isDir(url)) {
      return false;
    }

    if (!"file".equalsIgnoreCase(url.getProtocol())) {
      LOG.error("At this time, target must be a local location for pds3 volume validation.");
      return false;
    }
    
		Crawler crawler = CrawlerFactory.newInstance(url);
		// Check for AAREADME.TXT file.
		try {
  		List<Target> children = crawler.crawl(url);
  		for (Target child : children) {
  			Matcher matcher = VOLUME_README_PATTERN.matcher(FilenameUtils.getName(child.toString()));
  			if (matcher.matches()) {
  				return true;
  			}
  		}
		} catch (IOException io) {
		  //Ignore
		}

		return false;
	}

	@ValidationTest
	public void validateVolume() throws Exception {
		// get location of the master dictionary
		URL dictionaryURL = getClass().getResource("masterdd.full"); //$NON-NLS-1$
		
		// Allow the user to override the path.
		String dictionaryPath = SettingsManager.INSTANCE.getString(ValidationSettings.PDS3_DICTIONARY_PATH, null);
		if (dictionaryPath != null) {
			File dictionaryFile = new File(dictionaryPath);
			if (!dictionaryFile.canRead()) {
				LOG.error("Invalid data dictionary path '{}' - using built-in dictionary", dictionaryPath);
			} else {
				dictionaryURL = dictionaryFile.toURI().toURL();
			}
		}

		// manually init dictionary since retrieval is different for
		// applet
		LOG.info("About to initialize PDS data dictionary.");
		DataSetValidator.initMasterDictionary(dictionaryURL);
		LOG.info("Finished initializing PDS data dictionary.");

		// do validation and post results to db and cache directory
		// create a validator instance
		DataSetValidator validator = new DataSetValidator(PROC_ID, FileUtils.toFile(getTarget()), status);
		validator.addObserver(this);

		// do validation
		LOG.info("About to validate PDS3 volume.");
		validator.validate();
		LOG.info("Finished validation.");

		// get results from validator
		@SuppressWarnings("unused")
		ValidationResults results = validator.getResults();
	}
	
	/**
	 * Generic update to validation process, either updates message or posts a
	 * bucket
	 * 
	 * @param validator
	 *            validator action
	 * @param object
	 *            object to use in update
	 */
	@Override
	public void update(Observable validator, Object object) {
		if (object instanceof Bucket) {
			Bucket bucket = (Bucket) object;
			postProblems(bucket);
		} else {
			String message = this.status.getMessageKey() + " (step "
					+ this.status.getStep() + ")";
			LOG.info(message);
			this.status.seen();
		}
	}

	/**
	 * Post a bucket of problems to the exception handler.
	 */
	private void postProblems(final Bucket bucket) {
		for (SimpleProblem problem : bucket.getProblems()) {
			String key = problem.getKey();
			File file = problem.getFileObj();
			Object[] args = problem.getArguments();
			Constants.ProblemType type = problem.getType();
			int lineNumber = (problem.getLineNumber() != null ? problem.getLineNumber() : -1);
			
			ExceptionType exceptionType;
			switch (type.getSeverity()) {
			case ERROR:
				exceptionType = ExceptionType.ERROR;
				break;
			case WARNING:
				exceptionType = ExceptionType.WARNING;
				break;
				
			default:
				exceptionType = ExceptionType.INFO;
				break;
			}
			
		    /** Indicates a label that has invalid structure. */
		    ProblemDefinition problemDef = new ProblemDefinition(
		    		exceptionType,
		            ProblemType.INVALID_LABEL,
		            "File or label has errors",
		            Standard.PDS3_STANDARDS_REFERENCE,
		            null
		    );
		    
			String message = type.toString() + ": " + formatMessage(key, args);
			URL url = null;
			try {
			  url = file.toURI().toURL();
			} catch (Exception e) {
			  //Should never happen
			}
		  reportError(problemDef, url, lineNumber, -1, message);
		}
	}
	
	/**
	 * Formats an error message based on a key to a message template and
	 * arguments to be inserted into the message.
	 * 
	 * @param key the message key
	 * @param args the message arguments
	 * @return the formatted message
	 */
	private String formatMessage(String key, Object[] args) {
		MessageFormat messageTemplate = messageTemplates.get(key);
		if (messageTemplate == null) {
			LOG.error("Missing message resource with key '{}'", key);
			return key;
		}
		
		StringBuffer result = new StringBuffer();
		return messageTemplate.format(args, result, null).toString();
	}

}
