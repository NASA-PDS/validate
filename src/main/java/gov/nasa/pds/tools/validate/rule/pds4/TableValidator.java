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
package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencsv.exceptions.CsvValidationException;
import gov.nasa.pds.label.object.FieldDescription;
import gov.nasa.pds.label.object.TableObject;
import gov.nasa.pds.label.object.TableRecord;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.objectAccess.RawTableReader;
import gov.nasa.pds.objectAccess.table.AdapterFactory;
import gov.nasa.pds.objectAccess.table.DelimiterType;
import gov.nasa.pds.objectAccess.table.TableAdapter;
import gov.nasa.pds.objectAccess.table.TableBinaryAdapter;
import gov.nasa.pds.objectAccess.table.TableDelimitedAdapter;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.EveryNCounter;
import gov.nasa.pds.tools.util.FileService;
import gov.nasa.pds.tools.util.TableCharacterUtil;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.content.table.FieldContentFatalException;
import gov.nasa.pds.tools.validate.content.table.FieldValueValidator;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.tools.validate.rule.RuleContext;
import gov.nasa.pds.validate.constants.Constants;

/**
 * Class that does data content validation of tables.
 *
 * @author mcayanan
 * @author jordanpadams
 *
 */
public class TableValidator implements DataObjectValidator {
  private static final Logger LOG = LoggerFactory.getLogger(TableValidator.class);

  private int progressCounter = 0;
  private long currentObjectRecordCounter = 0;

  private ProblemListener listener = null;
  private RawTableReader currentTableReader = null;
  private RuleContext context = null;
  private TableObject tableObject = null;
  private TableAdapter tableAdapter = null;
  private URL dataFile = null;

  /**
   * Creates a new instance.
   * 
   * @throws InvalidTableException
   * @throws MalformedURLException
   */
  public TableValidator(RuleContext context, ProblemListener listener)
      throws InvalidTableException, MalformedURLException, Exception {
    this(context, listener, null);
  }

  /**
   * Creates a new instance.
   * 
   * @throws InvalidTableException
   * @throws MalformedURLException
   */
  public TableValidator(RuleContext context, ProblemListener listener, Object dataObject)
      throws InvalidTableException, MalformedURLException, Exception {
    this.context = context;
    this.listener = listener;
    this.tableObject = (TableObject) dataObject;
  }

  @Override
  public boolean validate() throws InvalidTableException, IOException, Exception {

    LOG.debug("START table validation");
    this.tableAdapter = AdapterFactory.INSTANCE.getTableAdapter(this.tableObject.getTableObject());;
    this.dataFile = this.tableObject.getDataFile();

    boolean valid = this.validateDataObjectDefinition();

    if (valid && this.context.getCheckData()) {
      valid = this.validateDataObjectContents();
    }
    LOG.debug("END table validation");
    return valid;
  }

  @Override
  public boolean validateDataObjectDefinition() throws InvalidTableException {

    LOG.debug("START table definition validation");
    boolean valid = true;

    // Check adapter

    // Check Field Definitions
    TableFieldDefinitionRule fieldDefinitionCheck =
        new TableFieldDefinitionRule(this.context, this.listener);
    valid = fieldDefinitionCheck.validateFieldFormats();

    LOG.debug("END table definition validation");
    return valid;

  }

