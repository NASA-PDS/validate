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
package gov.nasa.pds.validate.logging;

import java.util.logging.Level;

public class ToolsLevel extends Level {
    public static final Level CONFIGURATION = new ToolsLevel("CONFIGURATION",
            Level.SEVERE.intValue() + 7);
    public static final Level SKIP = new ToolsLevel("SKIP",
            Level.SEVERE.intValue() + 6);
    public static final Level INGEST_FAIL = new ToolsLevel("FAILURE",
            Level.SEVERE.intValue() + 5);
    public static final Level INGEST_ASSOC_FAIL = new ToolsLevel("FAILURE",
            Level.SEVERE.intValue() + 4);
    public static final Level INGEST_SUCCESS = new ToolsLevel("SUCCESS",
            Level.SEVERE.intValue() + 3);
    public static final Level INGEST_ASSOC_SUCCESS = new ToolsLevel("SUCCESS",
            Level.SEVERE.intValue() + 2);
    public static final Level NOTIFICATION = new ToolsLevel("NOTIFICATION",
            Level.SEVERE.intValue() + 1);

    protected ToolsLevel(String name, int value) {
        super(name, value);
    }
}
