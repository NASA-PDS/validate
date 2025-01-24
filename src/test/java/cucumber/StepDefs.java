package cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gov.nasa.pds.tools.validate.CrossLabelFileAreaReferenceChecker;
import gov.nasa.pds.validate.ValidateLauncher;
import gov.nasa.pds.validate.constants.TestConstants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefs {
  private Path datasrc;
  private Path datasink;
  private ValidateLauncher launcher = null;

  /**
   * @throws java.lang.Exception
   */
  void setUp() throws Exception {
    FileUtils.forceMkdir(this.datasink.toFile()); // Create directory if one does not already exist.
    System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
    this.launcher = new ValidateLauncher();
    this.datasink.resolve("cucumber.success").toFile().delete();
    this.datasink.resolve("cucumber.failed").toFile().createNewFile();
  }

  /**
   * @throws java.lang.Exception
   */
  void tearDown() throws Exception {
    this.launcher.flushValidators();
    CrossLabelFileAreaReferenceChecker.reset();
  }

  private String normalize (String s) {
    return s
        .replace("{datasink}", this.datasink.toAbsolutePath().toString())
        .replace("{datasrc}", this.datasrc.toAbsolutePath().toString())
        .replace("%20", " ");
  }
  private List<String> resolveArgumentStrings(String args) {
    boolean catalogNext = false, manifestNext = false;
    List<String> resolved = new ArrayList<String>(Arrays.asList("--report-file",
        this.datasink.resolve("report.json").toString(), "--report-style", "json"));
    for (String arg : args.split("\\s+")) {
      if (arg.contains("{reportDir}") || arg.contains("{resourceDir}")) {
        throw new IllegalArgumentException("{reportDir} and {resourceDir} are no longer valid.");
      }
      if (catalogNext) {
        catalogNext = false;
        if (!this.createCatalogFile(this.normalize(arg), this.datasrc.toAbsolutePath().toString())) {
          arg = arg.replace("{datasrc}", "{datasink}");
        }
      }
      if (manifestNext) {
        manifestNext = false;
        Path path = Paths.get(this.normalize(arg));
        try {
          Files.writeString(this.datasink.resolve(path.getFileName()),
              this.normalize(Files.readString(path)));
          arg = this.datasink.resolve(path.getFileName()).toAbsolutePath().toString();
        } catch (Exception e) {
          e.printStackTrace();
          fail("Test Failed Due To Exception: " + e.getMessage());
        }
      }
      if (arg.equals("-C") || arg.equals("--catalog")) {
        catalogNext = true;
      }
      if (arg.equals("-r}") || arg.equals("--report-file")) {
        throw new IllegalArgumentException("Defining the report file is no longer valid.");
      }
      if (arg.equals("-s") || arg.equals("--report-style")) {
        throw new IllegalArgumentException("{Defining the report style is no longer valid.");
      }
      if (arg.equals("--target-manifest") ) {
        manifestNext = true;
      }
      resolved.add(this.normalize(arg));
    }
    return resolved;
  }

  @Given("an {int}, , and {string}")
  public void an_and(Integer issueNumber, String datasrc) {
    an_and(issueNumber, null, datasrc);
  }

  @Given("an {int}, {int}, and {string}")
  public void an_and(Integer issueNumber, Integer count, String datasrc) {
    this.datasink = Paths.get(TestConstants.TEST_OUT_DIR,
        "issue-" + issueNumber + (count == null ? "" : ("-" + count.toString())));
    this.datasrc = Paths.get(TestConstants.TEST_DATA_DIR, datasrc);
  }

  @When("execute validate with {string}")
  public void execute_validate(String args) {
    List<String> arguments = this.resolveArgumentStrings(args);
    try {
      this.setUp();
      this.launcher.processMain(arguments.toArray(new String[0]));
      this.tearDown();
    } catch (ExitException e) {
      assertEquals(0, e.status, "Exit status");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Test Failed Due To Exception: " + e.getMessage());
    }
  }

  @Then("compare to the .")
  public void compare_to_the() {
    compare_to_the("");
  }

  @Then("compare to the {string}.")
  public void compare_to_the(String expectation) {
    for (String mustExpectation : Arrays.asList(
        "summary:totalErrors",
        "summary:totalWarnings",
        "summary:productValidation:failed",
        "summary:productValidation:skipped",
        "summary:referentialIntegrity:failed",
        "summary:referentialIntegrity:skipped")) {
      if (!expectation.contains(mustExpectation)) {
        expectation += (expectation.length() > 0 ? (",") : "") + mustExpectation + "=0"; 
      }
    }
    
    Gson gson = new Gson();
    try (FileReader report = new FileReader(this.datasink.resolve("report.json").toFile())) {
      HashMap<String,Integer> messages = new HashMap<String,Integer>();
      JsonObject reportJson = gson.fromJson(report, JsonObject.class), node;
      for (JsonElement msg : reportJson.getAsJsonObject("summary").getAsJsonArray("messageTypes")) {
        messages.put(
            msg.getAsJsonObject().getAsJsonPrimitive("messageType").getAsString(),
            msg.getAsJsonObject().getAsJsonPrimitive("total").getAsInt());
      }
      for (String detail : expectation.split(",")) {
        boolean nextIsMessage = false;
        Integer expected = Integer.valueOf(detail.split("=")[1].strip());
        Integer reported = -1;
        String keyword = detail.split("=")[0];
        node = reportJson;
        for (String key : keyword.split(":")) {
          if (nextIsMessage) {
            reported = messages.get(key);
            messages.remove(key);
          } else if (keyword.endsWith(key)) {
            reported = node.getAsJsonPrimitive(key).getAsInt();
          } else if (key.equals("messageTypes")) {
            nextIsMessage = true;
          } else {
            node = node.getAsJsonObject(key);
          }
        }
        assertEquals (expected, reported, keyword);
      }
      assertEquals (0, messages.size(), "Did not identify all message types generated.");
      this.datasink.resolve("cucumber.failed").toFile().delete();
      this.datasink.resolve("cucumber.success").toFile().createNewFile();
    } catch (ExitException e) {
      assertEquals(0, e.status, "Exit status");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Test Failed Due To Exception: " + e.getMessage());
    }
  }

  private boolean createCatalogFile(String catFile, String substitutePath) {
    boolean isNew = true;
    Path catPath = Paths.get(catFile);
    
    if (catPath.toFile().exists()) {
      try {
        Files.writeString(this.datasink.resolve(catPath.getFileName()),
            this.normalize(Files.readString(catPath)));
        isNew = true;
      } catch (Exception e) {
        e.printStackTrace();
        fail("Test Failed Due To Exception: " + e.getMessage());
      }
    } else {
      // Create catalog file
      String catText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!--\n"
          + "<!DOCTYPE catalog PUBLIC \"-//OASIS//DTD XML Catalogs V1.1//EN\" \"http://www.oasis-open.org/committees/entity/release/1.1/catalog.dtd\">\n"
          + "-->\n" + "<catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\">\n"
          + "    <rewriteURI uriStartString=\"http://pds.nasa.gov/pds4\" rewritePrefix=\"file://"
          + substitutePath + "\" />\n"
          + "    <rewriteURI uriStartString=\"https://pds.nasa.gov/pds4\" rewritePrefix=\"file://"
          + substitutePath + "\" />\n" + "</catalog>";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter (catPath.toFile()))) {
        writer.write(catText);
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
        fail("Test Failed Due To Exception: " + e.getMessage());
      }
    }
    return isNew;
  }
  
  protected static class ExitException extends SecurityException {
    private static final long serialVersionUID = -1535371619727142623L;

    public final int status;

    public ExitException(int status) {
      super("Program exited");
      this.status = status;
    }
  }
}
