// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.validate.commandline.options.ConfigKeys;
import gov.nasa.pds.validate.commandline.options.InvalidOptionException;
import gov.nasa.pds.validate.commandline.options.ToolsOption;
import gov.nasa.pds.validate.commandline.options.Flags;
import gov.nasa.pds.validate.report.FullReport;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.validate.target.Target;
import gov.nasa.pds.validate.target.TargetType;
import gov.nasa.pds.validate.util.Utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.xml.sax.SAXException;

/**
 * Wrapper class for the Validate Tool. Class handles command-line parsing and
 * querying, in addition to reporting setup.
 *
 * @author mcayanan
 *
 */
public class ValidateLauncher implements Flags, ConfigKeys {
    private final String PROPERTYFILE = "validate.properties";
    private final String PROPERTYTOOLNAME = "validate.name";
    private final String PROPERTYVERSION = "validate.version";
    private final String PROPERTYDATE = "validate.date";
    private final String PROPERTYCOPYRIGHT = "validate.copyright";

    private List<Target> targets;
    private List<String> regExps;
    private List<File> schemas;
    private File reportFile;
    private boolean traverse;
    private Level severity;

    private Options options;
    private Report report;

    /**
     * Constructor.
     *
     */
    public ValidateLauncher() {
        targets = new ArrayList<Target>();
        regExps = new ArrayList<String>();
        schemas = new ArrayList<File>();
        reportFile = null;
        traverse = true;
        severity = Level.WARNING;

        options = buildOptions();
        report = new FullReport();
    }

    /**
     * Builds the command-line option flags.
     *
     * @return A class representation of the command-line option flags.
     */
    private Options buildOptions() {
        ToolsOption to = null;
        Options options = new Options();

        options.addOption(new ToolsOption(
                HELP[SHORT], HELP[LONG], WHATIS_HELP));
        options.addOption(new ToolsOption(
                VERSION[SHORT], VERSION[LONG], WHATIS_VERSION));
        options.addOption(new ToolsOption(
                LOCAL[SHORT], LOCAL[LONG], WHATIS_LOCAL));

        // Option to specify a file pattern to seach for specific files
        // within a target directory
        to = new ToolsOption(REGEXP[SHORT], REGEXP[LONG], WHATIS_REGEXP);
        to.hasArgs(REGEXP[ARGNAME], String.class);
        options.addOption(to);

        //Option to specify the report file name
        to = new ToolsOption(REPORT[SHORT], REPORT[LONG], WHATIS_REPORT);
        to.hasArg(REPORT[ARGNAME], String.class);
        options.addOption(to);

        //Option to specify the report file name
        to = new ToolsOption(SCHEMA[SHORT], SCHEMA[LONG], WHATIS_SCHEMA);
        to.hasArgs(SCHEMA[ARGNAME], String.class);
        options.addOption(to);

        // Option to specify the label(s) to validate
        to = new ToolsOption(TARGET[SHORT], TARGET[LONG], WHATIS_TARGET);
        to.hasArgs(TARGET[ARGNAME], String.class);
        options.addOption(to);

        // Option to specify the severity level and above
        to = new ToolsOption(CONFIG[SHORT], CONFIG[LONG], WHATIS_CONFIG);
        to.hasArg(VERBOSE[ARGNAME], String.class);
        options.addOption(to);

        // Option to specify the severity level and above
        to = new ToolsOption(VERBOSE[SHORT], VERBOSE[LONG], WHATIS_VERBOSE);
        to.hasArg(VERBOSE[ARGNAME], short.class);
        options.addOption(to);

        return options;
    }

    /**
     * Parse the command-line arguments
     *
     * @param args The command-line arguments
     * @return A class representation of the command-line arguments
     *
     * @throws ParseException
     * @throws InvalidOptionException
     */
    public CommandLine parse(String[] args)
    throws ParseException, InvalidOptionException {
        CommandLineParser parser = new GnuParser();
        return parser.parse(options, args);
    }

