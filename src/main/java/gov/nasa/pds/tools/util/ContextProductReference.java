// Copyright 2006-2023, by the California Institute of Technology.
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
package gov.nasa.pds.tools.util;

import java.util.List;

/**
 * Class that represents the lidvid of a PDS4 data product.
 *
 * @author mcayanan
 *
 */
public class ContextProductReference {

  /** The logical identifier. */
  private String lid;

  /** The version. */
  private String version;

  /** The context types. */
  private List<String> types;

  /** The context names. */
  private List<String> names;

  /** Flag to indicate if a version exists. */
  private boolean hasVersion;

  public ContextProductReference(String lid) {
    this(lid, null, null, null);
  }

  public ContextProductReference(String lid, String version, List<String> types,
      List<String> names) {
    this.lid = lid;
    this.version = version;
    this.types = types;
    this.names = names;
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

  public List<String> getTypes() {
    return this.types;
  }

  public List<String> getNames() {
    return this.names;
  }

  public void setLid(String lid) {
    this.lid = lid;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }

  public void setName(List<String> names) {
    this.names = names;
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
    if (o instanceof ContextProductReference) {
      ContextProductReference lidvid = (ContextProductReference) o;

      // Compare Lid and/or version only
      if (this.lid.equalsIgnoreCase(lidvid.getLid())) {
        if (this.hasVersion) {
          if (lidvid.hasVersion() && this.version.equals(lidvid.getVersion())) {
            isEqual = true;
          }
        } else {
          isEqual = true;
        }
      }
    }
    return isEqual;
  }
  @Override
  public int hashCode() {
    String hcid = this.lid + (this.hasVersion ? "::" + this.version : "");
    return hcid.hashCode();
  }
}
