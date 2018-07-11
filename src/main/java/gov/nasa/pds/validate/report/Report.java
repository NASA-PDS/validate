// Copyright 2006-2018, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$

package gov.nasa.pds.validate.report;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.status.Status;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

/**
 * Abstract class that represents a Report for the Vtool command line API. This
 * class handles basic utilities for reporting and calling customized portions
 * of reports.
 *
 * @author pramirez
 *
 */
public abstract class Report {
  private int totalWarnings;
  private int totalErrors;
  private int totalInfos;
  private int numSkipped;
  private int numFailed;
  private int numPassed;
  protected final List<String> parameters;
  protected final List<String> configurations;
  protected PrintWriter writer;
  private ExceptionType level;
  protected Map<String, Long> messageSummary; 

  /**
   * Default constructor to initialize report variables. Initializes default
   * output to System.out if you wish to write the report to a different sourse
   * use the appropriate setOutput method.
   */
  public Report() {
    this.totalWarnings = 0;
    this.totalErrors = 0;
    this.totalInfos = 0;
    this.numFailed = 0;
    this.numPassed = 0;
    this.numSkipped = 0;
    this.parameters = new ArrayList<String>();
    this.configurations = new ArrayList<String>();
    this.writer = new PrintWriter(new OutputStreamWriter(System.out));
    this.messageSummary = new HashMap<String, Long>();
    this.level = ExceptionType.WARNING;
  }

  /**
   * Handles writing a Report to the writer interface. This is is useful if
   * someone would like to put the contents of the Report to something such as
   * {@link java.io.StringWriter}.
   *
   * @param writer
   *          which the report will be written to
   */
  public void setOutput(Writer writer) {
    this.writer = new PrintWriter(writer);
  }

  /**
   * Handle writing a Report to an {@link java.io.OutputStream}. This is useful
   * to get the report to print to something such as System.out
   *
   * @param os
   *          stream which the report will be written to
   */
  public void setOutput(OutputStream os) {
    this.setOutput(new OutputStreamWriter(os));
  }

  /**
   * Handles writing a Report to a {@link java.io.File}.
   *
   * @param file
   *          which the report will output to
   * @throws IOException
   *           if there is an issue in writing the report to the file
   */
  public void setOutput(File file) throws IOException {
    this.setOutput(new FileWriter(file));
  }

  /**
   * This method will display the default header for the Vtool command line
   * library reports. This is the standard header across all reports.
   */
  public void printHeader() {
    writer.println("PDS Validate Tool Report");
    writer.println();
    writer.println("Configuration:");
    for (String configuration : configurations) {
      writer.println(configuration);
    }
    writer.println();
    writer.println("Parameters:");
    for (String parameter : parameters) {
      writer.println(parameter);
    }
    writer.println();
    printHeader(this.writer, "Product Level Validation Results");
  }

  /**
   * Adds the string supplied to the parameter section in the heading of the
   * report.
   *
   * @param parameter
   *          in a string form that represents something that was passed in when
   *          the tool was run
   */
  public void addParameter(String parameter) {
    this.parameters.add(parameter);
  }

  /**
   * Adds the string supplied to the configuration section in the heading of the
   * report.
   *
   * @param configuration
   *          in a string form that represents a configuration that was used
   *          during parsing and validation
   */
  public void addConfiguration(String configuration) {
    this.configurations.add(configuration);
  }

  public void printHeader(String title) {
    printHeader(this.writer, title);
  }
  
  /**
   * Allows a Report to customize the header portion of the Report if necessary.
   *
   * @param writer
   *          passed down to write header contents to
   */
  protected abstract void printHeader(PrintWriter writer, String title);

  public Status record(URI sourceUri, final ValidationProblem problem) {
      List<ValidationProblem> problems = new ArrayList<ValidationProblem>();
      problems.add(problem);
      return record(sourceUri, problems);
  }

  /**
   * Allows a report to change how they manage reporting on a given file that
   * has been parsed and validated. Also handles generating a status for a file
   * and generating some summary statistics.
   *
   * @param sourceUri
   *          reference to the file that is being reported on
   * @param problems
   *          the set of issues found with the file. to be reported on
   * @return status of the file (i.e. PASS, FAIL, or SKIP)
   */
  public Status record(URI sourceUri, final List<ValidationProblem> problems) {
    int numErrors = 0;
    int numWarnings = 0;
    int numInfos = 0;
    Status status = Status.PASS;

    // TODO: Handle null problems

    for (ValidationProblem problem : problems) {
      if (problem.getProblem().getSeverity() == ExceptionType.ERROR ||
         problem.getProblem().getSeverity() == ExceptionType.FATAL) {
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
          numInfos++;
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      } else if (problem.getProblem().getSeverity() == ExceptionType.DEBUG) {
        if (ExceptionType.DEBUG.getValue() <= this.level.getValue()) {
          addToMessageSummary(problem.getProblem().getType().getKey());
        }
      }
    }
    this.totalErrors += numErrors;
    this.totalInfos += numInfos;
    this.totalWarnings += numWarnings;

    if (numErrors > 0) {
      this.numFailed++;
      status = Status.FAIL;
    } else {
      this.numPassed++;
    }
    printRecordMessages(this.writer, status, sourceUri, problems);
    this.writer.flush();
    return status;
  }
  
