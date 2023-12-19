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


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringEscapeUtils;
import com.jamesmurty.utils.XMLBuilder2;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.validate.status.Status;

public class XmlReport extends Report {
  final private Map<String, XMLBuilder2> externalProblems = new LinkedHashMap<>();
  final private Map<String, XMLBuilder2> contentProblems = new LinkedHashMap<>();
  private String target = "";
  private XMLBuilder2 bodyBlock = null;
  private XMLBuilder2 config = null;
  private XMLBuilder2 content = null;
  private XMLBuilder2 fragments = null;
  private XMLBuilder2 label = null;
  private XMLBuilder2 messages = null;
  private XMLBuilder2 msgSum = null;
  private XMLBuilder2 param = null;
  private XMLBuilder2 root = null;
  private XMLBuilder2 summary = null;

  @Override
  protected void append(Status status, String target) {
    this.target = target;
    this.label = this.bodyBlock.e("label").a("target", target).a("status", status.getName());
    this.content = this.label.e("dataContents");
    this.fragments = this.label.e("fragments");
    this.messages = this.label.e("messages");
  }
  @Override
  protected void append(String title) {
    title = title.replaceAll("\\s+", "");
    this.bodyBlock = this.root.e(title);
  }
  @Override
  protected void append(ValidationProblem problem) {
    String key;
    if (problem instanceof ContentProblem) {
      ContentProblem contentProb = (ContentProblem) problem;
      key = contentProb.getSource();
      if (!this.contentProblems.keySet().contains(key)) {
        this.contentProblems.put(key, this.content.e("dataFile").a("uri", key));
      }
      this.append (this.contentProblems.get(key), problem);
    } else if (((problem.getTarget() == null)) || (problem.getTarget().getLocation() == null)
        || this.target.equals(problem.getTarget().getLocation())) {
      append(this.messages, problem);
    } else {
      key = problem.getTarget().getLocation();
      if (!this.externalProblems.keySet().contains(key)) {
        this.externalProblems.put(key, this.fragments.e("dataFile").a("uri", key));
      }
      this.append (this.externalProblems.get(key), problem);
    }
  }
  private void append(XMLBuilder2 node, final ValidationProblem problem) {
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
    node = node.e("message").a("severity", severity);
    node = node.a("type", problem.getProblem().getType().getKey());
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
      if (tcProblem.getTableID() != null && !tcProblem.getTableID().equals("-1")) {
        node = node.a("table", tcProblem.getTableID());
      }
      if (tcProblem.getRecord() != -1) {
        node = node.a("record", String.valueOf(tcProblem.getRecord()));
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        node = node.a("field", tcProblem.getField().toString());
      }
    } else if (problem instanceof ArrayContentProblem) {
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
      if (aProblem.getArrayID() != null && !aProblem.getArrayID().equals("-1")) {
        node = node.a("array", aProblem.getArrayID());
      }
      if (aProblem.getLocation() != null) {
        node = node.a("location", aProblem.getLocation());
      }
    } else {
      if (problem.getLineNumber() != -1) {
        node = node.a("line", Integer.toString(problem.getLineNumber()));
      }
      if (problem.getColumnNumber() != -1) {
        node = node.a("column", Integer.toString(problem.getColumnNumber()));
      }
    }
    node = node.e("content").t(StringEscapeUtils.escapeXml(problem.getMessage())).up();
  }
  @Override
  protected void appendConfig(String label, String message, String value) {
    this.config.e(label).t(value).up();
  }
  @Override
  protected void appendParam(String label, String message, String value) {
    this.param.e(label).t(value).up();
  }
  @Override
  protected void begin(Block block) {
    switch (block) {
      case BODY:
        break;
      case FOOTER:
        this.summary = this.root.e("summary");
        this.msgSum = this.summary.e("messageTypes");
        break;
      case HEADER:
        this.root = XMLBuilder2.create("validateReport");
        this.config = this.root.e("configuration");
        this.param = this.root.e("parameters");
        break;
      case LABEL:
        this.label = null;
        this.target = "";
        break;
    }
  }
  @Override
  protected void end(Block block) {
    switch (block) {
      case BODY:
        break;
      case FOOTER:
        this.bodyBlock = null;
        this.config = null;
        this.param = null;
        Properties outputProperties = new Properties();
        // Pretty-print the XML output (doesn't work in all cases)
        outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
        // Get 2-space indenting when using the Apache transformer
        // outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
        this.root.toWriter(true, this.getWriter(), outputProperties);
        this.root = null;
        break;
      case HEADER:
        break;
      case LABEL:
        break;
    }
  }
  @Override
  protected void summarizeAddMessage(String msg, long count) {
    this.msgSum.e("messageType").a("total", String.valueOf(count)).t(msg);
  }
  @Override
  protected void summarizeDepWarn(String msg) {
    this.root.e("deprecatedFlagWarning").t(StringEscapeUtils.escapeXml(msg));
  }
  @Override
  protected void summarizeProds(int failed, int passed, int skipped, int total) {
    XMLBuilder2 prod = this.summary.e("productValidation");
    prod.e("passed").t (String.valueOf(passed));
    prod.e("failed").t (String.valueOf(failed));
    prod.e("skipped").t (String.valueOf(skipped));
    prod.e("total").t (String.valueOf(total));
  }
  @Override
  protected void summarizeRefs(int failed, int passed, int skipped, int total) {
    XMLBuilder2 irefs = this.summary.e("referentialIntegrity");
    irefs.e("passed").t (String.valueOf(passed));
    irefs.e("failed").t (String.valueOf(failed));
    irefs.e("skipped").t (String.valueOf(skipped));
    irefs.e("total").t (String.valueOf(total));
  }
  @Override
  protected void summarizeTotals(int errors, int total, int warnings) {
    this.summary.e("totalProducts").t(String.valueOf(total));
    this.summary.e("totalErrors").t(String.valueOf(errors));
    this.summary.e("totalWarnings").t(String.valueOf(warnings));
  }
}
