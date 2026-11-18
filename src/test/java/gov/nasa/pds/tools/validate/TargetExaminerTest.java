package gov.nasa.pds.tools.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.Utility;

class TargetExaminerTest {
  @TempDir
  Path directory;

  @Test
  void reportsMalformedProductAfterLenientExamination() throws Exception {
    URL url = write("bundle.xml", "<Product_Bundle xmlns=\"http://pds.nasa.gov/pds4/pds/v1\">\n"
        + "  <lid_reference>example</lid_refer\nence>\n</Product_Bundle>\n");
    Utility.getTargetType(url);
    ProblemContainer problems = new ProblemContainer();
    List<Target> targets = new ArrayList<>(List.of(new Target(url, false)));

    TargetExaminer.removeNonLabels(targets, problems);

    assertTrue(targets.isEmpty());
    assertEquals(1, problems.getProblemCount());
    ValidationProblem problem = problems.getProblems().get(0);
    assertEquals(ExceptionType.ERROR, problem.getProblem().getSeverity());
    assertEquals(ProblemType.BAD_LABEL_FILE_ERROR, problem.getProblem().getType());
    assertTrue(problem.getMessage().contains("lid_reference"));
    assertTrue(problem.getLineNumber() > 0);
    assertTrue(problem.getColumnNumber() > 0);
  }

  @Test
  void retainsWarningForAmbiguousXmlData() throws Exception {
    URL url = write("data.xml", "<data><value></data>");
    ProblemContainer problems = new ProblemContainer();
    List<Target> targets = new ArrayList<>(List.of(new Target(url, false)));

    TargetExaminer.removeNonLabels(targets, problems);

    assertTrue(targets.isEmpty());
    assertEquals(ExceptionType.WARNING, problems.getProblems().get(0).getProblem().getSeverity());
    assertEquals(ProblemType.BAD_LABEL_FILE_WARN, problems.getProblems().get(0).getProblem().getType());
    assertTrue(problems.getProblems().get(0).getMessage().contains("value"));
  }

  @Test
  void reportsMalformedLblxAsErrorEvenWithoutProductRoot() throws Exception {
    URL url = write("label.lblx", "");
    ProblemContainer problems = new ProblemContainer();
    List<Target> targets = new ArrayList<>(List.of(new Target(url, false)));

    TargetExaminer.removeNonLabels(targets, problems);

    assertEquals(ExceptionType.ERROR, problems.getProblems().get(0).getProblem().getSeverity());
    assertTrue(problems.getProblems().get(0).getLineNumber() > 0);
  }

  @Test
  void reportsMalformedBundleRootAsError() throws Exception {
    URL url = write("bundle.xml", "<Product_Bundle attr=\"a\" attr=\"b\"/>");
    ProblemContainer problems = new ProblemContainer();

    TargetExaminer.removeNonLabels(new ArrayList<>(List.of(new Target(url, false))), problems);

    assertEquals(ExceptionType.ERROR, problems.getProblems().get(0).getProblem().getSeverity());
    assertTrue(problems.getProblems().get(0).getMessage().contains("attr"));
  }

  @Test
  void reportsOriginalErrorAfterInternalEntity() throws Exception {
    URL url = write("bundle.xml", "<!DOCTYPE Product_Bundle [<!ENTITY x \"example\">]>\n"
        + "<Product_Bundle xmlns=\"http://pds.nasa.gov/pds4/pds/v1\">"
        + "<lid_reference>&x;</lid_refer\nence></Product_Bundle>");
    ProblemContainer problems = new ProblemContainer();

    TargetExaminer.removeNonLabels(new ArrayList<>(List.of(new Target(url, false))), problems);

    assertEquals(ExceptionType.ERROR, problems.getProblems().get(0).getProblem().getSeverity());
    assertTrue(problems.getProblems().get(0).getMessage().contains("lid_reference"));
  }

  @Test
  void keepsWellFormedLabels() throws Exception {
    URL url = write("label.xml", "<Product_Bundle xmlns=\"http://pds.nasa.gov/pds4/pds/v1\">"
        + "<logical_identifier>example</logical_identifier></Product_Bundle>");
    ProblemContainer problems = new ProblemContainer();
    List<Target> targets = new ArrayList<>(List.of(new Target(url, false)));

    TargetExaminer.removeNonLabels(targets, problems);

    assertEquals(1, targets.size());
    assertEquals(0, problems.getProblemCount());
  }

  private URL write(String name, String content) throws Exception {
    return Files.writeString(directory.resolve(name), content).toUri().toURL();
  }
}
