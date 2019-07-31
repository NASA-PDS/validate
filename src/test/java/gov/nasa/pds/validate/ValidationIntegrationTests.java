/**
 * 
 */
package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;

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
//    @AfterEach
//    void tearDown() throws Exception {
//        FileUtils.forceDelete(this.outputData);
//    }

    @Test
    void testPDS543() {
        try {
            // Setup paths
            System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + "/PDS-543");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report.json");
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
    @Disabled
    void testGithub28() {
        try {
            // Setup paths
            System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + "/github28");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report.json");
            File expected = new File(testPath + File.separator + "report.expected.json");

            // First test that we get an invalid context product error
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
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--add-context-products", testPath + File.separator + "new_context.json",
                    testPath + File.separator + "test_add_context_products.xml",
                    
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
