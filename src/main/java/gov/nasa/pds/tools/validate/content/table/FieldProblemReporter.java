package gov.nasa.pds.tools.validate.content.table;

import gov.nasa.pds.label.object.RecordLocation;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.content.ProblemReporter;

class FieldProblemReporter implements ProblemReporter {
  final private int field;
  final private ExceptionType exceptionType;
  final private FieldValueValidator parent;
  final private ProblemType problemType;
  final private RecordLocation recordLocation;

  public FieldProblemReporter(FieldValueValidator parent, ExceptionType exceptionType,
      ProblemType problemType, RecordLocation recordLocation, int field) {
    this.exceptionType = exceptionType;
    this.field = field;
    this.parent = parent;
    this.problemType = problemType;
    this.recordLocation = recordLocation;
  }

  @Override
  public void addProblem(String message) {
    parent.addTableProblem(exceptionType, problemType, message, recordLocation, field);
  }
}
