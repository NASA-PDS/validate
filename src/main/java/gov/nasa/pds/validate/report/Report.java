package gov.nasa.pds.validate.report;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemCategory;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.status.Status;

/**
 * This class is a visitor pattern where the Report uses the abstract methods to
 * define what the visitee should do. While this is not exactly a visitor pattern
 * this implementation was chosen to minimize changes throughout the system as
 * everyone in the universe does not have to pass a visitee now. It is simply
 * embedded in the concrete extension of this class.
 */
public abstract class Report {
  /**
   * A report is made up of a HEADER, BODY, then FOOTER. The BODY is then made up
   * of a series of LABELs. The different formats should use the state changes for
   * format specific tasks such as creating or closing subelements. Flushing of the
   * writer is done in this portion of the code to enforce consistent behavior.
   */
  protected enum Block { BODY, FOOTER, HEADER, LABEL };
  abstract protected void append (Status status, String target);
  abstract protected void append (String title);
  abstract protected void append (ValidationProblem problem);
  abstract protected void appendConfig (String label, String message, String value);
  abstract protected void appendParam (String label, String message, String value);
  abstract protected void begin(Block block);
  abstract protected void end(Block block);
  abstract protected void summarizeAddMessage (String msg, long count);
  abstract protected void summarizeDepWarn (String msg);
  abstract protected void summarizeProds (int failed, int passed, int skipped, int total);
  abstract protected void summarizeRefs (int failed, int passed, int skipped, int total);
  abstract protected void summarizeTotals (int errors, int total, int warnings);

  protected class Tuple {
    final String a,b,c;
    Tuple (String a, String b, String c){
      this.a = a;
      this.b = b;
      this.c = c;
    }
  };
  private static final Logger LOG = LoggerFactory.getLogger(Report.class);
  private static String DEPRECATED_FLAG_WARNING_MSG =
      ("NOTE: The following flags have been deprecated. These options will no longer be supported after December 20, 2021.\n"
          + "      Update execution as soon as possible to avoid issues. Contact pds_operator@jpl.nasa.gov if you have issues.\n\n"
          + "      --force (-f)--model-version (-m) flags have been deprecated. \n"
          + "      The default behavior of the Validate Tool validates against the schemas and \n"
          + "      schematrons specified in a label.  \n\n"
          + "      Please use -x and/or -S flag(s) to validate with the core PDS or user-specified \n"
          + "      schema and schematron.\n\n"
          + "      --no-check-data flag has been deprecated and replaced by --skip-content-validation to avoid confusion in naming "
          + "      of other flags. Please change software execution to use the new flag to avoid any issues.");