  @Override
  public boolean validateDataObjectContents() throws InvalidTableException, IOException, Exception {
		if (EveryNCounter.getInstance().getValue(EveryNCounter.Group.content_validation) % this.context.getEveryN() != 0) return true;

    LOG.debug("START table content validation");
    LOG.debug("validateTableDataContents:getTarget() {}", this.context.getTarget());

    this.currentTableReader = this.tableObject.getRawTableReader();

    int spotCheckData = this.context.getSpotCheckData();

    FieldValueValidator fieldValueValidator = new FieldValueValidator(this.listener, this.context, this.tableObject.getName());

    boolean keepQuotationsFlag = true; // Flag to optionally ask RawTableReader to preserve the
                                       // leading and
                                       // trailing quotes.
    // this.currentTableReader = null;
    // RandomAccessFile raf = null;
    // InputStream inputStream = null;

    boolean inventoryTable = false;

    try {
      this.currentObjectRecordCounter = 0;

      LOG.debug("currentTableReader {}", this.currentTableReader);

      TableRecord record = null;

      if (this.tableAdapter instanceof TableBinaryAdapter) {
        this.validateTableBinaryContent(fieldValueValidator, record, spotCheckData,
            keepQuotationsFlag);
      } else if (this.tableAdapter instanceof TableDelimitedAdapter &&
    		  !this.isTableLineOriented() && !this.getCheckInbetweenFields()) {
        // Determine if we should proceed with calling validateTableDelimited()
        // function.
        // Note that the function validateTableDelimited() can only be applied if the
        // user had specify a length of each record.
        LOG.debug("validateTableDataContents:TABLE_LINEWISE_FALSE {}", this.dataFile);
        try {
          this.validateTableDelimitedContent(fieldValueValidator, this.dataFile, spotCheckData,
              keepQuotationsFlag);
        } catch (Exception ex) {
          LOG.error("ERROR: Cannot validate data file {}", this.dataFile);
          // Print the stack trace to an external file for inspection.
          FileService.printStackTraceToFile(null, ex);
        }
      } else {
        LOG.debug("validateTableDataContents:TABLE_LINEWISE_TRUE {}", this.dataFile);
        this.validateTableCharacterContent(fieldValueValidator, this.dataFile, spotCheckData,
            keepQuotationsFlag, inventoryTable);
      }
    } catch (IOException | CsvValidationException e) {
      // Error occurred while reading the data file
      addTableProblem(ExceptionType.FATAL, ProblemType.TABLE_FILE_READ_ERROR,
          "Error occurred while reading the data file: " + e.getMessage(), this.dataFile, -1, -1);
      // Print the stack trace to an external file for inspection.
      FileService.printStackTraceToFile(null, e);
    } catch (InvalidTableException ex) {
      addTableProblem(ExceptionType.ERROR, ProblemType.TABLE_FILE_READ_ERROR, ex.getMessage(),
          this.dataFile, this.tableObject.getDataObjectLocation().getDataObject(), -1);
      // Print the stack trace to an external file for inspection.
      FileService.printStackTraceToFile(null, ex);


    } catch (Exception e) {
      addTableProblem(ExceptionType.FATAL, ProblemType.TABLE_FILE_READ_ERROR,
          "Error occurred while reading the data file: " + e.getMessage(), this.dataFile, -1, -1);
      // Print the stack trace to an external file for inspection.
      FileService.printStackTraceToFile(null, e);
      e.printStackTrace();
    } finally {
      if (this.currentTableReader != null) {
        this.currentTableReader.close();
      }
    }

    LOG.debug("END table content validation");
    return true;
  }

  private boolean getCheckInbetweenFields() {
    if (this.context == null) {
      LOG.error("Cannot get CHECK_INBETWEEN_FIELDS in ruleContext because ruleContext is null");
      return (false);
    }
    return (this.context.getCheckInbetweenFields());
  }