  private void addToMessageSummary(String type) {
    if (this.messageSummary.containsKey(type)) {
      long count = this.messageSummary.get(type).longValue();
      this.messageSummary.put(type, new Long(count + 1));
    } else {
      this.messageSummary.put(type, new Long(1));
    }
  }
  
  protected Map<String, Long> sortMessageSummary(Map<String, Long> messageSummary) {
    List<Map.Entry<String, Long>> entries = new 
        ArrayList<Map.Entry<String, Long>>(messageSummary.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
      public int compare(Map.Entry<String, Long> a, Map.Entry<String, Long> b){
        int result = 0;
        ExceptionType aKey = getExceptionType(a.getKey());
        ExceptionType bKey = getExceptionType(b.getKey());
        if (aKey.getValue() > bKey.getValue()) {
          result = 1;
        } else if (aKey.getValue() < bKey.getValue()) {
          result = -1;
        } else {
          // keys are the same severity level
          int valueResult = a.getValue().compareTo(b.getValue());
          if (valueResult > 0) {
            result = -1;
          } else if (valueResult < 0) {
            result = 1;
          } else {
            result = 0;
          }
        }
        return result;
      }
    });
    Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
    for (Map.Entry<String, Long> entry : entries) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }
  
  private ExceptionType getExceptionType(String type) {
    if (type.startsWith("error")) {
      return ExceptionType.ERROR;
    } else if (type.startsWith("warning")) {
      return ExceptionType.WARNING;
    } else if (type.startsWith("info")) {
      return ExceptionType.INFO;
    } else {
      return ExceptionType.DEBUG;
    }
  }
  
  public Status recordSkip(final URI sourceUri, final ValidationProblem problem) {
    this.numSkipped++;
    if (problem.getProblem().getSeverity().getValue() <= this.level.getValue()) {
      printRecordSkip(this.writer, sourceUri, problem);
    }
    return Status.SKIP;
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri,
      final ValidationProblem problem) {
    // no op
  }

  /**
   * Allows a report to customize how it handles reporting on a particular
   * label.
   *
   * @param writer
   *          passed on to write customized messages to
   * @param sourceUri
   *          reference to the file that is being reported on
   * @param problems
   *          which to report on for this source
   */
  protected abstract void printRecordMessages(PrintWriter writer,
      final Status status, final URI sourceUri,
      final List<ValidationProblem> problems);

  /**
   * Prints out the footer or the report and calls the customized footer
   * section.
   */
  public void printFooter() {
    printFooter(writer);
    writer.println();

    writer.println("Summary:");
    writer.println();
    writer.println("  " + totalErrors + " error(s)");
    writer.println("  " + totalWarnings + " warning(s)");
    writer.println();
    if (!this.messageSummary.isEmpty()) {
      writer.println("  Message Types:");
      Map<String, Long> sortedMessageSummary = sortMessageSummary(this.messageSummary);
      for (String type : sortedMessageSummary.keySet()) {
        writer.print("    ");
        writer.printf("%-10d", sortedMessageSummary.get(type));
        writer.print("   ");
        writer.println(type);
      }
    }
    writer.println();
    writer.println("End of Report");
    this.writer.flush();
  }

  /**
   * Allows customization of the footer section of the report
   *
   * @param writer
   *          passed on to writer customized footer contents
   */
  protected abstract void printFooter(PrintWriter writer);

  /**
   *
   * @return number of labels that passed (had no errors)
   */
  public int getNumPassed() {
    return this.numPassed;
  }

  /**
   *
   * @return number of labels that failed (had one or more errors)
   */
  public int getNumFailed() {
    return this.numFailed;
  }

  /**
   *
   * @return number of files that were not recognized as a label
   */
  public int getNumSkipped() {
    return this.numSkipped;
  }

  /**
   *
   * @return total number of errors that were found across all labels inspected.
   *         Will not count errors generated from files that were considered
   *         skipped.
   */
  public int getTotalErrors() {
    return this.totalErrors;
  }

  /**
   *
   * @return total number of warning that were found across all labels
   *         inspected. Will not count warnings generated from files that were
   *         considered skipped.
   */
  public int getTotalWarnings() {
    return this.totalWarnings;
  }

  /**
   *
   * @return total number of info messages that were found across all labels
   *         inspected. Will not count info messages from files that were
   *         considered skipped.
   */
  public int getTotalInfos() {
    return this.totalInfos;
  }

  /**
   *
   * @return flag indicating if errors were found in the inspected files
   */
  public boolean hasErrors() {
    return (this.totalErrors > 0) ? true : false;
  }

  /**
   *
   * @return flag indicating if warnings were found in the inspected files
   */
  public boolean hasWarnings() {
    return (this.totalWarnings > 0) ? true : false;
  }

  /**
   * Anything at or above the level will be reported. Default ExceptionType level is
   * info and above
   *
   * @param ExceptionType
   *          level on which items will be reported
   */
  public void setLevel(ExceptionType ExceptionType) {
    this.level = ExceptionType;
  }

  /**
   *
   * @return ExceptionType level of items that will be reported on. Anything at or
   *         above this level will be reported on
   */
  public ExceptionType getLevel() {
    return this.level;
  }
  
  public String getType(String systemId) {
    String type = "Fragment";
    String extension = FilenameUtils.getExtension(systemId);
    if ("xsd".equalsIgnoreCase(extension)) {
      type = "Schema";
    } else if ("sch".equalsIgnoreCase(extension)) {
      type = "Schematron";
    }
    return type;
  }
}
