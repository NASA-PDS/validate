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
// $Id: Flag.java -1M 2010-11-04 20:57:24Z (local) $
package gov.nasa.pds.validate.commandline.options;

/**
 * Class that holds the command-line option flags.
 *
 * @author mcayanan
 *
 */
public enum Flag {
  BASE_PATH("B", "base-path", "path", String.class, "Specify a path "
      + "for the tool to use in order to properly resolve relative file "
      + "references found in a checksum manifest file."),

  CATALOG("C", "catalog", "catalog files", String.class, true,
      "Specify catalog files to use during validation."),

  /** Flag to specify a configuration file to configure the tool behavior.
   */
  CONFIG("c", "config", "file", String.class, "Specify a configuration "
      + "file to set the tool behavior."),
  
  /**
   * Flag to specify file patterns to look for when validating a target
   * directory.
   */
  MAX_ERRORS("E", "max-errors", "value", short.class, "Specify the max "
      + "number of errors that the tool will report on before gracefully "
      + "exiting a validation run. Default is 100,000."),
  
  /**
   * Flag to specify file patterns to look for when validating a target
   * directory.
   */
  REGEXP("e", "regexp", "patterns", String.class, true, "Specify file patterns "
      + "to look for when validating a directory. Each pattern should "
      + "be surrounded by quotes. Default is to look for files ending with "
      + "a '.xml' or '.XML' file extension."),

  /**
   * Flag to force the tool to perform validation against the schema and
   * schematron specified in a given label.
   */
  FORCE("f", "force", "Force the tool to perform validation against the "
      + "schema and schematron specified in a given label."),

  /**
   * Flag to display the help.
   */
  HELP("h", "help", "Display usage."),

  /**
   * Flag that disables recursion when traversing a target directory.
   */
  LOCAL("L", "local", "Validate files only in the target directory rather "
      + "than recursively traversing down the subdirectories."),

  /**
   * Flag to specify one or more checksum manifest files in order to
   * perform checksum validation.
   */
  CHECKSUM_MANIFEST("M", "checksum-manifest", "file", String.class,
      "Specify a checksum manifest file to perform checksum validation "
      + "against the targets being validated."),

  /**
   * Flag to specify a model version to use during validation.
   */
  MODEL("m", "model-version", "version", String.class, "Specify a model "
      + "version to use during validation. The default is to use the "
      + "latest model."),

  /** Flag to disable data content validation. */
  NO_DATA("D", "no-data-check", "Disable data content validation."),
  
  /**
   * Flag to specify a report file name.
   */
  REPORT("r", "report-file", "file name", String.class, "Specify the "
      + "report file name. Default is standard out."),

  /**
   * Flag to specify a list of schematron files to use during validation.
   */
  SCHEMATRON("S", "schematron", "schematron files", String.class, true,
    "Specify schematron files."),

  /**
   * Flag to specify the report style.
   *
   */
  STYLE("s", "report-style", "full|json|xml", String.class,
      "Specify the level of detail for the reporting. Valid values are "
      + "'full' for a full view, 'json' for a json view, and 'xml' for an "
      + "XML view. Default is to see a full report if this flag is not "
      + "specified"),

  /**
   * Flag to explicitly specify the targets to validate.
   */
  TARGET("t", "target", "files,dirs", String.class, true, "Explicitly specify "
      + "the targets (files, directories) to validate. Targets can be "
      + "specified implicitly as well. "
      + "(example: validate product.xml)"),

  /**
   * Displays the tool version.
   */
  VERSION("V", "version", "Display application version."),

  /**
   * Flag to specify the severity level and above to include in the report.
   */
  VERBOSE("v", "verbose", "1|2|3", short.class, "Specify the severity "
      + "level and above to include in the human-readable report: "
      + "(1=Info, 2=Warning, 3=Error). Default is Warning and above. "),

  /**
   * Flag to specify a list of schemas to use during validation.
   */
  SCHEMA("x", "schema", "schema files", String.class, true,
      "Specify schema files."),
  
  SPOT_CHECK_DATA(null, "spot-check-data", "num", int.class, "Tells the tool to skip every nth "
      + "records or lines during data content validation."),
  
  
  RULE("R", "rule", "validation rule name", String.class,
	   "Specifies the validation rules to apply. (pds4.bundle|pds4.collection|pds4.folder|pds4.label|pds3.volume)."
	   + " Default is to auto-detect based on the contents at the location specified.");

  /** The short name. */
  private final String shortName;

  /** The long name. */
  private final String longName;

  /** The argument name. */
  private final String argName;

  /** The type of argument that the flag accepts. */
  private final Object argType;

  /** A flag that allows multiple argument values. */
  private final boolean allowsMultipleArgs;

  /** A description of the flag. */
  private final String description;

  /**
   * Constructor.
   *
   * @param shortName The short name.
   * @param longName The long name.
   * @param description A description of the flag.
   */
  private Flag(final String shortName, final String longName,
      final String description) {
    this(shortName, longName, null, null, description);
  }

  /**
   * Constructor for flags that can take arguments.
   *
   * @param shortName The short name.
   * @param longName The long name.
   * @param argName The argument name.
   * @param argType The argument type.
   * @param description A description of the flag.
   */
  private Flag(final String shortName, final String longName,
      final String argName, final Object argType,
      final String description) {
    this(shortName, longName, argName, argType, false, description);
  }

  /**
   * Constructor for flags that can take arguments.
   *
   * @param shortName The short name.
   * @param longName The long name.
   * @param argName The argument name.
   * @param argType The argument type.
   * @param description A description of the flag.
   */
  private Flag(final String shortName, final String longName,
      final String argName, final Object argType,
      final boolean allowsMultipleArgs, final String description) {
    this.shortName = shortName;
    this.longName = longName;
    this.argName = argName;
    this.argType = argType;
    this.allowsMultipleArgs = allowsMultipleArgs;
    this.description = description;
  }

  /**
   * Get the short name of the flag.
   *
   * @return The short name.
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Get the long name of the flag.
   *
   * @return The long name.
   */
  public String getLongName() {
    return longName;
  }

  /**
   * Get the argument name of the flag.
   *
   * @return The argument name.
   */
  public String getArgName() {
    return argName;
  }

  /**
   * Find out if the flag can handle multiple arguments.
   *
   * @return 'true' if yes.
   */
  public boolean allowsMultipleArgs() {
    return allowsMultipleArgs;
  }

  /**
   * Get the argument type of the flag.
   *
   * @return The argument type.
   */
  public Object getArgType() {
    return argType;
  }

  /**
   * Get the flag description.
   *
   * @return The description.
   */
  public String getDescription() {
    return description;
  }
}
