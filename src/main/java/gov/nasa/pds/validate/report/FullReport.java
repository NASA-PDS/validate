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

import gov.nasa.pds.tools.label.ContentException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.validate.content.array.ArrayContentException;
import gov.nasa.pds.tools.validate.content.table.TableContentException;
import gov.nasa.pds.validate.status.Status;

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
  protected void printHeader(PrintWriter writer, String title) {
    //writer.println("Validation Details:");
    writer.println();
    writer.println();
    writer.println(title);
  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<LabelException> problems) {
    Map<String, List<LabelException>> externalProblems = new LinkedHashMap<String, List<LabelException>>();
    Map<String, List<ContentException>> contentProblems = new LinkedHashMap<String, List<ContentException>>();
      writer.println();
      writer.print("  ");
      writer.print(status.getName());
      writer.print(": ");
      writer.println(sourceUri.toString());

    // Print all the sources problems and gather all external problems
    for (Iterator<LabelException> iterator = problems.iterator(); iterator.hasNext();) {
      LabelException problem = iterator.next();
      if (problem instanceof ContentException) {
        ContentException contentProb = (ContentException) problem;
        List<ContentException> contentProbs = contentProblems.get(contentProb.getSource());
        if (contentProbs == null) {
          contentProbs = new ArrayList<ContentException>();
        }
        contentProbs.add(contentProb);
        contentProblems.put(contentProb.getSource(), contentProbs);
      } else {
        if ( ((problem.getPublicId() == null)
            && (problem.getSystemId() == null))
            || sourceUri.toString().equals(problem.getSystemId())) {
          printProblem(writer, problem);
        } else {
          List<LabelException> extProbs = externalProblems.get(problem.getSystemId());
          if (extProbs == null) {
            extProbs = new ArrayList<LabelException>();
          }
          extProbs.add(problem);
          externalProblems.put(problem.getSystemId(), extProbs);
        }
      }
      iterator.remove();
    }
    for (String extSystemId : externalProblems.keySet()) {
      writer.print("    Begin " + getType(extSystemId) + ": ");
      writer.println(extSystemId);
      for (LabelException problem : externalProblems.get(extSystemId)) {
        printProblem(writer, problem);
      }
      writer.print("    End " + getType(extSystemId) + ": ");
      writer.println(extSystemId);
    }
    for (String dataFile : contentProblems.keySet()) {
      writer.print("    Begin Content Validation: ");
      writer.println(dataFile);
      for (ContentException problem : contentProblems.get(dataFile)) {
        printProblem(writer, problem);
      }
      writer.print("    End Content Validation: ");
      writer.println(dataFile);
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
    } else if (problem.getExceptionType() == ExceptionType.INFO) {
      severity = "INFO";
    } else if (problem.getExceptionType() == ExceptionType.DEBUG) {
      severity = "DEBUG";
    }
    writer.print(severity);
    writer.print("  ");
    if (problem instanceof TableContentException) {
      TableContentException tcProblem = (TableContentException) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
        writer.print("table ");
        writer.print(tcProblem.getTable().toString());
      }
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        writer.print(", ");
        writer.print("record " + tcProblem.getRecord().toString());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        writer.print(", ");
        writer.print("field " + tcProblem.getField().toString());        
      }
      writer.print(": ");
    } else if (problem instanceof ArrayContentException) {
      // For now, we'll assuming we will report on image arrays
      ArrayContentException aProblem = (ArrayContentException) problem;
      if (aProblem.getArray() != null && aProblem.getArray() != -1) {
        writer.print("array ");
        writer.print(aProblem.getArray().toString());
      }
      if (aProblem.getLocation() != null) {
        writer.print(", ");
        writer.print("location " + aProblem.getLocation());
      }
      writer.print(": ");      
    } else {
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
      if (problem.getExceptionType() == ExceptionType.FATAL) {
        severity = "FATAL_ERROR";
      } else if (problem.getExceptionType() == ExceptionType.ERROR) {
        severity = "ERROR";
      } else if (problem.getExceptionType() == ExceptionType.WARNING) {
        severity = "WARNING";
      } else if (problem.getExceptionType() == ExceptionType.INFO) {
        severity = "INFO";
      } else if (problem.getExceptionType() == ExceptionType.DEBUG) {
        severity = "DEBUG";
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
