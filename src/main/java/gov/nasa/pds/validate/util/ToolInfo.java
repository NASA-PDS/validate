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
// $Id: ToolInfo.java -1M 2010-11-04 18:18:10Z (local) $
package gov.nasa.pds.validate.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Class to get tool release information.
 *
 * @author mcayanan
 *
 */
public class ToolInfo {
    public static final String FILE = "validate.properties";

    public static final String NAME = "validate.name";

    public static final String VERSION = "validate.version";

    public static final String RELEASE_DATE = "validate.date";

    public static final String COPYRIGHT = "validate.copyright";

    private static final Properties props = new Properties();

    static {
        try {
        URL propertyFile = ToolInfo.class.getResource(FILE);
         InputStream in = propertyFile.openStream();
         props.load(in);
        } catch(IOException io) {
            throw new RuntimeException(io.getMessage());
        }
    }

    public static String getName() {
        return props.getProperty(NAME);
    }

    public static String getVersion() {
        return props.getProperty(VERSION);
      }

    public static String getReleaseDate() {
        return props.getProperty(RELEASE_DATE);
    }

    public static String getCopyright() {
        return props.getProperty(COPYRIGHT);
      }
}
