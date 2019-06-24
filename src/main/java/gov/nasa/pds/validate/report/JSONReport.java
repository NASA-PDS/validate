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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import com.google.gson.stream.JsonWriter;

/**
 * This class represents a full report in JSON format.
 *
 * @author mcayanan
 *
 */
public class JSONReport extends Report {
  private JsonWriter jsonWriter;

  public JSONReport() {
    super();
    this.jsonWriter = new JsonWriter(this.writer);
    this.jsonWriter.setIndent("  ");
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
    this.jsonWriter = new JsonWriter(this.writer);
    this.jsonWriter.setIndent("  ");
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

  public void printHeader() {
    try {
      this.jsonWriter.beginObject();
      this.jsonWriter.name("title").value("PDS Validation Tool Report");
      this.jsonWriter.name("configuration");
      this.jsonWriter.beginObject();
      for (String configuration : configurations) {
        String[] tokens = configuration.trim().split("\\s{2,}+", 2);
        String key = tokens[0].replaceAll("\\s", "");
        this.jsonWriter.name(WordUtils.uncapitalize(key)).value(tokens[1]);
      }
      this.jsonWriter.endObject();
      this.jsonWriter.name("parameters");
      this.jsonWriter.beginObject();
      for (String parameter : parameters) {
        String[] tokens = parameter.trim().split("\\s{2,}+", 2);
        String key = tokens[0].replaceAll("\\s","");
        this.jsonWriter.name(WordUtils.uncapitalize(key)).value(tokens[1]);
      }
      this.jsonWriter.endObject();
      this.jsonWriter.name("productLevelValidationResults");
      this.jsonWriter.beginArray();
    } catch (ArrayIndexOutOfBoundsException ae) {
      ae.printStackTrace();
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  @Override
  protected void printHeader(PrintWriter writer, String title) {
    try {
      this.jsonWriter.endArray();
      title = title.replaceAll("\\s+", "");
      this.jsonWriter.name(title);
      this.jsonWriter.beginArray();
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<ValidationProblem> problems) {
    Map<String, List<ValidationProblem>> externalProblems = new LinkedHashMap<String, List<ValidationProblem>>();
    Map<String, List<ContentProblem>> contentProblems = new LinkedHashMap<String, List<ContentProblem>>();
    try {
      this.jsonWriter.beginObject();
      this.jsonWriter.name("status").value(status.getName());
      this.jsonWriter.name("label").value(sourceUri.toString());
      this.jsonWriter.name("messages");
      this.jsonWriter.beginArray();
      for (ValidationProblem problem : problems) {
        if (problem instanceof ContentProblem) {
          ContentProblem contentProb = (ContentProblem) problem;
          List<ContentProblem> contentProbs = contentProblems.get(contentProb.getSource());
          if (contentProbs == null) {
            contentProbs = new ArrayList<ContentProblem>();
          }
          contentProbs.add(contentProb);
          contentProblems.put(contentProb.getSource(), contentProbs);
        } else {      
          if ( ((problem.getTarget() == null)) || 
              (problem.getTarget().getLocation() == null) || 
              sourceUri.toString().equals(problem.getTarget().getLocation())) {
            printProblem(problem);
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
      }
      this.jsonWriter.endArray();
      this.jsonWriter.name("fragments");
      this.jsonWriter.beginArray();
      for (String extSystemId : externalProblems.keySet()) {
        this.jsonWriter.beginObject();
        this.jsonWriter.name(getType(extSystemId).toLowerCase()).value(extSystemId.toString());
        this.jsonWriter.name("messages");
        this.jsonWriter.beginArray();
        for (ValidationProblem problem : externalProblems.get(extSystemId)) {
          printProblem(problem);
        }
        this.jsonWriter.endArray();
        this.jsonWriter.endObject();
      }
      this.jsonWriter.endArray();
      this.jsonWriter.name("dataContents");
      this.jsonWriter.beginArray();
      for (String dataFile : contentProblems.keySet()) {
        this.jsonWriter.beginObject();
        this.jsonWriter.name("dataFile").value(dataFile);
        this.jsonWriter.name("messages");
        this.jsonWriter.beginArray();
        for (ContentProblem problem : contentProblems.get(dataFile)) {
          printProblem(problem);
        }
        this.jsonWriter.endArray();
        this.jsonWriter.endObject();
      }
      this.jsonWriter.endArray();
      this.jsonWriter.endObject();
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  private void printProblem(final ValidationProblem problem) throws IOException {
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
    this.jsonWriter.beginObject();
    this.jsonWriter.name("severity").value(severity);
    this.jsonWriter.name("type").value(problem.getProblem().getType().getKey());
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
        this.jsonWriter.name("table").value(tcProblem.getTable());
      }
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        this.jsonWriter.name("record").value(tcProblem.getRecord());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        this.jsonWriter.name("field").value(tcProblem.getField());        
      }
    } else if (problem instanceof ArrayContentProblem) {
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
      if (aProblem.getArray() != null && aProblem.getArray() != -1) {
        this.jsonWriter.name("array").value(aProblem.getArray());
      }
      if (aProblem.getLocation() != null) {
        this.jsonWriter.name("location").value(aProblem.getLocation());
      }
    } else {
      if (problem.getLineNumber() != -1) {
        this.jsonWriter.name("line").value(problem.getLineNumber());
        if (problem.getColumnNumber() != -1) {
          this.jsonWriter.name("column").value(problem.getColumnNumber());
        }
      }
    }
    this.jsonWriter.name("message").value(problem.getMessage());
    this.jsonWriter.endObject();
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri,
      final ValidationProblem problem) {
    try {
      this.jsonWriter.beginObject();
      this.jsonWriter.name("status").value(Status.SKIP.getName());
      this.jsonWriter.name("label").value(sourceUri.toString());
      this.jsonWriter.name("messages");
      this.jsonWriter.beginArray();

      printProblem(problem);
      
      this.jsonWriter.endArray();
      this.jsonWriter.endObject();
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  @Override
  protected void printFooter(PrintWriter writer) {

  }

  public void printFooter() {
    try {
      this.jsonWriter.endArray();
      this.jsonWriter.name("summary");
      this.jsonWriter.beginObject();
      this.jsonWriter.name("totalErrors").value(getTotalErrors());
      this.jsonWriter.name("totalWarnings").value(getTotalWarnings());
      this.jsonWriter.name("messageTypes");
      this.jsonWriter.beginArray();
      Map<String, Long> sortedMessageSummary = sortMessageSummary(this.messageSummary);
      for (String type : sortedMessageSummary.keySet()) {
        this.jsonWriter.beginObject();
        this.jsonWriter.name("messageType").value(type);
        this.jsonWriter.name("total").value(sortedMessageSummary.get(type));
        this.jsonWriter.endObject();
      }
      this.jsonWriter.endArray();
      this.jsonWriter.endObject();
      this.jsonWriter.endObject();
      this.jsonWriter.flush();
      this.jsonWriter.close();
    } catch (IOException io) {
      io.getMessage();
    }
  }
}
