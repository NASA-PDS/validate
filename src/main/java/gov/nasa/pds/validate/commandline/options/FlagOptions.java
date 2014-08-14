// Copyright 2006-2014, by the California Institute of Technology.
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
// $Id: FlagOptions.java -1M 2010-11-04 18:09:19Z (local) $
package gov.nasa.pds.validate.commandline.options;

import org.apache.commons.cli.Options;

/**
 * Class that builds the command-line options.
 *
 * @author mcayanan
 *
 */
public class FlagOptions {
    /** Holds a list of valid options. */
    private static Options options;

    static {
        options = new Options();

        options.addOption(new ToolsOption(Flag.CATALOG));
        options.addOption(new ToolsOption(Flag.CHECKSUM_MANIFEST));
        options.addOption(new ToolsOption(Flag.CONFIG));
        options.addOption(new ToolsOption(Flag.FORCE));
        options.addOption(new ToolsOption(Flag.REGEXP));
        options.addOption(new ToolsOption(Flag.HELP));
        options.addOption(new ToolsOption(Flag.REPORT));
        options.addOption(new ToolsOption(Flag.TARGET));
        options.addOption(new ToolsOption(Flag.VERBOSE));
        options.addOption(new ToolsOption(Flag.SCHEMA));
        options.addOption(new ToolsOption(Flag.SCHEMATRON));
        options.addOption(new ToolsOption(Flag.LOCAL));
        options.addOption(new ToolsOption(Flag.VERSION));
        options.addOption(new ToolsOption(Flag.MODEL));
        options.addOption(new ToolsOption(Flag.STYLE));
        options.addOption(new ToolsOption(Flag.INTEGRITY));
    }

    /**
     * Get the list of options.
     *
     * @return A list of options.
     */
    public static Options getOptions() {
        return options;
    }
}
