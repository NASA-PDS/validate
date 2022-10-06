package gov.nasa.pds.web.ui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.web.ui.containers.ColumnInfo;
import gov.nasa.pds.web.ui.containers.tabularData.Column;
import gov.nasa.pds.web.ui.containers.tabularData.Row;

public class TabularData {

  // TODO: init at correct size
  private List<Column> columns = new ArrayList<>();

  private List<ColumnInfo> columnDefs = new ArrayList<>();

  private List<Row> rows = new ArrayList<>();

  private File dataFile;

  private URL dataUrl;

  private final List<LabelParserException> problems = new ArrayList<>();

  // instantiate with meta data (from label?)

  // try to type data and do error handling? or just always do strings

  // need to resort columns with new indexes if provided overriding indexes
  // from label

  // LAB 8/2/09 - arguments are turned around
  public TabularData(final File tabularFile, final Long readRows, long startByte) {
    this(tabularFile, null, readRows, startByte);
  }

  public TabularData(final File tabularFile, List<ColumnInfo> columnInfos, long startByte) {
    this(tabularFile, columnInfos, startByte, null);
  }

  public TabularData(final File tabularFile, List<ColumnInfo> columnInfos, long startByte,
      final Long readRows) {
    this(toURL(tabularFile), columnInfos, startByte, readRows);
    this.dataFile = tabularFile;
  }

  public TabularData(URL fileUrl, List<ColumnInfo> columnInfos, long startByte,
      final Long readRows) {
    this.dataFile = null; // no file, url instead
    this.dataUrl = fileUrl;
    if (columnInfos != null) {
      this.columnDefs.addAll(columnInfos);
      for (ColumnInfo columnInfo : columnInfos) {
        final int currentSize = this.columns.size();
        final Column column = new Column(columnInfo, currentSize);
        // TODO: fix to use index from column info
        this.columns.add(column);
      }

      Collections.sort(this.columns, Comparators.TABULAR_COLUMN_COMPARATOR);

      try {

        // need to handle here if url case is wrong?
        final BufferedReader br = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
        String currLine = null;
        int lineNumber = 1;

        br.skip(startByte);

        while ((currLine = br.readLine()) != null && (readRows == null || readRows >= lineNumber)) {
          final Row row = new Row(currLine, this.columns, lineNumber, this);
          lineNumber++;
          this.rows.add(row);
        }

      } catch (IOException e) {
        // TODO Auto-generated catch block
        throw new RuntimeException("Problem reading source file:" //$NON-NLS-1$
            + fileUrl.toString());
      }
    }

  }

  /**
   * Used for TableDelimited
   * 
   * @param fileUrl
   * @param columnInfos
   * @param startByte
   * @param readRows
   * @param fieldDelimiter
   */
  public TabularData(URL fileUrl, List<ColumnInfo> columnInfos, long startByte, final Long readRows,
      String fieldDelimiter, int fieldsCount) {
    this.dataFile = null;
    this.dataUrl = fileUrl;

    if (columnInfos != null) {
      this.columnDefs.addAll(columnInfos);
      for (ColumnInfo columnInfo : columnInfos) {
        final int currentSize = this.columns.size();
        final Column column = new Column(columnInfo, currentSize);
        // TODO: fix to use index from column info
        this.columns.add(column);
      }

      Collections.sort(this.columns, Comparators.TABULAR_COLUMN_COMPARATOR);

      try {

        // need to handle here if url case is wrong?
        final BufferedReader br = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
        String currLine = null;
        int lineNumber = 1;
        br.skip(startByte);

        while ((currLine = br.readLine()) != null && (readRows == null || readRows >= lineNumber)) {
          final Row row =
              new Row(lineNumber, currLine, this.columns, this, fieldDelimiter, fieldsCount);

          lineNumber++;
          this.rows.add(row);
        }

      } catch (IOException e) {
        // TODO Auto-generated catch block
        throw new RuntimeException("Problem reading source file:" //$NON-NLS-1$
            + fileUrl.toString());
      }
    }

  }

  public Column getColumn(final String searchName) {
    for (final Column column : this.columns) {
      final String columnName = column.getName();
      if (columnName != null && columnName.equalsIgnoreCase(searchName)) {
        return column;
      }
    }
    throw new RuntimeException("No column exists with the name \"" //$NON-NLS-1$
        + searchName + "\"."); //$NON-NLS-1$
  }

  public Column getColumn(int index) {
    try {
      return this.columns.get(index);
    } catch (Exception e) {
      Column column = new Column(index);
      this.columns.set(index, column);
      return this.columns.get(index);
    }
  }

  public List<Column> getColumns() {
    return this.columns;
  }

  public List<Row> getRows() {
    return this.rows;
  }

  public List<ColumnInfo> getColumnDefs() {
    return this.columnDefs;
  }

  public File getDataFile() {
    return this.dataFile;
  }

  private static URL toURL(final File file) {
    try {
      return file.toURI().toURL();
    } catch (final Exception e) {
      // for convenience, recast as runtime
      throw new RuntimeException(e);
    }
  }

  // should only be used when you don't have a statement since statements
  // retain knowledge of their source file and we don't want to double report
  // an error when statements are imported
  public void addProblem(final int lineNumber, final String key, final ProblemType type,
      final Object... arguments) {
    addProblem(lineNumber, null, key, type, arguments);
  }

  public void addProblem(final int lineNumber, final Integer column, final String key,
      final ProblemType type, final Object... arguments) {
    if (this.dataFile != null) {
      addProblem(this.dataFile, lineNumber, column, key, type, arguments);
    } else {
      try {
        addProblem(this.dataUrl.toURI(), lineNumber, column, key, type, arguments);
      } catch (final Exception e) {
        // TODO: anything else to do here?
        e.printStackTrace();
      }
    }
  }

  public void addProblem(final Statement statement, final String key, final ProblemType type,
      final Object... arguments) {
    addProblem(statement, null, key, type, arguments);
  }

  public void addProblem(final Statement statement, final Integer column, final String key,
      final ProblemType type, final Object... arguments) {
    if (statement.getSourceFile() != null) {
      addProblem(statement.getSourceFile(), statement.getLineNumber(), column, key, type,
          arguments);
    } else {
      addProblem(statement.getSourceURI(), statement.getLineNumber(), column, key, type, arguments);
    }
  }

  public void addProblem(final URI sourceURI, final int lineNumber, final Integer column,
      final String key, final ProblemType type, final Object... arguments) {

    final LabelParserException e =
        new LabelParserException(sourceURI, lineNumber, column, key, type, arguments);
    addProblemLocal(e);

  }

  public void addProblem(final File sourceFile, final int lineNumber, final Integer column,
      final String key, final ProblemType type, final Object... arguments) {
    final LabelParserException e =
        new LabelParserException(sourceFile, lineNumber, column, key, type, arguments);
    addProblem(e);
  }

  // for internal use so you can skip the test against the lpe source file or
  // url
  private void addProblemLocal(final LabelParserException e) {
    this.problems.add(e);
  }

  // try to only use when exception has context
  public void addProblem(final LabelParserException e) {
    this.problems.add(e);
  }

  public List<LabelParserException> getProblems() {
    return this.problems;
  }
}
