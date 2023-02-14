package gov.nasa.pds.validate.ri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
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
    this.opts.addOption(Option.builder("A")
        .argName("auth-file")
        .desc("file with the URL and credential content to have full (all product states) read-only access to the registry API")
        .hasArg(true)
        .longOpt("auth-api")
        .numberOfArgs(1)
        .optionalArg(true)
        .build());
    this.opts.addOption(Option.builder("a")
        .argName("auth-file")
        .desc("file with the URL and credential content to have full, direct read-only access to the search DB")
        .hasArg(true)
        .longOpt("auth-search")
        .numberOfArgs(1)
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
        .desc("the complete LIDVID to start doing reference checks and will be walked down to the product level if it does not start there")
        .hasArg(true)
        .longOpt("lidvid")
        .optionalArg(true)
        .build());
  }

  private void help() {
    new HelpFormatter().printHelp("ValidateReferenceIntegrity",
        "Checks the search DB that all references exist. If the api-auth is provided, then it will also check that the registry API also finds all the references. For lidvid, multiple values can be given using a comma like 'urn:foo::1.0,urn:bar::2.0'.",
        opts,
        "An auth-file is either a text file of the Java property format with two variables: 'url' and 'credentials'. The 'url' property should be the complete base URL like 'https://localhost:9876/base' and 'credentials' a java property file with the user name, password, and other credential information as that used by harvest. Or it is an XML text file used by harvest with <registry> containing the 'auth' attribute.",
        true);
  }

  public void process(String[] args) {
    try {
      CommandLine cl = new DefaultParser().parse(this.opts, args);
      
      if (cl.hasOption('h')) {
        this.help();
        return;
      }
      
      if (!cl.hasOption("a"))
        throw new ParseException("Must provide search authorization information.");
      if (!cl.hasOption("l"))
        throw new ParseException("Must provide at least one LIDVID as a starting point.");
      if (!cl.hasOption("A"))
        log.warning ("Only the DB will be checked because no registry authorization was given");

      this.temp(Arrays.asList(cl.getOptionValue("lidvid").split(",")),
          AuthInformation.buildFrom(cl.getOptionValue("auth-api", "")),
          AuthInformation.buildFrom(cl.getOptionValue("auth-search")));
    }
    catch (ParseException pe) {
      System.err.println("[ERROR] " + pe.getMessage());
      this.help();
    }
  }
  
  public void temp(List<String> lidvids, AuthInformation registry, AuthInformation search) {
    System.out.println ("lidvids:  " + lidvids);
    System.out.println ("registry: " + registry);
    System.out.println ("search:   " + search);
  }
}
