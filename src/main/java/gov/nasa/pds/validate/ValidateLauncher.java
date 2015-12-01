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
// $Id$
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.CachedEntityResolver;
import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.MissingLabelSchemaException;
import gov.nasa.pds.tools.label.SchematronTransformer;
import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.tools.label.validate.FileReferenceValidator;
import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.validate.checksum.ChecksumManifest;
import gov.nasa.pds.validate.commandline.options.ConfigKey;
import gov.nasa.pds.validate.commandline.options.Flag;
import gov.nasa.pds.validate.commandline.options.FlagOptions;
import gov.nasa.pds.validate.commandline.options.InvalidOptionException;
import gov.nasa.pds.validate.report.FullReport;
import gov.nasa.pds.validate.report.JSONReport;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.validate.report.XmlReport;
import gov.nasa.pds.validate.schema.SchemaValidator;
import gov.nasa.pds.validate.target.Target;
import gov.nasa.pds.validate.util.ToolInfo;
import gov.nasa.pds.validate.util.Utility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

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
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
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
  private List<URL> schemas;

  /** A list of catalog files to use during validation. */
  private List<String> catalogs;

  private List<URL> schematrons;

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
   * Flag to perform referential integrity.
   *
   */
  private boolean integrityCheck;

  /**
   * A checksum manifest file to use for checksum validation.
   *
   */
  private URL checksumManifest;

  /**
   * Default file filters.
   */
  private String[] DEFAULT_FILE_FILTERS = {"*.xml", "*.XML"};

  private SchemaValidator schemaValidator;

  private SchematronTransformer schematronTransformer;

  private List<Transformer> transformedSchematrons;

  private CachedEntityResolver resolver;

  /**
   * path to use as the base when looking up file references in
   * a manifest file.
   */
  private URL manifestBasePath;

  /**
   * Constructor.
   * @throws TransformerConfigurationException
   *
   */
  public ValidateLauncher() throws TransformerConfigurationException {
    targets = new ArrayList<URL>();
    regExps = new ArrayList<String>();
    catalogs = new ArrayList<String>();
    schemas = new ArrayList<URL>();
    schematrons = new ArrayList<URL>();
    checksumManifest = null;
    manifestBasePath = null;
    reportFile = null;
    traverse = true;
    severity = ExceptionType.WARNING;
    modelVersion = VersionInfo.getDefaultModelVersion();
    report = null;
    reportStyle = "full";
    force = false;
    integrityCheck = false;
    regExps.addAll(Arrays.asList(DEFAULT_FILE_FILTERS));
    schemaValidator = new SchemaValidator();
    schematronTransformer = new SchematronTransformer();
    transformedSchematrons = new ArrayList<Transformer>();
    resolver = new CachedEntityResolver();
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
      } else if (Flag.INTEGRITY.getShortName().equals(o.getOpt())) {
        setIntegrityCheck(true);
      } else if (Flag.CHECKSUM_MANIFEST.getShortName().equals(o.getOpt())) {
        setChecksumManifest(o.getValue());
      } else if (Flag.BASE_PATH.getShortName().equals(o.getOpt())) {
        setManifestBasePath(o.getValue());
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
    if (integrityCheck) {
      for (URL target : targets) {
        if (!Utility.toTarget(target).isDir()) {
          throw new InvalidOptionException("Must specify a target directory "
              + "when performing integrity checking: " + target);
        }
      }
    }
    if (checksumManifest != null) {
      if ( (targets.size() > 1) && (manifestBasePath == null) ) {
        throw new InvalidOptionException("Must specify the base path "
            + "flag option ('-B' flag) when specifying a checksum manifest "
            + "file and multiple targets.");
      }
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
      if (config.containsKey(ConfigKey.INTEGRITY)) {
        setIntegrityCheck(config.getBoolean(ConfigKey.INTEGRITY));
      }
      if (config.containsKey(ConfigKey.CHECKSUM)) {
        setChecksumManifest(config.getString(ConfigKey.CHECKSUM));
      }
      if (config.containsKey(ConfigKey.BASE_PATH)) {
        setManifestBasePath(config.getString(ConfigKey.BASE_PATH));
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
   * Set the checksum manifest.
   *
   * @param manifest A checksum manifest file.
   * @throws MalformedURLException
   */
  public void setChecksumManifest(String manifest)
  throws MalformedURLException {
    URL url = null;
    try {
      url = new URL(manifest);
      this.checksumManifest = url;
    } catch (MalformedURLException u) {
      File file = new File(manifest);
      this.checksumManifest = file.toURI().normalize().toURL();
    }
  }

  /**
   * Set the base path to look up relative file references in a
   * given checksum manifest file.
   *
   * @param path A path.
   * @throws MalformedURLException
   */
  public void setManifestBasePath(String path)
      throws MalformedURLException {
    this.manifestBasePath = Utility.toURL(path);
  }

  /**
   * Set the schemas.
   *
   * @param schemas A list of schemas.
   * @throws MalformedURLException
   */
  public void setSchemas(List<String> schemas) throws MalformedURLException {
    while (schemas.remove(""));
    for (String schema : schemas) {
      this.schemas.add(Utility.toURL(schema));
    }
  }

  /**
   * Set the schematrons.
   *
   * @param schematrons A list of schematrons.
   * @throws MalformedURLException
   */
  public void setSchematrons(List<String> schematrons)
      throws MalformedURLException {
    while (schematrons.remove(""));
    for (String schematron: schematrons) {
      this.schematrons.add(Utility.toURL(schematron));
    }
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

  public void setIntegrityCheck(boolean value) {
    this.integrityCheck = value;
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
    if (integrityCheck) {
      report.addParameter("   Referential Integrity Check   on");
    } else {
      report.addParameter("   Referential Integrity Check   off");
    }
    if (checksumManifest != null) {
      report.addParameter("   Checksum Manifest File        " + checksumManifest.toString());
      report.addParameter("   Manifest File Base Path       " + manifestBasePath.toString());
    }
    report.printHeader();
  }

  /**
   * Performs validation.
   * @throws Exception
   */
  public void doValidation(Map<URL, String> checksumManifest)
  throws Exception {
    ReferentialIntegrityValidator refIntegrityValidator =
        new ReferentialIntegrityValidator();
    FileReferenceValidator fileRefValidator = new FileReferenceValidator();
    if (!checksumManifest.isEmpty()) {
      fileRefValidator.setChecksumManifest(checksumManifest);
    }
    // Initialize the Factory Class
    List<DocumentValidator> docValidators = new ArrayList<DocumentValidator>();
    docValidators.add(fileRefValidator);
    if (integrityCheck) {
      docValidators.add(refIntegrityValidator);
    }
    ValidatorFactory factory = ValidatorFactory.getInstance();
    factory.setModelVersion(modelVersion);
    factory.setReport(report);
    factory.setDocumentValidators(docValidators);
    for (URL target : targets) {
      if (integrityCheck) {
        refIntegrityValidator.clearSources();
        System.out.println("Begin gathering LIDVIDs, bundle and collection "
            + "members from the given target: " + target);
        refIntegrityValidator.setSources(Utility.toTarget(target), traverse,
            regExps);
        System.out.println("Finished gathering LIDVIDs, bundle and "
            + "collection members from the given target: " + target);
      }
      try {
        Validator validator = factory.newInstance(target);
        validator.setForce(force);
        if (validator instanceof DirectoryValidator) {
          DirectoryValidator dv = (DirectoryValidator) validator;
          dv.setFileFilters(regExps);
          dv.setRecurse(traverse);
          validator = dv;
        }
        if (!schemas.isEmpty()) {
          validator.setSchemas(schemas);
          validator.setCachedEntityResolver(resolver);
          validator.setCachedLSResourceResolver(
              schemaValidator.getCachedLSResolver());
        }
        if (!catalogs.isEmpty()) {
          validator.setCatalogs(catalogs);
        }
        if (!transformedSchematrons.isEmpty()) {
          validator.setSchematrons(transformedSchematrons);
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
   * Transforms a given schematron.
   *
   * @param schematron A schematron to transform.
   * @param container Container to hold problems.
   *
   * @return The ISO Schematron transformer associated with the given
   * schematron.
   *
   * @throws TransformerException If an error occurred during the
   * transform process.
   */
  private Transformer transformSchematron(URL schematron,
      ExceptionContainer container) {
    Transformer transformer = null;
    try {
      transformer = schematronTransformer.transform(schematron,
          container);
      return transformer;
    } catch (Exception e) {
      container.addException(new LabelException(ExceptionType.FATAL,
          "Error occurred while processing schematron '"
          + schematron + "': " + e.getMessage(),
          schematron.toString()));
    }
    return transformer;
  }

  /**
   * Validates a given schema.
   *
   * @param schema The URL to the schema.
   *
   * @return 'true' if the schema was valid, 'false' otherwise.
   * @throws Exception
   */
  private boolean validateSchemas(List<URL> schemas)
      throws Exception {
    boolean isValid = true;
    List<StreamSource> sources = new ArrayList<StreamSource>();
    String locations = "";
    for (URL schema : schemas) {
      LSInput input = schemaValidator.getCachedLSResolver()
          .resolveResource("", "", "", schema.toString(), schema.toString());
      StreamSource streamSource = new StreamSource(
          input.getByteStream());
      streamSource.setSystemId(schema.toString());
      sources.add(streamSource);
      try {
        InputSource inputSource = new InputSource(
            input.getByteStream());
        inputSource.setSystemId(input.getSystemId());
        //TODO: Should we log an error if the targetNamespace is missing?
        XMLExtractor extractor = new XMLExtractor(inputSource);
        String namespace = extractor.getTargetNamespace();
        if (!namespace.isEmpty()) {
          locations += namespace + " " + schema + "\n";
        }
        inputSource.getByteStream().reset();
      } catch (Exception e) {
        throw new Exception("Error while getting targetNamespace of schema '"
            + schema.toString() + "': " + e.getMessage());
      }
    }
    schemaValidator.setExternalLocations(locations);
    for(StreamSource source : sources) {
      ExceptionContainer problems = schemaValidator.validate(source);
      if (problems.getExceptions().size() != 0) {
        isValid = false;
        report.record(new URI(source.getSystemId()),
            problems.getExceptions());
      }
    }
    return isValid;
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
      Map<URL, String> checksumManifestMap = new HashMap<URL, String>();
      if (checksumManifest != null) {
        if (manifestBasePath == null) {
          URL base = null;
          Target t = Utility.toTarget(targets.get(0));
          try {
            if (t.isDir()) {
              base = t.getUrl();
            } else {
              base = t.getUrl().toURI().getPath().endsWith("/") ?
                t.getUrl().toURI().resolve("..").toURL() :
                  t.getUrl().toURI().resolve(".").toURL();
            }
            manifestBasePath = base;
          } catch (URISyntaxException ue) {
            throw new IOException("Error occurred while getting parent of '"
                + t + "': " + ue.getMessage());
          }
        }
        ChecksumManifest cm = new ChecksumManifest(
            manifestBasePath.toString());
        try {
          checksumManifestMap.putAll(cm.read(checksumManifest));
        } catch (IOException io) {
          throw new Exception("Error occurred while reading checksum "
              + "manifest file '" + checksumManifest.toString() + "': "
              + io.getMessage());
        }
      }
      setupReport();
      // Validate schemas and schematrons first before performing label
      // validation
      boolean invalidSchemas = false;
      if (!schemas.isEmpty()) {
        if (!validateSchemas(schemas)) {
          invalidSchemas = true;
        }
      }
      boolean invalidSchematron = false;
      if (!schematrons.isEmpty()) {
        for (URL schematron : schematrons) {
          ExceptionContainer container = new ExceptionContainer();
          Transformer transformer = transformSchematron(schematron, container);
          if (container.getExceptions().size() != 0) {
            report.record(schematron.toURI(), container.getExceptions());
            invalidSchematron = true;
          } else {
            transformedSchematrons.add(transformer);
          }
        }
      }
      if ( !(invalidSchemas) && !(invalidSchematron) ) {
        doValidation(checksumManifestMap);
      }
      printReportFooter();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Main class that launches the Validate Tool.
   *
   * @param args A list of command-line arguments.
   * @throws TransformerConfigurationException
   */
  public static void main(String[] args)
      throws TransformerConfigurationException {
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
