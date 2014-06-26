// Copyright 2006-2014, by the California Institute of Technology.
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

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.MissingLabelSchemaException;
import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.validate.commandline.options.ConfigKey;
import gov.nasa.pds.validate.commandline.options.Flag;
import gov.nasa.pds.validate.commandline.options.FlagOptions;
import gov.nasa.pds.validate.commandline.options.InvalidOptionException;
import gov.nasa.pds.validate.report.FullReport;
import gov.nasa.pds.validate.report.JSONReport;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.validate.report.XmlReport;
import gov.nasa.pds.validate.util.ToolInfo;
import gov.nasa.pds.validate.util.Utility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Wrapper class for the Validate Tool. Class handles command-line parsing and
 * querying, in addition to reporting setup.
 *
 * @author mcayanan
 *
 */
public class ValidateLauncher {
  /** List of targets to validate. */
  private List<URL> targets;

  /** List of regular expressions for the file filter. */
  private List<String> regExps;

  /** A list of user-given schemas to validate against. */
  private List<String> schemas;

  /** A list of catalog files to use during validation. */
  private List<String> catalogs;

  private List<String> schematrons;

  /** A report file. */
  private File reportFile;

  /** A flag to enable/disable directory recursion. */
  private boolean traverse;

  /** The severity level and above to include in the report. */
  private ExceptionType severity;

  /** An object representation of a Validate Tool report. */
  private Report report;

  /** Indicates the report style format. */
  private String reportStyle;

  /** The model version to use during validation. */
  private String modelVersion;

  /** Flag to force the tool to validate against the schema and schematron 
   *  specified in the given label. 
   */
  private boolean force;
  
  /**
   * Constructor.
   *
   */
  public ValidateLauncher() {
    targets = new ArrayList<URL>();
    regExps = new ArrayList<String>();
    catalogs = new ArrayList<String>();
    schemas = new ArrayList<String>();
    schematrons = new ArrayList<String>();
    reportFile = null;
    traverse = true;
    severity = ExceptionType.WARNING;
    modelVersion = VersionInfo.getDefaultModelVersion();
    report = null;
    reportStyle = "full";
    force = false;
  }

  /**
   * Parse the command-line arguments
   *
   * @param args The command-line arguments
   * @return A class representation of the command-line arguments
   *
   * @throws ParseException If an error occurred during parsing.
   */
  public CommandLine parse(String[] args) throws ParseException {
    CommandLineParser parser = new GnuParser();
    return parser.parse(FlagOptions.getOptions(), args);
  }

  /**
   * Query the command-line and process the command-line option flags set.
   *
   * @param line A CommandLine object containing the flags that were set.
   *
   * @throws Exception If an error occurred while processing the
   * command-line options.
   */
  public void query(CommandLine line) throws Exception {
    List<Option> processedOptions = Arrays.asList(line.getOptions());
    List<String> targetList = new ArrayList<String>();

    //Gets the implicit targets
    for (java.util.Iterator<String> i = line.getArgList().iterator();
    i.hasNext();) {
      String[] values = i.next().split(",");
      for (int index = 0; index < values.length; index++) {
        targetList.add(values[index].trim());
      }
    }
    for (Option o : processedOptions) {
      if (Flag.HELP.getShortName().equals(o.getOpt())) {
        displayHelp();
        System.exit(0);
      } else if (Flag.VERSION.getShortName().equals(o.getOpt())) {
        displayVersion();
        System.exit(0);
      } else if (Flag.CONFIG.getShortName().equals(o.getOpt())) {
        File c = new File(o.getValue());
        if (c.exists()) {
          query(c);
        } else {
          throw new Exception("Configuration file does not exist: " + c);
        }
      } else if (Flag.REPORT.getShortName().equals(o.getOpt())) {
        setReport(new File(o.getValue()));
      } else if (Flag.LOCAL.getShortName().equals(o.getOpt())) {
        setTraverse(false);
      } else if (Flag.CATALOG.getShortName().equals(o.getOpt())) {
        setCatalogs(o.getValuesList());
      } else if (Flag.SCHEMA.getShortName().equals(o.getOpt())) {
        setSchemas(o.getValuesList());
      } else if (Flag.SCHEMATRON.getShortName().equals(o.getOpt())) {
        setSchematrons(o.getValuesList());
      } else if (Flag.TARGET.getShortName().equals(o.getOpt())) {
        targetList.addAll(o.getValuesList());
      } else if (Flag.VERBOSE.getShortName().equals(o.getOpt())) {
        short value = 0;
        try {
         value = Short.parseShort(o.getValue());
        } catch (IllegalArgumentException a) {
         throw new InvalidOptionException("Problems parsing severity level "
             + "value.");
        }
        setSeverity(value);
      } else if (Flag.REGEXP.getShortName().equals(o.getOpt())) {
        setRegExps((List<String>) o.getValuesList());
      } else if (Flag.MODEL.getShortName().equals(o.getOpt())) {
        setModelVersion(o.getValue());
      } else if (Flag.STYLE.getShortName().equals(o.getOpt())) {
        setReportStyle(o.getValue());
      } else if (Flag.FORCE.getShortName().equals(o.getOpt())) {
        setForce(true);
      }
    }
    if (!targetList.isEmpty()) {
      setTargets(targetList);
    }
    if (force 
        && (!schemas.isEmpty() 
            || !schematrons.isEmpty() 
            || !catalogs.isEmpty())) {
      throw new InvalidOptionException("Cannot specify user schemas, "
            + "schematrons, and/or catalog files with the 'force' flag option");
    }
  }

