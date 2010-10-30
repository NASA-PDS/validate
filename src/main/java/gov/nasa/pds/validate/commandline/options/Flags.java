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
package gov.nasa.pds.validate.commandline.options;

/**
 * Class to hold the command-line option flags.
 *
 * @author mcayanan
 *
 */
public interface Flags {
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public static final int ARGNAME = 2;

    public static final String CONFIG[] = {"c", "config", "file"};
    public static final String REGEXP[] = {"e", "regexp", "patterns"};
    public static final String HELP[] = {"h", "help"};
    public static final String REPORT[] = {"r", "report-file", "file name"};
    public static final String TARGET[] = {"t", "target",
        "files,dirs"};
    public static final String VERBOSE[] = {"v", "verbose", "1|2|3"};
    public static final String SCHEMA[] = {"x", "xsd", "schema file"};

    public static final String LOCAL[] = {"L", "local"};
    public static final String VERSION[] = {"V", "version"};

    public static final String WHATIS_HELP = "Display usage.";

    public static final String WHATIS_LOCAL = "Validate files only in the "
        + "target directory rather than recursively traversing down "
        + "the subdirectories.";

    public static final String WHATIS_REGEXP = "Specify file patterns to look"
        + " for when validating a directory. Each pattern should be"
        + " surrounded by quotes. (i.e. -e \"*.xml\")";

    public static final String WHATIS_REPORT = "Specify the report file name."
        + " Default is standard out.";

    public static final String WHATIS_SCHEMA = "Specify a schema file.";

    public final static String WHATIS_TARGET = "Explicitly specify the "
 //       + "targets (files, directories, bundles, collections) to validate. "
        + "targets (files, directories) to validate. "
        + "Targets can be specified implicitly as well. "
        + "(example: validate product.xml)";

    public final static String WHATIS_VERBOSE = "Specify the severity level "
        + "and above to include in the human-readable report: "
        + "(1=Info, 2=Warning, 3=Error). "
        + "Default is Warning and above (level 2).";


    public static final String WHATIS_VERSION = "Display application "
        + "version.";

    public static final String WHATIS_CONFIG = "Specify a configuration "
        + "file to set the tool behavior.";
}
