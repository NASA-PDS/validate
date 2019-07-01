package gov.nasa.pds.tools.validate.rule;

import org.apache.commons.chain.Command;

/**
 * Defines an interface that validation rule classes must implement.
 */
public interface ValidationRule extends Command {

	/**
	 * Tests whether a rule is applicable to a target location.
	 *
	 * @param location the target location to validate
	 * @return true, if the rule is applicable to the target, false otherwise
	 */
	boolean isApplicable(String location);

	/**
	 * Gets a caption describing the rule.
	 *
	 * @return a string caption
	 */
	String getCaption();

}
