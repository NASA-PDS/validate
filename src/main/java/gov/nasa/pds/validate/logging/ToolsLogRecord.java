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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ToolsLogRecord extends LogRecord {
    private String filename;
    private int line;

    public ToolsLogRecord(Level level, String message) {
        this(level, message, null, -1);
    }

    public ToolsLogRecord(Level level, File filename) {
        this(level, "", filename.toString(), -1);
    }

    public ToolsLogRecord(Level level, String message, String filename) {
        this(level, message, filename, -1);
    }

    public ToolsLogRecord(Level level, String message, File filename) {
        this(level, message, filename.toString(), -1);
    }

    public ToolsLogRecord(Level level, String message, String filename,
            int line) {
        super(level, message);
        this.filename = filename;
        this.line = line;
    }

    public String getFilename() {
        return filename;
    }

    public int getLine() {
        return line;
    }
}
