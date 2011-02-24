// Copyright 2006-2010, by the California Institute of Technology.
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
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.validate.status.Status;

import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a full report for the Vtool command line. This is the
 * standard report that will display all problems generated for every file that
 * was inspected. Messages are grouped at the file level and then summarized at
 * the end.
 *
 * @author pramirez
 *
 */
public class FullReport extends Report {

  @Override
  protected void printHeader(PrintWriter writer) {
    writer.println("Validation Details:");
  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<LabelException> problems) {
    Map<URI, List<LabelException>> externalProblems = new HashMap<URI, List<LabelException>>();
      writer.println();
      writer.print("  ");
      writer.print(status.getName());
      writer.print(": ");
      writer.println(sourceUri.toString());

    // Print all the sources problems and gather all external problems
    for (LabelException problem : problems) {
        printProblem(writer, problem);
    }
  }

  private void printProblem(PrintWriter writer,
      final LabelException problem) {
    writer.print("      ");
    String severity = "";
    if (problem.getExceptionType() == ExceptionType.FATAL) {
        severity = "FATAL_ERROR";
    } else if (problem.getExceptionType() == ExceptionType.ERROR) {
        severity = "ERROR";
    } else if (problem.getExceptionType() == ExceptionType.WARNING) {
        severity = "WARNING";
    }
    writer.print(severity);
    writer.print("  ");
    if (problem.getLineNumber() != null
        && problem.getLineNumber() != -1) {
      writer.print("line ");
      writer.print(problem.getLineNumber().toString());
      if (problem.getColumnNumber() != null
        && problem.getColumnNumber() != -1) {
        writer.print(", ");
        writer.print(problem.getColumnNumber().toString());
      }
      writer.print(": ");
    }
    writer.println(problem.getMessage());
  }

  @Override
  protected void printFooter(PrintWriter writer) {
    // No op
  }

  @Override
  protected void printRecordSkip(PrintWriter writer, final URI sourceUri,
      final Exception exception) {
    writer.println();
    writer.print("  ");
    writer.print(Status.SKIP.getName());
    writer.print(": ");
    writer.println(sourceUri.toString());

    if (exception instanceof LabelException) {
      LabelException problem = (LabelException) exception;
      writer.print("      ");
      String severity = "";
      if(problem.getExceptionType() == ExceptionType.FATAL) {
          severity = "FATAL_ERROR";
      } else if(problem.getExceptionType() == ExceptionType.ERROR) {
          severity = "ERROR";
      } else if(problem.getExceptionType() == ExceptionType.WARNING) {
          severity = "WARNING";
      }
      writer.print(severity);
      writer.print("  ");
      if (problem.getLineNumber() != null
          && problem.getLineNumber() != -1) {
        writer.print("line ");
        writer.print(problem.getLineNumber().toString());
        if (problem.getColumnNumber() != null
            && problem.getColumnNumber() != -1) {
          writer.print(", ");
          writer.print(problem.getColumnNumber().toString());
        }
        writer.print(": ");
      }
      writer.println(problem.getMessage());
    } else {
      writer.print("      ");
      writer.print("ERROR");
      writer.print("  ");
      writer.println(exception.getMessage());
    }
  }
}
