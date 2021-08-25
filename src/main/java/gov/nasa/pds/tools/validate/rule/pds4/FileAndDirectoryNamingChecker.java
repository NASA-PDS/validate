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
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.FilenameUtils;

/**
 * Extend FileAndDirectoryNamingRule to enforcing file and directory naming standards
 * without a listener.   This allow any class to call checkFileOrDirectoryNameWithChecker() to get a list of ValidationProblem 
 * directly without a need to report errors to a listener.
 */
public class FileAndDirectoryNamingChecker extends FileAndDirectoryNamingRule {
    private static final Logger LOG = LoggerFactory.getLogger(FileAndDirectoryNamingChecker.class);
    public FileAndDirectoryNamingChecker() {
    }

    private ValidationProblem constructError(ProblemDefinition defn,
                                             URL targetUrl,
                                             int lineNumber,
                                             int columnNumber) {
        ValidationProblem problem = new ValidationProblem(defn, new ValidationTarget(targetUrl), lineNumber, columnNumber, defn.getMessage());
        return(problem);
    }

	public List<ValidationProblem> checkFileAndDirectoryNamingWithChecker(List<Target> list) {
		Map<String, String> seenNames = new HashMap<String, String>();
        List<ValidationProblem> validationProblems = new ArrayList<ValidationProblem>();

		for (Target t : list) {
            ValidationProblem validationProblem = checkFileOrDirectoryNameWithChecker(t.getUrl(), seenNames, t.isDir());
            // Only add problem if we have a legitimate problem.
            if (validationProblem != null) {
                validationProblems.add(validationProblem);
            }
		}
        return(validationProblems);
	}

	private ValidationProblem checkFileOrDirectoryNameWithChecker(URL u, Map<String, String> seenNames, boolean isDirectory) {
        String name = FilenameUtils.getName(Utility.removeLastSlash(u.toString()));
        LOG.debug("checkFileOrDirectoryNameWithChecker: u,name [{}],[{}]",u,name);

        // File names must be no longer than 255 characters.
        // Use member function to perform the check.
        if (isFilenameTooLong(name)) {
            LOG.debug("File names must be no longer than 255 characters: {}",name);
            return(this.constructError(isDirectory ? PDS4Problems.DIRECTORY_NAME_TOO_LONG : PDS4Problems.FILE_NAME_TOO_LONG,
				u,
				-1,
				-1));
        }
    
		// File names must use legal characters.
        // Use member function to perform the check.
        if (!isFilenameContainingLegalCharacters(name)) {
            LOG.error("File containing invalid characters " + name);
            LOG.debug("File names must use legal characters: {}",name);
            return(this.constructError(isDirectory ? PDS4Problems.DIRECTORY_NAME_USES_INVALID_CHARACTER : PDS4Problems.FILE_NAME_USES_INVALID_CHARACTER,
                                       u,
                                       -1,
                                       -1));
		}

		// Additionally, directories cannot have extensions.
        // Use member function to perform the check.
        if (isDirectoryContainingInvalidCharacter(name,isDirectory)) {
            LOG.debug("Directories cannot have extensions: {}",name);
            return(this.constructError(PDS4Problems.DIRECTORY_NAME_USES_INVALID_CHARACTER,
					u,
					-1,
					-1));
		}

		// File names must be unique, up to case-insensitivity.
		String lcName = name.toLowerCase(Locale.getDefault());
		if (!seenNames.containsKey(lcName)) {
			seenNames.put(lcName, name);
		} else {
            LOG.debug("Directory or file has name conflict in case: {}",name);
            return(this.constructError(isDirectory ? PDS4Problems.DIRECTORY_NAME_CONFLICTS_IN_CASE : PDS4Problems.FILE_NAME_CONFLICTS_IN_CASE,
					u,
					-1,
					-1));
		}

		// Prohibited file names.
        // The file "core" is checked below in isFilenameProhibited() function so no need to check here.
        if (!isDirectory && ("a.out".equalsIgnoreCase(name))) {
            LOG.debug("Prohibited file name: {}",name);
			return(this.constructError(PDS4Problems.UNALLOWED_FILE_NAME, u, -1, -1));
		}

        // Use the common isFilenameProhibited() function to check against prohibited basename.
		if (isFilenameProhibited(name)) {
            LOG.debug("Prohibited base name or directory name: {}",name);
			return(this.constructError(
					isDirectory ? PDS4Problems.UNALLOWED_DIRECTORY_NAME : PDS4Problems.UNALLOWED_BASE_NAME,
					u,
					-1,
					-1));
		}
        // If got to here, there is no problem to report.
        return null;
	}
}
