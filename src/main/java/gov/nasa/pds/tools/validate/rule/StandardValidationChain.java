package gov.nasa.pds.tools.validate.rule;

import org.apache.commons.chain.Command;

/**
 * Implements a validation chain that is applicable if any of its
 * contained validation commands are applicable, and performs validation
 * by invoking all of its contained commands in turn.
 */
public class StandardValidationChain extends AbstractValidationChain {

    @Override
    public boolean isApplicable(String location) {
        boolean applicable = false;

        for (Command cmd : commands) {
            if (cmd instanceof ValidationRule) {
                applicable |= ((ValidationRule) cmd).isApplicable(location);
            }
        }

        return applicable;
    }

}
