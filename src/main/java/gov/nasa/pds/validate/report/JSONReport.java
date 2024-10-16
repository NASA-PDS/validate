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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.WordUtils;
import com.google.gson.stream.JsonWriter;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.validate.status.Status;

/**
 * This class represents a full report in JSON format.
 *
 * @author mcayanan
 *
 */
public class JSONReport extends Report {
  private boolean ignoreTitle = true;
  final private ArrayList<Tuple> configs = new ArrayList<Tuple>();
  final private ArrayList<Tuple> msgs = new ArrayList<Tuple>();
  final private ArrayList<ValidationProblem> otherProblems = new ArrayList<ValidationProblem>();
  final private ArrayList<Tuple> params = new ArrayList<Tuple>();
  final private Map<String, List<ValidationProblem>> contentProblems = new LinkedHashMap<>();
  final private Map<String, List<ValidationProblem>> externalProblems = new LinkedHashMap<>();
  private JsonWriter stream = null;
  private Status status = Status.PASS;
  private String target = "";

  private void append (ArrayList<Tuple> tuples) throws IOException {
    for (Tuple t : tuples) {
      this.stream.beginObject();
      this.stream.name("messageType").value(t.a);
      this.stream.name("total").value(t.b);
      this.stream.endObject();
    }
  }
  @Override
  protected void append(Status status, String target) {
    try {
      this.target = target;
      this.stream.name("status").value(status.getName());
      this.stream.name("label").value(target);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void append(String title) {
    try {
      if (!this.ignoreTitle) {
        title = WordUtils.uncapitalize(title.replaceAll("\\s+", ""));
        this.stream.name(title).beginArray();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  private void append (String name, ArrayList<Tuple> tuples) throws IOException {
    this.stream.name (name).beginObject();
    for (Tuple t : tuples) {
      this.stream.name(t.a).value(t.b);
    }
    this.stream.endObject();
  }
  private void append (String name, Map<String, List<ValidationProblem>> content, boolean getType) throws IOException {
    this.stream.name (name).beginArray();
    for (String key : content.keySet()) {
      if (0 < key.length()) {
        this.stream.beginObject();
        if (getType) {
          this.stream.name(getType(key).toLowerCase()).value(key);
        } else {
          this.stream.name("dataFile").value(key);
        }
        this.stream.name("messages");
        this.stream.beginArray();
      }
      for (ValidationProblem problem : content.get(key)) {
        appendProblem(problem);
      }
      if (0 < key.length()) {
        this.stream.endArray();
        this.stream.endObject();
      }
    }
    this.stream.endArray();
  }
  @Override
  protected void append(ValidationProblem problem) {
    if (this.status == Status.SKIP)
    {
      try {
        this.stream.name("messages");
        this.stream.beginArray();
        appendProblem(problem);
        this.stream.endArray();
        this.stream.endObject();
      } catch (IOException io) {
        io.printStackTrace();
      }
    } else if (problem instanceof ContentProblem) {
      ContentProblem contentProb = (ContentProblem) problem;
      List<ValidationProblem> contentProbs = this.contentProblems.get(contentProb.getSource());
      if (contentProbs == null) {
        contentProbs = new ArrayList<>();
      }
      contentProbs.add(contentProb);
      this.contentProblems.put(contentProb.getSource(), contentProbs);
    } else if (((problem.getTarget() == null)) || (problem.getTarget().getLocation() == null)
        || this.target.equals(problem.getTarget().getLocation())) {
      this.otherProblems.add(problem);
    } else {
      List<ValidationProblem> extProbs =
          this.externalProblems.get(problem.getTarget().getLocation());
      if (extProbs == null) {
        extProbs = new ArrayList<>();
      }
      extProbs.add(problem);
      this.externalProblems.put(problem.getTarget().getLocation(), extProbs);
    }
  }
  @Override
  protected void appendConfig(String label, String message, String value) {
    this.configs.add (new Tuple(label, value, ""));
  }
  @Override
  protected void appendParam(String label, String message, String value) {
    this.params.add (new Tuple(label, value, ""));
  }
  private void appendProblem(final ValidationProblem problem) throws IOException {
    this.stream.beginObject();
    this.stream.name("severity").value(problem.getProblem().getSeverity().getName());
    this.stream.name("type").value(problem.getProblem().getType().getKey());
    if (problem instanceof TableContentProblem) {
      TableContentProblem tcProblem = (TableContentProblem) problem;
      if (tcProblem.getTableID() != null && !tcProblem.getTableID().equals("-1")) {
        this.stream.name("table").value(tcProblem.getTableID());
      }
      if (tcProblem.getRecord() != -1) {
        this.stream.name("record").value(tcProblem.getRecord());
      }
      if (tcProblem.getField() != null && tcProblem.getField() != -1) {
        this.stream.name("field").value(tcProblem.getField());
      }
    } else if (problem instanceof ArrayContentProblem) {
      ArrayContentProblem aProblem = (ArrayContentProblem) problem;
      if (aProblem.getArrayID() != null && !aProblem.getArrayID().equals("-1")) {
        this.stream.name("array").value(aProblem.getArrayID());
      }
      if (aProblem.getLocation() != null) {
        this.stream.name("location").value(aProblem.getLocation());
      }
    } else if (problem.getLineNumber() != -1) {
      this.stream.name("line").value(problem.getLineNumber());
      if (problem.getColumnNumber() != -1) {
        this.stream.name("column").value(problem.getColumnNumber());
      }
    }
    this.stream.name("message").value(problem.getMessage());
    this.stream.endObject();
  }

  @Override
  protected void begin(Block block) {
    try {
      switch (block) {
        case BODY:
          this.ignoreTitle = false;
          break;
        case FOOTER:
          this.stream.name("summary");
          this.stream.beginObject();
          break;
        case HEADER:
          this.stream = new JsonWriter(this.getWriter());
          this.stream.setIndent("  ");
          this.stream.beginObject(); // start root
          this.stream.name("title").value("PDS Validation Tool Report");
          this.configs.clear();
          this.msgs.clear();
          this.params.clear();
          this.target = "";
          break;
        case LABEL:
          this.status = Status.PASS;
          this.target = "";
          this.stream.beginObject();
          break;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void end(Block block) {
    try {
      switch (block) {
        case BODY:
          this.stream.endArray();
          break;
        case FOOTER:
          this.stream.name("messageTypes");
          this.stream.beginArray();
          this.append(this.msgs);
          this.stream.endArray();
          this.stream.endObject(); // finish summary
          this.stream.endObject(); // finish root
          this.msgs.clear();
          break;
        case HEADER:
          this.append ("configuration", this.configs);
          this.append ("parameters", this.params);
          this.configs.clear();
          this.params.clear();
          break;
        case LABEL:
          Map<String, List<ValidationProblem>> others = new LinkedHashMap<String, List<ValidationProblem>>();
          others.put ("", this.otherProblems);
          this.append ("messages", others, false);
          this.append ("fragments", this.externalProblems, true);
          this.append ("dataContents", this.contentProblems, false);
          this.stream.endObject();
          this.status = Status.PASS;
          this.target = "";
          break;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void summarizeAddMessage(String msg, long count) {
    this.msgs.add (new Tuple(msg, String.valueOf(count), ""));
  }
  @Override
  protected void summarizeDepWarn(String msg) {
    try {
      this.stream.name("deprecatedFlagWarning").value(msg);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void summarizeProds(int failed, int passed, int skipped, int total) {
    try {
      this.stream.name("productValidation").beginObject();
      this.stream.name("passed").value (String.valueOf(passed));
      this.stream.name("failed").value (String.valueOf(failed));
      this.stream.name("skipped").value (String.valueOf(skipped));
      this.stream.name("total").value (String.valueOf(total));
      this.stream.endObject();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void summarizeRefs(int failed, int passed, int skipped, int total) {    
    try {
      this.stream.name("referentialIntegrity").beginObject();
      this.stream.name("passed").value (String.valueOf(passed));
      this.stream.name("failed").value (String.valueOf(failed));
      this.stream.name("skipped").value (String.valueOf(skipped));
      this.stream.name("total").value (String.valueOf(total));
      this.stream.endObject();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  @Override
  protected void summarizeTotals(int errors, int total, int warnings) {
    try {
      this.stream.name("totalProducts").value(total);
      this.stream.name("totalErrors").value(errors);
      this.stream.name("totalWarnings").value(warnings);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
