// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate;

/**
 * Class that represents the lidvid of a PDS4 data product.
 *
 * @author mcayanan
 *
 */
public class Identifier {

  /** The logical identifier. */
  private String lid;

  /** The version. */
  private String version;

  /** Flag to indicate if a version exists. */
  private boolean hasVersion;

  public Identifier(String id) {
    this(id, null);
  }

  public Identifier(String lid, String version) {
    this.lid = lid;
    this.version = version;
    if (this.version == null) {
      hasVersion = false;
    } else {
      hasVersion = true;
    }
  }

  public String getLid() {
    return this.lid;
  }

  public String getVersion() {
    return this.version;
  }

  public boolean hasVersion() {
    return this.hasVersion;
  }

  @Override
  public String toString() {
    String identifier = this.lid;
    if (hasVersion) {
      identifier += "::" + this.version;
    }
    return identifier;
  }

  /**
   * Determines where 2 LIDVIDs are equal.
   *
   */
  @Override
  public boolean equals(Object o) {
    boolean isEqual = false;
    Identifier identifier = (Identifier) o;
    if (this.lid.equals(identifier.getLid())) {
      if (this.hasVersion) {
        if (identifier.hasVersion() && this.version.equals(identifier.getVersion())) {
          isEqual = true;
        }
      } else {
        isEqual = true;
      }
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 17;
    // result = prime * result
    // + (hasVersion ? 0 : 1);
    result = prime * result + ((lid == null) ? 0 : lid.hashCode());
    // result = prime * result
    // + ((version == null) ? 0 : version.hashCode());
    return result;
  }
}
