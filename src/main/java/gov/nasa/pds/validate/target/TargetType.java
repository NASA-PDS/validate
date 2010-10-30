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

public enum TargetType {
    BUNDLE(0,"Context_Bundle"), COLLECTION(1,"Context_Collection"),
    DIRECTORY(2,"Directory"), FILE(3,"File");

    private final int value;
    private final String name;

    private TargetType(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
      return this.value;
    }

    public String getName() {
      return this.name;
    }
}
