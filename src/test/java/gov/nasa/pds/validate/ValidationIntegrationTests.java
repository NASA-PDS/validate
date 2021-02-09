// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
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

import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.validate.constants.TestConstants;
import gov.nasa.pds.validate.test.util.Utility;

/**
 * @author jpadams
 *
 */
@Disabled
class ValidationIntegrationTests {
    
    private File outputData = null;

    private ValidateLauncher launcher;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        this.outputData = new File(TestConstants.TEST_OUT_DIR);
        FileUtils.forceMkdir(this.outputData);
        System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
        this.launcher = new ValidateLauncher();
    }
    
    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
//        FileUtils.forceDelete(this.outputData);
        this.launcher.flushValidators();
    }

    @Test
    void testPDS543() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "PDS-543");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_PDS543.json");
            File expected = new File(testPath + File.separator + "report.expected.json");

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "ladee_mission_bundle_SIP_4col_manifest_v1.0.xml"
                    };

            this.launcher.processMain(args);

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
            

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);
            
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
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github28");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github28_1.json");

            // First test that we get an invalid context product error
            //System.out.println(testPath + File.separator + "test_add_context_products.xml");
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "test_add_context_products.xml"
                    };

            this.launcher.processMain(args);

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

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

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
    void testGithub15() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github15");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            
            //test one. Found all products and the name/type are case match
            File report = new File(outFilePath + File.separator + "report_github15_pass.json");

            //System.out.println("Test file: " + testPath + File.separator + "test_check-pass_context_products.xml");
            String[] args = {
                    "-v1",
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-R", "pds4.label",
                    testPath + File.separator + "test_check-pass_context_products.xml"
                    };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND.getKey());
            count += this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());

            assertEquals(count, 4, "4 valid context references should be found.");

            //get warnings/errors test
            report = new File(outFilePath + File.separator + "report_github15_no-pass.json");

            //System.out.println("Test file: " + testPath + File.separator + "test_check-no-pass_context_products.xml");
            String[] args2 = {
                    "-v1",
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    testPath + File.separator + "test_check-no-pass_context_products.xml"
                    };

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());
            count += this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND_MISMATCH.getKey());
            assertEquals(count, 3, "Three errors expected for invalid context reference (Lid, name, value) test.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub47() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github47");
            String outFilePath = TestConstants.TEST_OUT_DIR;

            // test with option: "--skip-context-validation"
            File report = new File(outFilePath + File.separator + "report_github47_disable-valid.json");

            // System.out.println("Test file: " + testPath + File.separator +
            // "test_context_products.xml");
            String[] args = { "-r", report.getAbsolutePath(), "-s", "json", "-R", "pds4.label", "--skip-context-validation",
                    testPath + File.separator + "test_context_products.xml" };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());
            count += this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND_MISMATCH.getKey());
            assertEquals(count, 0, "No errors expected. Context validation disabled.");

            // test without option: "--no-context-valid"
            report = new File(outFilePath + File.separator + "report_github47_enable-valid.json");

            // System.out.println("Test file: " + testPath + File.separator +
            // "test_context_products.xml");
            String[] args2 = {
                    "-v1",
                    "-r", report.getAbsolutePath(), 
                    "-s", "json",
                    "-R", "pds4.label",
                    testPath + File.separator + "test_context_products.xml" };

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());
            count += this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND_MISMATCH.getKey());
            assertEquals(count, 3, "Three errors expected. Context validation enabled.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub62() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github62");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github62_1.json");
            
            // First test that we get an invalid context product error
            String[] args = {
                    "-v1",
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "-t", testPath + File.separator + "ele_mom_tblChar.xml"
                    };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int infos = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND.getKey());

            assertEquals(infos, 4, "Found " + Integer.toString(infos) + " but expecting 4 info.label.context_ref_found info messages.\n" + reportJson.toString());

            // Now let's test for additional context products in another product
            report = new File(outFilePath + File.separator + "report_github62_2.json");
            String[] args2 = {
                    "-v1",
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "-t", testPath + File.separator + "spacecraft.orex_1.1.xml"
                    };

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            
            int count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_FOUND.getKey());
            count += this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());

            assertEquals(count, 8, "8 " + ProblemType.CONTEXT_REFERENCE_FOUND.getKey() + " and " + ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey() + " info/error messages expected.\n" + reportJson.toString());

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
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github71");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github71_1.json");
            String catFile =outFilePath + File.separator + "catalog.xml";

            // Create catalog file
            String catText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                    "<!--\n" + 
                    "<!DOCTYPE catalog PUBLIC \"-//OASIS//DTD XML Catalogs V1.1//EN\" \"http://www.oasis-open.org/committees/entity/release/1.1/catalog.dtd\">\n" +
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

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int errors = this.getMessageCount(reportJson, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey());
            assertEquals(errors, 1, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey() + " error message expected.");

            report = new File(outFilePath + File.separator + "report_github71_2.json");
            // First test that we get an invalid context product error
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-C", catFile,
//                    "--no-data-check",
                    "-t", testPath + File.separator + "ELE_MOM.xml"
                    };

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            errors = this.getMessageCount(reportJson, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey());
            assertEquals(errors, 0, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey() + " error message not expected.");
            
            errors = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());
            assertEquals(errors, 2278, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " error message not expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub09() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github09");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github09_1.json");

            // Try with a bad example tha will throw 2 errors
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "minimal_test_product_good2.xml",
                    
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages expected.");

            // Try with a good example that will throw no errors
            report = new File(outFilePath + File.separator + "report_github09_2.json");
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "minimal_test_product_good.xml",
                    
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages not expected.");
            
            // Github170 - Try again, this time with a DSV and an empty fields 
            report = new File(outFilePath + File.separator + "report_github09_3.json");
            String[] args3 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "csv_empty_field_test_VALID.xml",
                    
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args3);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages not expected.");
            
            report = new File(outFilePath + File.separator + "report_github09_4.json");
            String[] args4 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "csv_empty_field_test_INVALID.xml",

                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args4);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 1, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages expected.");
            
            // Issue #206 - updated tested per issue with space-padded fields in Table_Character validation
            report = new File(outFilePath + File.separator + "report_github09_5.json");
            String[] args5 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "val9a.xml",

                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args5);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages expected.");
            
            report = new File(outFilePath + File.separator + "report_github09_6.json");
            String[] args6 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "val9b.xml",

                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args6);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 1, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub50() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github50");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github50_1.json");

            String manifestFile = outFilePath + File.separator + "target-manifest.xml";

            // Create catalog file
            String manifestText = testPath + File.separator + "ele_evt_12hr_orbit_2011-2012.xml\n" +
                    testPath + File.separator + "ele_evt_8hr_orbit_2012-2013.xml";

            BufferedWriter writer = new BufferedWriter(new FileWriter(manifestFile));
            writer.write(manifestText);
            writer.close();

            // First test that we get file of target list from command line, and add to the target list to validate.
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "--target-manifest", manifestFile
                    };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.MISSING_REFERENCED_FILE.getKey());

            assertEquals(count, 2, "2 " + ProblemType.MISSING_REFERENCED_FILE.getKey() + " error messages expected.\n" + reportJson.toString());
            
            // Now try with manifest and target
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "--target-manifest", manifestFile,
                    "-t", testPath + File.separator + "ele_evt_8hr_orbit_2014-2015.xml"
                    };

            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.MISSING_REFERENCED_FILE.getKey());

            assertEquals(count, 3, "3 " + ProblemType.MISSING_REFERENCED_FILE.getKey() + " error messages expected.\n" + reportJson.toString());

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub84() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github84");
            String dataPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github71");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github84_1.json");

            String configFile = testPath + File.separator + "config.txt";

            // Test that config file works as expected
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "--no-data-check",
                    "-c", configFile,
                    "-t", dataPath + File.separator + "ELE_MOM.xml"
                    };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub87() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github87");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github87_1.json");
            String catFile = outFilePath + File.separator + "catalog.xml";
            String testfile1 = testPath + File.separator + "2t126632959btr0200p3002n0a1.xml";
            String testfile2 = testPath + File.separator + "2t126646972btr0200p3001n0a1.xml";

            // Create catalog file
            String catText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                    "<!--\n" + 
                    "<!DOCTYPE catalog PUBLIC \"-//OASIS//DTD XML Catalogs V1.1//EN\" \"http://www.oasis-open.org/committees/entity/release/1.1/catalog.dtd\">\n" +
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
                    "-R", "pds4.label",
                    "--no-data-check",
                    "-t", testfile1, testfile2,
                    "-C", catFile
                    };

            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int errors = this.getMessageCount(reportJson, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey());
            assertEquals(errors, 0, ProblemType.LABEL_UNRESOLVABLE_RESOURCE.getKey() + " no errors expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub137() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github137");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github137_1.json");

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "delimited_table_good.xml",
                    
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages expected.");
            
            // Now test with added context products
            report = new File(outFilePath + File.separator + "report_github137_2.json");
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "delimited_table_bad.xml",
                    
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey());

            assertEquals(count, 2, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH.getKey() + " info/error messages not expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub149() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github173");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File dir = new File(testPath);
            
            // Check all VALID examples
            File[] files = dir.listFiles((d, name) -> name.startsWith("VALID") && name.endsWith(".xml"));
            int expectedErrorCount = 0;
            for (File xmlfile : files) {
                File report = new File(outFilePath + File.separator + "report_github149_" + xmlfile.getName() + ".json");
                
                String[] args = {
                        "-r", report.getAbsolutePath(),
                        "-s", "json",
                        "-t" , xmlfile.getAbsolutePath(),
                        };
                this.launcher.processMain(args);

                Gson gson = new Gson();
                JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                int count = this.getMessageCount(reportJson, "totalErrors");
                assertEquals(expectedErrorCount, count, expectedErrorCount + " error messages expected. See validation report: " + report.getAbsolutePath());
                this.launcher.flushValidators();
            }
            
            // Check all WARNING examples
            files = dir.listFiles((d, name) -> name.startsWith("WARN") && name.endsWith(".xml"));
            for (File xmlfile : files) {
                File report = new File(outFilePath + File.separator + "report_github149_" + xmlfile.getName() + ".json");
                
                String[] args = {
                        "-r", report.getAbsolutePath(),
                        "-s", "json",
                        "-t" , xmlfile.getAbsolutePath(),
                        };
                this.launcher.processMain(args);

                Gson gson = new Gson();
                JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                int count = this.getMessageCount(reportJson, "totalErrors");
                assertEquals(expectedErrorCount, count, expectedErrorCount + " error messages expected. See validation report: " + report.getAbsolutePath());
                this.launcher.flushValidators();
            }
            
            // Check all INVALID examples
            files = dir.listFiles((d, name) -> name.startsWith("FAIL") && name.endsWith(".xml"));
            expectedErrorCount = 1;
            for (File xmlfile : files) {
                File report = new File(outFilePath + File.separator + "report_github149_" + xmlfile.getName() + ".json");
                
                String[] args = {
                        "-r", report.getAbsolutePath(),
                        "-s", "json",
                        "-t" , xmlfile.getAbsolutePath(),
                        };
                this.launcher.processMain(args);

                Gson gson = new Gson();
                JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

                int count = this.getMessageCount(reportJson, "totalErrors");
                assertEquals(expectedErrorCount, count, expectedErrorCount + " error message(s) expected. See validation report: " + report.getAbsolutePath());
                this.launcher.flushValidators();
            }
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    @Test
    void testGithub173() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github173");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github173_1.json");

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-R", "pds4.bundle",
                    "-t" , testPath + File.separator + "valid" + File.separator,
                    "--skip-content-validation"
                    
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = this.getMessageCount(reportJson, ProblemType.RECORDS_MISMATCH.getKey());

            assertEquals(count, 0, ProblemType.RECORDS_MISMATCH.getKey() + " info/error messages expected.");
            
            // Now test with added context products
            report = new File(outFilePath + File.separator + "report_github173_2.json");
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-R", "pds4.bundle",
                    "-t" , testPath + File.separator + "invalid" + File.separator,
                    "--skip-content-validation"
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.RECORDS_MISMATCH.getKey());

            assertEquals(count, 1, ProblemType.RECORDS_MISMATCH.getKey() + " info/error messages not expected.");

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub51() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github51");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github51_1.json");

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath,
                    "--skip-content-validation"
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            //int count = this.getMessageCount(reportJson, ProblemType.RECORDS_MISMATCH.getKey());

            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

            // Test with the 'valid' directory appended.
            report = new File(outFilePath + File.separator + "report_github51_2.json");
            String[] args2 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid" + File.separator,
                    "--skip-content-validation"
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args2);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

            // Test with --alternate_file_paths option.
            report = new File(outFilePath + File.separator + "report_github51_3.json");
            String[] args3 = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-R", "pds4.bundle",
                    "--alternate_file_paths",
                    "src/test/resources/github51_additionals/additional_dir1/data_spectra,src/test/resources/github51_additionals/additional_dir2/data_spectra",
                    "-t" , testPath + File.separator + "valid" + File.separator + "bundle_kaguya_derived.xml",
                    "--skip-content-validation",
                    "--skip-product-validation"
                    };
            this.launcher = new ValidateLauncher();
            this.launcher.processMain(args3);

            gson = new Gson();
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

            int count = this.getMessageCount(reportJson, ProblemType.GENERAL_INFO.getKey());
            assertEquals(count, 2, ProblemType.GENERAL_INFO.getKey() + " info/error messages expected.");
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub230() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github230");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github230_invalid.json");

            // Run the invalid test.
            // The content does not have LIDVID for 'P' member:
           //  cat -n src/test/resources/github230/invalid/cocirs_c2h4abund/data/collection_cocirs_c2h4abund_inventory.txt
           //      1  P,urn:nasa:pds:cocirs_c2h4abund:data_derived:c2h4_abund_profiles
           //      2  P,urn:nasa:pds:cocirs_c2h4abund:data_derived:c2h4_temp_profiles

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/cocirs_c2h4abund/bundle_cocirs_c2h4abund.xml",
                    "--skip-content-validation"
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            assertEquals(2, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "2 error messages expected.\n" + reportJson.toString());

            int count = this.getMessageCount(reportJson, ProblemType.MISSING_VERSION.getKey());
            assertEquals(2, count,  "2 " + ProblemType.MISSING_VERSION.getKey() + "  messages expected.\n" + reportJson.toString());


            // Run the valid test.
            // The content has proper LIDVID  for 'P' member:
            // cat -n src/test/resources/github230/valid/cocirs_c2h4abund/data/collection_cocirs_c2h4abund_inventory.txt
            //    1  P,urn:nasa:pds:cocirs_c2h4abund:data_derived:c2h4_abund_profiles::1.0
            //    2  P,urn:nasa:pds:cocirs_c2h4abund:data_derived:c2h4_temp_profiles::1.0

            File report2 = new File(outFilePath + File.separator + "report_github230_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/cocirs_c2h4abund/bundle_cocirs_c2h4abund.xml",
                    "--skip-content-validation"
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

            int count2 = this.getMessageCount(reportJson, ProblemType.MISSING_VERSION.getKey());
            assertEquals(0, count2,  "0 " + ProblemType.MISSING_VERSION.getKey() + "  messages expected.\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub17() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github17");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github17_invalid.json");

            // Run the invalid test.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "delimited_table_bad.xml",
                    "--skip-context-validation",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            assertEquals(2, reportJson.getAsJsonObject("summary").get("totalWarnings").getAsInt(),  "2 warning messages expected.\n" + reportJson.toString());

            // Run the valid test.

            File report2 = new File(outFilePath + File.separator + "report_github17_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "delimited_table_good.xml",
                    "--skip-context-validation",
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub153() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github153");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github153_invalid.json");

            // Run the invalid test.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "iue_asteroid_spectra/document/3juno_lwr01896_ines fits headers.pdfa.xml",
                    "--skip-context-validation",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            assertEquals(1, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "1 error messages expected.\n" + reportJson.toString());

            // Run the valid test.

            File report2 = new File(outFilePath + File.separator + "report_github153_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "iue_asteroid_spectra/document/collection_iue_asteroid_spectra_document.xml",
                    "--skip-context-validation",
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub11() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github11");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github11_invalid_1.json");

            // Run the invalid test #1 with bad format [%+8s] in field_number 2

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "test_data/science_index_bad_1.xml",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            //assertEquals(3, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "3 error messages expected.\n" + reportJson.toString());

            int count = this.getMessageCount(reportJson, ProblemType.INVALID_LABEL.getKey());
            assertEquals(1, count,  "1 " + ProblemType.INVALID_LABEL.getKey() + "  messages expected.\n" + reportJson.toString());

            // Run the invalid test #2 with bad format [%-2d] in field_number 6.
            File report2 = new File(outFilePath + File.separator + "report_github11_invalid_2.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "test_data/science_index_bad_2.xml",
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            //assertEquals(3, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "3 error messages expected.\n" + reportJson.toString());

            int count2 = this.getMessageCount(reportJson, ProblemType.INVALID_LABEL.getKey());
            assertEquals(1, count2,  "1 " + ProblemType.INVALID_LABEL.getKey() + "  messages expected.\n" + reportJson.toString());

            // Run the valid test with good formats.
            // Note that because we want the test to pass we use --skip-context-validation so as it is easier to 
            // see if the format is good.  We are only interested in validating the format of the table, not the file in archive.

            File report3 = new File(outFilePath + File.separator + "report_github11_valid.json");

            String[] args3 = {
                    "-r", report3.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "test_data/science_index_good.xml",
                    "--skip-context-validation",
                    };
            this.launcher.processMain(args3);

            reportJson = gson.fromJson(new FileReader(report3), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "No error messages expected.\n" + reportJson.toString());

            int count3 = this.getMessageCount(reportJson, ProblemType.INVALID_LABEL.getKey());
            assertEquals(0, count3,  "0 " + ProblemType.INVALID_LABEL.getKey() + "  messages expected.\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub5() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github5");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github5_bundle_invalid.json");

            // Run the invalid test #1 with bad file names and bad directory name with dot '.' character.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/bundle_kaguya_derived.xml",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = 0;

            count = this.getMessageCount(reportJson, ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(5, count,  "5 " + ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            count = this.getMessageCount(reportJson, ProblemType.UNALLOWED_BASE_NAME.getKey());
            assertEquals(3, count,  "3 " + ProblemType.UNALLOWED_BASE_NAME.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            count = this.getMessageCount(reportJson, ProblemType.UNALLOWED_FILE_NAME.getKey());
            assertEquals(0, count,  "0 " + ProblemType.UNALLOWED_FILE_NAME.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            count = this.getMessageCount(reportJson, ProblemType.DIR_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(0, count,  "0 " + ProblemType.DIR_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            // Run the valid bundle test.
            File report2 = new File(outFilePath + File.separator + "report_github5_bundle_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/bundle_kaguya_derived.xml",
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "0 error messages expected.\n" + reportJson.toString());

            // Run the invalid label test to catch file with invalid character.  
            File report3 = new File(outFilePath + File.separator + "report_github5_label_invalid.json");

            String[] args3 = {
                    "-r", report3.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/VALID_odf07155_msgr_11_with_bad_char_#_.xml",
                    };   
            this.launcher.processMain(args3);

            reportJson = gson.fromJson(new FileReader(report3), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(1, count,  "1 " + ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected.\n" + reportJson.toString());

            // Run the valid label test.
            File report4 = new File(outFilePath + File.separator + "report_github5_label_valid.json");

            String[] args4 = {
                    "-r", report4.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/VALID_odf07155_msgr_11.xml",
                    "--skip-content-validation"
                    };
            this.launcher.processMain(args4);

            reportJson = gson.fromJson(new FileReader(report4), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(0, count,  "0 " + ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected.\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub278() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github278");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github278_label_invalid.json");

            // Run the invalid label test with version id greater than registered context.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/trk-2-34-revn-l5_tnf_invalid.xml",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = 0;

            count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());
            assertEquals(1, count,  "1 " + ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            // Run the valid label test.
            File report2 = new File(outFilePath + File.separator + "report_github278_label_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/trk-2-34-revn-l5_tnf.xml",
                    };
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey());
            assertEquals(0, count,  "0 " + ProblemType.CONTEXT_REFERENCE_NOT_FOUND.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }




    @Test
    void testGithub281() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github281");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github281_label_invalid_1.json.json");

            // Run the invalid label test with bad file size.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/collection_gwe_spk_invalid_1_bad_filesize.xml",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = 0;

            count = this.getMessageCount(reportJson, ProblemType.FILESIZE_MISMATCH.getKey());
            assertEquals(1, count,  "1 " + ProblemType.FILESIZE_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());


            File report2 = new File(outFilePath + File.separator + "report_github281_label_invalid_2.json");

            // Run the invalid label test with bad checksum.

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/collection_gwe_spk_invalid_2_bad_checksum.xml",
                    };   
            this.launcher.processMain(args2);

            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.CHECKSUM_MISMATCH.getKey());
            assertEquals(1, count,  "1 " + ProblemType.CHECKSUM_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());


            File report3 = new File(outFilePath + File.separator + "report_github281_label_invalid_3.json");

            // Run the invalid label test with bad checksum, no file size.

            String[] args3 = {
                    "-r", report3.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/collection_gwe_spk_invalid_3_bad_checksum_no_filesize.xml",
                    };   
            this.launcher.processMain(args3);
            reportJson = gson.fromJson(new FileReader(report3), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.CHECKSUM_MISMATCH.getKey());
            assertEquals(1, count,  "1 " + ProblemType.CHECKSUM_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            // Run the valid label test 1.
            File report4 = new File(outFilePath + File.separator + "report_github281_label_valid_1.json");

            String[] args4 = {
                    "-r", report4.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/collection_gwe_spk_valid_1.xml",
                    };
            this.launcher.processMain(args4);
            reportJson = gson.fromJson(new FileReader(report4), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FILESIZE_MISMATCH.getKey());
            assertEquals(0, count,  "0 " + ProblemType.FILESIZE_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            File report5 = new File(outFilePath + File.separator + "report_github281_label_valid_2.json");

            String[] args5 = {
                    "-r", report5.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/collection_gwe_spk_valid_2_no_filesize.xml",
                    };
            this.launcher.processMain(args5);
            reportJson = gson.fromJson(new FileReader(report5), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FILESIZE_MISMATCH.getKey());
            assertEquals(0, count,  "0 " + ProblemType.FILESIZE_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());

            File report6 = new File(outFilePath + File.separator + "report_github281_label_valid_3.json");

            String[] args6 = {
                    "-r", report6.getAbsolutePath(),
                    "-R", "pds4.label",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/collection_gwe_spk_valid_3_no_filesize_no_checksum.xml",
                    };
            this.launcher.processMain(args6);
            reportJson = gson.fromJson(new FileReader(report6), JsonObject.class);

            count = this.getMessageCount(reportJson, ProblemType.FILESIZE_MISMATCH.getKey());
            assertEquals(0, count,  "0 " + ProblemType.FILESIZE_MISMATCH.getKey() + "  messages expected, received " + Integer.toString(count) + ".\n" + reportJson.toString());
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }



    @Test
    void testGithub6() {
        // Note: This test testGithub6 is very similar to testGithub5, except for the test resource containing an extra file:
        //
        //           data_spectra/000DANGLING_FILE_WITHOUT_EXTENSION
        //
        //       to demonstrate that the code now is able to 'see' a file without extension.

        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github6");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File report = new File(outFilePath + File.separator + "report_github6_bundle_invalid.json");

            // Run the invalid test #1 with bad file names and bad directory name with dot '.' character.

            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "invalid/bundle_kaguya_derived.xml",
                    };
            this.launcher.processMain(args);

            Gson gson = new Gson();
            JsonObject reportJson = null;

            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

            int count = 0;

            count = this.getMessageCount(reportJson, ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(6, count,  "6 " + ProblemType.FILE_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected.\n" + reportJson.toString());

            count = this.getMessageCount(reportJson, ProblemType.UNALLOWED_BASE_NAME.getKey());
            assertEquals(3, count,  "3 " + ProblemType.UNALLOWED_BASE_NAME.getKey() + "  messages expected.\n" + reportJson.toString());

            // The file a.out cannot be easily added to github.  The decision to not use a.out as a test artifact.
            count = this.getMessageCount(reportJson, ProblemType.UNALLOWED_FILE_NAME.getKey());
            assertEquals(0, count,  "0 " + ProblemType.UNALLOWED_FILE_NAME.getKey() + "  messages expected.\n" + reportJson.toString());

            // The directory my_dir.ext cannot be easily added to github.  The decision to not use directory my_dir.ext as a test artifact.
            count = this.getMessageCount(reportJson, ProblemType.DIR_NAME_HAS_INVALID_CHARS.getKey());
            assertEquals(0, count,  "0 " + ProblemType.DIR_NAME_HAS_INVALID_CHARS.getKey() + "  messages expected.\n" + reportJson.toString());

            // Run the valid bundle test.
            File report2 = new File(outFilePath + File.separator + "report_github6_bundle_valid.json");

            String[] args2 = {
                    "-r", report2.getAbsolutePath(),
                    "-R", "pds4.bundle",
                    "-s", "json",
                    "-t" , testPath + File.separator + "valid/bundle_kaguya_derived.xml",
                    };
            this.launcher.processMain(args2);
            reportJson = gson.fromJson(new FileReader(report2), JsonObject.class);
            assertEquals(0, reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt(),  "0 error messages expected.\n" + reportJson.toString());

        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }

    @Test
    void testGithub209() {
        try {
            // Setup paths
            String testPath = Utility.getAbsolutePath(TestConstants.TEST_DATA_DIR + File.separator + "github209");
            String outFilePath = TestConstants.TEST_OUT_DIR;
            File dir = new File(testPath);

            validateRunner(dir, outFilePath, "VALID", 0);
            validateRunner(dir, outFilePath, "FAIL1", 1);
            validateRunner(dir, outFilePath, "FAIL2", 2);
        } catch (ExitException e) {
            assertEquals(0, e.status, "Exit status");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test Failed Due To Exception: " + e.getMessage());
        }
    }
    
    void validateRunner(File dir, String outFilePath, String validity, int expectedCount) throws Exception {
        File[] files = dir.listFiles((d, name) -> name.startsWith(validity) && name.endsWith(".xml"));
        for (File xmlfile : files) {
            File report = new File(outFilePath + File.separator + "report_" + System.currentTimeMillis() + xmlfile.getName() + ".json");
            
            String[] args = {
                    "-r", report.getAbsolutePath(),
                    "-s", "json",
                    "-t" , xmlfile.getAbsolutePath(),
                    };
            this.launcher.processMain(args);
    
            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
    
            reportJson = gson.fromJson(new FileReader(report), JsonObject.class);
            
            String messageVar = "totalErrors";
            String msg = "error";
            if (validity.startsWith("WARN")) {
                messageVar = "totalWarnings";
                msg = "warning";
            }
            int count = this.getMessageCount(reportJson, messageVar);
            assertEquals(expectedCount, count, expectedCount + " " + msg + " message(s) expected. See validation report: " + report.getAbsolutePath());
            this.launcher.flushValidators();
        }
    }
    
    int getMessageCount(JsonObject reportJson, String messageTypeName) {
        int i = 0;
        JsonObject message = null;
        int count = 0;
        if (messageTypeName.equals("totalErrors")) {
            return reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt();
        }
        while (true) {
            try {
                message = reportJson.getAsJsonObject("summary").get("messageTypes").getAsJsonArray().get(i).getAsJsonObject();
                if (message.get("messageType").getAsString().equals(messageTypeName)) {
                    count = message.get("total").getAsInt();
                    return count;
                }
            } catch (IndexOutOfBoundsException e) {
                return count;
            }
            i++;
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
