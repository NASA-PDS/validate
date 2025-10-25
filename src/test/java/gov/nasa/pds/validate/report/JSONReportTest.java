// Copyright © 2019, California Institute of Technology ("Caltech").
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

package gov.nasa.pds.validate.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Unit tests for JSONReport class
 *
 * @author al-niessner
 * @author Claude (Anthropic)
 */
class JSONReportTest {

  /**
   * Test for GitHub issue #1408: JSON reports duplicate messages per product specified
   *
   * This test verifies that when validating multiple products, validation messages
   * from earlier products do not appear in later products' reports due to collections
   * not being cleared between products.
   */
  @Test
  void testNoDuplicateMessagesAcrossProducts() throws Exception {
    // Create a StringWriter to capture JSON output and wrap in PrintWriter
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    // Create JSONReport instance
    JSONReport report = new JSONReport();
    report.setWriter(printWriter);
    report.setLevel(ExceptionType.WARNING);

    // Start the report
    report.printHeader();
    report.startBody("Product Level Validation Results");

    // Simulate first product with an error
    URL product1Url = new URL("file:/test/product1.xml");
    ValidationProblem problem1 = new ValidationProblem(
        new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVALID_LABEL,
            "Test error in product 1"),
        product1Url);
    report.record(product1Url.toURI(), problem1);

    // Simulate second product with NO errors
    URL product2Url = new URL("file:/test/product2.xml");
    report.record(product2Url.toURI(), new ArrayList<>());  // Empty problems list

    // Complete the report
    report.printFooter();

    // Parse the generated JSON
    String jsonOutput = stringWriter.toString();
    Gson gson = new Gson();
    JsonObject reportJson = gson.fromJson(jsonOutput, JsonObject.class);

    // Verify the structure
    JsonArray products = reportJson.getAsJsonArray("productLevelValidationResults");
    assertEquals(2, products.size(), "Should have 2 products in report");

    // Check first product has the error
    JsonObject firstProduct = products.get(0).getAsJsonObject();
    JsonArray messages1 = firstProduct.getAsJsonArray("messages");
    assertEquals(1, messages1.size(), "First product should have 1 error message");

    // Check second product has NO errors (this is the key test for bug #1408)
    JsonObject secondProduct = products.get(1).getAsJsonObject();
    JsonArray messages2 = secondProduct.getAsJsonArray("messages");
    assertEquals(0, messages2.size(),
        "Second product should have 0 messages - bug #1408 would cause it to have " + messages1.size());

    // Also verify status
    assertEquals("FAIL", firstProduct.get("status").getAsString(),
        "First product should have FAIL status");
    assertEquals("PASS", secondProduct.get("status").getAsString(),
        "Second product should have PASS status");
  }

  /**
   * Test that multiple products each with their own unique errors don't cross-contaminate
   */
  @Test
  void testMultipleProductsWithUniqueErrors() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    JSONReport report = new JSONReport();
    report.setWriter(printWriter);
    report.setLevel(ExceptionType.WARNING);

    report.printHeader();
    report.startBody("Product Level Validation Results");

    // Product 1 with error type A
    URL product1Url = new URL("file:/test/product1.xml");
    ValidationProblem problem1 = new ValidationProblem(
        new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVALID_LABEL, "Error type A"),
        product1Url);
    report.record(product1Url.toURI(), problem1);

    // Product 2 with error type B (different from product 1)
    URL product2Url = new URL("file:/test/product2.xml");
    ValidationProblem problem2 = new ValidationProblem(
        new ProblemDefinition(ExceptionType.ERROR, ProblemType.MISSING_REFERENCED_FILE, "Error type B"),
        product2Url);
    report.record(product2Url.toURI(), problem2);

    // Product 3 with no errors
    URL product3Url = new URL("file:/test/product3.xml");
    report.record(product3Url.toURI(), new ArrayList<>());  // Empty problems list

    report.printFooter();

    // Parse JSON
    String jsonOutput = stringWriter.toString();
    Gson gson = new Gson();
    JsonObject reportJson = gson.fromJson(jsonOutput, JsonObject.class);
    JsonArray products = reportJson.getAsJsonArray("productLevelValidationResults");

    assertEquals(3, products.size());

    // Verify each product has only its own error
    JsonObject prod1 = products.get(0).getAsJsonObject();
    JsonArray msgs1 = prod1.getAsJsonArray("messages");
    assertEquals(1, msgs1.size());
    assertTrue(msgs1.get(0).getAsJsonObject().get("message").getAsString().contains("Error type A"));

    JsonObject prod2 = products.get(1).getAsJsonObject();
    JsonArray msgs2 = prod2.getAsJsonArray("messages");
    assertEquals(1, msgs2.size());
    assertTrue(msgs2.get(0).getAsJsonObject().get("message").getAsString().contains("Error type B"));

    JsonObject prod3 = products.get(2).getAsJsonObject();
    JsonArray msgs3 = prod3.getAsJsonArray("messages");
    assertEquals(0, msgs3.size(), "Third product should have no errors");
  }
}
