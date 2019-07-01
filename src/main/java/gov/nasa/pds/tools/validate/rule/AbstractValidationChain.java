package gov.nasa.pds.tools.validate.rule;

import org.apache.commons.chain.impl.ChainBase;

/**
 * Implements a command chain that also implements {@link ValidationRule},
 * so we can distinguish command chains that can be used for data
 * validation.
 */
public abstract class AbstractValidationChain extends ChainBase implements ValidationRule {

	private String caption;

	@Override
	public abstract boolean isApplicable(String location);

	@Override
	public String getCaption() {
		return caption;
	}

	/**
	 * Sets the caption for this chain.
	 *
	 * @param caption the new caption string
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

}
