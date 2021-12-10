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

import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.rule.GenericProblems;

import java.net.URL;
import java.util.Collection;

public class ValidationProblem {

  private ProblemDefinition problemDefinition;
	private ValidationTarget target;
	private String source;
	private int lineNumber;
	private int columnNumber;
	private Collection<ValidationTarget> ancestorTargets;
	private String message;

	public ValidationProblem(ProblemDefinition defn, URL target,
	                         int lineNumber, int columnNumber, String message) {
		this(defn,
			Utility.getValidationTarget(target),
			lineNumber,
			columnNumber,
			message);
	}
	
	public ValidationProblem(ProblemDefinition defn, ValidationTarget target,
	    int lineNumber, int columnNumber, String message) {
		problemDefinition = defn;
		this.target = target;
    this.source = target.getLocation();
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		if (GenericProblems.UNCAUGHT_EXCEPTION.equals(defn)) {
		  this.message = defn.getMessage();
		  this.message += ": " + message;
		} else {
		  this.message = message==null ? defn.getMessage() : message;
		}
	}

  public ValidationProblem(ProblemDefinition defn, URL target, int lineNumber,
      int columnNumber) {
    this(defn, Utility.getValidationTarget(target), lineNumber, columnNumber);
  }
	
  public ValidationProblem(ProblemDefinition defn, ValidationTarget target, 
      int lineNumber, int columnNumber) {
    this(defn, target, lineNumber, columnNumber, null);
  }	
	  
  public ValidationProblem(ProblemDefinition defn, URL target,
      String message) {
    this(defn, Utility.getValidationTarget(target), message);
  }
  
  public ValidationProblem(ProblemDefinition defn, ValidationTarget target,
      String message) {
    this(defn, target, -1, -1, message);
  }

	public ValidationProblem(ProblemDefinition defn, URL target) {
		this(defn, Utility.getValidationTarget(target));
	}
  
	public ValidationProblem(ProblemDefinition defn, ValidationTarget target) {
	  this(defn, target, -1, -1, null);
	}
	
	public ProblemDefinition getProblem() {
		return problemDefinition;
	}

	public int getProblemDefinitionId() {
		return problemDefinition.getID();
	}

	public ValidationTarget getTarget() {
		return target;
	}

	public void setTarget(ValidationTarget target) {
		this.target = target;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int n) {
		lineNumber = n;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int n) {
		columnNumber = n;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Collection<ValidationTarget> getAncestorTargets() {
		return ancestorTargets;
	}

	public void setAncestorTargets(
	    Collection<ValidationTarget> ancestorTargets) {
		this.ancestorTargets = ancestorTargets;
	}

	public String getSource() {
	  return this.source;
	}
	
	public void setSource(String source) {
	  this.source = source;
	}
}
