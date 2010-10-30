// Copyright 2006-2010, by the California Institute of Technology.
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
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.validate.report.Report;

import java.io.File;

/**
 * Class that validates a single file.
 *
 * @author mcayanan
 *
 */
public class FileValidator extends Validator{
    /**
     * Constructor.
     *
     * @param report A Report object to output the results.
     */
    public FileValidator(Report report) {
        super(report);
    }

    /**
     * Validate a PDS product file.
     *
     * @param file A PDS product file.
     *
     */
    public void validate(File file) {
        LabelValidator lv = new LabelValidator();
        ExceptionContainer exceptionContainer = new ExceptionContainer();
        try {
            if(schema != null) {
                lv.validate(exceptionContainer, file, schema);
            } else {
                lv.validate(exceptionContainer, file);
            }
            report.record(file.toURI(), exceptionContainer.getExceptions());
        } catch(Exception e) {
            LabelException le = new LabelException(ExceptionType.FATAL,
                    e.getMessage(), file.toString(), file.toString(),
                    null, null);
            report.record(file.toURI(), le);
        }
    }
}
