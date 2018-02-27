//Copyright 2006-2013, by the California Institute of Technology.
//ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//Any commercial use must be negotiated with the Office of Technology Transfer
//at the California Institute of Technology.
//
//This software is subject to U. S. export control laws and regulations
//(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//is subject to U.S. export control laws and regulations, the recipient has
//the responsibility to obtain export licenses or other export authority as
//may be required before exporting such information to foreign countries or
//providing access to foreign nationals.

// $Id$
//
package gov.nasa.pds.validate.report;

import gov.nasa.pds.tools.label.ContentException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.validate.content.array.ArrayContentException;
import gov.nasa.pds.tools.validate.content.table.TableContentException;
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
import java.util.HashMap;
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
      URI sourceUri, List<LabelException> problems) {
    Map<String, List<LabelException>> externalProblems = new LinkedHashMap<String, List<LabelException>>();
    Map<String, List<ContentException>> contentProblems = new LinkedHashMap<String, List<ContentException>>();
    try {
      this.jsonWriter.beginObject();
      this.jsonWriter.name("status").value(status.getName());
      this.jsonWriter.name("label").value(sourceUri.toString());
      this.jsonWriter.name("messages");
      this.jsonWriter.beginArray();
      for (LabelException problem : problems) {
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
            printProblem(problem);
          } else {
            List<LabelException> extProbs = externalProblems.get(problem.getSystemId());
            if (extProbs == null) {
              extProbs = new ArrayList<LabelException>();
            }
            extProbs.add(problem);
            externalProblems.put(problem.getSystemId(), extProbs);
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
        for (LabelException problem : externalProblems.get(extSystemId)) {
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
        for (ContentException problem : contentProblems.get(dataFile)) {
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

  private void printProblem(final LabelException problem) throws IOException {
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
    this.jsonWriter.beginObject();
    this.jsonWriter.name("severity").value(severity);
    if (problem instanceof TableContentException) {
      TableContentException tcProblem = (TableContentException) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
        this.jsonWriter.name("table").value(tcProblem.getTable().toString());
      }
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        this.jsonWriter.name("record").value(tcProblem.getRecord().toString());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        this.jsonWriter.name("field").value(tcProblem.getField().toString());        
      }
    } else if (problem instanceof ArrayContentException) {
      ArrayContentException aProblem = (ArrayContentException) problem;
      if (aProblem.getArray() != null && aProblem.getArray() != -1) {
        this.jsonWriter.name("array").value(aProblem.getArray().toString());
      }
      if (aProblem.getPlane() != null && aProblem.getPlane().getElement() != -1) {
        String name = aProblem.getPlane().getName();
        if (name.isEmpty()) {
          name = "plane";
        }
        this.jsonWriter.name(name).value(aProblem.getPlane().getElement());
      }
      if (aProblem.getRow() != null) {
        String name = aProblem.getRow().getName();
        if (name == null) {
          name = "row";
        }
        this.jsonWriter.name(name).value(aProblem.getRow().getElement());
      }
      if (aProblem.getColumn() != null) {
        String name = aProblem.getColumn().getName();
        if (name == null) {
          name = "column";
        }
        this.jsonWriter.name(name).value(aProblem.getColumn().getElement());
      }
    } else {
      if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
        this.jsonWriter.name("line").value(problem.getLineNumber());
        if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
          this.jsonWriter.name("column").value(problem.getColumnNumber());
        }
      }
    }
    this.jsonWriter.name("message").value(problem.getMessage());
    this.jsonWriter.endObject();
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri,
      final Exception exception) {
    try {
      this.jsonWriter.beginObject();
      this.jsonWriter.name("status").value(Status.SKIP.getName());
      this.jsonWriter.name("label").value(sourceUri.toString());
      this.jsonWriter.name("messages");
      this.jsonWriter.beginArray();
      if (exception instanceof LabelException) {
        LabelException problem = (LabelException) exception;
        printProblem(problem);
      } else {
        this.jsonWriter.beginObject();
        this.jsonWriter.name("severity").value("ERROR");
        this.jsonWriter.name("message").value(exception.getMessage());
        this.jsonWriter.endObject();
      }
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
    int totalFiles = this.getNumPassed() + this.getNumFailed()
        + this.getNumSkipped();
    int totalValidated = this.getNumPassed() + this.getNumFailed();
    try {
      this.jsonWriter.endArray();
      this.jsonWriter.name("summary");
      this.jsonWriter.beginObject();
      this.jsonWriter.name("totalErrors").value(getTotalErrors());
      this.jsonWriter.name("totalWarnings").value(getTotalWarnings());
/*
      this.jsonWriter.name("totalProcessed").value(totalFiles);
      this.jsonWriter.name("totalSkipped").value(this.getNumSkipped());
      this.jsonWriter.name("totalValidated").value(totalValidated);
      this.jsonWriter.name("totalPassedValidation").value(this.getNumPassed());
*/
      this.jsonWriter.endObject();
      this.jsonWriter.endObject();
      this.jsonWriter.flush();
      this.jsonWriter.close();
    } catch (IOException io) {
      io.getMessage();
    }
  }
}
