package gov.nasa.pds.tools.validate.content.array;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.content.ProblemReporter;

class ArrayProblemReporter implements ProblemReporter {
  final private ArrayContentValidator parent;
  final private ArrayLocation location;
  final private ExceptionType exceptionType;
  final private ProblemType problemType;
  final private String prefix;
  public ArrayProblemReporter(ArrayContentValidator parent, ExceptionType exceptionType, ProblemType problemType, String prefix,
      ArrayLocation location) {
    this.exceptionType = exceptionType;
    this.location = location;
    this.parent = parent;
    this.prefix = prefix;
    this.problemType = problemType;
  }
  @Override
  public void addProblem(String message) {
    this.parent.addArrayProblem(exceptionType, problemType, this.prefix + message, location);
  }

}
