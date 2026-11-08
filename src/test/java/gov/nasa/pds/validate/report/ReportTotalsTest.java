package gov.nasa.pds.validate.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;

/**
 * Regression tests for NASA-PDS/validate#1651: error and warning totals above
 * Integer.MAX_VALUE were reported as negative numbers.
 */
class ReportTotalsTest {
  private static final String PAST_INT_MAX = "2147483648";

  /** Starts both totals at Integer.MAX_VALUE, records one more of each, returns the report. */
  private String reportPastIntMax(Report report) throws Exception {
    StringWriter output = new StringWriter();
    report.setWriter(new PrintWriter(output));
    report.setLevel(ExceptionType.WARNING);
    report.printHeader();
    report.startBody("Product Level Validation Results");
    for (String total : List.of("totalErrors", "totalWarnings")) {
      Field field = Report.class.getDeclaredField(total);
      field.setAccessible(true);
      field.set(report, Integer.MAX_VALUE);
    }
    URL product = new URL("file:/test/product.xml");
    report.record(product.toURI(), List.of(
        new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
            ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH, "test error"), product),
        new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
            ProblemType.CONTEXT_REFERENCE_NOT_FOUND, "test warning"), product)));
    report.printFooter();
    report.getWriter().flush();
    return output.toString();
  }

  @Test
  void fullReportKeepsTotalsPositive() throws Exception {
    String text = reportPastIntMax(new FullReport());
    assertTrue(text.contains("  " + PAST_INT_MAX + " error(s)"), text);
    assertTrue(text.contains("  " + PAST_INT_MAX + " warning(s)"), text);
  }

  @Test
  void jsonReportKeepsTotalsPositive() throws Exception {
    String json = reportPastIntMax(new JSONReport()).replaceAll("\\s", "");
    assertTrue(json.contains("\"totalErrors\":" + PAST_INT_MAX), json);
    assertTrue(json.contains("\"totalWarnings\":" + PAST_INT_MAX), json);
  }

  @Test
  void xmlReportKeepsTotalsPositive() throws Exception {
    String xml = reportPastIntMax(new XmlReport());
    assertTrue(xml.contains("<totalErrors>" + PAST_INT_MAX + "</totalErrors>"), xml);
    assertTrue(xml.contains("<totalWarnings>" + PAST_INT_MAX + "</totalWarnings>"), xml);
  }
}
