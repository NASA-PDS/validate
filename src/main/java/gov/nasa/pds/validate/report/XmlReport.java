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

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

import com.jamesmurty.utils.XMLBuilder2;

public class XmlReport extends Report {
  private XMLBuilder2 xmlBuilder;

  public XmlReport() {
    super();
    this.xmlBuilder = XMLBuilder2.create("validateReport");
  }
  
  public void printHeader() {
    xmlBuilder = xmlBuilder.e("configuration");
    for (String config : configurations) {
      String[] tokens = config.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s", "");
      xmlBuilder = xmlBuilder.e(WordUtils.uncapitalize(key)).t(tokens[1]).up();
    }
    xmlBuilder = xmlBuilder.up();
    xmlBuilder = xmlBuilder.e("parameters");
    for (String param : parameters) {
      String[] tokens = param.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s","");
      xmlBuilder = xmlBuilder.e(WordUtils.uncapitalize(key)).t(tokens[1]).up();
    }
    xmlBuilder = xmlBuilder.up();
    xmlBuilder = xmlBuilder.e("ProductLevelValidationResults");
  }

  @Override
  protected void printHeader(PrintWriter writer, String title) {
    xmlBuilder = xmlBuilder.up();
    title = title.replaceAll("\\s+", "");
    xmlBuilder = xmlBuilder.e(title);
  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<ValidationProblem> problems) {
    Map<String, List<ValidationProblem>> externalProblems = 
        new LinkedHashMap<String, List<ValidationProblem>>();
    Map<String, List<ContentProblem>> contentProblems = 
        new LinkedHashMap<String, List<ContentProblem>>();
    xmlBuilder = xmlBuilder.e("label").a("target", sourceUri.toString())
        .a("status", status.getName());
    for (ValidationProblem problem : problems) {
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
    }
    xmlBuilder = xmlBuilder.e("fragments");
    for (String extSystemId : externalProblems.keySet()) {
      xmlBuilder = xmlBuilder.e(getType(extSystemId).toLowerCase())
          .a("uri", extSystemId.toString());
      for (ValidationProblem problem : externalProblems.get(extSystemId)) {
        printExtProblem(writer, problem);
      }
      xmlBuilder = xmlBuilder.up();
    }
    xmlBuilder = xmlBuilder.up();
    
    for (String dataFile : contentProblems.keySet()) {
      xmlBuilder = xmlBuilder.e("dataFile").a("uri", dataFile.toString());
      for (ContentProblem problem : contentProblems.get(dataFile)) {
        printExtProblem(writer, problem);
      }
      xmlBuilder = xmlBuilder.up();
    }
    xmlBuilder = xmlBuilder.up();
  }

  private void printExtProblem(PrintWriter writer, 
      final ValidationProblem problem) {
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
    xmlBuilder = xmlBuilder.e("message").a("severity", severity);
    xmlBuilder = xmlBuilder.a("type", problem.getProblem().getType().getKey());
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
      //if (tcProblem.getTable() != null) {
        //if (tcProblem.getTable() != -1) 
		  xmlBuilder = xmlBuilder.a("table", tcProblem.getTable().toString());
		//else
		//  xmlBuilder = xmlBuilder.a("table", "0");
      } 
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        xmlBuilder = xmlBuilder.a("record", tcProblem.getRecord().toString());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        xmlBuilder = xmlBuilder.a("field", tcProblem.getField().toString());        
      }   
    } else if (problem instanceof ArrayContentProblem) {
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
      if (aProblem.getArray() != null && aProblem.getArray() != -1) {
        xmlBuilder = xmlBuilder.a("array", aProblem.getArray().toString());
      }
      if (aProblem.getLocation() != null) {
        xmlBuilder = xmlBuilder.a("location", aProblem.getLocation());
      }      
    } else {   
      if (problem.getLineNumber() != -1) {
        xmlBuilder = xmlBuilder.a("line", 
            Integer.toString(problem.getLineNumber()));
      }
      if (problem.getColumnNumber() != -1) {
        xmlBuilder = xmlBuilder.a("column", 
            Integer.toString(problem.getColumnNumber()));
      }
    }
    xmlBuilder = xmlBuilder.e("content").t(StringEscapeUtils.escapeXml(
        problem.getMessage())).up();
    xmlBuilder = xmlBuilder.up();
  }

  private void printProblem(PrintWriter writer, 
      final ValidationProblem problem) {
    String severity = "";
    if (problem.getProblem().getSeverity() == ExceptionType.FATAL) {
        severity = "FATAL_ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.ERROR) {
        severity = "ERROR";
    } else if (problem.getProblem().getSeverity() == ExceptionType.WARNING) {
        severity = "WARNING";
    } else if (problem.getProblem().getSeverity() == ExceptionType.INFO) {
      severity = "INFO";
    }
    xmlBuilder = xmlBuilder.e("message").a("severity", severity);
    xmlBuilder = xmlBuilder.a("type", problem.getProblem().getType().getKey());
    if (problem.getLineNumber() != -1) {
      xmlBuilder = xmlBuilder.a("line", 
          Integer.toString(problem.getLineNumber()));
    }
    if (problem.getColumnNumber() != -1) {
      xmlBuilder = xmlBuilder.a("column", 
          Integer.toString(problem.getColumnNumber()));
    }
    xmlBuilder = xmlBuilder.e("content").t(StringEscapeUtils.escapeXml(
        problem.getMessage())).up();
    xmlBuilder = xmlBuilder.up();
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri, 
      final ValidationProblem problem) {
    xmlBuilder = xmlBuilder.e("label").a("target", sourceUri.toString())
        .a("status", Status.SKIP.getName());

    printProblem(writer, problem);

    xmlBuilder = xmlBuilder.up();
  }

  @Override
  protected void printFooter(PrintWriter writer) {
    // TODO Auto-generated method stub

  }

  public void printFooter() {
    xmlBuilder = xmlBuilder.e("summary");
    xmlBuilder = xmlBuilder.e("totalErrors").t(
        Integer.toString(getTotalErrors())).up();
    xmlBuilder = xmlBuilder.e("totalWarnings").t(
        Integer.toString(getTotalWarnings())).up();
    xmlBuilder = xmlBuilder.e("messageTypes");
    Map<String, Long> sortedMessageSummary = sortMessageSummary(this.messageSummary);
    for (String type : sortedMessageSummary.keySet()) {
      xmlBuilder.e("messageType")
          .a("total", sortedMessageSummary.get(type).toString())
          .t(type).up();
    }
    xmlBuilder = xmlBuilder.up();
    xmlBuilder = xmlBuilder.up();
    xmlBuilder = xmlBuilder.up();
    Properties outputProperties = new Properties();
    // Pretty-print the XML output (doesn't work in all cases)
    outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
    // Get 2-space indenting when using the Apache transformer
    //outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
    this.xmlBuilder.toWriter(true, this.writer, outputProperties);
    writer.flush();
    writer.close();
  }
}
