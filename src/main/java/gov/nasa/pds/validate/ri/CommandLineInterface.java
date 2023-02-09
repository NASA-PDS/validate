package gov.nasa.pds.validate.ri;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineInterface {
  final private Options opts;

  public CommandLineInterface() {
    super();
    this.opts = new Options();
    this.opts.addOption(Option.builder("a")
        .argName("auth-file")
        .desc("a java property file with the URL and credential content to have full (all product states) read-only access to the registry API")
        .hasArg(true)
        .longOpt("auth-api")
        .numberOfArgs(1)
        .optionalArg(true)
        .build());
    this.opts.addOption(Option.builder("A")
        .argName("auth-file")
        .desc("a java property file with the URL and credential content to have full, direct read-only access to the search DB")
        .hasArg(true)
        .longOpt("auth-search")
        .numberOfArgs(1)
        .optionalArg(true)
        .build());
    this.opts.addOption(Option.builder("c")
        .argName("config-file")
        .desc("an XML file used with harvest ")
        .hasArg(true)
        .longOpt("config-harvest")
        .optionalArg(true)
        .build());
    this.opts.addOption(Option.builder("h")
        .desc("show this text and exit")
        .hasArg(false)
        .longOpt("help")
        .optionalArg(true)
        .build());
    this.opts.addOption(Option.builder("l")
        .argName("lidvid")
        .desc("an XML file used with harvest ")
        .hasArg(true)
        .longOpt("lidvid")
        .optionalArg(true)
        .build());
  }

  private void help() {
    new HelpFormatter().printHelp("ValidateReferenceIntegrity",
        "Checks the search DB that all references exist. If the api-auth is provided, then it will also check that the registry API also finds all the references. The lidvid and harvest config file cannot both be given. When the harvest config is given, the auth-search is ignored. For lidvid and config-harvest, multiple values can be given using a comma like 'fileA.xml,fileB.xml'.",
        opts,
        "An auth-file is a text file of the Java property format with two variables: 'url' and 'credentials'. The 'url' property should be the complete URL like 'https://localhost:9876/root/endpoint' and 'credentials' a java property file with the user name, password, and other credential information as those used by harvest.",
        true);
  }

  public void process(String[] args) {
    try {
      CommandLine cl = new DefaultParser().parse(this.opts, args);
      
      if (cl.hasOption('h')) {
        this.help();
        return;
      }
      
      if (cl.hasOption("c") && cl.hasOption("l"))
        throw new ParseException("Cannot process both a lidvid and harvest config file at the same time.");
      if (!cl.hasOption("c") && !cl.hasOption("l"))
        throw new ParseException("Must provide either a lidvid or a harvest config.");
      if (cl.hasOption("l") && !cl.hasOption("A"))
        throw new ParseException("Must provide the auth-search when providing lidvid(s).");

      System.out.println(cl.getOptionValue("l"));
    }
    catch (ParseException pe) {
      System.err.println("[ERROR] " + pe.getMessage());
      this.help();
    }
  }
}
