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

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.validate.content.table.TableContentException;
import gov.nasa.pds.validate.status.Status;

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

public class XmlReport extends Report {

  public void printHeader() {
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.println("<validateReport>");
    writer.println("  <configuration>");
    for (String config : configurations) {
      String[] tokens = config.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s", "");
      writer.println("    <" + WordUtils.uncapitalize(key) + ">" + tokens[1]
          + "</" + WordUtils.uncapitalize(key) + ">");
    }
    writer.println("  </configuration>");
    writer.println("  <parameters>");
    for (String param : parameters) {
      String[] tokens = param.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s","");
      writer.println("    <" + WordUtils.uncapitalize(key) + ">" + tokens[1]
          + "</" + WordUtils.uncapitalize(key) + ">");
    }
    writer.println("  </parameters>");
  }

  @Override
  protected void printHeader(PrintWriter writer) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<LabelException> problems) {
    Map<String, List<LabelException>> externalProblems = new LinkedHashMap<String, List<LabelException>>();
    Map<String, List<TableContentException>> contentProblems = new LinkedHashMap<String, List<TableContentException>>();
    writer.println("  <label target=\"" + sourceUri.toString() + "\" status=\"" + status.getName() + "\">");
    for (LabelException problem : problems) {
      if (problem instanceof TableContentException) {
        TableContentException contentProb = (TableContentException) problem;
        List<TableContentException> contentProbs = contentProblems.get(contentProb.getSource());
        if (contentProbs == null) {
          contentProbs = new ArrayList<TableContentException>();
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

    for (String extSystemId : externalProblems.keySet()) {
      writer.println("    <fragment uri=\"" + extSystemId.toString() + "\">");
      for (LabelException problem : externalProblems.get(extSystemId)) {
        printExtProblem(writer, problem);
      }
      writer.println("    </fragment>");
    }
    
    for (String dataFile : contentProblems.keySet()) {
      writer.println("    <dataFile uri=\"" + dataFile.toString() + "\">");
      for (TableContentException problem : contentProblems.get(dataFile)) {
        printExtProblem(writer, problem);
      }
      writer.println("    </dataFile>");
    }
    
    writer.println("  </label>");
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
    writer.print("      ");
    writer.print("<message severity=\"" + severity + "\"");
    if (problem instanceof TableContentException) {
      TableContentException tcProblem = (TableContentException) problem;
      if (tcProblem.getTable() != null && tcProblem.getTable() != -1) {
        writer.print(" table=\"" + tcProblem.getTable().toString() + "\"");
      }
      if (tcProblem.getRecord() != null && tcProblem.getRecord() != -1) {
        writer.print(" record=\"" + tcProblem.getRecord().toString() + "\"");
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        writer.print(" field=\"" + tcProblem.getField().toString() + "\"");        
      }   
    } else {   
      if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
        writer.print(" line=\"" + problem.getLineNumber().toString() + "\"");
      }
      if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
        writer.print(" column=\"" + problem.getColumnNumber().toString() + "\"");
      }
    }
    writer.print(">" + "\n");
    writer.println("        <content>" + StringEscapeUtils.escapeXml(problem.getMessage()) + "</content>");
    writer.println("      " + "</message>");
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
    writer.print("    ");
    writer.print("<message severity=\"" + severity + "\"");
    if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
      writer.print(" line=\"" + problem.getLineNumber().toString() + "\"");
    }
    if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
      writer.print(" column=\"" + problem.getColumnNumber().toString() + "\"");
    }
    writer.print(">" + "\n");
    writer.println("      <content>" + StringEscapeUtils.escapeXml(problem.getMessage()) + "</content>");
    writer.println("    " + "</message>");
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri, final Exception exception) {
    writer.print("  <label target=\"" + sourceUri.toString() + "\" status=\"" + Status.SKIP.getName() + "\">" + "\n");
    if (exception instanceof LabelException) {
      LabelException problem = (LabelException) exception;
      printProblem(writer, problem);
    } else {
      writer.print("    ");
      writer.print("<message severity=\"ERROR\">" + "\n");
      writer.println("     <content>" + StringEscapeUtils.escapeXml(exception.getMessage()) + "</content>");
      writer.println("    " + "</message>");
    }
    writer.println("  </label>");
  }

  @Override
  protected void printFooter(PrintWriter writer) {
    // TODO Auto-generated method stub

  }

  public void printFooter() {
    writer.println("</validateReport>");
    writer.flush();
    writer.close();
  }
}
