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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.validate.status.Status;

/**
 * This class represents a full report for the Vtool command line. This is the standard report that
 * will display all problems generated for every file that was inspected. Messages are grouped at
 * the file level and then summarized at the end.
 *
 * @author pramirez
 *
 */
public class FullReport extends Report {
  private boolean firstMSG = false;
  private int maxC = 0, maxP = 0;
  final private ArrayList<Tuple> configurations = new ArrayList<Tuple>();
  final private ArrayList<Tuple> parameters = new ArrayList<Tuple>();
  final private Map<String, List<ContentProblem>> contentProblems = new LinkedHashMap<>();
  final private Map<String, List<ValidationProblem>> externalProblems = new LinkedHashMap<>();
  private Status currentStatus = Status.PASS;
  private String currentTarget = "";
  
  @Override
  protected void append(Status status, String target) {
    this.currentStatus = status;
    this.currentTarget = target;
    this.getWriter().println();
    this.getWriter().print("  ");
    this.getWriter().print(status.getName());
    this.getWriter().print(": ");
    this.getWriter().println(target);
  }

  @Override
  protected void append(String title) {
    this.getWriter().println();
    this.getWriter().println(title);
  }

  @Override
  protected void append(ValidationProblem problem) {
    if (this.currentStatus == Status.SKIP) {
      this.getWriter().print("      ");
      this.getWriter().print(problem.getProblem().getSeverity().getName());
      this.getWriter().print("  ");
      this.getWriter().print("[" + problem.getProblem().getType().getKey() + "]");
      this.getWriter().print("   ");
      if (problem.getLineNumber() != -1) {
        this.getWriter().print("line ");
        this.getWriter().print(problem.getLineNumber());
        if (problem.getColumnNumber() != -1) {
          this.getWriter().print(", ");
          this.getWriter().print(problem.getColumnNumber());
        }
        this.getWriter().print(": ");
      }
      this.getWriter().println(problem.getMessage());      
    } else if (problem instanceof ContentProblem) {
      ContentProblem contentProb = (ContentProblem) problem;
      List<ContentProblem> contentProbs = contentProblems.get(contentProb.getSource());
      if (contentProbs == null) {
        contentProbs = new ArrayList<>();
      }
      contentProbs.add(contentProb);
      contentProblems.put(contentProb.getSource(), contentProbs);
    } else if (((problem.getTarget() == null)) || (problem.getTarget().getLocation() == null)
        || this.currentTarget.equals(problem.getTarget().getLocation())) {
      printProblem(this.getWriter(), problem);
    } else {
      List<ValidationProblem> extProbs = externalProblems.get(problem.getTarget().getLocation());
      if (extProbs == null) {
        extProbs = new ArrayList<>();
      }
      extProbs.add(problem);
      externalProblems.put(problem.getTarget().getLocation(), extProbs);
    }
  }

  @Override
  protected void appendConfig(String label, String message, String value) {
    this.configurations.add (new Tuple(message, value, ""));
    if (message.length() > this.maxC) this.maxC = message.length();
  }

  @Override
  protected void appendParam(String label, String message, String value) {
    this.parameters.add (new Tuple(message, value, ""));
    if (message.length() > this.maxP) this.maxP = message.length();
  }

  @Override
  protected void begin(Block block) {
    switch (block) {
      case BODY:
        break;
      case FOOTER:
        this.getWriter().println();
        this.getWriter().println("Summary:");
        this.getWriter().println();
        this.firstMSG = true;
        break;
      case HEADER:
        this.maxC = 0;
        this.maxP = 0;
        this.configurations.clear();
        this.parameters.clear();
        break;
      case LABEL:
        this.contentProblems.clear();
        this.externalProblems.clear();
        this.currentStatus = Status.PASS;
        this.currentTarget = "";
        break;
    }
  }

  @Override
  protected void end(Block block) {
    switch (block) {
      case BODY:
        break;
      case FOOTER:
        this.getWriter().println();
        this.getWriter().println("End of Report");
        break;
      case HEADER:
        this.getWriter().println();
        this.getWriter().println("Configuration:");
        for (Tuple configuration : this.configurations) {
          this.getWriter().print ("   ");
          this.getWriter().print(configuration.a);
          this.getWriter().print(" ".repeat(this.maxC - configuration.a.length() + 5));
          this.getWriter().println(configuration.b);
        }
        this.getWriter().println();
        this.getWriter().println("Parameters:");
        for (Tuple parameter : this.parameters) {
          this.getWriter().print ("   ");
          this.getWriter().print(parameter.a);
          this.getWriter().print(" ".repeat(this.maxP - parameter.a.length() + 5));
          this.getWriter().println(parameter.b);
        }
        this.getWriter().println();
        this.getWriter().println();
        break;
      case LABEL:
        processMaps();
        this.contentProblems.clear();
        this.externalProblems.clear();
        this.currentStatus = Status.PASS;
        this.currentTarget = "";
        break;
    }
  }

