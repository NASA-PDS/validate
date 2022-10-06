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
 * Defines a reference from a location to an identifier.
 */
public class IdentifierReference implements Comparable<IdentifierReference> {

  private final String referenceLocation;
  private final Identifier identifier;
  private int knownHashCode;

  /**
   * Creates a new instance with a given location for the target referring to an identifier.
   *
   * @param referenceLocation the location of the target containing the reference
   * @param identifier the identifier referred to
   */
  public IdentifierReference(String referenceLocation, Identifier identifier) {
    this.referenceLocation = referenceLocation;
    this.identifier = identifier;
  }

  /**
   * Gets the location of the target containing the reference.
   *
   * @return the target location containing the reference
   */
  public String getReferenceLocation() {
    return referenceLocation;
  }

  /**
   * Gets the identifier referenced.
   *
   * @return the identifier
   */
  public Identifier getIdentifier() {
    return identifier;
  }

  @Override
  public int compareTo(IdentifierReference other) {
    int result = referenceLocation.compareTo(other.referenceLocation);
    if (result != 0) {
      return result;
    }
    return identifier.getLid().compareTo(other.identifier.getLid());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof IdentifierReference)) {
      return false;
    }

    IdentifierReference other = (IdentifierReference) obj;
    return identifier.equals(other.identifier) && referenceLocation.equals(other.referenceLocation);
  }

  @Override
  public int hashCode() {
    if (knownHashCode == 0) {
      String combined = identifier + ":" + referenceLocation;
      knownHashCode = combined.hashCode();
    }

    return knownHashCode;
  }

}
