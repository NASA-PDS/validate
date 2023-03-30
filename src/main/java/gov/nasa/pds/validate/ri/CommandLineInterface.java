package gov.nasa.pds.validate.ri;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class CommandLineInterface {
  final private Logger log = LogManager.getLogger(CommandLineInterface.class);
  private long broken = -1, total = -1;
  final private Options opts;

  public CommandLineInterface() {
    super();
    this.opts = new Options();
    this.opts.addOption(Option.builder("A").argName("auth-file").desc(
        "file with the URL and credential content to have full (all product states) read-only access to the registry API")
        .hasArg(true).longOpt("auth-api").numberOfArgs(1).optionalArg(true).build());
    this.opts.addOption(Option.builder("a").argName("auth-file").desc(
        "file with the URL and credential content to have full, direct read-only access to the search DB")
        .hasArg(true).longOpt("auth-opensearch").numberOfArgs(1).optionalArg(true).build());
    this.opts.addOption(Option.builder("h").desc("show this text and exit").hasArg(false)
        .longOpt("help").optionalArg(true).build());
    this.opts.addOption(Option.builder("t").argName("count").desc(
        "process the lidvids in parallel (multiple threads) with this argument being the maximum number of threads")
        .hasArg(true).longOpt("threads").optionalArg(true).build());
    this.opts.addOption(Option.builder("verbose").desc("set logging level to INFO").hasArg(false)
        .longOpt("verbose").optionalArg(true).build());
  }

  public void help() {
    new HelpFormatter().printHelp("ValidateReferenceIntegrity",
        "\nChecks the search DB that all references exist. If the api-auth is provided, then it will also check that the registry API also finds all the references. For lidvid, multiple values can be given using a comma like 'urn:foo::1.0,urn:bar::2.0'.\n\n",
        opts,
        "\nAn auth-file is either a text file of the Java property format with two variables: 'url' and 'credentials'. The 'url' property should be the complete base URL to the Registry Search endpoint or Search API, e.g. 'https://localhost:9876/base', and 'credentials' a path to a java property file with the user name, password, and other credential information as that used by harvest. Or it is an XML text file used by harvest with <registry> containing the 'auth' attribute.\n\n",
        true);
  }

  public void process(String[] args)
      throws IOException, ParseException, ParserConfigurationException, SAXException {
    int cylinders = 1;
    CommandLine cl = new DefaultParser().parse(this.opts, args);
    if (cl.hasOption('h')) {
      this.help();
      return;
    }
    if (cl.hasOption("v"))
      Logger.getRootLogger().setLevel(Level.INFO);
    if (!cl.hasOption("a"))
      throw new ParseException(
          "Not yet implemented. Must provide OpenSearch Registry authorization information.");
    if (cl.getArgList().size() < 1)
      throw new ParseException("Must provide at least one LIDVID as a starting point.");
    if (!cl.hasOption("A"))
      log.warn("Using OpenSearch Registry to check references.");

    if (cl.hasOption("t")) {
      try {
        cylinders = Integer.valueOf(cl.getOptionValue("t"));

        if (cylinders < 1)
          throw new NumberFormatException();
      } catch (NumberFormatException nfe) {
        throw new ParseException("The thread count must be an integer greater than 0.");
      }
    } else
      log.info("lidvids will be sequentially processed.");

    this.log.info("Starting the reference integrity checks.");
    try {
      Engine engine = new Engine(cylinders, cl.getArgList(),
          AuthInformation.buildFrom(cl.getOptionValue("auth-api", "")),
          AuthInformation.buildFrom(cl.getOptionValue("auth-opensearch")));
      engine.processQueueUntilEmpty();
      broken = engine.getBroken();
      total = engine.getTotal();
    } catch (IOException e) {
      log.fatal("Cannot process request because of IO problem.", e);
      throw e;
    } catch (ParserConfigurationException e) {
      log.fatal("Could not parse the harvest configuration file.", e);
      throw e;
    } catch (SAXException e) {
      log.fatal("Mal-formed harvest configuration file.", e);
      throw e;
    }
    if (-1 < this.total)
      this.log.info("Completed the reference integrity checks for " + this.total
          + " products and found " + this.broken + " broken references.");
  }

  public long getBroken() {
    return broken;
  }

  public long getTotal() {
    return total;
  }
}
