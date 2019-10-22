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
package gov.nasa.pds.tools.validate.rule;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.ValidationProblem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.chain.Context;

/**
 * The base class for validation rules. To implement validation rules,
 * write the validation tests as public void methods and annotate
 * them with the {@link ValidationTest} annotation. The tests will
 * be invoked in they appear within the class.
 */
public abstract class AbstractValidationRule implements ValidationRule {

	private RuleContext context;
	private ProblemListener listener;
  private String caption;

	/**
	 * {@inheritDoc}
	 *
	 * Invokes the validation tests in the rule in their declared order.
	 * Validation tests are denoted by public methods annotated with
	 * the {@link ValidationTest} annotation.
	 *
	 * @param theContext the context for the rule, which must be a {@link RuleContext}
	 * @throws {@inheritDoc}
	 */
	@Override
	public boolean execute(Context theContext) throws Exception {
		this.context = (RuleContext) theContext;
		listener = context.getProblemListener();
		if (isApplicable(getTarget().toString())) {
  		// Run each annotated validation test.
  		for (Method m : getClass().getMethods()) {
  			Annotation a = m.getAnnotation(ValidationTest.class);
  			if (a != null) {
  				m.invoke(this, new Object[0]);
  			}
  		}
		}

		return false;
	}

	/**
	 * Gets the rule context.
	 *
	 * @return the context
	 */
	protected RuleContext getContext() {
	  return context;
	}

	/**
	 * Gets a rule context for validating a file or directory inside
	 * the current target.
	 *
	 * @param child the child target
	 * @return a new rule context for validating the child
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 */
	protected RuleContext getChildContext(URL child) 
	    throws MalformedURLException, URISyntaxException {
    RuleContext newContext = new RuleContext();

    newContext.setProblemListener(context.getProblemListener());
    newContext.setRuleManager(context.getRuleManager());
    newContext.setTargetRegistrar(context.getTargetRegistrar());
    newContext.setTarget(child);
    newContext.setRootTarget(false);
    newContext.setRecursive(context.isRecursive());
    newContext.setCrawler(context.getCrawler());
    newContext.setFileFilters(context.getFileFilters());
    newContext.setChecksumManifest(context.getChecksumManifest());
    newContext.setForceLabelSchemaValidation(context.isForceLabelSchemaValidation());
    newContext.setRule(context.getRule());
    newContext.setCatalogs(context.getCatalogs());
    newContext.setCatalogResolver(context.getCatalogResolver());
    newContext.setCheckData(context.getCheckData());
    newContext.setRegisteredProducts(context.getRegisteredProducts());
    newContext.setSpotCheckData(context.getSpotCheckData());
    newContext.setAllowUnlabeledFiles(context.getAllowUnlabeledFiles());
    newContext.setValidateContext(context.getValidateContext());
    newContext.setSkipProductValidation(context.getSkipProductValidation());
    
    return newContext;
	}

	/**
	 * Gets the target of this rule.
	 *
	 * @return the validation target
	 */
	protected URL getTarget() {
		return context.getTarget();
	}

	/**
	 * Gets the parent target location for this rule.
	 *
	 * @return the parent target location, or null if no parent
	 */
	protected String getParentTarget() {
	  return context.getParentTarget();
	}

  /**
   * Gets the problem listener for this validation rule.
   *
   * @return the problem listener
   */
  protected ProblemListener getListener() {
    return listener;
  }

  /**
   * Gets the target registrar for this validation rule.
   *
   * @return the target registrar
   */
  protected TargetRegistrar getRegistrar() {
    return context.getTargetRegistrar();
  }

  /**
   * Reports an error to the validation listener.
   *
   * @param defn the problem definition
   * @param targetFile the validation target file containing the problem
   * @param lineNumber the line number, or -1 if no line number applies
   * @param columnNumber the column number, or -1 if no column number applies
   */
  protected void reportError(
          ProblemDefinition defn,
          URL targetUrl,
          int lineNumber,
          int columnNumber
  ) {
    ValidationProblem problem = new ValidationProblem(defn, new ValidationTarget(targetUrl), lineNumber, columnNumber, defn.getMessage());
    listener.addProblem(problem);
  }

  /**
   * Reports an error to the validation listener with a custom message.
   *
   * @param defn the problem definition
   * @param target the validation target containing the problem
   * @param lineNumber the line number, or -1 if no line number applies
   * @param columnNumber the column number, or -1 if no column number applies
   * @param message the error message to report
   */
  protected void reportError(
          ProblemDefinition defn,
          URL target,
          int lineNumber,
          int columnNumber,
          String message
  ) {
    ValidationProblem problem = new ValidationProblem(defn, new ValidationTarget(target), lineNumber, columnNumber, message);
    listener.addProblem(problem);
  }

	/**
	 * Tests whether a rule is applicable to a target location.
	 *
	 * @param location the target location
	 * @return true, if the rule is applicable to the target, false otherwise
	 */
	@Override
	public abstract boolean isApplicable(String location);

  @Override
  public final String getCaption() {
    return caption;
  }

  /**
   * Sets the caption for this chain.
   *
   * @param caption the new caption string
   */
  public final void setCaption(String caption) {
    this.caption = caption;
  }
  
  /**
   * Verifies that the lid contains the parent bundle/collection lid. Applies to
   * Bundle and Collection referential integrity (L5.PRP.VA.36)
   * 
   * @see gov.nasa.pds.tools.validate.rule.pds4.BundleReferentialIntegrityRule
   * @see gov.nasa.pds.tools.validate.rule.pds4.CollectionReferentialIntegrityRule
   * 
   * @param lid
   * @param parentLid
   * @param status
   * @param url
   */
  protected void verifyLidPrefix (String lid, String parentLid, String status,
          URL url) {
    if (status.equalsIgnoreCase("Primary") || status.equalsIgnoreCase("P")) {
        if (!lid.startsWith(parentLid + ":")) {
          getListener().addProblem(new ValidationProblem(
              new ProblemDefinition(
                  ExceptionType.ERROR,
                  ProblemType.MISSING_PARENT_PREFIX,
                  "Member LID " + lid + " does not begin with required parent LID " +
                  parentLid),
              url));
        } else {
            getListener().addProblem(new ValidationProblem(
                new ProblemDefinition(
                    ExceptionType.INFO,
                    ProblemType.PARENT_PREFIX_FOUND,
                    "Member LID " + lid + " begins with required parent LID " +
                    parentLid),
                url));
        }
    }
  }
}
