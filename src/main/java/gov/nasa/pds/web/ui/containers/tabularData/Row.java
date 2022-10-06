package gov.nasa.pds.web.ui.containers.tabularData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.web.ui.utils.TabularData;

public class Row implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Element> elements = new ArrayList<>();

  public Row(List<Element> elements) {
    this.elements = elements;
  }

  public Row(final String rowData, final List<Column> columns, final int lineNumber,
      final TabularData tabularData) {
    for (Column column : columns) {

      int startIndex = column.getStartByte() - 1;
      int endIndex = column.getBytes() + startIndex;
      String data = null;
      try {
        data = rowData.substring(startIndex, endIndex);

      } catch (StringIndexOutOfBoundsException e) {
        // missing values in row at end probably, try getting truncated
        // version
        try {
          data = rowData.substring(startIndex);
          // add COLUMN_LENGTH_MISMATCH problem
          final int length = endIndex - startIndex;
          final AttributeStatement statement = getBytesStatement(column);
          tabularData.addProblem(statement, "validation.error.columnLengthMismatch", //$NON-NLS-1$
              ProblemType.COLUMN_LENGTH_MISMATCH, length, data.length());
          // column def indicated a length of ## but line length
          // limited
        } catch (StringIndexOutOfBoundsException e2) {
          // note, DataSetValidator.validateTabularFile() will
          // complain about COLUMN_NUMBER_MISMATCH
          final AttributeStatement statement = getStartByteStatement(column);
          tabularData.addProblem(statement, "validation.error.columnOutOfRange", //$NON-NLS-1$
              ProblemType.COLUMN_DEF_OOR, startIndex, rowData.length());
        }
      }
      if (data != null) {
        Element element = new Element(data, column, lineNumber);
        column.addElement(element);
        this.elements.add(element);
      }
    }
  }

  public Row(final int lineNumber, final String rowData, final List<Column> columns,
      final TabularData tabularData) {

    for (Column column : columns) {

      int startIndex = column.getStartByte() - 1;
      int endIndex = column.getBytes() + startIndex;
      String data = null;
      try {
        data = rowData.substring(startIndex, endIndex);

      } catch (StringIndexOutOfBoundsException e) {
        // missing values in row at end probably, try getting truncated
        // version
        try {
          data = rowData.substring(startIndex);
          // add COLUMN_LENGTH_MISMATCH problem
          final int length = endIndex - startIndex;
          final AttributeStatement statement = getBytesStatement(column);
          tabularData.addProblem(statement, "validation.error.columnLengthMismatch", //$NON-NLS-1$
              ProblemType.COLUMN_LENGTH_MISMATCH, length, data.length());
          // column def indicated a length of ## but line length
          // limited
        } catch (StringIndexOutOfBoundsException e2) {
          // note, DataSetValidator.validateTabularFile() will
          // complain about COLUMN_NUMBER_MISMATCH
          final AttributeStatement statement = getStartByteStatement(column);
          tabularData.addProblem(statement, "validation.error.columnOutOfRange", //$NON-NLS-1$
              ProblemType.COLUMN_DEF_OOR, startIndex, rowData.length());
        }
      }
      if (data != null) {
        Element element = new Element(data, column, lineNumber);
        column.addElement(element);
        this.elements.add(element);
      }
    }
  }

  /**
   * Used for FieldDelimited tables since there is no start byte or bytes attributes. Need to
   * separate the string by using the specified delimiter
   * 
   * @param lineNumber
   * @param rowData
   * @param columns
   * @param tabularData
   * @param fieldDelimiter
   */
  public Row(final int lineNumber, final String rowData, final List<Column> columns,
      final TabularData tabularData, String fieldDelimiter, int fieldsCount) {

    // index and storage for delimited data
    int rowFieldIndex = 0;
    String[] rowFields = null;

    rowFields = rowData.split(fieldDelimiter, fieldsCount);

    for (Column column : columns) {
      String data = null;
      try {
        data = rowFields[rowFieldIndex];
        data = data.replaceAll(fieldDelimiter, "");// remove any delimiters, if any
        data = data.replaceAll("\\s+", "");// remove all white spaces and non visible characters
        data = data.trim();// ensure all leading and trailing spaces are not there to avoid error

        rowFieldIndex++;

      } catch (Exception e) {
        // missing values in row at end probably, try getting truncated
        // version
        tabularData.addProblem(lineNumber, "validation.error.columnOutOfRange",
            ProblemType.COLUMN_LENGTH_MISMATCH, rowData.length());

      }

      if (data != null) {
        Element element = new Element(data, column, lineNumber);
        column.addElement(element);
        this.elements.add(element);
      }
    }
  }

  public List<Element> getElements() {
    return this.elements;
  }

  private AttributeStatement getBytesStatement(final Column column) {
    return getStatement(column, "BYTES"); //$NON-NLS-1$
  }

  private AttributeStatement getStartByteStatement(final Column column) {
    return getStatement(column, "START_BYTE"); //$NON-NLS-1$
  }

  private AttributeStatement getStatement(final Column column, final String attribName) {
    final ObjectStatement def = column.getColumnDef();
    if (def != null) {
      final AttributeStatement bytesAttrib = def.getAttribute(attribName);
      return bytesAttrib;
    }
    return null;
  }

  // init with string array
  // init with string array and list of columns
  // get by index
  // get by name string for column

}
