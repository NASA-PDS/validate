// Copyright 2006-2013, by the California Institute of Technology.
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
      URI sourceUri, List<LabelException> problems) {
    Map<String, List<LabelException>> externalProblems = new LinkedHashMap<String, List<LabelException>>();
    Map<String, List<ContentException>> contentProblems = new LinkedHashMap<String, List<ContentException>>();
    xmlBuilder = xmlBuilder.e("label").a("target", sourceUri.toString()).a("status", status.getName());
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
    }
    xmlBuilder = xmlBuilder.e("fragments");
    for (String extSystemId : externalProblems.keySet()) {
      xmlBuilder = xmlBuilder.e(getType(extSystemId).toLowerCase()).a("uri", extSystemId.toString());
      for (LabelException problem : externalProblems.get(extSystemId)) {
        printExtProblem(writer, problem);
      }
      xmlBuilder = xmlBuilder.up();
    }
    xmlBuilder = xmlBuilder.up();
    
    for (String dataFile : contentProblems.keySet()) {
      xmlBuilder = xmlBuilder.e("dataFile").a("uri", dataFile.toString());
      for (ContentException problem : contentProblems.get(dataFile)) {
        printExtProblem(writer, problem);
      }
      xmlBuilder = xmlBuilder.up();
    }
    xmlBuilder = xmlBuilder.up();
  }

  private void printExtProblem(PrintWriter writer, final LabelException problem) {
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
    xmlBuilder = xmlBuilder.e("message").a("severity", severity);
    if (problem instanceof TableContentException) {
      TableContentException tcProblem = (TableContentException) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
        xmlBuilder = xmlBuilder.a("table", tcProblem.getTable().toString());
      }
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        xmlBuilder = xmlBuilder.a("record", tcProblem.getRecord().toString());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        xmlBuilder = xmlBuilder.a("field", tcProblem.getField().toString());        
      }   
    } else if (problem instanceof ArrayContentException) {
      ArrayContentException aProblem = (ArrayContentException) problem;
      if (aProblem.getArray() != null && aProblem.getArray() != -1) {
        xmlBuilder = xmlBuilder.a("array", aProblem.getArray().toString());
      }
      if (aProblem.getLocation() != null) {
        xmlBuilder = xmlBuilder.a("location", aProblem.getLocation());
      }      
    } else {   
      if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
        xmlBuilder = xmlBuilder.a("line", problem.getLineNumber().toString());
      }
      if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
        xmlBuilder = xmlBuilder.a("column", problem.getColumnNumber().toString());
      }
    }
    xmlBuilder = xmlBuilder.e("content").t(StringEscapeUtils.escapeXml(problem.getMessage())).up();
    xmlBuilder = xmlBuilder.up();
  }

  private void printProblem(PrintWriter writer, final LabelException problem) {
    String severity = "";
    if (problem.getExceptionType() == ExceptionType.FATAL) {
        severity = "FATAL_ERROR";
    } else if (problem.getExceptionType() == ExceptionType.ERROR) {
        severity = "ERROR";
    } else if (problem.getExceptionType() == ExceptionType.WARNING) {
        severity = "WARNING";
    } else if (problem.getExceptionType() == ExceptionType.INFO) {
      severity = "INFO";
    }
    xmlBuilder = xmlBuilder.e("message").a("severity", severity);
    if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
      xmlBuilder = xmlBuilder.a("line", problem.getLineNumber().toString());
    }
    if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
      xmlBuilder = xmlBuilder.a("column", problem.getColumnNumber().toString());
    }
    xmlBuilder = xmlBuilder.e("content").t(StringEscapeUtils.escapeXml(problem.getMessage())).up();
    xmlBuilder = xmlBuilder.up();
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri, final Exception exception) {
    xmlBuilder = xmlBuilder.e("label").a("target", sourceUri.toString()).a("status", Status.SKIP.getName());
    if (exception instanceof LabelException) {
      LabelException problem = (LabelException) exception;
      printProblem(writer, problem);
    } else {
      xmlBuilder = xmlBuilder.e("message").a("severity", "ERROR");
      xmlBuilder = xmlBuilder.e("content").t(StringEscapeUtils.escapeXml(exception.getMessage())).up();
      xmlBuilder = xmlBuilder.up();
    }
    xmlBuilder = xmlBuilder.up();
  }

  @Override
  protected void printFooter(PrintWriter writer) {
    // TODO Auto-generated method stub

  }

  public void printFooter() {
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
