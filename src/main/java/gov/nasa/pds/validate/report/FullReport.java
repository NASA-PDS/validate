// Copyright © 2019, California Institute of Technology ("Caltech").
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

package gov.nasa.pds.validate.report;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.validate.status.Status;
import gov.nasa.pds.tools.util.Utility;

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

    // A hacky way to properly track when we are completing product validation versus integrity 
    // checks. Once we try to print a header other than the initial product level validation,
    // we are now into integrity checks.
    if (title.toLowerCase().contains("pds4 bundle") || title.toLowerCase().contains("pds4 collection")) {
        this.integrityCheckFlag = true;
    }

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
    // issue_132: for the progress monitoring
    if (!Utility.isDir(sourceUri.toString())) {
      String msg = "";
      if (totalProducts > 0) {
        msg = "        " + totalProducts + " product validation(s) completed";
      }
      
      if (totalIntegrityChecks > 0) {
        msg = "        " + totalIntegrityChecks + " integrity check(s) completed";
      }

      writer.println(msg);
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
