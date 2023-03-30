package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import gov.nasa.pds.validate.ri.AuthInformation;
import gov.nasa.pds.validate.ri.CliExecutioner;
import gov.nasa.pds.validate.ri.FakeOpensearch;

public class ReferenceIntegrityTest {
  @Test
  public void checkBundle() {
    final FakeOpensearch db = new FakeOpensearch(); // necessary for side effects despite it not
                                                    // being used
    final String[] args = {"--auth-opensearch", "src/test/resources/riut/auth.txt", "--threads",
        "1", "urn:nasa:pds:insight_rad::2.1"};
    CliExecutioner cli = new CliExecutioner(args);
    assertDoesNotThrow(cli);
    assertEquals(46, cli.getCli().getTotal(), "Did not process the expected number of products.");
    assertEquals(257, cli.getCli().getBroken(),
        "Did not find the expected number of missing product references");
  }

  @Test
  public void checkBundleMultithreaded() {
    final FakeOpensearch db = new FakeOpensearch(); // necessary for side effects despite it not
                                                    // being used
    final String[] args = {"--auth-opensearch", "src/test/resources/riut/auth.txt", "--threads",
        "10", "urn:nasa:pds:insight_rad::2.1"};
    CliExecutioner cli = new CliExecutioner(args);
    assertDoesNotThrow(cli);
    assertEquals(46, cli.getCli().getTotal(), "Did not process the expected number of products.");
    assertEquals(257, cli.getCli().getBroken(),
        "Did not find the expected number of missing product references");
  }

  @Test
  public void invalidArg() {
    final String[] args =
        {"--auth-serch", "src/test/resources/riut/auth.txt", "urn:nasa:pds:insight_rad::2.1"};
    assertThrows(ParseException.class, new CliExecutioner(args));
  }

  @Test
  public void invalidThreadValue() {
    final String[] args = {"--auth-opensearch", "src/test/resources/riut/auth.txt", "-t", "1",
        "urn:nasa:pds:insight_rad::2.1"};
    assertDoesNotThrow(new CliExecutioner(args));
    final String[] argsA = {"--auth-opensearch", "src/test/resources/riut/auth.txt", "-t", "a",
        "urn:nasa:pds:insight_rad::2.1"};
    assertThrows(ParseException.class, new CliExecutioner(argsA));
    final String[] argsNeg = {"--auth-opensearch", "src/test/resources/riut/auth.txt", "-t", "-1",
        "urn:nasa:pds:insight_rad::2.1"};
    assertThrows(ParseException.class, new CliExecutioner(argsNeg));
  }

  @Test
  public void missingFile() {
    final String[] args = {"--auth-opensearch", "src/test/resources/riut/no-auth.txt",
        "urn:nasa:pds:insight_rad::2.1"};
    assertThrows(IOException.class, new CliExecutioner(args));
  }

  @Test
  public void missingLIDVID() {
    final String[] args = {"--auth-opensearch", "src/test/resources/riut/auth.txt"};
    assertThrows(ParseException.class, new CliExecutioner(args));
  }

  @Test
  public void missingRequired() {
    final String[] args = {"urn:nasa:pds:insight_rad::2.1"};
    assertThrows(ParseException.class, new CliExecutioner(args));
  }

  @Test
  public void readSearchAuth() {
    try {
      AuthInformation asProperties = AuthInformation.buildFrom("src/test/resources/riut/auth.txt");
      AuthInformation asXML = AuthInformation.buildFrom("src/test/resources/riut/harvest.xml");
      assertEquals(asProperties.getPassword(), asXML.getPassword());
      assertEquals(asProperties.getTrustSelfSigned(), asXML.getTrustSelfSigned());
      assertEquals(asProperties.getUrl(), asXML.getUrl());
      assertEquals(asProperties.getUsername(), asXML.getUsername());
    } catch (IOException | ParserConfigurationException | SAXException e) {
      assertTrue(false, "Had an exception and that should not have occurred.");
    }
  }
}
