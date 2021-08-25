// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.task;

import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.TargetWithErrors;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.RuleContext;
import gov.nasa.pds.tools.validate.rule.ValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationRuleManager;

import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.label.ExceptionType;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a background task for performing a validation.
 */
public class ValidationTask implements Task {

  private static final Logger LOG = LoggerFactory.getLogger(ValidationTask.class);

  private String location;
  private ValidationRule rule;
  private int errorLimit;
  private ProblemListener problemListener;
  private RuleContext context;
  private TargetRegistrar targetRegistrar;
  private ValidationRuleManager ruleManager;

  /**
   * Creates a new instance of the validation task.
   *
   * @param listener the problem listener for the task
   */
  public ValidationTask(ProblemListener problemListener, RuleContext context, TargetRegistrar targetRegistrar) {
    this.problemListener = problemListener;
    this.context = context;
    this.targetRegistrar = targetRegistrar;
  }

  /**
   * Gets the location of the target to validate.
   *
   * @return the target location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the location to validate.
   *
   * @param location the location to validate
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Gets the limit on the number of errors found.
   *
   * @return the error limit
   */
  public int getErrorLimit() {
    return errorLimit;
  }

  /**
   * Sets the limit on the number of errors encountered before
   * the validation will terminate.
   *
   * @param limit the error limit
   */
  public void setErrorLimit(int limit) {
    this.errorLimit = limit;
  }

  /**
   * Gets the type of validation.
   *
   * @return the validation type
   */
  public String getValidationType() {
    return rule.getCaption();
  }

  /**
   * Sets the validation rule to use.
   *
   * @param rule the validation rule
   */
  public void setRule(ValidationRule rule) {
    this.rule = rule;
  }

  @Override
  public void execute(TaskAdvisor advisor) {
    LOG.info("Starting validation task for location '{}'", location);

    context.setRootTarget(true);
    context.setProblemListener(problemListener);
    try {
      context.setTarget(new URL(location));
    } catch (Exception e) {
      LOG.error("Malformed URL: " + location, e);
    }
    context.setTargetRegistrar(targetRegistrar);
    context.setRuleManager(ruleManager);

    try {
      rule.execute(context);
    } catch (Throwable ex) {
    	ex.printStackTrace();
    	try {
    		problemListener.addProblem(
    	        new ValidationProblem(new ProblemDefinition(
    	            ExceptionType.ERROR,
    	            ProblemType.INTERNAL_ERROR,
    	            "Error executing validationTask: " + ex.getCause().toString()),
    	        	new URL(location)));
    	} catch (Exception e) {
    	   //e.printStackTrace();
    	   LOG.error("Malformed URL: " + location, e);
        }
      LOG.error("Unexpected exception executing validation rule", ex);
    }

    LOG.info("Validation complete for location '{}'", location);
  }

  /**
   * Gets the count of errors encountered.
   *
   * @return the error count
   */
  public int getErrorCount() {
    return problemListener.getErrorCount();
  }

  /**
   * Gets the count of warnings encountered.
   *
   * @return the warning count
   */
  public int getWarningCount() {
    return problemListener.getWarningCount();
  }

  /**
   * Gets the count of informational events encountered.
   *
   * @return the count of info messages
   */
  public int getInfoCount() {
    return problemListener.getInfoCount();
  }

  /**
   * Sets the validation rule manager to use for finding validation rules.
   *
   * @param ruleManager the rule manager to use
   */
  public void setRuleManager(ValidationRuleManager ruleManager) {
    this.ruleManager = ruleManager;
  }

  /**
   * Gets the registrar for getting target information.
   *
   * @return the target registrar
   */
  public TargetRegistrar getRegistrar() {
    return targetRegistrar;
  }

  /**
   * Gets the problem listener for getting problems found.
   *
   * @return the problem listener
   */
  public ProblemListener getProblemListener() {
    return problemListener;
  }

  public List<TargetWithErrors> getAllTargets() {
    List<TargetWithErrors> allTargets = new ArrayList<TargetWithErrors>();
    addTarget(targetRegistrar.getRoot(), allTargets);
    return allTargets;
  }

  private void addTarget(ValidationTarget target, List<TargetWithErrors> allTargets) {
    allTargets.add(new TargetWithErrors(target, problemListener));

    Collection<ValidationTarget> children = targetRegistrar.getChildTargets(target);
    if (children.size() > 0) {
      for (ValidationTarget child : children) {
        addTarget(child, allTargets);
      }
    }
  }

}
