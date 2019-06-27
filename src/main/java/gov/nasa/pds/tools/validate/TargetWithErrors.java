package gov.nasa.pds.tools.validate;

import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;

import java.util.Collection;

public class TargetWithErrors {

    private ValidationTarget target;
    private ProblemListener listener;

    public TargetWithErrors(ValidationTarget target, ProblemListener listener) {
        this.target = target;
        this.listener = listener;
    }

    public String getLocation() {
        return target.getLocation();
    }

    public Collection<ValidationProblem> getProblems() {
        return listener.getProblemsForLocation(target.getLocation(), false);
    }

}
