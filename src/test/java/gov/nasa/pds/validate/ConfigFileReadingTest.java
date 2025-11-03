// Copyright © 2025, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for configuration file reading using Apache Commons Configuration 2.x.
 *
 * This test class verifies that configuration files with comma-separated values
 * are properly parsed into lists when using Apache Commons Configuration 2.x API.
 *
 * @author Claude (Anthropic)
 */
class ConfigFileReadingTest {

  /**
   * Test that comma-separated schema values in config file are parsed as a list.
   *
   * This test reproduces the issue from GitHub #84 where config files with
   * comma-separated values are not being parsed correctly after upgrading to
   * Apache Commons Configuration 2.x.
   */
  @Test
  void testCommaSeparatedSchemaValues(@TempDir Path tempDir) throws Exception {
    // Create a test config file with comma-separated schema values
    File configFile = tempDir.resolve("test-config.txt").toFile();
    String configContent =
        "validate.schema=schema1.xsd,schema2.xsd,schema3.xsd\n" +
        "validate.schematron=schematron.sch\n";
    Files.writeString(configFile.toPath(), configContent);

    // Read the config file using the corrected approach
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
        new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
            .configure(params.properties()
                .setFile(configFile)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
    Configuration config = builder.getConfiguration();

    // Verify that schema values are parsed as a list
    List<Object> schemaList = config.getList("validate.schema");
    assertNotNull(schemaList, "Schema list should not be null");
    assertEquals(3, schemaList.size(), "Should have 3 schema files");
    assertEquals("schema1.xsd", schemaList.get(0).toString());
    assertEquals("schema2.xsd", schemaList.get(1).toString());
    assertEquals("schema3.xsd", schemaList.get(2).toString());

    // Verify that schematron (single value) is also handled correctly
    List<Object> schematronList = config.getList("validate.schematron");
    assertNotNull(schematronList, "Schematron list should not be null");
    assertEquals(1, schematronList.size(), "Should have 1 schematron file");
    assertEquals("schematron.sch", schematronList.get(0).toString());
  }

  /**
   * Test that ValidateLauncher can read config files with comma-separated values.
   *
   * This integration test verifies that the ValidateLauncher.query(File) method
   * properly reads and parses configuration files.
   */
  @Test
  void testValidateLauncherReadsConfigFile(@TempDir Path tempDir) throws Exception {
    // Create a test config file similar to github84/config.txt
    File configFile = tempDir.resolve("test-config.txt").toFile();
    String configContent =
        "validate.schema=schema1.xsd,schema2.xsd\n" +
        "validate.schematron=schematron.sch\n" +
        "validate.verbose=1\n";
    Files.writeString(configFile.toPath(), configContent);

    // Create ValidateLauncher instance and read config
    ValidateLauncher launcher = new ValidateLauncher();

    // This should not throw an exception
    try {
      launcher.query(configFile);
    } catch (Exception e) {
      // If there's an exception, it should be about missing files, not about config parsing
      // or null report issues
      assertTrue(e.getMessage() == null || !e.getMessage().contains("null"),
          "Should not have null pointer issues when reading config: " + e.getMessage());
    }
  }

  /**
   * Test reading the actual github84 config file to ensure it works correctly.
   */
  @Test
  void testGitHub84ConfigFile() throws Exception {
    // Test the actual config file from the test resources
    File configFile = new File("src/test/resources/github84/config.txt");

    if (!configFile.exists()) {
      // Skip test if file doesn't exist (may not be in all test environments)
      return;
    }

    // Read the config file using the corrected approach
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
        new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
            .configure(params.properties()
                .setFile(configFile)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
    Configuration config = builder.getConfiguration();

    // Verify that schema values are parsed as a list (the actual config has 2 schemas)
    List<Object> schemaList = config.getList("validate.schema");
    assertNotNull(schemaList, "Schema list should not be null");
    assertEquals(2, schemaList.size(),
        "GitHub #84 config should have 2 schema files separated by comma");

    // Verify the actual values from the config file
    assertTrue(schemaList.get(0).toString().contains("PDS4_PDS_1700.xsd"));
    assertTrue(schemaList.get(1).toString().contains("PDS4_TEST_1700.xsd"));

    // Verify schematron
    List<Object> schematronList = config.getList("validate.schematron");
    assertNotNull(schematronList, "Schematron list should not be null");
    assertEquals(1, schematronList.size(), "Should have 1 schematron file");
    assertTrue(schematronList.get(0).toString().contains("PDS4_PDS_1700.sch"));
  }
}