  /**
   * Query the configuration file.
   *
   * @param configuration A configuration file.
   *
   * @throws ConfigurationException If an error occurred while querying
   * the configuration file.
   */
  public void query(File configuration) throws ConfigurationException {
    try {
      Configuration config = null;
      AbstractConfiguration.setDefaultListDelimiter(',');
      config = new PropertiesConfiguration(configuration);
      if (config.isEmpty()) {
        throw new ConfigurationException("Configuration file is empty: "
            + configuration);
      }
      if (config.containsKey(ConfigKey.REGEXP)) {
        // Removes quotes surrounding each pattern being specified
        List<String> list = config.getList(ConfigKey.REGEXP);
        list = Utility.removeQuotes(list);
        setRegExps(list);
      }
      if (config.containsKey(ConfigKey.REPORT)) {
        setReport(new File(config.getString(ConfigKey.REPORT)));
      }
      if (config.containsKey(ConfigKey.TARGET)) {
        // Removes quotes surrounding each pattern being specified
        List<String> list = config.getList(ConfigKey.TARGET);
        list = Utility.removeQuotes(list);
        setTargets(list);
      }
      if (config.containsKey(ConfigKey.VERBOSE)) {
        setSeverity(config.getShort(ConfigKey.VERBOSE));
      }
      if (config.containsKey(ConfigKey.SCHEMA)) {
        // Removes quotes surrounding each pattern being specified
        List<String> list = config.getList(ConfigKey.SCHEMA);
        list = Utility.removeQuotes(list);
        setSchemas(list);
      }
      if (config.containsKey(ConfigKey.SCHEMATRON)) {
        // Removes quotes surrounding each pattern being specified
        List<String> list = config.getList(ConfigKey.SCHEMATRON);
        list = Utility.removeQuotes(list);
        setSchematrons(list);
      }
      if (config.containsKey(ConfigKey.LOCAL)) {
        if (config.getBoolean(ConfigKey.LOCAL) == true) {
          setTraverse(false);
        } else {
          setTraverse(true);
        }
      }
      if (config.containsKey(ConfigKey.MODEL)) {
        setModelVersion(config.getString(ConfigKey.MODEL));
      }
      if (config.containsKey(ConfigKey.STYLE)) {
        setReportStyle(config.getString(ConfigKey.STYLE));
      }
      if (config.containsKey(ConfigKey.FORCE)) {
        setForce(config.getBoolean(ConfigKey.FORCE));
      }
    } catch (Exception e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  /**
   * Set the target.
   *
   * @param targets A list of targets.
   * @throws MalformedURLException
   */
  public void setTargets(List<String> targets)
  throws MalformedURLException {
    this.targets.clear();
    while (targets.remove(""));
    for (String t : targets) {
      URL url = null;
      try {
        url = new URL(t);
        this.targets.add(url);
      } catch (MalformedURLException u) {
        File file = new File(t);
        this.targets.add(file.toURI().normalize().toURL());
      }
    }
  }

  /**
   * Set the schemas.
   *
   * @param schemas A list of schemas.
   */
  public void setSchemas(List<String> schemas) {
    while (schemas.remove(""));
    this.schemas.addAll(schemas);
  }

  /**
   * Set the schematrons.
   *
   * @param schematrons A list of schematrons.
   */
  public void setSchematrons(List<String> schematrons) {
    while (schematrons.remove(""));
    this.schematrons.addAll(schematrons);
  }

  /**
   * Set the catalogs.
   *
   * @param catalogs A list of catalogs.
   */
  public void setCatalogs(List<String> catalogs) {
    while (catalogs.remove(""));
    this.catalogs.addAll(catalogs);
  }

  /**
   * Sets the report file.
   *
   * @param report A report file.
   */
  public void setReport(File report) {
    this.reportFile = report;
  }

  /**
   * Gets the object representation of the Validation Report.
   *
   * @return The Report object.
   */
  public Report getReport() {
    return report;
  }

  /**
   * Set the output style for the report.
   * @param style 'sum' for a summary report, 'min' for a minimal report,
   *  and 'full' for a full report
   * @throws ApplicationException
   */
  public void setReportStyle(String style) throws Exception {
    if ( (style.equalsIgnoreCase("full") == false) &&
        (style.equalsIgnoreCase("json") == false) &&
        (style.equalsIgnoreCase("xml") == false)) {
        throw new Exception(
            "Invalid value entered for 's' flag. Value can only "
            + "be either 'full', 'json' or 'xml'");
    }
    this.reportStyle = style;
  }

  /**
   * Sets the flag to enable/disable directory recursion.
   *
   * @param value A boolean value.
   */
  public void setTraverse(boolean value) {
    this.traverse = value;
  }

  /**
   * Sets the severity level for the report.
   *
   * @param level An interger value.
   */
  public void setSeverity(int level) {
    if (level < 1 || level > 3) {
      throw new IllegalArgumentException("Severity level value can only "
          + "be 1, 2, or 3");
    }
    if (level == 1) {
      this.severity = ExceptionType.INFO;
    } else if (level == 2) {
      this.severity = ExceptionType.WARNING;
    } else if (level == 3) {
      this.severity = ExceptionType.ERROR;
    }
  }

  /**
   * Sets the list of file patterns to look for if traversing a directory.
   *
   * @param patterns A list of file patterns.
   */
  public void setRegExps(List<String> patterns) {
    this.regExps = patterns;
    while (this.regExps.remove(""));
  }

  /**
   * Sets the model version to use during validation.
   *
   * @param version The model version.
   */
  public void setModelVersion(String version) {
    this.modelVersion = version;
  }

  public void setForce(boolean value) {
    this.force = value;
  }
  
  /**
   * Displays tool usage.
   *
   */
  public void displayHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(80, "validate <target> <options>", null,
        FlagOptions.getOptions(), null);
  }

  /**
   * Displays the current version and disclaimer notice.
   *
   * @throws IOException If there was an error that occurred while
   * getting the tool information.
   */
  public void displayVersion() throws IOException {
    String schema = VersionInfo.getSchemasFromJar(
        VersionInfo.getDefaultModelVersion()).toString()
        .replaceAll("[\\[\\]]", "");
    String schematron = VersionInfo.getSchematronsFromJar(
        VersionInfo.getDefaultModelVersion()).toString()
        .replaceAll("[\\[\\]]", "");

    System.err.println("\n" + ToolInfo.getName());
    System.err.println(ToolInfo.getVersion());
    System.err.println("Release Date: " + ToolInfo.getReleaseDate());
    System.err.println("Core Schema: " + schema);
    System.err.println("Core Schematron: " + schematron);
    System.err.println(ToolInfo.getCopyright() + "\n");
  }

  /**
   * Setup the report.
   *
   * @throws IOException If an error occurred while setting up the report.
   */
  public void setupReport() throws IOException {
    if (this.reportStyle.equals("full")) {
      this.report = new FullReport();
    } else if (this.reportStyle.equals("json")){
      this.report = new JSONReport();
    } else if (this.reportStyle.equals("xml")) {
      this.report = new XmlReport();
    }
    report.setLevel(severity);
    if (reportFile != null) {
      report.setOutput(reportFile);
    }
    String version = ToolInfo.getVersion().replaceFirst("Version", "").trim();
    SimpleDateFormat df = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = Calendar.getInstance().getTime();
    List<String> coreSchemas = VersionInfo.getSchemasFromJar(modelVersion);
    List<String> coreSchematrons =
      VersionInfo.getSchematronsFromJar(modelVersion);
    report.addConfiguration("   Version                       " + version);
    report.addConfiguration("   Date                          "
        + df.format(date));
    if (!force) {
      if (schemas.isEmpty() && catalogs.isEmpty()) {
        report.addConfiguration("   Core Schemas                  "
            + coreSchemas);
      }
      if (schematrons.isEmpty()) {
        report.addConfiguration("   Core Schematrons              "
            + coreSchematrons);
      }
      if ( schematrons.isEmpty() || (schemas.isEmpty() && catalogs.isEmpty()) ) {
        report.addConfiguration("   Model Version                 "
            + modelVersion);
      }
    }
    report.addParameter("   Targets                       " + targets);
    if (!schemas.isEmpty()) {
      report.addParameter("   User Specified Schemas        " + schemas);
    }
    if (!catalogs.isEmpty()) {
      report.addParameter("   User Specified Catalogs       " + catalogs);
    }
    if (!schematrons.isEmpty()) {
      report.addParameter("   User Specified Schematrons    " + schematrons);
    }
    report.addParameter("   Severity Level                " + severity.getName());
    report.addParameter("   Recurse Directories           " + traverse);
    if (!regExps.isEmpty()) {
      report.addParameter("   File Filters Used             " + regExps);
    }
    if (force) {
      report.addParameter("   Force Mode                    on");
    } else {
      report.addParameter("   Force Mode                    off");      
    }
    report.printHeader();
  }

  /**
   * Performs validation.
   *
   * @throws SAXException If one of the schemas is malformed.
   * @throws ParserConfigurationException 
   */
  public void doValidation() throws SAXException, ParserConfigurationException {
    for (URL target : targets) {
      Validator validator = new FileValidator(modelVersion, report);
      try {
        if (target.getProtocol().equalsIgnoreCase("file")) {
          File file = FileUtils.toFile(target);
          if (file.isDirectory()) {
            DirectoryValidator dv = new DirectoryValidator(modelVersion,
              report);
            dv.setFileFilters(regExps);
            dv.setRecurse(traverse);
            validator = dv;
          }
        } else if ("".equals(FilenameUtils.getExtension(target.toString()))) {
          DirectoryValidator dv = new DirectoryValidator(modelVersion,
              report);
          dv.setFileFilters(regExps);
          dv.setRecurse(traverse);
          validator = dv;
        }
        validator.setForce(force);
        if (!schemas.isEmpty()) {
          validator.setSchemas(schemas);
        }
        if (!catalogs.isEmpty()) {
          validator.setCatalogs(catalogs);
        }
        if (!schematrons.isEmpty()) {
          validator.setSchematrons(schematrons);
        }
        validator.validate(target);
      } catch (Exception e) {
        LabelException le = null;
        if (e instanceof MissingLabelSchemaException) {
          MissingLabelSchemaException mse = (MissingLabelSchemaException) e;
          le = new LabelException(ExceptionType.WARNING, mse.getMessage(), 
              target.toString(), target.toString(), null, null);
          try {
            report.recordSkip(target.toURI(), le);
          } catch (URISyntaxException u) {
            le = new LabelException(ExceptionType.FATAL,
                e.getMessage(), target.toString(), target.toString(),
                null, null);
          }
        } else {
          if (e instanceof SAXParseException) {
            SAXParseException se = (SAXParseException) e;
            le = new LabelException(ExceptionType.FATAL, se.getMessage(),
                target.toString(), target.toString(), se.getLineNumber(),
                se.getColumnNumber());
          } else {
            le = new LabelException(ExceptionType.FATAL,
                e.getMessage(), target.toString(), target.toString(),
                null, null);
          }
          try {
            report.record(target.toURI(), le);
          } catch (URISyntaxException u) {
            le = new LabelException(ExceptionType.FATAL,
                e.getMessage(), target.toString(), target.toString(),
                null, null);
          }
        }
      }
    }
  }

  /**
   * Print the report footer.
   *
   */
  public void printReportFooter() {
    report.printFooter();
  }

  /**
   * Wrapper method for the main class.
   *
   * @param args list of command-line arguments.
   */
  private void processMain(String[] args) {
    try {
      CommandLine cmdLine = parse(args);
      query(cmdLine);
      setupReport();
      doValidation();
      printReportFooter();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Main class that launches the Validate Tool.
   *
   * @param args A list of command-line arguments.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("\nType 'validate -h' for usage");
      System.exit(0);
    }
    ConsoleAppender ca = new ConsoleAppender(new PatternLayout("%-5p %m%n"));
    ca.setThreshold(Priority.FATAL);
    BasicConfigurator.configure(ca);
    new ValidateLauncher().processMain(args);
  }
}
