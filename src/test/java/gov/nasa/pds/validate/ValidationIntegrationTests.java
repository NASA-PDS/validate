/**
 * 
 */
package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import gov.nasa.pds.validate.constants.TestConstants;
import gov.nasa.pds.validate.test.util.Utility;

/**
 * @author jpadams
 *
 */
class ValidationIntegrationTests {
    
    private File outputData = null;

    
    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        this.outputData = new File(TestConstants.TEST_OUT_DIR);
        FileUtils.forceMkdir(this.outputData);
    }
    
    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
        FileUtils.forceDelete(this.outputData);
    }

    @Test
    void testPDS543() {
        try {
            // Setup paths
            System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + "/PDS-543");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_PDS543.json");
            File expected = new File(testPath + File.separator + "report.expected.json");

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "ladee_mission_bundle_SIP_4col_manifest_v1.0.xml"
                    };

            ValidateLauncher.main(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            JsonObject expectedJson = gson.fromJson(new FileReader(expected), JsonObject.class);

            // Check expected file exists
            assertTrue(expected.exists(), expected.getAbsolutePath() + " does not exist.");

            // Check output was generated
            assertTrue(report.exists(), report.getAbsolutePath() + " does not exist.");

            // Check the JSON reports match
            assertEquals(reportJson.get("summary").toString(), expectedJson.get("summary").toString(),
                    expected.getAbsolutePath() + " and " + report.getAbsolutePath() + " do not match.");
            
            // Verify failure with additional errors in label
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "ladee_mission_bundle_SIP_4col_manifest_v1.0.unexpected.xml"
                    };
            
            ValidateLauncher.main(args2);
            
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            assertNotEquals(reportJson.get("summary").toString(), expectedJson.get("summary").toString(),
                    expected.getAbsolutePath() + " and " + report.getAbsolutePath() + " match when they should not.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub28() {
        try {
            // Setup paths
            System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + "/github28");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github28_1.json");

            // First test that we get an invalid context product error
            System.out.println(testPath + File.separator + "test_add_context_products.xml");
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "test_add_context_products.xml"
                    };

            ValidateLauncher.main(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            
            // Make sure we have 1 error as expected
            assertEquals(reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(), 1, "One error expected for invalid context reference test.");

            // Now test with added context products
            report = new File(outFilePath + File.separator + "report_github28_2.json");
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--add-context-products", testPath + File.separator + "new_context.json",
                    "-t" , testPath + File.separator + "test_add_context_products.xml",
                    
                    };
            ValidateLauncher.main(args2);

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            assertEquals(reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(), 0, "No errors expected for add additional context test.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub71() {
        try {
            // Setup paths
            System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + "/github71");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github71_1.json");
            String catFile =outFilePath + File.separator + "catalog.xml";

            // Create catalog file
            String catText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                    "<!--\n" + 
                    "<!DOCTYPE catalog PUBLIC \"-//OASIS//DTD XML Catalogs V1.1//EN\" \"http://www.oasis-open.org/committee\\\n" + 
                    "s/entity/release/1.1/catalog.dtd\">\n" + 
                    "-->\n" + 
                    "<catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\">\n" + 
                    "    <rewriteURI uriStartString=\"http://pds.nasa.gov/pds4\" rewritePrefix=\"file://"+ testPath +"\" />\n" + 
                    "    <rewriteURI uriStartString=\"https://pds.nasa.gov/pds4\" rewritePrefix=\"file://"+ testPath +"\" />\n" + 
                    "</catalog>";

            BufferedWriter writer = new BufferedWriter(new FileWriter(catFile));
            writer.write(catText);
            writer.close();
            
            // First test that we get an invalid context product error
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "-t", testPath + File.separator + "ELE_MOM.xml"
                    };

            ValidateLauncher.main(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            
            // TODO Bug due to executing Github28 prior to this. It will still attempt context resolution after being unable to parse
            // the label properly, so for some reason the code still use the context from previous run
            JsonObject message = reportJson.getAsJsonObject("summary").get("messageTypes").getAsJsonArray().get(1).getAsJsonObject();

            assertEquals(message.get("messageType").getAsString(), "error.label.unresolvable_resource", "error.label.unresolvable_resource expected.");
            assertEquals(message.get("total").getAsInt(), 1, "One error expected.");

            report = new File(outFilePath + File.separator + "report_github71_2.json");
            // First test that we get an invalid context product error
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-C", catFile,
                    "--no-data-check",
                    "-t", testPath + File.separator + "ELE_MOM.xml"
                    };

            ValidateLauncher.main(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            assertEquals(reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(), 0, "Zero errors expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    protected static class ExitException extends SecurityException 
    {
        private static final long serialVersionUID = -1535371619727142623L;

        public final int status;
        public ExitException(int status) 
        {
            super("Program exited");
            this.status = status;
        }
    }

}
