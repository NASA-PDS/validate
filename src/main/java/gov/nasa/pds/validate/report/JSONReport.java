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

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.validate.status.Status;
import gov.nasa.pds.validate.util.Utility;

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class represents a full report in JSON format.
 *
 * @author mcayanan
 *
 */
public class JSONReport extends Report {
  public void printHeader() {
    writer.println("{");
    writer.print("  ");
    try {
      JsonObject config = new JsonObject();
      for (String configuration : configurations) {
        String[] tokens = configuration.trim().split("\\s{2,}+", 2);
        String key = tokens[0].replaceAll("\\s", "");
        config.addProperty(WordUtils.uncapitalize(key), tokens[1]);
      }
      JsonObject params = new JsonObject();
      for (String parameter : parameters) {
        String[] tokens = parameter.trim().split("\\s{2,}+", 2);
        String key = tokens[0].replaceAll("\\s","");
        params.addProperty(WordUtils.uncapitalize(key), tokens[1]);
      }
      JsonObject header = new JsonObject();
      header.addProperty("title", "PDS Validation Tool Report");
      header.add("configuration", config);
      header.add("parameters", params);
      writer.print(Utility.toStringNoBraces(header));
    } catch (ArrayIndexOutOfBoundsException ae) {
      ae.printStackTrace();
    }
  }

  @Override
  protected void printHeader(PrintWriter writer) {
  }

  @Override
  protected void printRecordMessages(PrintWriter writer, Status status,
      URI sourceUri, List<LabelException> problems) {
    Map<String, List<LabelException>> externalProblems = new HashMap<String, List<LabelException>>();
    JsonObject validateMsgs = new JsonObject();
    validateMsgs.addProperty("status", status.getName());
    validateMsgs.addProperty("label", sourceUri.toString());

    JsonArray probs = new JsonArray();
    for (LabelException problem : problems) {
      if ( ((problem.getPublicId() == null)
          && (problem.getSystemId() == null))
          || sourceUri.toString().equals(problem.getSystemId())) {
        probs.add(printProblem(problem));
      } else {
        List<LabelException> extProbs = externalProblems.get(problem.getSystemId());
        if (extProbs == null) {
          extProbs = new ArrayList<LabelException>();
        }
        extProbs.add(problem);
        externalProblems.put(problem.getSystemId(), extProbs);
      }
    }
    JsonArray extProbs = new JsonArray();
    for (String extSystemId : externalProblems.keySet()) {
      JsonObject jo = new JsonObject();
      jo.addProperty("fragment", extSystemId.toString());
      JsonArray msgs = new JsonArray();
      for (LabelException problem : externalProblems.get(extSystemId)) {
        msgs.add(printProblem(problem));
      }
      jo.add("messages", msgs);
      extProbs.add(jo);
    }
    validateMsgs.add("messages", probs);
    validateMsgs.add("fragments", extProbs);
    writer.println(",");
    writer.print("  ");
    writer.print(Utility.toStringNoBraces(validateMsgs));
  }

  private JsonObject printProblem(final LabelException problem) {
    String severity = "";
    if (problem.getExceptionType() == ExceptionType.FATAL) {
        severity = "FATAL_ERROR";
    } else if (problem.getExceptionType() == ExceptionType.ERROR) {
        severity = "ERROR";
    } else if (problem.getExceptionType() == ExceptionType.WARNING) {
        severity = "WARNING";
    }
    JsonObject message = new JsonObject();
    message.addProperty("severity", severity);
    if (problem.getLineNumber() != null && problem.getLineNumber() != -1) {
      message.addProperty("line", problem.getLineNumber());
      if (problem.getColumnNumber() != null && problem.getColumnNumber() != -1) {
        message.addProperty("column", problem.getColumnNumber());
      }
    }
    message.addProperty("message", problem.getMessage());
    return message;
  }

  protected void printRecordSkip(PrintWriter writer, final URI sourceUri,
      final Exception exception) {
    JsonObject skipMessage = new JsonObject();
    skipMessage.addProperty("status", Status.SKIP.getName());
    skipMessage.addProperty("label", sourceUri.toString());
    JsonObject prob = new JsonObject();
    if (exception instanceof LabelException) {
      LabelException problem = (LabelException) exception;
      prob = printProblem(problem);
    } else {
      prob.addProperty("severity", "ERROR");
      prob.addProperty("message", exception.getMessage());
    }
    skipMessage.add("messages", prob);
    writer.println(",");
    writer.print("  ");
    writer.print(Utility.toStringNoBraces(skipMessage));
  }

  @Override
  protected void printFooter(PrintWriter writer) {

  }

  public void printFooter() {
    writer.println("");
    writer.println("}");
    writer.flush();
    writer.close();
  }
}
