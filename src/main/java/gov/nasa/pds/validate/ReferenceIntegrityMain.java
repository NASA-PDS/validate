package gov.nasa.pds.validate;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;
import gov.nasa.pds.validate.ri.CommandLineInterface;

public class ReferenceIntegrityMain {
  public static void main(String[] args) {
    CommandLineInterface cli = new CommandLineInterface();
    try {
      cli.process(args);
    } catch (ParseException pe) {
      System.err.println("[ERROR] " + pe.getMessage());
      cli.help();
    } catch (IOException | ParserConfigurationException | SAXException e) {
      // do nothing because they are already logged.
    }
  }
}
