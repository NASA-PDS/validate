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
package gov.nasa.pds.validate.target;

import gov.nasa.pds.validate.util.XMLExtractor;

import java.io.File;

/**
 * Class representation of a target in the Validate Tool.
 *
 * @author mcayanan
 *
 */
public class Target {
    private final static String PRODUCT_TYPE_XPATH =
        "//*[ends-with(name(),'Identification_Area')]/product_class";

    private String file;
    private TargetType type;

    /**
     * Constructor.
     *
     * @param file A file.
     */
    public Target(File file) {
        this(file.toString());
    }

    /**
     * Constructor.
     *
     * @param file A file.
     */
    public Target(String file) {
        this.file = file;
        this.type = TargetType.FILE;
        if(new File(file).isDirectory()) {
            type = TargetType.DIRECTORY;
        } else {
            try {
                XMLExtractor extractor = new XMLExtractor(file);
                String type = extractor.getValueFromDoc(PRODUCT_TYPE_XPATH);
                if(TargetType.BUNDLE.getName().equals(type)) {
                    this.type = TargetType.BUNDLE;
                } else if(TargetType.COLLECTION.getName().equals(type)) {
                    this.type = TargetType.COLLECTION;
                }
            } catch (Exception e) {
                //Don't do anything yet
            }
        }
    }

    /**
     * Gets the name of the file.
     *
     * @return the file name.
     */
    public String getFilename() {
        return file;
    }

    /**
     * Returns the target type.
     *
     * @return A target type.
     */
    public TargetType getType() {
        return type;
    }
}
