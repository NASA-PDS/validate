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
  final private String lid;

  /** The version. */
  final private String version;

  /** Flag to indicate if a version exists. */
  final private boolean hasVersion;

  final private String representation;
  
  public Identifier(String id) {
    this(id, null);
  }

  public Identifier(String lid, String version) {
    if (lid == null) throw new IllegalArgumentException("cannot be an identifier if the lid is null");
    this.hasVersion = version != null;
    this.lid = lid;
    this.representation = lid + "::" + (version == null ? "-1.-1" : version);
    this.version = version;
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
   * Determines where 2 LIDVIDs are near neighbors (equal in some cases).
   *
   */
  public boolean nearNeighbor(Identifier identifier) {
    return this.lid.equals(identifier.lid) && 
        (this.version == null || identifier.version == null || this.version.equals(identifier.version));
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Identifier) {
      Identifier i = (Identifier)o;
      return this.representation.equals(i.representation);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return this.representation.hashCode();
  }
}
