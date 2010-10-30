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
package gov.nasa.pds.validate.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class XMLValidationEventHandler implements ValidationEventHandler {
    private static Logger log = Logger.getLogger(
            XMLValidationEventHandler.class.getName());

    public boolean handleEvent(ValidationEvent event) {
        Level level = null;
        if(event.getSeverity() == ValidationEvent.ERROR ||
           event.getSeverity() == ValidationEvent.FATAL_ERROR) {
            level = Level.SEVERE;
        } else if(event.getSeverity() == ValidationEvent.WARNING) {
            level = Level.WARNING;
        }
/*
        log.log(new ToolsLogRecord(level, event.getMessage(),
                event.getLocator().getURL().toString(),
                event.getLocator().getLineNumber()));
*/
        return false;
    }

}
