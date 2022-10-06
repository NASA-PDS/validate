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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Implements a validation rule enforcing file and directory naming standards.
 */
public class FileAndDirectoryNamingRule extends AbstractValidationRule {
  private static final Logger LOG = LoggerFactory.getLogger(FileAndDirectoryNamingRule.class);
  protected static final int MAXIMUM_FILE_NAME_LENGTH = 255;

  protected static final Pattern NAMING_PATTERN = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_.-]*");

  private static final Set<String> PROHIBITED_BASE_NAMES = new HashSet<>();
  static {
    PROHIBITED_BASE_NAMES.add("aux");
    PROHIBITED_BASE_NAMES.add("com1");
    PROHIBITED_BASE_NAMES.add("com2");
    PROHIBITED_BASE_NAMES.add("com3");
    PROHIBITED_BASE_NAMES.add("com4");
    PROHIBITED_BASE_NAMES.add("com5");
    PROHIBITED_BASE_NAMES.add("com6");
    PROHIBITED_BASE_NAMES.add("com7");
    PROHIBITED_BASE_NAMES.add("com8");
    PROHIBITED_BASE_NAMES.add("com9");
    PROHIBITED_BASE_NAMES.add("con");
    PROHIBITED_BASE_NAMES.add("core");
    PROHIBITED_BASE_NAMES.add("lpt1");
    PROHIBITED_BASE_NAMES.add("lpt2");
    PROHIBITED_BASE_NAMES.add("lpt3");
    PROHIBITED_BASE_NAMES.add("lpt4");
    PROHIBITED_BASE_NAMES.add("lpt5");
    PROHIBITED_BASE_NAMES.add("lpt6");
    PROHIBITED_BASE_NAMES.add("lpt7");
    PROHIBITED_BASE_NAMES.add("lpt8");
    PROHIBITED_BASE_NAMES.add("lpt9");
    PROHIBITED_BASE_NAMES.add("nul");
    PROHIBITED_BASE_NAMES.add("prn");
  }

  /**
   * Checks that the files and directories in the target conform to the naming rules in sections
   * 6C.1 and 6C.2.
   */
  @ValidationTest
  public void checkFileAndDirectoryNaming() {
    Crawler crawler = getContext().getCrawler();
    try {
      checkFileAndDirectoryNaming(crawler.crawl(getTarget()));
    } catch (IOException io) {
      reportError(GenericProblems.UNCAUGHT_EXCEPTION, getTarget(), -1, -1, io.getMessage());
    }
  }

  // Protected function to allow this and children function to check whether a
  // given filename is prohibited or not.
  protected boolean isFilenameProhibited(String filename) {
    boolean filenameIsProhibitedFlag = false;
    if (PROHIBITED_BASE_NAMES.contains(filename)) {
      filenameIsProhibitedFlag = true;
    }
    LOG.debug("isFilenameProhibited:filename,filenameIsProhibitedFlag {},{}", filename,
        filenameIsProhibitedFlag);
    return (filenameIsProhibitedFlag);
  }

  // Protected function to allow this and children function to check whether a
  // given filename is too long or not.
  protected boolean isFilenameTooLong(String filename) {
    boolean filenameIsTooLongFlag = false;
    // File names must be no longer than 255 characters.
    if (filename.length() > MAXIMUM_FILE_NAME_LENGTH) {
      filenameIsTooLongFlag = true;
    }
    LOG.debug("isFilenameTooLong:filename,filenameIsTooLongFlag {},{}", filename,
        filenameIsTooLongFlag);
    return (filenameIsTooLongFlag);
  }

  // Protected function to allow this and children function to check whether a
  // given filename contains only valid characters.
  protected boolean isFilenameContainingLegalCharacters(String filename) {
    boolean filenameContainsLegalCharactersFlag = false;
    // File names must use legal characters.
    Matcher matcher = NAMING_PATTERN.matcher(filename);
    if (matcher.matches()) {
      filenameContainsLegalCharactersFlag = true;
    }
    LOG.debug(
        "isFilenameContainingLegalCharacters:filename,filenameContainsLegalCharactersFlag {},{}",
        filename, filenameContainsLegalCharactersFlag);
    return (filenameContainsLegalCharactersFlag);
  }

  // Protected function to allow this and children function to check whether a
  // given directory contains extension or not.
  protected boolean isDirectoryContainingInvalidCharacter(String filename, boolean isDirectory) {
    boolean filenameHasExtensionFlag = false;

    // Additionally, directories cannot have extensions.
    if (isDirectory && filename.contains(".")) {
      filenameHasExtensionFlag = true;
    }
    LOG.debug(
        "isDirectoryContainingInvalidCharacter:filename,isDirectory,filenameHasExtensionFlag {},{},{}",
        filename, isDirectory, filenameHasExtensionFlag);
    return (filenameHasExtensionFlag);
  }

  void checkFileAndDirectoryNaming(List<Target> list) {
    Map<String, String> seenNames = new HashMap<>();

    for (Target t : list) {
      checkFileOrDirectoryName(t.getUrl(), seenNames, t.isDir());
    }
  }

  // Default scope, for unit testing.
  void checkFileOrDirectoryName(URL u, Map<String, String> seenNames, boolean isDirectory) {
    String name = FilenameUtils.getName(Utility.removeLastSlash(u.toString()));

    // File names must be no longer than 255 characters.
    // Use member function to perform the check.
    if (isFilenameTooLong(name)) {
      reportError(
          isDirectory ? PDS4Problems.DIRECTORY_NAME_TOO_LONG : PDS4Problems.FILE_NAME_TOO_LONG, u,
          -1, -1);
    }

    // File names must use legal characters.
    // Use member function to perform the check.
    if (!isFilenameContainingLegalCharacters(name)) {
      LOG.error("File containing invalid characters " + name);
      reportError(isDirectory ? PDS4Problems.DIRECTORY_NAME_USES_INVALID_CHARACTER
          : PDS4Problems.FILE_NAME_USES_INVALID_CHARACTER, u, -1, -1);
    }

    // Additionally, directories cannot have extensions.
    // Use member function to perform the check.
    if (isDirectoryContainingInvalidCharacter(name, isDirectory)) {
      LOG.error("Directory containing invalid character " + name);
      reportError(PDS4Problems.DIRECTORY_NAME_USES_INVALID_CHARACTER, u, -1, -1);
    }

    // File names must be unique, up to case-insensitivity.
    String lcName = name.toLowerCase(Locale.getDefault());
    if (!seenNames.containsKey(lcName)) {
      seenNames.put(lcName, name);
    } else {
      reportError(isDirectory ? PDS4Problems.DIRECTORY_NAME_CONFLICTS_IN_CASE
          : PDS4Problems.FILE_NAME_CONFLICTS_IN_CASE, u, -1, -1);
    }

    // Prohibited file names.
    // The file "core" is checked below in isFilenameProhibited() function so no
    // need to check here.
    if (!isDirectory && ("a.out".equalsIgnoreCase(name))) {
      LOG.error("File name is prohibited " + name);
      reportError(PDS4Problems.UNALLOWED_FILE_NAME, u, -1, -1);
    }

    // Note: The check (PROHIBITED_BASE_NAMES.contains(baseName) is too strict and
    // commented
    // out. It checks if the base name,
    // e.g. 'core' of 'core.vmac' is in PROHIBITED_BASE_NAMES set.
    // So it thinks core.vmac is invalid, when in reality it is a valid file.
    // A better check is against the variable 'name' instead of 'baseName':
    // the entire name of the file (name plus extension).
    //
    // So with the new check:
    //
    // 'core' should not be valid
    // 'core.vmac' should be valid
    // 'lpt8' should not be valid
    // 'com4' should not be valid
    // 'file_aux_should_be_ok.tab' should be valid
    // 'lpt9.tab' should be valid
    // 'lpt9_should_be_ok.tab' should be valid
    // 'this_file_should_be_ok_core.tab' should be valid

    // LOG.debug("checkFileAndDirectoryNaming:name,baseName,PROHIBITED_BASE_NAMES.contains(baseName)
    // {},{},{}",name,baseName,PROHIBITED_BASE_NAMES.contains(baseName));
    // LOG.debug("checkFileAndDirectoryNaming:name,PROHIBITED_BASE_NAMES.contains(name)
    // {},{},{}",name,PROHIBITED_BASE_NAMES.contains(name));
    // Use the common isFilenameProhibited() function to check against prohibited
    // basename.
    if (isFilenameProhibited(name)) {
      LOG.error("File name is prohibited " + name);
      reportError(
          isDirectory ? PDS4Problems.UNALLOWED_DIRECTORY_NAME : PDS4Problems.UNALLOWED_BASE_NAME, u,
          -1, -1);
    }
  }

  @Override
  public boolean isApplicable(String location) {
    return Utility.isDir(location);
  }

}
