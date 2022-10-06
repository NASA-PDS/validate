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

/**
 * Class that holds information about an Axis in an Array.
 *
 * @author mcayanan
 *
 */
public class Axis {
  private String name;

  private Integer element;

  /**
   * Constructor.
   * 
   * @param name axis name.
   */
  public Axis(String name) {
    this(name, -1);
  }

  /**
   * Constructor.
   * 
   * @param element number of elements in the axis.
   */
  public Axis(int element) {
    this("", element);
  }

  /**
   * Constructor.
   * 
   * @param name The axis name.
   * @param element number of elements in the axis.
   */
  public Axis(String name, int element) {
    this.name = name;
    this.element = element;
  }

  /**
   * 
   * @return the name of the axis.
   */
  public String getName() {
    return this.name.toLowerCase();
  }

  /**
   * Set the axis name.
   * 
   * @param name the name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 
   * @return the number of elements.
   */
  public Integer getElement() {
    return this.element;
  }

  /**
   * Sets the number of elements.
   * 
   * @param element the number of elements.
   */
  public void setElement(int element) {
    this.element = new Integer(element);
  }
}
