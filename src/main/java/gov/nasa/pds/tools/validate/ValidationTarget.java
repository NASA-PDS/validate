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

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.FilenameUtils;
import gov.nasa.pds.tools.util.Utility;

/**
 * Represents a location within a validation subtree that can have errors reported against it.
 */
public class ValidationTarget implements Comparable<ValidationTarget> {

  private TargetType type;
  private String name;
  private String location;
  private URL url;
  private boolean targetIsLabel;
  private Identifier identifier;

  private int knownHashCode;

  /**
   * Creates a new instance.
   *
   * @param target the target file or directory
   */
  public ValidationTarget(URL target) {
    this(target, null);
  }
  public ValidationTarget(URL target, URL label) {
    this.url = target;
    if (target != null) {
      if (label == null) {
        type = Utility.getTargetType(target);
      } else {
        type = Utility.getTargetType(target, label);
      }
      location = target.toString();
      // In case we have a directory, we need to remove the backslash at the end
      // to properly get the name
      if (type.equals(TargetType.DIRECTORY)) {
        name = FilenameUtils.getName(Utility.removeLastSlash(target.toString()));
      } else {
        name = FilenameUtils.getName(target.toString());
      }
    } else {
      type = TargetType.FILE;
      location = null;
      name = null;
    }
  }

  /**
   * Creates an instance with a given location.
   *
   * @param location the location
   * @throws MalformedURLException
   */
  public ValidationTarget(String location, TargetType type) throws MalformedURLException {
    this.location = location;
    this.type = type;
    this.url = new URL(location);

    int slashPos = location.lastIndexOf('/');
    if (slashPos < 0) {
      slashPos = location.lastIndexOf('\\');
    }

    if (slashPos < 0) {
      name = location;
    } else {
      name = location.substring(slashPos + 1);
    }
  }

  /**
   * Gets the target type.
   *
   * @return the target type
   */
  public TargetType getType() {
    return type;
  }

  /**
   * Sets the target type.
   *
   * @param type the new target type
   */
  public void setType(TargetType type) {
    this.type = type;
  }

  /**
   * Gets the target location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the target location.
   *
   * @param location the new location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Gets the name of the target.
   *
   * @return the target name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the target name.
   *
   * @param newName the new target name
   */
  public void setName(String newName) {
    name = newName;
  }

  /**
   * Tests whether the target is a label.
   *
   * @return true, if the target is a label
   */
  public boolean isLabel() {
    return targetIsLabel;
  }

  /**
   * Sets whether the target is a label.
   *
   * @param flag true, if the target is a label
   */
  public void setLabel(boolean flag) {
    targetIsLabel = flag;
  }

  /**
   * Gets the identification string for this target.
   *
   * @return the identifier
   */
  public Identifier getIdentifier() {
    return identifier;
  }

  /**
   * Sets the identification string for this target.
   *
   * @param identifier the identifier
   */
  public void setIdentifier(Identifier identifier) {
    this.identifier = identifier;
  }

  @Override
  public int compareTo(ValidationTarget other) {
    return location.compareTo(other.getLocation());
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    if (knownHashCode == 0) {
      String combined = location + ":" + (type == null ? "" : type.toString());
      knownHashCode = combined.hashCode();
    }

    return knownHashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ValidationTarget)) {
      return false;
    }

    ValidationTarget other = (ValidationTarget) obj;
    return location.equals(other.location);
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

}
