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
 * An interface that contains the valid property keys for the Validate Tool
 * configuration file.
 *
 * @author mcayanan
 *
 */
public interface ConfigKeys {
    public static final String REGEXPKEY = "validate.regexp";
    public static final String REPORTKEY = "validate.report";
    public static final String TARGETKEY = "validate.target";
    public static final String VERBOSEKEY = "validate.verbose";
    public static final String SCHEMAKEY = "validate.schema";
    public static final String CATALOGKEY = "validate.catalog";
    public static final String LOCALKEY = "validate.local";
}
