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
package gov.nasa.pds.validate.logging.handler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

/**
 * Class to setup a file handler for the tools logging capability.
 *
 * @author mcayanan
 *
 */
public class ToolsFileHandler extends FileHandler {

    /**
     * Constructor that does not append to a file and automatically
     * sets the log level to 'ALL'.
     *
     * @param file A file name to store the logging messages. If the file
     * exists, it will overwrite the existing contents.
     * @param formatter Formatter to be used to format the log messages.
     *
     * @throws SecurityException
     * @throws IOException
     */
    public ToolsFileHandler(String file, Formatter formatter)
    throws SecurityException, IOException {
        this(file, false, Level.ALL, formatter);
    }

    /**
     * Constructor that does not append to a file.
     *
     * @param file A file name to store the logging messages.
     * @param level Sets the logging level.
     * @param formatter Formatter to be used to format the log messages.
     *
     * @throws SecurityException
     * @throws IOException
     */
    public ToolsFileHandler(String file, Level level, Formatter formatter)
    throws SecurityException, IOException {
        this(file, false, level, formatter);
    }

    /**
     * Constructor.
     *
     * @param file A file name to store the logging messages.
     * @param append A flag to tell the handler to append to the file or
     * to overwrite the existing contents.
     * @param level Sets the logging level.
     * @param formatter Formatter to be used to format the log messages.
     *
     * @throws SecurityException
     * @throws IOException
     */
    public ToolsFileHandler(String file, boolean append, Level level,
            Formatter formatter) throws SecurityException, IOException {
        super(file, append);
        setLevel(level);
        setFormatter(formatter);
    }

}