    public void query(CommandLine line) throws Exception {
        List<Option> processedOptions = Arrays.asList(line.getOptions());
        List<String> targetList = new ArrayList<String>();

        //Gets the implicit targets
        for(java.util.Iterator<String> i = line.getArgList().iterator();
        i.hasNext();) {
            String[] values = i.next().split(",");
            for(int index=0; index < values.length; index++) {
                targetList.add(values[index].trim());
            }
        }
        for(Option o : processedOptions) {
            if(HELP[SHORT].equals(o.getOpt())) {
                displayHelp();
                System.exit(0);
            } else if(VERSION[SHORT].equals(o.getOpt())) {
                displayVersion();
                System.exit(0);
            } else if(CONFIG[SHORT].equals(o.getOpt())) {
                File c = new File(o.getValue());
                if(c.exists()) {
                    query(c);
                } else {
                    throw new Exception("Configuration file does not exist: "
                            + c);
                }
            } else if(REPORT[SHORT].equals(o.getOpt())) {
                setReport(new File(o.getValue()));
            } else if(LOCAL[SHORT].equals(o.getOpt())) {
                setTraverse(false);
            } else if(SCHEMA[SHORT].equals(o.getOpt())) {
                setSchemas(o.getValuesList());
            } else if(TARGET[SHORT].equals(o.getOpt())) {
                targetList.addAll(o.getValuesList());
            } else if(VERBOSE[SHORT].equals(o.getOpt())) {
                short value = 0;
                try {
                   value = Short.parseShort(o.getValue());
                } catch(IllegalArgumentException a) {
                    throw new InvalidOptionException(
                            "Problems Parsing severity level value.");
                }
                setSeverity(value);
            } else if(REGEXP[SHORT].equals(o.getOpt())) {
                setRegExps((List<String>) o.getValuesList());
            }
        }
        if(!targetList.isEmpty()) {
            setTargets(targetList);
        }
    }

    /**
     * Query the configuration file.
     *
     * @param configuration A configuration file.
     *
     * @throws ConfigurationException
     */
    public void query(File configuration) throws ConfigurationException {
        try {
            Configuration config = null;
            AbstractConfiguration.setDefaultListDelimiter(',');
            config = new PropertiesConfiguration(configuration);
            if(config.isEmpty()) {
                throw new ConfigurationException(
                        "Configuration file is empty: " + configuration);
            }
            if(config.containsKey(REGEXPKEY)) {
                // Removes quotes surrounding each pattern being specified
                List<String> list = config.getList(REGEXPKEY);
                list = Utility.removeQuotes(list);
                setRegExps(list);
            }
            if(config.containsKey(REPORTKEY)) {
                setReport(new File(config.getString(REPORTKEY)));
            }
            if(config.containsKey(TARGETKEY)) {
                // Removes quotes surrounding each pattern being specified
                List<String> list = config.getList(TARGETKEY);
                list = Utility.removeQuotes(list);
                setTargets(list);
            }
            if(config.containsKey(VERBOSEKEY)) {
                setSeverity(config.getShort(VERBOSEKEY));
            }
            if(config.containsKey(SCHEMAKEY)) {
                // Removes quotes surrounding each pattern being specified
                List<String> list = config.getList(SCHEMAKEY);
                list = Utility.removeQuotes(list);
                setSchemas(list);
            }
            if(config.containsKey(LOCALKEY)) {
                if(config.getBoolean(LOCALKEY) == true) {
                    setTraverse(false);
                } else {
                    setTraverse(true);
                }
            }
        } catch(Exception e) {
            throw new ConfigurationException(e.getMessage());
        }
    }

    private void setTargets(List<String> targets) {
        this.targets.clear();
        while(targets.remove(""));
        for(String t : targets) {
            this.targets.add(new Target(t));
        }
    }

    private void setSchemas(List<String> schemas) {
        while(schemas.remove(""));
        List<File> list = new ArrayList<File>();
        for(String s : schemas) {
             list.add(new File(s));
         }
        this.schemas = list;
    }

    private void setReport(File report) {
        this.reportFile = report;
    }

    private void setTraverse(boolean value) {
        this.traverse = value;
    }