  private boolean deprecatedFlagWarning = false;
  private boolean integrityCheckFlag = false;
  private int totalWarnings = 0;
  private int totalErrors = 0;
  private int numSkipped = 0;
  private int numPassedProds = 0;
  private int numFailedProds = 0;
  private int numSkippedProds = 0;
  private int totalProducts = 0;
  private int numPassedIntegrityChecks = 0;
  private int numFailedIntegrityChecks = 0;
  private int numSkippedIntegrityChecks = 0;
  private int totalIntegrityChecks = 0;
  private final ArrayList<Tuple> parameters = new ArrayList<Tuple>();
  private final ArrayList<Tuple> configurations = new ArrayList<Tuple>();
  private PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));;
  private ExceptionType level = ExceptionType.WARNING;
  private final HashMap<String, Long> messageSummary = new HashMap<String, Long>();

  final public void addConfiguration (String label, String msg, String value) {
    this.configurations.add (new Tuple(label,msg,value));
  }
  final private void addToMessageSummary(String type) {
    if (this.messageSummary.containsKey(type)) {
      long count = this.messageSummary.get(type).longValue();
      this.messageSummary.put(type, Long.valueOf(count + 1));
    } else {
      this.messageSummary.put(type, Long.valueOf(1));
    }
  }
  final public void addParameter (String label, String msg, String value) {
    this.parameters.add (new Tuple(label,msg,value));
  }
  final public int getTotalIntegrityChecks() {
    return totalIntegrityChecks;
  }
  final public int getTotalProducts() {
    return this.totalProducts;
  }
  final protected String getType(String systemId) {
    String type = "Fragment";
    String extension = FilenameUtils.getExtension(systemId);
    if ("xsd".equalsIgnoreCase(extension)) {
      type = "Schema";
    } else if ("sch".equalsIgnoreCase(extension)) {
      type = "Schematron";
    }
    return type;
  }
  final public PrintWriter getWriter() {
    return writer;
  }
  final public void printFooter() {
    this.end (Block.BODY);
    this.begin (Block.FOOTER);
    this.summarizeTotals (this.totalErrors, this.totalProducts, this.totalWarnings);
    this.summarizeProds (this.numFailedProds, this.numPassedProds, this.numSkippedProds, this.totalProducts);
    this.summarizeRefs (this.numFailedIntegrityChecks, this.numPassedIntegrityChecks, this.numSkippedIntegrityChecks, this.totalIntegrityChecks);
    List<String> keys = new ArrayList<String>(this.messageSummary.keySet());
    Collections.sort(keys);  // yuk, need a sorted function or something in java because this is just terrible
    for (String key : keys) {
      this.summarizeAddMessage (key, this.messageSummary.get(key));
    }
    if (this.deprecatedFlagWarning) {
      this.summarizeDepWarn (Report.DEPRECATED_FLAG_WARNING_MSG);
    }
    this.end (Block.FOOTER);
    this.writer.flush();
  }
  final public void printHeader(String title) {
    this.begin (Block.HEADER);
    if (title != null) {
      /*
       * barrowed code/comment from older version of FullReport:
       * 
       * A hacky way to properly track when we are completing product validation versus integrity
       * checks. Once we try to print a header other than the initial product level validation,
       * we are now into integrity checks.
       */
      if (title.toLowerCase().contains("pds4 bundle")
          || title.toLowerCase().contains("pds4 collection")) {
        this.integrityCheckFlag = true;
      }
      this.append (title);
    } else {
      this.append("PDS Validate Tool Report");
    }
    for (Tuple t : this.configurations) {
      this.appendConfig (t.a, t.b, t.c);
    }
    for (Tuple t : this.parameters) {
      this.appendParam (t.a, t.b, t.c);
    }
    this.end (Block.HEADER);
    this.begin (Block.BODY);
    this.append ("Product Level Validation Results");
    this.writer.flush();
  }
 /**
   * Allow single call that turns it into many.
   * 
   * @param sourceUri
   * @param problem
   * @return
   */
  final public Status record(URI sourceUri, final ValidationProblem problem) {
    List<ValidationProblem> problems = new ArrayList<>();
    problems.add(problem);
    LOG.debug("record:RECORDING_PROBLEM:sourceUri {}", sourceUri);
    return record(sourceUri, problems);
  }

  /**
   * Allows a report to change how they manage reporting on a given file that has been parsed and
   * validated. Also handles generating a status for a file and generating some summary statistics.
   *
   * @param sourceUri reference to the file that is being reported on
   * @param problems the set of issues found with the file. to be reported on
   * @return status of the file (i.e. PASS, FAIL, or SKIP)
   */
  final public Status record(URI sourceUri, final List<ValidationProblem> problems) {
    int numErrors = 0;
    int numWarnings = 0;
    Status status = Status.PASS;
    LOG.debug("record:RECORDING_PROBLEM:sourceUri,problems.size {},{}", sourceUri, problems.size());

    // TODO: Handle null problems
    int ignoreFromProductCounts = 0;
    for (ValidationProblem problem : problems) {
      if (problem.getProblem().getSeverity() == ExceptionType.ERROR
          || problem.getProblem().getSeverity() == ExceptionType.FATAL) {
        if (ExceptionType.ERROR.getValue() <= this.level.getValue()) {
          numErrors++;
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      } else if (problem.getProblem().getSeverity() == ExceptionType.WARNING) {
        if (ExceptionType.WARNING.getValue() <= this.level.getValue()) {
          numWarnings++;
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      } else if (problem.getProblem().getSeverity() == ExceptionType.INFO) {
        if (ExceptionType.INFO.getValue() <= this.level.getValue()) {
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      } else if (problem.getProblem().getSeverity() == ExceptionType.DEBUG) {
        if (ExceptionType.DEBUG.getValue() <= this.level.getValue()) {
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      }

      // Check ProblemCategory to remove from product counts
      ProblemCategory category = problem.getProblem().getType().getProblemCategory();
      if (category.equals(ProblemCategory.GENERAL) || category.equals(ProblemCategory.EXECUTION)) {
        ignoreFromProductCounts++;
      }
    }
    this.totalErrors += numErrors;
    this.totalWarnings += numWarnings;

    if (numErrors > 0) {
      status = Status.FAIL;
      LOG.debug("record:sourceUri {}", sourceUri);
      LOG.debug("record:sourceUri.toString {}", sourceUri == null ? "null":sourceUri.toString());

      if (!Utility.isDir(sourceUri.toString())) {
        if (!this.integrityCheckFlag) {
          this.numFailedProds++;
        } else {
          this.numFailedIntegrityChecks++;
        }
      }
    } else {
      if (!Utility.isDir(sourceUri.toString())) {
        if (!this.integrityCheckFlag) {
          this.numPassedProds++;
        } else {
          this.numPassedIntegrityChecks++;
        }
      }
    }

    this.totalProducts = this.numFailedProds + this.numPassedProds - ignoreFromProductCounts;
    this.totalIntegrityChecks = this.numFailedIntegrityChecks + this.numPassedIntegrityChecks
        + this.numSkippedIntegrityChecks;
    this.begin (Block.LABEL);
    this.append (status, sourceUri == null ? "null":sourceUri.toString());
    for (ValidationProblem problem : problems) {
      this.append (problem);
    }
    this.end (Block.LABEL);
    this.writer.flush();
    return status;
  }
  public Status recordSkip(final URI sourceUri, final ValidationProblem problem) {
    this.numSkipped++;
    LOG.debug("recordSkip:sourceUri,numSkipped {},{}", sourceUri, this.numSkipped);
    LOG.debug(
        "recordSkip:sourceUri,problem.getProblem().getSeverity().getValue(),this.level.getValue() {},{},{},{}",
        sourceUri, problem.getProblem().getSeverity().getValue(), this.level.getValue(),
        (problem.getProblem().getSeverity().getValue() <= this.level.getValue()));
    if (!Utility.isDir(sourceUri.toString())) {
      LOG.debug("recordSkip:sourceUri,integrityCheckFlag {},{}", sourceUri,
          this.integrityCheckFlag);
      if (!this.integrityCheckFlag) {
        this.numSkippedProds++;
      } else {
        this.numSkippedIntegrityChecks++;
      }
    }
    this.begin (Block.LABEL);
    this.append (Status.SKIP, sourceUri.toString());
    this.append(problem);
    this.end (Block.LABEL);
    return Status.SKIP;
  }
  final public void setDeprecatedFlagWarning(boolean deprecatedFlagWarning) {
    this.deprecatedFlagWarning = deprecatedFlagWarning;
  }
  final public void setLevel(ExceptionType level) {
    this.level = level;
  }
  final public void setWriter(PrintWriter writer) {
    this.writer = writer;
  }
}
