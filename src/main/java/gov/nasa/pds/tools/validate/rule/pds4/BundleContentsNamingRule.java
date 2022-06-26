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
package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

/**
 * Implements a validation rule ensuring that only valid files and
 * directories appear in the root directory of a bundle.
 */
public class BundleContentsNamingRule extends AbstractValidationRule {

  private static final String DIRECTORY_ALLOWED_CHARACTERS = "[A-Za-z0-9_-]";

  private static final String[] ALLOWED_BUNDLE_NAME_PREFIXES = {
    "browse",
    "calibration",
    "context",
    "data",
    "document",
    "geometry",
    "miscellaneous",
    "xml_schema",
    "spice_kernels",
  };

  private static final Pattern[] ALLOWED_DIRECTORY_NAME_PATTERNS = new Pattern[ALLOWED_BUNDLE_NAME_PREFIXES.length];
  static {
    for (int i=0; i < ALLOWED_BUNDLE_NAME_PREFIXES.length; ++i) {
      StringBuilder builder = new StringBuilder(ALLOWED_BUNDLE_NAME_PREFIXES[i]);
      builder.append("(");
      builder.append(DIRECTORY_ALLOWED_CHARACTERS);
      builder.append("*)?");

      ALLOWED_DIRECTORY_NAME_PATTERNS[i] = Pattern.compile(builder.toString());
    }
  }
  
  private static final String ALLOWED_FILE_NAMES[] = {
    "readme.html",
    "readme([A-Za-z0-9_.-]*)?\\.txt"
  };

	/**
	 * Checks that files and directories at the root of the bundle are
	 * valid. See section 2B.2.2.1.
	 */
	@ValidationTest
	public void checkFileAndDirectoryNaming() {
      Pattern[] fileNamePatterns = getFileNamePatterns();
	  Crawler crawler = getContext().getCrawler();
	  try {
	    List<Target> targets = crawler.crawl(getTarget());
      for (Target t : targets) {
        if (!t.isDir()) {
          checkFileNaming(t.getUrl(), PDS4Problems.UNEXPECTED_FILE_IN_BUNDLE_ROOT, fileNamePatterns);
        } else {
          checkFileNaming(t.getUrl(), PDS4Problems.INVALID_COLLECTION_NAME, ALLOWED_DIRECTORY_NAME_PATTERNS);
        }
      }
	  } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, io.getMessage());
    }
	}

	private void checkFileNaming(URL u, ProblemDefinition problem, Pattern... allowedPatterns) {
    for (Pattern pat : allowedPatterns) {
      String name = FilenameUtils.getName(Utility.removeLastSlash(u.toString()));
      Matcher matcher = pat.matcher(name);
      if (matcher.matches()) {
        return;
      }
    }

	  // If we get here, no pattern matched.
	  reportError(problem, u, -1, -1);
  }

    @Override
	public boolean isApplicable(String location) {
    return Utility.isDir(location);
	}
    
    private Pattern[] getFileNamePatterns() {
    	// Add bundle string
    	Pattern[] patterns = {};
    	for (int i=0; i < ALLOWED_FILE_NAMES.length; ++i) {
    		patterns[i] = Pattern.compile(ALLOWED_FILE_NAMES[i]);
    	}
    	
    	patterns[patterns.length] = getContext().getBundleLabelPattern();
    	return patterns;
    }

}
