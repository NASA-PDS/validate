// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.content.array;

import java.net.URL;
import java.util.Arrays;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;

/**
 * Class that stores problems related to Array data objects.
 *
 * @author mcayanan
 *
 */
public class ArrayContentProblem extends ContentProblem {

  private Integer array;
  private int[] location;

  /**
   * Constructor.
   * 
   * @param exceptionType The severity level.
   * @param problemType The problem type.
   * @param message The problem message.
   * @param source The url of the data file associated with this message.
   * @param label The url of the label file.
   * @param array The index of the array associated with this message.
   * @param location The location associated with the message.
   */
  public ArrayContentProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL source, URL label, int array, int[] location) {
    this(new ProblemDefinition(exceptionType, problemType, message), source, label, array,
        location);
  }

  /**
   * Constructor.
   * 
   * @param defn The problem definition.
   * @param source The url of the data file associated with this message.
   * @param label The url of the label file.
   * @param array The index of the array associated with this message.
   * @param location The location associated with the message.
   */
  public ArrayContentProblem(ProblemDefinition defn, URL source, URL label, int array,
      int[] location) {
    super(defn, source, label);
    this.array = array;
    if (location != null) {
      this.location = location;
    } else {
      this.location = null;
    }
  }

  /**
   * Constructor.
   * 
   * @param defn The problem definition.
   * @param source The url of the data file associated with this message.
   */
  public ArrayContentProblem(ProblemDefinition defn, URL source) {
    this(defn, source, null, -1, null);
  }

  /**
   * @return the index of the array.
   */
  public Integer getArray() {
    return this.array;
  }

  /**
   * 
   * @return the array location.
   */
  public String getLocation() {
    String loc = null;
    if (this.location != null) {
      loc = Arrays.toString(this.location);
      if (this.location.length > 1) {
        loc = loc.replaceAll("\\[", "\\(");
        loc = loc.replaceAll("\\]", "\\)");
      } else {
        loc = loc.replaceAll("\\[", "");
        loc = loc.replaceAll("\\]", "");
      }
    }
    return loc;
  }

}
