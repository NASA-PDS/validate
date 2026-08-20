// Copyright © 2026, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
//   conditions and the following disclaimer in the documentation and/or other
//   materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
//   Laboratory, nor the names of its contributors may be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import gov.nasa.pds.validate.constants.TestConstants;
import gov.nasa.pds.validate.util.ToolInfo;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ValidateLauncher.getLatestJsonContext() — the registry update path
 * triggered by the {@code -u} / {@code --latest-json-file} flag.
 *
 * <p>Uses a JDK built-in {@link HttpServer} as a local mock so no network
 * access is required.
 *
 * <p>Relates to GitHub issue NASA-PDS/validate#1659.
 */
class GetLatestJsonContextTest {

  private static final String SORT_KEY = "ops:Label_File_Info.ops:creation_date_time";

  private HttpServer server;
  private File outputFile;
  private String originalSearchUrl;

  @BeforeEach
  void setUp() throws Exception {
    originalSearchUrl = ToolInfo.getSearchURL();
    System.setProperty("resources.home", TestConstants.RESOURCES_DIR);
    outputFile = File.createTempFile("registered_context_products_test_", ".json");
    outputFile.deleteOnExit();
  }

  @AfterEach
  void tearDown() throws Exception {
    if (server != null) {
      server.stop(0);
    }
    ToolInfo.setProperty(ToolInfo.SEARCH_URL, originalSearchUrl);
  }

  /**
   * Happy path: mock server returns 2 context products in a single page.
   * Verifies the output file is written and contains the expected number of entries.
   */
  @Test
  void testSuccessfulSinglePageDownload() throws Exception {
    String responseBody = buildPageResponse(2, 2, "2024-01-01T00:00:00Z");
    server = startMockServer(200, responseBody);
    int port = server.getAddress().getPort();
    ToolInfo.setProperty(ToolInfo.SEARCH_URL, "http://localhost:" + port);

    ValidateLauncher launcher = new ValidateLauncher();
    launcher.setReportStyle("full");
    launcher.setupReport(new String[0]);
    launcher.setRegisteredProductsFile(outputFile);

    launcher.getLatestJsonContext();

    assertTrue(outputFile.exists(), "Output file should be written");
    assertTrue(outputFile.length() > 0, "Output file should not be empty");

    String content = new String(Files.readAllBytes(outputFile.toPath()), StandardCharsets.UTF_8);
    JsonObject json = new Gson().fromJson(content, JsonObject.class);
    assertTrue(json.has("Product_Context"), "Output JSON should contain Product_Context array");
    JsonArray products = json.getAsJsonArray("Product_Context");
    assertEquals(2, products.size(), "Should have written 2 context products");
  }

  /**
   * Pagination: mock server reports total=3 but serves 2 per page; the second page
   * supplies the remaining 1. Verifies the do/while loop pages correctly.
   */
  @Test
  void testPaginatedDownload() throws Exception {
    // Page 1: hits=3, returns 2 items
    String page1 = buildPageResponse(3, 2, "2024-01-02T00:00:00Z");
    // Page 2: hits=3, returns 1 item (loop terminates when contexts.size() >= total)
    String page2 = buildPageResponse(3, 1, "2024-01-03T00:00:00Z");

    int[] requestCount = {0};
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext("/" + ToolInfo.getEndpoint(), exchange -> {
      String body = requestCount[0]++ == 0 ? page1 : page2;
      byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    });
    server.start();

    int port = server.getAddress().getPort();
    ToolInfo.setProperty(ToolInfo.SEARCH_URL, "http://localhost:" + port);

    ValidateLauncher launcher = new ValidateLauncher();
    launcher.setReportStyle("full");
    launcher.setupReport(new String[0]);
    launcher.setRegisteredProductsFile(outputFile);

    launcher.getLatestJsonContext();

    assertEquals(2, requestCount[0], "Should have made exactly 2 HTTP requests for pagination");
    assertTrue(outputFile.exists(), "Output file should be written after paginated download");
  }

  /**
   * Error path: mock server returns HTTP 400. Verifies the method catches the
   * IOException and does not throw, leaving the output file unwritten or unchanged.
   */
  @Test
  void testHttp400DoesNotThrow() throws Exception {
    server = startMockServer(400, "Bad Request");
    int port = server.getAddress().getPort();
    ToolInfo.setProperty(ToolInfo.SEARCH_URL, "http://localhost:" + port);

    File originalFile = outputFile;
    // Remove the file so we can assert it was NOT written on error
    Files.deleteIfExists(originalFile.toPath());

    ValidateLauncher launcher = new ValidateLauncher();
    launcher.setReportStyle("full");
    launcher.setupReport(new String[0]);
    launcher.setRegisteredProductsFile(originalFile);

    // Should not throw
    launcher.getLatestJsonContext();

    assertFalse(originalFile.exists(), "Output file should not be written when registry returns 400");
  }

  // --- helpers ---

  private HttpServer startMockServer(int statusCode, String body) throws IOException {
    HttpServer s = HttpServer.create(new InetSocketAddress(0), 0);
    s.createContext("/" + ToolInfo.getEndpoint(), exchange -> {
      byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(statusCode, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    });
    s.start();
    return s;
  }

  /**
   * Builds a minimal PDS Search API JSON response with {@code count} synthetic
   * Product_Context entries, reporting {@code total} hits.
   *
   * <p>The {@code properties} object mirrors the actual API shape that
   * {@code parseJsonObjectWriteTofile} reads: {@code lidvid}, {@code title},
   * the sort key, and at least one typed field.
   */
  private String buildPageResponse(int total, int count, String creationDate) {
    StringBuilder data = new StringBuilder("[");
    for (int i = 0; i < count; i++) {
      if (i > 0) data.append(",");
      String lidvid = "urn:nasa:pds:context:target:test.target_" + i + "::1.0";
      data.append("{")
          .append("\"id\":\"").append(lidvid).append("\",")
          .append("\"type\":\"Product_Context\",")
          .append("\"properties\":{")
          .append("\"lidvid\":[\"").append(lidvid).append("\"],")
          .append("\"title\":[\"Test Target ").append(i).append("\"],")
          .append("\"").append(SORT_KEY).append("\":[\"").append(creationDate).append("\"],")
          .append("\"pds:Target.pds:type\":[\"Asteroid\"]")
          .append("}}");
    }
    data.append("]");

    return "{\"summary\":{\"hits\":" + total + "},\"data\":" + data + "}";
  }
}
