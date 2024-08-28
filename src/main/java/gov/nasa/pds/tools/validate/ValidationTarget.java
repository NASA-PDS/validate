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
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;
import gov.nasa.pds.tools.util.Utility;

/**
 * Represents a location within a validation subtree that can have errors reported against it.
 */
public class ValidationTarget implements Comparable<ValidationTarget> {
  // A static cache of the ValidationTargets.
  // There is no need to re-evaluate and/or create these
  // as validation proceeds, as they are static things like
  // a file or a URL.
  public static HashMap<String, ValidationTarget> cachedTargets = new HashMap<>();

  private TargetType type;
  private String name;
  private String location;
  private URL url;
  private boolean targetIsLabel;
  private Identifier identifier;

  private int knownHashCode;

  private static ValidationTarget build (URL target, URL source, TargetType type) {
    String key = target == null ? "" : target.toString();
    if (!cachedTargets.containsKey(key)) {
      cachedTargets.put (key,new ValidationTarget(target, source, type));
    }
    return cachedTargets.get(key);
  }
  public static ValidationTarget build (URL target) {return build (target, null, null);}
  public static ValidationTarget build (URL target, URL label) {return build (target, label, null);}
  public static ValidationTarget build (String targetLocation, TargetType type) throws MalformedURLException
  {
    int slashPos = targetLocation.lastIndexOf('/');
    ValidationTarget result = build(new URL(targetLocation), null, type);
    result.setLocation(targetLocation);
    if (slashPos < 0) {
      slashPos = targetLocation.lastIndexOf('\\');
    }

    if (slashPos < 0) {
      result.setName(targetLocation);
    } else {
      result.setName(targetLocation.substring(slashPos + 1));
    }
    return result;
  }

  private ValidationTarget(URL target, URL label, TargetType type) {
    this.url = target;
    this.type = type;
    if (target != null && type == null) {
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
      location = null;
      name = null;
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
  protected void setLocation(String location) {
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
      knownHashCode = location.hashCode();
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
}
