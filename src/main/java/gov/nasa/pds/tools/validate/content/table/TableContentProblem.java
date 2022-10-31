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
package gov.nasa.pds.tools.validate.content.table;

import java.net.URL;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ContentProblem;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;

/**
 * Class that stores information about an error that has occurred during table content validation.
 *
 * @author mcayanan
 *
 */
public class TableContentProblem extends ContentProblem {
  /** The data object index associated with the problem. */
  private Integer dataObjectIndex;

  /** The record number associated with the problem. */
  private long record;

  /** The field number associated with the problem. */
  private Integer field;

  /**
   * Constructor.
   * 
   * @param exceptionType The severity level.
   * @param problemType The problem type.
   * @param message The problem message.
   * @param source The data file url of the exception.
   * @param label The associated label url of the exception.
   * @param table The index of the table associated with the message.
   * @param record The index of the record as an integer associated with the message.
   * @param field The index of the field associated with the message.
   */
  public TableContentProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL source, URL label, int dataObjectIndex, int record, int field) {
    this(new ProblemDefinition(exceptionType, problemType, message), source, label, dataObjectIndex,
        record, field);
  }

  /**
   * Constructor.
   * 
   * @param exceptionType The severity level.
   * @param problemType The problem type.
   * @param message The problem message.
   * @param source The data file url of the exception.
   * @param label The associated label url of the exception.
   * @param table The index of the table associated with the message.
   * @param record The index of the record as an integer associated with the message.
   * @param field The index of the field associated with the message.
   */
  public TableContentProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL source, URL label, int dataObjectIndex, long record, int field) {
    this(new ProblemDefinition(exceptionType, problemType, message), source, label, dataObjectIndex,
        record, field);
  }

  /**
   * Constructor.
   * 
   * @param defn The problem definition.
   * @param source The data file url of the exception.
   * @param label The associated label url of the exception.
   * @param dataObjectIndex The index of the data object associated with the message.
   * @param record The index of the record associated with the message.
   * @param field The index of the field associated with the message.
   */
  public TableContentProblem(ProblemDefinition defn, URL source, URL label, int dataObjectIndex,
      long record, int field) {
    super(defn, source, label);
    if (dataObjectIndex == -1) {
      this.dataObjectIndex = -1;
    } else {
      this.dataObjectIndex = dataObjectIndex;
    }
    if (record == -1) {
      this.record = -1;
    } else {
      this.record = record;
    }

    if (field == -1) {
      this.field = -1;
    } else {
      this.field = field;
    }
  }

  /**
   * Constructor.
   * 
   * @param defn The problem definition.
   * @param source The data file url of the exception.
   * @param label The associated label url of the exception.
   * @param table The index of the table associated with the message.
   * @param record The index of the record associated with the message.
   */
  public TableContentProblem(ProblemDefinition defn, URL source, URL label, Integer table,
      Integer record) {
    this(defn, source, label, table, record, -1);
  }

  /**
   * Constructor.
   * 
   * @param defn The problem definition.
   * @param source The data file url of the exception.
   */
  public TableContentProblem(ProblemDefinition defn, URL source) {
    this(defn, source, null, -1, -1, -1);
  }

  /**
   * 
   * @return the table index.
   */
  public Integer getTable() {
    return this.dataObjectIndex;
  }

  /**
   * 
   * @return the record index.
   */
  public long getRecord() {
    return this.record;
  }

  /**
   * 
   * @return the field index.
   */
  public Integer getField() {
    return this.field;
  }
}
