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
import gov.nasa.pds.validate.status.Status;

import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlReport extends Report {

  public void printHeader() {
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.println("<validateReport>");
    writer.println("  <configuration>");
    for (String config : configurations) {
      String[] tokens = config.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s", "").toUpperCase();
      if ("CORESCHEMAS".equals(key)) {
        writer.println("    <coreSchemas>" + tokens[1] + "</coreSchemas>");
      } else if ("TIME".equals(key)) {
        writer.println("    <time>" + tokens[1] + "</time>");
      } else if ("CORESCHEMATRONS".equals(key)) {
        writer.println("    <coreSchematrons>" + tokens[1] + "</coreSchematrons>");
      } else if ("VERSION".equals(key)) {
        writer.println("    <version>" + tokens[1] + "</version>");
      } else if ("MODELVERSION".equals(key)) {
        writer.println("    <modelVersion>" + tokens[1] + "</modelVersion>");
      }
    }
    writer.println("  </configuration>");
    writer.println("  <parameters>");
    for (String param : parameters) {
      String[] tokens = param.trim().split("\\s{2,}+", 2);
      String key = tokens[0].replaceAll("\\s", "").toUpperCase();
      if ("TARGETS".equals(key)) {
        writer.println("    <targets>" + tokens[1] + "</targets>");
      } else if ("USERSPECIFIEDSCHEMAS".equals(key)) {
        writer.println("    <userSpecifiedSchemas>" + tokens[1] + "</userSpecifiedSchemas>");
      } else if ("USERSPECIFIEDCATALOGS".equals(key)) {
        writer.println("    <userSpecifiedCatalogs>" + tokens[1] + "</userSpecifiedCatalogs>");
      } else if ("USERSPECIFIEDSCHEMATRONS".equals(key)) {
        writer.println("    <userSpecifiedSchematrons>" + tokens[1] + "</userSpecifiedSchematrons>");
      } else if ("SEVERITYLEVEL".equals(key)) {
        writer.println("    <severityLevel>" + tokens[1] + "</severityLevel>");
      } else if ("RECURSEDIRECTORIES".equals(key)) {
        writer.println("    <recurseDirectories>" + tokens[1] + "</recurseDirectories>");
      } else if ("FILEFILTERSUSED".equals(key)) {
        writer.println("    <fileFiltersUsed>" + tokens[1] + "</fileFiltersUsed>");
      }
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
    Map<URI, List<LabelException>> externalProblems = new HashMap<URI, List<LabelException>>();
    writer.println("  <label target=\"" + sourceUri.toString() + "\" status=\"" + status.getName() + "\">");
    for (LabelException problem : problems) {
        printProblem(writer, problem);
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
    }
    writer.print("      ");
    writer.print("<message severity=\"" + severity + "\"");
    if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
      writer.print(" line=\"" + problem.getLineNumber().toString() + "\"");
    }
    if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
      writer.print(" column=\"" + problem.getColumnNumber().toString() + "\"");
    }
    writer.print(">" + "\n");
    writer.println("        " + StringEscapeUtils.escapeXml(problem.getMessage()));
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
    writer.println("      " + StringEscapeUtils.escapeXml(problem.getMessage()));
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
      writer.println("     " + StringEscapeUtils.escapeXml(exception.getMessage()));
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
