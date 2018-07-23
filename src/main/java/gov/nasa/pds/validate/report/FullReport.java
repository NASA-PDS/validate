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
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
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
      URI sourceUri, List<ValidationProblem> problems) {
    Map<String, List<ValidationProblem>> externalProblems = 
        new LinkedHashMap<String, List<ValidationProblem>>();
    Map<String, List<ContentProblem>> contentProblems = 
        new LinkedHashMap<String, List<ContentProblem>>();
      writer.println();
      writer.print("  ");
      writer.print(status.getName());
      writer.print(": ");
      writer.println(sourceUri.toString());

    // Print all the sources problems and gather all external problems
    for (Iterator<ValidationProblem> iterator = problems.iterator();
        iterator.hasNext();) {
      ValidationProblem problem = iterator.next();
      if (problem instanceof ContentProblem) {
        ContentProblem contentProb = (ContentProblem) problem;
        List<ContentProblem> contentProbs = contentProblems.get(
            contentProb.getSource());
        if (contentProbs == null) {
          contentProbs = new ArrayList<ContentProblem>();
        }
        contentProbs.add(contentProb);
        contentProblems.put(contentProb.getSource(), contentProbs);
      } else {
        if ( ((problem.getTarget() == null)) || 
              (problem.getTarget().getLocation() == null) || 
              sourceUri.toString().equals(problem.getTarget().getLocation())) {
          printProblem(writer, problem);
        } else {
          List<ValidationProblem> extProbs = externalProblems.get(
              problem.getTarget().getLocation());
          if (extProbs == null) {
            extProbs = new ArrayList<ValidationProblem>();
          }
          extProbs.add(problem);
          externalProblems.put(problem.getTarget().getLocation(), extProbs);
        }
      }
      iterator.remove();
    }
    for (String extSystemId : externalProblems.keySet()) {
      writer.print("    Begin " + getType(extSystemId) + ": ");
      writer.println(extSystemId);
      for (ValidationProblem problem : externalProblems.get(extSystemId)) {
        printProblem(writer, problem);
      }
      writer.print("    End " + getType(extSystemId) + ": ");
      writer.println(extSystemId);
    }
    for (String dataFile : contentProblems.keySet()) {
      writer.print("    Begin Content Validation: ");
      writer.println(dataFile);
      for (ContentProblem problem : contentProblems.get(dataFile)) {
        printProblem(writer, problem);
      }
      writer.print("    End Content Validation: ");
      writer.println(dataFile);
    }    
  }

  private void printProblem(PrintWriter writer,
      final ValidationProblem problem) {
    writer.print("      ");
    String severity = "";
    if (problem.getProblem().getSeverity() == ExceptionType.FATAL) {
      severity = "FATAL_ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.ERROR) {
      severity = "ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.WARNING) {
      severity = "WARNING";
    } else if (problem.getProblem().getSeverity() == ExceptionType.INFO) {
      severity = "INFO";
    } else if (problem.getProblem().getSeverity() == ExceptionType.DEBUG) {
      severity = "DEBUG";
    }
    writer.print(severity);
    writer.print("  ");
    writer.print("[" + problem.getProblem().getType().getKey() + "]");
    writer.print("   ");
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
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
    } else if (problem instanceof ArrayContentProblem) {
      // For now, we'll assuming we will report on image arrays
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
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
      if (problem.getLineNumber() != -1) {
        writer.print("line ");
        writer.print(problem.getLineNumber());
        if (problem.getColumnNumber() != -1) {
          writer.print(", ");
          writer.print(problem.getColumnNumber());
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
      final ValidationProblem problem) {
    writer.println();
    writer.print("  ");
    writer.print(Status.SKIP.getName());
    writer.print(": ");
    writer.println(sourceUri.toString());

    writer.print("      ");
    String severity = "";
    if (problem.getProblem().getSeverity() == ExceptionType.FATAL) {
      severity = "FATAL_ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.ERROR) {
      severity = "ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.WARNING) {
      severity = "WARNING";
    } else if (problem.getProblem().getSeverity() == ExceptionType.INFO) {
      severity = "INFO";
    } else if (problem.getProblem().getSeverity() == ExceptionType.DEBUG) {
      severity = "DEBUG";
    }
    writer.print(severity);
    writer.print("  ");
    writer.print("[" + problem.getProblem().getType().getKey() + "]");
    writer.print("   ");
    if (problem.getLineNumber() != -1) {
      writer.print("line ");
      writer.print(problem.getLineNumber());
      if (problem.getColumnNumber() != -1) {
        writer.print(", ");
        writer.print(problem.getColumnNumber());
      }
      writer.print(": ");
    }
    writer.println(problem.getMessage());
    
  }
}