    private void setSeverity(int level) {
        if(level < 1 || level > 3) {
            throw new IllegalArgumentException(
                    "Severity level value can only be 1, 2, or 3");
        }
        if(level == 1) {
            this.severity = Level.INFO;
        } else if(level == 2) {
            this.severity = Level.WARNING;
        } else if(level == 3) {
            this.severity = Level.SEVERE;
        }
    }

    private void setRegExps(List<String> patterns) {
        this.regExps = patterns;
        while(this.regExps.remove(""));
    }

    /**
     * Displays tool usage.
     *
     */
    public void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "validate <policy file> <options>",
                null, options, null);
    }

    /**
     * Displays the current version and disclaimer notice.
     *
     * @throws IOException
     */
    public void displayVersion() throws IOException {
        URL propertyFile = this.getClass().getResource(PROPERTYFILE);
        Properties p  = new Properties();
        InputStream in = null;
        try {
            in = propertyFile.openStream();
            p.load(in);
        } finally {
            in.close();
        }
        System.err.println("\n" + p.get(PROPERTYTOOLNAME));
        System.err.println(p.get(PROPERTYVERSION));
        System.err.println("Release Date: " + p.get(PROPERTYDATE));
        System.err.println(p.get(PROPERTYCOPYRIGHT) + "\n");
    }

    private void setupReport() throws IOException {
        if(reportFile != null) {
            report.setOutput(reportFile);
        }
        Properties p = new Properties();
        InputStream in = null;
        try {
           in = this.getClass().getResource(PROPERTYFILE).openStream();
           p.load(in);
        } finally {
            in.close();
        }
        String version = p.getProperty(PROPERTYVERSION)
           .replaceFirst("Version", "").trim();
        SimpleDateFormat df = new SimpleDateFormat(
                "EEE, MMM dd yyyy 'at' hh:mm:ss a");
        Date date = Calendar.getInstance().getTime();
        List<String> files = new ArrayList<String>();
        for(Target t : targets) {
            files.add(t.getFilename());
        }
        report.addConfiguration("   Version                  " + version);
        report.addConfiguration("   Time                     "
                + df.format(date));
        report.addConfiguration("   Core Schemas             "
                + VersionInfo.getSchemas());
        report.addConfiguration("   Model Version            "
                + VersionInfo.getModelVersion());
        report.addParameter("   Target(s)                "
                    + files);
        if(!schemas.isEmpty()) {
            report.addParameter("   User-Specified Schemas   " + schemas);
        }
        report.addParameter("   Severity Level           Warnings");
        report.addParameter("   Recurse Directories      " + traverse);
        if(!regExps.isEmpty()) {
            report.addParameter("   File Filter(s) Used      " + regExps);
        }
        report.printHeader();
    }

    /**
     * Performs validation.
     *
     * @throws SAXException
     */
    private void doValidation() throws SAXException {
        Validator validator = new FileValidator(report);
        for(Target t : targets) {
            if(TargetType.DIRECTORY.equals(t.getType())) {
                DirectoryValidator dv = new DirectoryValidator(report);
                dv.setFileFilters(regExps);
                dv.setRecurse(traverse);
                validator = dv;
            }/*
            else if(COLLECTION.equals(t.getType())) {
                validator = new CollectionValidator(report);
            } else if(BUNDLE.equals(t.getType())) {
                validator = new BundleValidator(report);
            }*/
            if(!schemas.isEmpty()) {
                validator.setSchema(schemas);
            }
            validator.validate(new File(t.getFilename()));
        }
    }

    private void printReportFooter() {
        report.printFooter();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("\nType 'Validate -h' for usage");
            System.exit(0);
        }
        ConsoleAppender ca = new ConsoleAppender(new PatternLayout("%-5p %m%n"));
        ca.setThreshold(Priority.FATAL);
        BasicConfigurator.configure(ca);
        ValidateLauncher launcher = new ValidateLauncher();
        try {
            CommandLine cmdLine = launcher.parse(args);
            launcher.query(cmdLine);
            launcher.setupReport();
            launcher.doValidation();
            launcher.printReportFooter();
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
    }
}
