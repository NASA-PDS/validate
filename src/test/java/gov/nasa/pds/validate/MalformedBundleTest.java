package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import gov.nasa.pds.tools.util.ReferentialIntegrityUtil;
import gov.nasa.pds.tools.validate.CrossLabelFileAreaReferenceChecker;
import gov.nasa.pds.validate.constants.TestConstants;

class MalformedBundleTest {
  @TempDir
  Path directory;

  @Test
  void reportsParserErrorForBundleInDirectoryWhenProductValidationIsSkipped() throws Exception {
    Path bundleDirectory = Files.createDirectory(directory.resolve("bundle"));
    Files.writeString(bundleDirectory.resolve("bundle.xml"),
        "<Product_Bundle xmlns=\"http://pds.nasa.gov/pds4/pds/v1\">\n"
            + "  <lid_reference>example</lid_refer\nence>\n</Product_Bundle>\n");
    Path report = directory.resolve("report.txt");
    String previousResourcesHome = System.getProperty("resources.home");
    System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
    ValidateLauncher launcher = new ValidateLauncher();
    try {
      launcher.processMain(new String[] {"--skip-context-validation", "--skip-product-validation",
          "--skip-content-validation", "-R", "pds4.bundle", "-t",
          bundleDirectory.toString(), "--report-file", report.toString()});
      String output = Files.readString(report);
      assertTrue(output.contains("error.label.not_understandable"), output);
      assertTrue(output.contains("lid_reference"), output);
      assertTrue(output.contains("line"), output);
    } finally {
      launcher.flushValidators();
      CrossLabelFileAreaReferenceChecker.reset();
      ReferentialIntegrityUtil.reset();
      if (previousResourcesHome == null) {
        System.clearProperty("resources.home");
      } else {
        System.setProperty("resources.home", previousResourcesHome);
      }
    }
  }
}