  private void printProblem(PrintWriter writer, final ValidationProblem problem) {
    writer.print("      ");
    writer.print(problem.getProblem().getSeverity().getName());
    writer.print("  ");
    writer.print("[" + problem.getProblem().getType().getKey() + "]");
    writer.print("   ");
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
      if (tcProblem.getTableID() != null && !tcProblem.getTableID().equals("-1")) {
        writer.print("data object ");
        writer.print(tcProblem.getTableID());
      }
      if (tcProblem.getRecord() != -1) {
        writer.print(", ");
        writer.print("record " + String.valueOf(tcProblem.getRecord()));
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        writer.print(", ");
        writer.print("field " + tcProblem.getField().toString());
      }
      writer.print(": ");
    } else if (problem instanceof ArrayContentProblem) {
      // For now, we'll assuming we will report on image arrays
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
      if (aProblem.getArrayID() != null && !aProblem.getArrayID().equals("-1")) {
        writer.print("array ");
        writer.print(aProblem.getArrayID());
      }
      if (aProblem.getLocation() != null) {
        writer.print(", ");
        writer.print("location " + aProblem.getLocation());
      }
      writer.print(": ");
    } else if (problem.getLineNumber() != -1) {
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
  private void processMaps() {
    for (String extSystemId : externalProblems.keySet()) {
      this.getWriter().print("    Begin " + getType(extSystemId) + ": ");
      this.getWriter().println(extSystemId);
      for (ValidationProblem problem : externalProblems.get(extSystemId)) {
        printProblem(this.getWriter(), problem);
      }
      this.getWriter().print("    End " + getType(extSystemId) + ": ");
      this.getWriter().println(extSystemId);
    }
    for (String dataFile : contentProblems.keySet()) {
      this.getWriter().print("    Begin Content Validation: ");
      this.getWriter().println(dataFile);
      for (ContentProblem problem : contentProblems.get(dataFile)) {
        printProblem(this.getWriter(), problem);
      }
      this.getWriter().print("    End Content Validation: ");
      this.getWriter().println(dataFile);
    }
    // issue_132: for the progress monitoring
    if (!Utility.isDir(this.currentTarget)) {
      String msg = "";
      if (this.getTotalProducts() > 0) {
        msg = "        " + getTotalProducts() + " product validation(s) completed";
      }
      if (this.getTotalIntegrityChecks() > 0) {
        msg = "        " + this.getTotalIntegrityChecks() + " integrity check(s) completed";
      }
      this.getWriter().println(msg);
    }    
  }

  @Override
  protected void summarizeAddMessage(String msg, long count) {  
    if (firstMSG) {
      this.getWriter().println("  Message Types:");
      this.firstMSG = false;
    }
    this.getWriter().print("    ");
    this.getWriter().printf("%-10d", count);
    this.getWriter().print("   ");
    this.getWriter().println(msg);
  }

  @Override
  protected void summarizeDepWarn(String msg) {
    this.getWriter().println();
    this.getWriter().println(msg);
    this.getWriter().println();
  }

  @Override
  protected void summarizeProds(int failed, int passed, int skipped, int total) {
    this.getWriter().println("  Product Validation Summary:");
    this.getWriter().printf("    %-10d product(s) passed\n", passed);
    this.getWriter().printf("    %-10d product(s) failed\n", failed);
    this.getWriter().printf("    %-10d product(s) skipped\n", skipped);
    this.getWriter().printf("    %-10d product(s) total\n", total);
    this.getWriter().println();
  }

  @Override
  protected void summarizeRefs(int failed, int passed, int skipped, int total) {
    this.getWriter().println("  Referential Integrity Check Summary:");
    this.getWriter().printf("    %-10d check(s) passed\n", passed);
    this.getWriter().printf("    %-10d check(s) failed\n", failed);
    this.getWriter().printf("    %-10d check(s) skipped\n", skipped);
    this.getWriter().printf("    %-10d check(s) total\n", total);
    this.getWriter().println();
  }

  @Override
  protected void summarizeTotals(int errors, int total, int warnings) {
    this.getWriter().println("  " + total + " product(s)");
    this.getWriter().println("  " + errors + " error(s)");
    this.getWriter().println("  " + warnings + " warning(s)");
    this.getWriter().println();
  }
}
