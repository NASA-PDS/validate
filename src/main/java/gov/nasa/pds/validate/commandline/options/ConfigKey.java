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
// $Id: ConfigKey.java -1M 2010-11-04 18:13:37Z (local) $
package gov.nasa.pds.validate.commandline.options;

/**
 * An interface that contains the valid property keys for the Validate Tool
 * configuration file.
 *
 * @author mcayanan
 *
 */
public class ConfigKey {
  /** List of file patterns to look for if traversing a target directory. */
  public static final String REGEXP = "validate.regexp";

  /** The report. */
  public static final String REPORT = "validate.report";

  /** A list of targets to validate. */
  public static final String TARGET = "validate.target";

  /** A severity level for the output report. */
  public static final String VERBOSE = "validate.verbose";

  /** A list of schema files to validate against. */
  public static final String SCHEMA = "validate.schema";

  /** A list of schematron files to validate against. */
  public static final String SCHEMATRON = "validate.schematron";

  /** The catalog file to use during validation. */
  public static final String CATALOG = "validate.catalog";

  /** Property to specify one or more checksum manifest files. */
  public static final String CHECKSUM = "validate.checksum";

  /**
   * List of paths to use as the base when looking up file references
   * in a checksum manifest file.
   */
  public static final String BASE_PATH = "validate.basePath";

  /** The model version to use during validation. */
  public static final String MODEL = "validate.model";

  /** Enables/disables direcotry recursion. */
  public static final String LOCAL = "validate.local";

  /** Configures the report style format. */
  public static final String STYLE = "validate.reportStyle";

  /** Force the tool to validate against the schema and schematron specified
   *  in the label.
   */
  public static final String FORCE = "validate.force";
  
  /**
   * Property to specify the validation rule type.
   */
  public static final String RULE = "validate.rule";
  
  /**
   * Property to disable data content validation.
   */
  public static final String NO_DATA = "validate.noDataCheck";
  
  /**
   * Property to specify the maximum number of errors to report before 
   * terminating a validation run.
   */
  public static final String MAX_ERRORS = "validate.maxErrors";
  
  /**
   * Property to specify how many lines or records to skip during
   * content validation.
   */
  public static final String SPOT_CHECK_DATA = "validate.spotCheckData";
  
  /**
   * Property to allow the tool to not report on unlabeled files in a bundle
   * or collection.
   */
  public static final String ALLOW_UNLABELED_FILES = "validate.allowUnlabeledFiles";
}