  /**
   * Validate a table content one record at a time.
   * 
   * @param fieldValueValidator The FieldValueValidator to validate each field
   * @param table The table has an Object
   * @param dataFile The URL of the data file
   * @param spotCheckData The number of records to spot check
   * @param keepQuotationsFlag Flag to keep the double quote or not
   * @return None
   */
  private void validateTableDelimitedContent(FieldValueValidator fieldValueValidator, URL dataFile,
      int spotCheckData, boolean keepQuotationsFlag) throws IOException {
    // Validate a table content record by record.

    LOG.debug("validateTableDelimited:table instanceof TableCharacter");
    LOG.debug("validateTableDelimited:tableIndex,spotCheckData,keepQuotationsFlag {},{},{}",
        this.tableObject.getDataObjectLocation().getDataObject(), spotCheckData,
        keepQuotationsFlag);
    LOG.debug("validateTableDelimited:dataFile {}", this.dataFile);

    TableRecord record = null;
    String line = null, recordDelimiter = this.tableAdapter.getRecordDelimiter();
    int dataObjectIndex = this.tableObject.getDataObjectLocation().getDataObject();

    try {
      line = this.currentTableReader.readNextLine();
      record = this.currentTableReader.getRecord(this.currentTableReader.getCurrentRow(), keepQuotationsFlag);
      
      while (record != null) {
        LOG.debug("validateTableDelimited: recordNumber {}", currentObjectRecordCounter);
        LOG.debug("record {}", record);
        progressCounter();
        this.currentObjectRecordCounter++;

        if (line.length() != record.length()) {
            addTableProblem(ExceptionType.ERROR, ProblemType.RECORD_LENGTH_MISMATCH,
            		"Delimiter is not at the end of the record."
            		+ " Record read using delimiter is " + line.length() + " bytes long"
            		+ " while record is defined to be " + record.length() + " bytes.",
            		dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          }

        try {
          LOG.debug("getFields(): " + this.currentTableReader.getFields().length);
          fieldValueValidator.validate(record, this.currentTableReader.getFields(), false);
          this.checkEOL(recordDelimiter, line, dataObjectIndex);
        } catch (FieldContentFatalException e) {
          // If we get a fatal error, we can avoid an overflow of error output
          // by killing the loop through all the table records
          // This error is FATAL, print the stack trace.
          // Print the stack trace to an external file for inspection.
          FileService.printStackTraceToFile(null, e); // The filename sent to
                                                      // printStackTraceToFile() should
                                                      // be a file that ends with .txt or .log
          break;
        }

        if (spotCheckData != -1) {
          try {
            // TODO: Need to update this logic to count every record, even if we don't
            // validate them
            long nextRow = this.currentTableReader.getCurrentRow() + spotCheckData;

            if (nextRow <= this.tableAdapter.getRecordCount()) {
              this.currentTableReader.setCurrentRow(nextRow-1);
              line = this.currentTableReader.readNextLine();
              record = this.currentTableReader.getRecord(
            		  this.currentTableReader.getCurrentRow(), keepQuotationsFlag);
            } else {
              break;
            }
          } catch (Exception ioEx) {
            record = null;
            throw new IOException("Error occurred " + "while reading data object '"
                + this.tableObject.getDataObjectLocation().getDataObject() + "', " + "record '"
                + (this.currentTableReader.getCurrentRow() + spotCheckData) + "'");
          }
        } else {
          line = this.currentTableReader.readNextLine();
          record = this.currentTableReader.getRecord(this.currentTableReader.getCurrentRow(), keepQuotationsFlag);
        }
      } // end while (record != null)
    } catch (Exception ioEx) {
      FileService.printStackTraceToFile(null, ioEx); // The filename sent to printStackTraceToFile()
                                                     // should be a
                                                     // file that ends with .txt or .log
      LOG.error("Unxpected exeption reached while reading data file {}", dataFile);
      ioEx.printStackTrace();
      throw new IOException("Unexpected exception reached while reading data object '"
          + this.tableObject.getDataObjectLocation().getDataObject() + "', record '"
          + this.currentTableReader.getCurrentRow() + "'");
    }
  }

  private void recordLineLength(ArrayList<Integer> lineLengthsArray,
      ArrayList<Long> lineNumbersArray, String line, long l) {
    // This is a special processing for file that ends .tab but records have
    // different lengths.
    // For every line encountered, record the line number, line length, and the URL
    // of the data file.
    // After all the records are recorded, another function will check that all
    // lines have the same length. If the file name ends with .tab, all the records
    // should have the same length.

    // Check if a particular line length has not been record already. We only want
    // to record lines that are of different lengths.
    int lineLength = line.length();
    if (!lineLengthsArray.contains(lineLength)) {
      lineLengthsArray.add(lineLength);
      lineNumbersArray.add(l);
      LOG.debug("recordLineLength:ADDING:lineLength,lineLengthsArray.size,lineNumber {},{},{}",
          lineLength, lineLengthsArray.size(), l);
    } else {
      LOG.debug("recordLineLength:REJECTING:lineLength,lineLengthsArray.size,lineNumber {},{},{}",
          lineLength, lineLengthsArray.size(), l);
    }
  }

  private void reportIfDifferentLengths(ArrayList<Integer> lineLengthsArray,
      ArrayList<Long> lineNumbersArray, URL dataFile, int tableIndex) {
    // If the size if lineLengthsArray is more than 1, that means there are more
    // than 2 records of different lengths. Report it as a ERROR.
    if (lineLengthsArray.size() > 1) {
      String errorMessage = "";
      String standardMessage =
          "Table of fixed length record should have all records of same length.  Line number:"
              + lineNumbersArray.get(0) + ", record length:" + lineLengthsArray.get(0)
              + ", Line number:" + lineNumbersArray.get(1) + ", record length:"
              + lineLengthsArray.get(1);
      StringBuffer stringBuffer = new StringBuffer();
      int arrayIndex = 0;
      for (Long lineNum : lineNumbersArray) {
        if (arrayIndex == 0) {
          stringBuffer.append(lineNum);
          stringBuffer.append(":");
          stringBuffer.append(lineLengthsArray.get(arrayIndex));
        } else {
          stringBuffer.append(", ");
          stringBuffer.append(lineNum);
          stringBuffer.append(":");
          stringBuffer.append(lineLengthsArray.get(arrayIndex));
        }
        arrayIndex += 1;
      }
      if (lineLengthsArray.size() == 2) {
        errorMessage = standardMessage;
      } else {
        // If there are more than 2 records of different lengths, report all the line
        // numbers and the length of each.
        errorMessage = standardMessage + ". Exhaustive list of all line number and line length: "
            + stringBuffer.toString();
      }

      addTableProblem(ExceptionType.ERROR, ProblemType.RECORD_LENGTH_MISMATCH, errorMessage,
          dataFile, tableIndex, -1);
    }
  }

  private boolean checkEOL (String recordDelimiter, String line, int dataObjectIndex)
  {
	  boolean manuallyParseRecord = false;
      // Check record delimiter
      if (recordDelimiter != null) {
        // Check for how the line ends keying off what was provided in the label.
        // If the delimiter is "Carriage-Return Line-Feed" then the line should end with
        // a carriage return and a line feed.
        if (recordDelimiter.equalsIgnoreCase(DelimiterType.CARRIAGE_RETURN_LINE_FEED.getXmlType())
            && !line.endsWith(DelimiterType.CARRIAGE_RETURN_LINE_FEED.getRecordDelimiter())) {
          addTableProblem(ExceptionType.ERROR, ProblemType.MISSING_CRLF,
              "Record does not end in carriage-return line feed.", dataFile, dataObjectIndex,
              this.currentTableReader.getCurrentRow());
          manuallyParseRecord = true;
        } else if (recordDelimiter.equalsIgnoreCase(DelimiterType.LINE_FEED.getXmlType())) {
          if (!line.endsWith(DelimiterType.LINE_FEED.getRecordDelimiter())) { // If the delimiter is
                                                                              // Line-Feed, then the
                                                                              // line should end
            // with "\n"
            // Perform a check if the record ends in line feed or not ("\n")
            // https://github.com/nasa-pds/validate/issues/292
            // If the delimiter is "Line-Feed" then the line should end with a line feed.
            addTableProblem(ExceptionType.ERROR, ProblemType.MISSING_LF,
                "Record does not end in line feed.", dataFile, dataObjectIndex,
                this.currentTableReader.getCurrentRow());
          }
          if (line.endsWith(DelimiterType.CARRIAGE_RETURN_LINE_FEED.getRecordDelimiter())) { //
            // If the delimiter is Line-Feed, then the line should not end with "\r\n"
            addTableProblem(ExceptionType.ERROR, ProblemType.MISSING_LF,
                "Record delimited with 'Line-Feed' should not end with carriage-return line-feed.",
                dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          }
          manuallyParseRecord = true;
        } else {
          addTableProblem(ExceptionType.DEBUG, ProblemType.CRLF_DETECTED,
              "Record ends in carriage-return line feed.", dataFile, dataObjectIndex,
              this.currentTableReader.getCurrentRow());
        }
      } else // If cannot find a record delimiter, check for the default carriage return line
             // feed.
      if (!line.endsWith(DelimiterType.CARRIAGE_RETURN_LINE_FEED.getRecordDelimiter())) {
        addTableProblem(ExceptionType.ERROR, ProblemType.MISSING_CRLF,
            "Record does not end in carriage-return line feed.", dataFile, dataObjectIndex,
            this.currentTableReader.getCurrentRow());
        manuallyParseRecord = true;
      } else {
        addTableProblem(ExceptionType.DEBUG, ProblemType.CRLF_DETECTED,
            "Record ends in carriage-return line feed.", dataFile, dataObjectIndex,
            this.currentTableReader.getCurrentRow());
      }
      return manuallyParseRecord;
  }

  /**
   * Validate a table content one line at a time.
   * 
   * @param fieldValueValidator The FieldValueValidator to validate each field
   * @param table The table has an Object
   * @param record A TableRecord in a table.
   * @param reader A read to read a line at a time
   * @param dataFile The URL of the data file
   * @param DataObjectLocation The location of the table
   * @param spotCheckData The number of records to spot check
   * @param keepQuotationsFlag Flag to keep the double quote or not
   * @param recordDelimiter How the record ends
   * @param recordLength The length of each record
   * @param recordMaxLength The maximum length of all records
   * @param definedNumRecords The length specified in the label
   * @param inventoryTable Flag indicating if this table is an inventory table
   * @param tableCharacterUtil Util object to parse in between fields
   * @return None
   * @throws InvalidTableException
   */
  private void validateTableCharacterContent(FieldValueValidator fieldValueValidator, URL dataFile,
      int spotCheckData, boolean keepQuotationsFlag, boolean inventoryTable)
      throws IOException, InvalidTableException {
    // The content of this function was copied from the main validate function to
    // reduced the function size.
    TableCharacterUtil tableCharacterUtil = null;
    boolean manuallyParseRecord = false;
    String line;
    long lineNumber = 0;
    int dataObjectIndex = this.tableObject.getDataObjectLocation().getDataObject();

    // The checking for same line length should be done not based on the file name
    // (ending with .tab) but with the table type.
    // If the type of the table is not TableDelimited, the checking of same line
    // length should be done.
    boolean tableIsFixedLength = !(this.tableAdapter instanceof TableDelimitedAdapter);

    if (tableIsFixedLength) {
      tableCharacterUtil = new TableCharacterUtil(this.context.getTarget(), this.listener);
      tableCharacterUtil.parseFieldsInfo();
    }

    String recordDelimiter = this.tableAdapter.getRecordDelimiter();

    // Add 2 arrays to keep track of each line number and its length.
    ArrayList<Integer> lineLengthsArray = new ArrayList<>(0);
    ArrayList<Long> lineNumbersArray = new ArrayList<>(0);

    line = tableIsFixedLength ? this.currentTableReader.readNextFixedLine() : this.currentTableReader.readNextLine();

    if (line != null) {
      LOG.debug("validateTableCharacter:POSITION_1:lineNumber,line {},[{}],{}", lineNumber, line,
          line.length());
      LOG.debug("validateTableCharacter:POSITION_1:lineNumber,line.length,line {},{},[{}]",
          lineNumber, line.length(), line);
      if (tableIsFixedLength) {
        this.recordLineLength(lineLengthsArray, lineNumbersArray, line, lineNumber + 1);
        int minLineLength = 0;
        for (FieldDescription field : this.currentTableReader.getFields()) {
          if (field.getOffset() + field.getLength() > minLineLength) {
            minLineLength = field.getOffset() + field.getLength();
          }
        }
        minLineLength = minLineLength + DelimiterType.getDelimiterType(recordDelimiter).getRecordDelimiter().length();
        if (line.length() < minLineLength) {
          addTableProblem(ExceptionType.ERROR, ProblemType.RECORD_LENGTH_MISMATCH,
              "The fixed line size from label of " + line.length()
              + " bytes is less than the number of bytes defined by the fields " + minLineLength,
              dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          return;
        }
      }
    } else {
      LOG.debug("validateTableCharacter:POSITION_1:lineNumber,line {},[{}]", lineNumber, line);
    }

    while (line != null) {
      progressCounter();
      lineNumber += 1;

      this.currentObjectRecordCounter++;

      manuallyParseRecord = this.checkEOL (recordDelimiter, line, dataObjectIndex);
      // Check record length
      long recordLength = this.tableAdapter.getRecordLength();
      if (recordLength != -1) {
        if (line.length() != recordLength) {
          addTableProblem(ExceptionType.ERROR, ProblemType.RECORD_LENGTH_MISMATCH,
              "Record does not equal the defined record length " + "(expected " + recordLength
                  + ", got " + line.length() + ").",
              dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          manuallyParseRecord = true;
        } else {
          addTableProblem(ExceptionType.DEBUG, ProblemType.RECORD_MATCH,
              "Record equals the defined record length " + "(expected " + recordLength + ", got "
                  + line.length() + ").",
              dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
        }
      }

      // Check max record length (delimited tables only)
      long maximumRecordLength = this.tableAdapter.getMaximumRecordLength();
      if (maximumRecordLength != -1) {
        if (line.length() > maximumRecordLength) {
          addTableProblem(ExceptionType.ERROR, ProblemType.RECORD_LENGTH_MISMATCH,
              "Record length exceeds the max defined record length " + "(expected "
                  + maximumRecordLength + ", got " + line.length() + ").",
              dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          manuallyParseRecord = true;
        } else {
          addTableProblem(ExceptionType.DEBUG, ProblemType.GOOD_RECORD_LENGTH,
              "Record length is less than or equal to the max " + "defined record length " + "(max "
                  + maximumRecordLength + ", got " + line.length() + ").",
              dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
        }
      }

      // Need to manually parse the line if we're past the defined
      // number of records. The PDS4-JParser library won't let us
      // read past the defined number of records.

      // TODO Move this to PDS4 JParser
      if (this.currentTableReader.getCurrentRow() > this.tableAdapter.getRecordCount()) {
        manuallyParseRecord = true;
      }

      TableRecord record = null;
      try {
        if (manuallyParseRecord && !(this.tableAdapter instanceof TableDelimitedAdapter)) {
          record = this.currentTableReader.toRecord(line, this.currentTableReader.getCurrentRow());
        } else {
          record = this.currentTableReader.getRecord(this.currentTableReader.getCurrentRow(),
              keepQuotationsFlag);
        }

        // https://github.com/NASA-PDS/validate/issues/57 As a user, I want to be warned
        // when there are alphanumeric characters between fields in Table_Character
        LOG.debug("this.getCheckInbetweenFields(): {}", this.getCheckInbetweenFields());
        LOG.debug("tableCharacterUtil: ", tableCharacterUtil);
        if ((tableCharacterUtil != null) && this.getCheckInbetweenFields()) {
          tableCharacterUtil.validateInBetweenFields(line, lineNumber);
        }

        // Validate fields within the record here
        try {
          fieldValueValidator.validate(record, this.currentTableReader.getFields());
        } catch (FieldContentFatalException e) {
          // If we get a fatal error, we can avoid an overflow of error output
          // by killing the loop through all the table records
          // Print the stack trace to an external file for inspection.
          // This error is FATAL, print the stack trace.
          FileService.printStackTraceToFile(null, e);
          LOG.error("TableDataContentValidationRule:isApplicable:message:" + e.getMessage());
          break;
        }
        LOG.debug("validateTableCharacter:POSITION_12");

        // Validate collection inventory member status
        if (inventoryTable) {
          Map<String, Integer> fieldMap = this.currentTableReader.getFieldMap();
          String memberStatus = null;
          if (fieldMap.containsKey("Member_Status")) {
            memberStatus = record.getString(fieldMap.get("Member_Status").intValue());
          } else if (fieldMap.containsKey("Member Status")) {
            memberStatus = record.getString(fieldMap.get("Member Status").intValue());
          }
          if (!memberStatus.startsWith("P") && !memberStatus.startsWith("S")) {
            addTableProblem(ExceptionType.ERROR, ProblemType.INVALID_MEMBER_STATUS,
                "Invalid member status " + memberStatus + ".  It should begin with 'P' or 'S'.",
                dataFile, dataObjectIndex, this.currentTableReader.getCurrentRow());
          }
        }
      } catch (IOException io) {
        // An exception here means that the number of fields read in
        // did not equal the number of fields exepected
        addTableProblem(ExceptionType.ERROR, ProblemType.FIELDS_MISMATCH, io.getMessage(), dataFile,
            dataObjectIndex, this.currentTableReader.getCurrentRow());
      }

      if (spotCheckData > 1) {
        // If spot checking is turned on, we want to skip to
        // the record just before the one we want to read
        try {
          record = this.currentTableReader.getRecord(
              this.currentTableReader.getCurrentRow() + (spotCheckData - 1), keepQuotationsFlag);
        } catch (IllegalArgumentException iae) {
          line = null;
          continue;
        } catch (IOException io) {
          throw new IOException(
              "Error occurred " + "while reading table '" + dataObjectIndex + "', " + "record '"
                  + (this.currentTableReader.getCurrentRow() + spotCheckData) + "'");
        }
      }

      // WARNING: All checks must happen before this in case we
      // reach end of the table
      if (this.tableAdapter instanceof TableDelimitedAdapter
          && this.tableAdapter.getRecordCount() == this.currentTableReader.getCurrentRow()) {
        LOG.debug("recordNumber: {}, definedNumRecords: {}", lineNumber,
            this.tableAdapter.getRecordCount());
        // The break statement assures that the code does not read passed what was
        // advertised in the label "records" field.
        break;
      }

      line = tableIsFixedLength ? this.currentTableReader.readNextFixedLine() : this.currentTableReader.readNextLine();
      if (line != null) {
        LOG.debug("recordNumber: {}, line.length: {}, record: [{}]", lineNumber, line.length(),
            line);
        if (tableIsFixedLength) {
          this.recordLineLength(lineLengthsArray, lineNumbersArray, line, lineNumber + 1);
        }
      } else {
        LOG.debug("recordNumber: {}, record: [{}]", lineNumber, line);
      }
    } // while (line != null) {

    // only give error message when the actual record number is smaller than the
    // defined in the label
    if (this.tableAdapter.getRecordCount() != -1
        && (this.tableAdapter.getRecordCount() > this.currentTableReader.getCurrentRow())
        && spotCheckData == -1) {
      String message = "Number of records read is not equal "
          + "to the defined number of records in the label (expected "
          + this.tableAdapter.getRecordCount() + ", got " + this.currentTableReader.getCurrentRow()
          + ").";
      LOG.error("validateTableCharacter:POSITION_2:{}", message);
      addTableProblem(ExceptionType.ERROR, ProblemType.RECORDS_MISMATCH, message, dataFile,
          dataObjectIndex, -1);
    }

    // Provide a warning if for some reason the number of records read is more than
    // advertised
    // (perhaps due to an extra carriage return in a field value which can bumps the
    // number of lines by 1)
    if (this.currentObjectRecordCounter > this.tableAdapter.getRecordCount()) {
      String message = "Number of records in file may be more than defined by label. Expected: "
          + this.tableAdapter.getRecordCount() + ", Read in: "
          + this.currentTableReader.getCurrentRow();
      LOG.warn("validateTableCharacter:POSITION_2:{}", message);
      addTableProblem(ExceptionType.WARNING, ProblemType.RECORDS_MISMATCH, message, dataFile,
          dataObjectIndex, -1);
    }

    // If the table is fixed length all the records length are not the same, report
    // it as an error.
    if (tableIsFixedLength) {
      reportIfDifferentLengths(lineLengthsArray, lineNumbersArray, dataFile, dataObjectIndex);
    }

    LOG.debug("validateTableCharacter: currentObjectRecordCounter,definedNumRecords {},{}",
        this.currentObjectRecordCounter, this.tableAdapter.getRecordCount());
    LOG.debug("validateTableCharacter: DONE_VALIDATING");
  }

  /**
   * Given a text table object, determine if it a line oriented table or not. A line-oriented table
   * contains some record delimiter and optional record_length.
   * 
   * @param table The table object
   * @param dataFile The URL to the data file
   * @return true if the line is line-oriented and false if not
   */
  private boolean isTableLineOriented() {
    LOG.debug("isTableLineOriented:table isinstanceof {}",
        this.tableAdapter.getClass().getSimpleName());

    // Must check to see that the record_delimiter tag is not "Line-Feed" or
    // "Carriage-Return Line-Feed" as the function validateTableContentRecordWise
    // cannot process such a file.

    // Assume we will read using the record delimiter, unless a record length is given
    boolean tableIsLineOrientedFlag = true;

    // If record length is not set
    if (this.tableAdapter.getRecordLength() > 0) {
      tableIsLineOrientedFlag = false;
    }

    return tableIsLineOrientedFlag;
  }

  private void validateTableBinaryContent(FieldValueValidator fieldValueValidator,
      TableRecord record, int spotCheckData, boolean keepQuotationsFlag)
      throws IOException, CsvValidationException, InvalidTableException {
    LOG.debug("table instanceof TableBinary");
    try {
      record = this.currentTableReader.readNext();
      while (record != null) {
        progressCounter();
        this.currentObjectRecordCounter++;

        try {
          fieldValueValidator.validate(record, this.currentTableReader.getFields(), false);
        } catch (FieldContentFatalException e) {
          // If we get a fatal error, we can avoid an overflow of error output
          // by killing the loop through all the table records
          // Print the stack trace to an external file for inspection.
          // This is FATAL error: "Fatal field content read error. Discontinue reading
          // records"
          FileService.printStackTraceToFile(null, e);
          LOG.error("TableDataContentValidationRule:message:" + e.getMessage());
          break;
        } catch (ArrayIndexOutOfBoundsException e) {
          addTableProblem(ExceptionType.ERROR, ProblemType.INVALID_OBJECT_DEFINITION,
              e.getMessage(), this.dataFile,
              this.tableObject.getDataObjectLocation().getDataObject(),
              record.getLocation().getRecord());
        }
        if (spotCheckData != -1) {
          try {
            record = this.currentTableReader.getRecord(
                this.currentTableReader.getCurrentRow() + spotCheckData, keepQuotationsFlag);
          } catch (IllegalArgumentException iae) {
            record = null;
          } catch (IOException io) {
            throw new IOException("Error occurred " + "while reading file area "
                + this.tableObject.getDataObjectLocation().getFileArea() + ", data object "
                + this.tableObject.getDataObjectLocation().getDataObject() + ", " + "record '"
                + (this.currentTableReader.getCurrentRow() + spotCheckData) + "'");
          }
        } else {
          record = this.currentTableReader.readNext();
        }
      }
    } catch (BufferUnderflowException be) {
      throw new IOException("Unexpected end-of-file reached while reading file area "
          + this.tableObject.getDataObjectLocation().getFileArea() + ", data object "
          + this.tableObject.getDataObjectLocation().getDataObject() + ", " + "record '"
          + this.currentTableReader.getCurrentRow() + "'");
    }
  }

  /**
   * Adds a table-related exception to the ProblemListener.
   * 
   * @param exceptionType The exception type.
   * @param message The message.
   * @param dataFile The data file associated with the exception.
   * @param table The index of the table associated with the exception.
   * @param record The index of the record as an integer associated with the exception.
   */
  private void addTableProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, int table, int record) {
    addTableProblem(exceptionType, problemType, message, dataFile, table, record, -1);
  }

  /**
   * Adds a table-related exception to the ProblemListener.
   * 
   * @param exceptionType The exception type.
   * @param message The message.
   * @param dataFile The data file associated with the exception.
   * @param table The index of the table associated with the exception.
   * @param record The index of the record as a long associated with the exception.
   */
  private void addTableProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, int table, long record) {
    addTableProblem(exceptionType, problemType, message, dataFile, table, record, -1);
  }

  /**
   * Adds a table-related exception to the ProblemListener.
   * 
   * @param exceptionType The exception type.
   * @param message The message.
   * @param dataFile The data file associated with the exception.
   * @param table The index of the table associated with the exception.
   * @param record The index of the record associated with the exception.
   * @param field The index of the field associated with the exception.
   */
  private void addTableProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, int dataObjectIndex, long record, int field) {
    String id = Integer.toString(dataObjectIndex);
    if (this.tableObject.getName() != null && 0 < this.tableObject.getName().strip().length()) {
      id = this.tableObject.getName() + " or index " + id;
    }
    this.listener.addProblem(new TableContentProblem(exceptionType, problemType, message, dataFile,
        this.context.getTarget(), id, record, field));
  }

  private void progressCounter() {
    if (progressCounter++ == Integer.MAX_VALUE) {
      progressCounter = 0;
    } else if (progressCounter % Constants.CONTENT_VAL_PROGRESS_COUNTER == 0) {
      System.out.print(".");
    }
  }

  public TableAdapter getTable() {
    return tableAdapter;
  }

  public void setTableAdapter(TableAdapter tableAdapter) {
    this.tableAdapter = tableAdapter;
  }

  public URL getDataFile() {
    return dataFile;
  }

  public void setDataFile(URL dataFile) {
    this.dataFile = dataFile;
  }

  public RawTableReader getCurrentTableReader() {
    return currentTableReader;
  }

  public void setCurrentTableReader(RawTableReader currentTableReader) {
    this.currentTableReader = currentTableReader;
  }
}
