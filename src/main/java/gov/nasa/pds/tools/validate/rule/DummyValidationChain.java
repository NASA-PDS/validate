package gov.nasa.pds.tools.validate.rule;


/**
 * Implements an empty validation chain that is never applicable.
 */
public class DummyValidationChain extends AbstractValidationChain {

	@Override
	public boolean isApplicable(String location) {
		return false;
	}

}
