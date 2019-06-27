// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate;

import gov.nasa.pds.tools.label.ExceptionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements a problem listener that accumulates problems in a list.
 */
public class ProblemContainer implements ProblemListener {

	private List<ValidationProblem> problems = new ArrayList<ValidationProblem>();

  private int errorCount;
  private int fatalCount;
  private int warningCount;
  private int infoCount;

	@Override
	public void addProblem(ValidationProblem problem) {
		problems.add(problem);

    switch (problem.getProblem().getSeverity()) {
    case INFO:
      ++infoCount;
      break;
    case WARNING:
      ++warningCount;
      break;
    case FATAL:
      ++fatalCount;
    default:
      ++errorCount;
      break;
    }
	}
	
	public void addLocation(String location) {
	  //TODO
	}
	
	
	/**
	 * Gets the problems encountered.
	 *
	 * @return the problems
	 */
	public List<ValidationProblem> getProblems() {
		return problems;
	}

	/**
	 * Gets the number of problems reported.
	 *
	 * @return the count of problems
	 */
	public int getProblemCount() {
		return problems.size();
	}

	/**
	 * Clears all problems.
	 */
	public void clear() {
    problems.clear();
    errorCount = 0;
    warningCount = 0;
    infoCount = 0;
	}

  @Override
  public int getErrorCount() {
    return errorCount;
  }

  public Boolean hasError() {
    if (errorCount > 0) {
      return true;
    } else {
      return false;
    }
  }
  
  public Boolean hasFatal() {
    if (fatalCount > 0) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int getWarningCount() {
    return warningCount;
  }

  public Boolean hasWarning() {
    if (warningCount > 0) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int getInfoCount() {
    return infoCount;
  }
  
  public Boolean hasInfo() {
    if (infoCount > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Collection<ValidationProblem> getProblemsForLocation(
          String location, boolean includeChildren) {
    Collection<ValidationProblem> foundProblems = new ArrayList<ValidationProblem>();

    for (ValidationProblem problem : problems) {
      if (problem.getTarget().getLocation().equals(location)
        || (includeChildren && problem.getTarget().getLocation().startsWith(location))) {
        foundProblems.add(problem);
      }
    }

    return foundProblems;
  }

  @Override
  public boolean hasProblems(String location, boolean includeChildren) {
    for (ValidationProblem problem : problems) {
      if ((includeChildren && problem.getTarget().getLocation().startsWith(location))
        || problem.getTarget().getLocation().equals(location)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public ExceptionType getSeverity(String location, boolean includeChildren) {
    ExceptionType severity = ExceptionType.INFO;

    for (ValidationProblem problem : problems) {
      if ((includeChildren && problem.getTarget().getLocation().startsWith(location))
        || problem.getTarget().getLocation().equals(location)) {
        if (problem.getProblem().getSeverity().compareTo(severity) > 0) {
          severity = problem.getProblem().getSeverity();
        }
      }
    }

    return severity;
  }
}
