// Copyright 2006-2019, by the California Institute of Technology.
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.primitives.UnsignedLong;
import gov.nasa.arc.pds.xml.generated.SpecialConstants;
import gov.nasa.pds.label.object.FieldDescription;
import gov.nasa.pds.label.object.FieldType;
import gov.nasa.pds.label.object.RecordLocation;
import gov.nasa.pds.label.object.TableRecord;
import gov.nasa.pds.objectAccess.DelimitedTableRecord;
import gov.nasa.pds.objectAccess.FixedTableRecord;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.FileService;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.SpecialConstantChecker;
import gov.nasa.pds.tools.validate.rule.RuleContext;
import gov.nasa.pds.tools.validate.rule.pds4.DateTimeValidator;

/**
 * Class that performs content validation on the field values of a given record.
 *
 * @author mcayanan
 *
 */
public class FieldValueValidator {
  private static final Logger LOG = LoggerFactory.getLogger(FieldValueValidator.class);
  /** List of invalid values. */

  private static List<FieldType> realTypes = Arrays.asList(
      FieldType.IEEE754LSBDOUBLE,
      FieldType.IEEE754LSBSINGLE,
      FieldType.IEEE754MSBDOUBLE,
      FieldType.IEEE754MSBSINGLE);

  /** Container to capture messages. */
  private ProblemListener listener;
  private String parentName;
  private RuleContext ruleContext;

  private static final Pattern formatPattern =
      Pattern.compile("%([\\+,-])?([0-9]+)(\\.([0-9]+))?([doxfeEs])");
  private static final Pattern leadingWhiteSpacePattern = Pattern.compile("\\s+.*");
  private static final Pattern trailingWhiteSpacePattern = Pattern.compile(".*\\s+");
  private static final Pattern asciiBibCodePattern =
      Pattern.compile("\\d{4}[A-Za-z\\d\\.\\&]{5}[A-Za-z\\d\\.]{9}[A-Z]");
  private static final Pattern asciiIntegerPattern = Pattern.compile("[+-]?\\d+");
  private static final Pattern asciiNonNegativeIntPattern = Pattern.compile("[+]?\\d+");
  private static final Pattern asciiReal =
      Pattern.compile("(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?");
  private static final Pattern asciiNumericBase2Pattern = Pattern.compile("[0-1]{1,255}");
  private static final Pattern asciiNumericBase8Pattern = Pattern.compile("[0-7]{1,255}");
  private static final Pattern asciiNumericBase16Pattern = Pattern.compile("[0-9a-fA-F]{1,255}");
  private static final Pattern asciiMd5ChecksumPattern = Pattern.compile("[0-9a-fA-F]{32}");
  private static final Pattern asciiDoiPattern = Pattern.compile("10\\.\\S+/\\S+");
  private static final Pattern asciiLidPattern =
      Pattern.compile("urn(:[\\p{Ll}\\p{Nd}\\-._]+){3,5}");
  private static final Pattern asciiLidVidPattern =
      Pattern.compile("urn(:[\\p{Ll}\\p{Nd}\\-._]+){3,5}::\\p{Nd}+\\.\\p{Nd}+");
  private static final Pattern asciiLidVidLidPattern =
      Pattern.compile("urn(:[\\p{Ll}\\p{Nd}\\-._]+){3,5}(::\\p{Nd}+\\.\\p{Nd}+)?");
  private static final Pattern asciiVidPattern =
      Pattern.compile("[1-9][0-9]*\\.[0-9]+(\\.[0-9]+)?(\\.[0-9]+)?");
  private static final Pattern asciiDirPathNamePattern =
      Pattern.compile("[A-Za-z0-9][A-Za-z0-9_-]*[A-Za-z0-9]");

  // https://github.com/NASA-PDS/validate/issues/299 Validate tool does not PASS a
  // bundle with a single-character filename
  // A better pattern allows for at least one character file name.
  //
  /* regex changed to match standard reference section 6C.1.
   *   The file name must be constructed from the character set
         A-Z ASCII 0x41 through 0x5A,
         a-z ASCII 0x61 through 0x7A,
         0-9 ASCII 0x30 through 0x39,
         dash “-” ASCII 0x2D,
         underscore “_” ASCII 0x5F, and
         period “.” ASCII 0x2E.
   *   The file name must not begin or end with a dash, underscore, or period.
   *   The file name must include at least one period followed by an extension. A file name may
   *     have more than one period, but PDS will consider all periods other than the final one to
   *     be part of the base name
   */
  private static final Pattern asciiFileNamePattern =
      Pattern.compile("^[A-Za-z0-9]+[A-Za-z0-9-_\\.]*\\.[A-Za-z0-9-_]*[A-Za-z0-9]+$");

  private static final Pattern dirPattern =
      Pattern.compile("/?([A-Za-z0-9][A-Za-z0-9_-]*[A-Za-z0-9]/?|[A-Za-z0-9][^-_]/?)*");

  /**
   * Constructor.
   * 
   * @param target The label.
   * @param dataFile The data file.
   */
  public FieldValueValidator(ProblemListener listener, RuleContext context, String parentName) {
    this.listener = listener;
    this.parentName = parentName;
    this.ruleContext = context;
  }

  /**
   * Validates the field values in the given record.
   * 
   * @param record The record containing the fields to validate.
   * @param fields An array of the field descriptions.
   */
  public void validate(TableRecord record, FieldDescription[] fields)
      throws FieldContentFatalException {
    validate(record, fields, true);
  }

  /**
   * Validates the field values in the given record.
   * 
   * @param record The record containing the fields to validate.
   * @param fields An array of the field descriptions.
   * @param checkFieldFormat A flag to determine whether to check the field values against its
   *        specified field format, if present in the label.
   */
  public void validate(TableRecord record, FieldDescription[] fields, boolean checkFieldFormat)
      throws FieldContentFatalException {
    // Set variable if we get an error that will be a problem for all records
    boolean fatalError = false;

    LOG.debug("validate:fields.length {}", fields.length);

    // Flag to store whether field is a UNSIGNEDBITSTRING or not. Because bit fields
    // cannot be used to check for offset as normally as other kinds of fields, we
    // must first know
    // if fieldIsBitStringFlag is false before checking for offset.
    // The reason is the bit field can have the same offset as the next field
    // because the fields are in bits.

    int actualFieldNumber = 1;
    for (int i = 0; i < fields.length; i++) {
      boolean fieldIsBitStringFlag = false; // Flag to store whether field is a UNSIGNEDBITSTRING or
                                            // SIGNEDBITSTRING
      String value = "dummy_value"; // Set to a dummy value to allow inspection when the value
                                    // changed to a
                                    // legitimate value.

      try {
        // Use the enum types defined in FieldType class.
        if (fields[i].getType() == FieldType.SIGNEDBITSTRING
            || fields[i].getType() == FieldType.UNSIGNEDBITSTRING) {
          fieldIsBitStringFlag = true;
        }
        value = record.getString(i + 1);
        LOG.debug("validate: field #{}, value [{}]", i, value);

        // https://github.com/NASA-PDS/validate/issues/357 Validate allows CRLF within a
        // Table_Delimited field
        // For DelimitedTableRecord, we make an additional check to make sure it does
        // not contain a carriage return or linefeed.
        if (record instanceof DelimitedTableRecord) {
          // Check if value contains a carriage return or line feed.
          if (value.contains("\r") || value.contains("\n")) {
            LOG.error(
                "validate:Field value cannot contain a carriage return or linefeed for Table_Delimited: field {}, value [{}]",
                i + 1, value);
            addTableProblem(ExceptionType.ERROR, ProblemType.INVALID_FIELD_VALUE,
                "Field value cannot contain a carriage return or linefeed for Table_Delimited",
                record.getLocation(), (i + 1));
            continue;
          }
        }

        // issue_298: validate misses double quotes within a delimited table
        //
        // New logic to check if the field starts with a double quote and then also
        // contain a double quote inside.

        boolean fieldIsEnclosedByQuotes = false;
        // Remove the leading and trailing quotes from value if the field is enclosed by
        // it.

        LOG.debug("Value paren check. startswith: {}, endswith: {} ", value.startsWith("\""),
            value.endsWith("\""));
        if (value.startsWith("\"") && value.endsWith("\"")) {
          fieldIsEnclosedByQuotes = true;
          String preSanitizedValue = value;
          value = value.substring(1, value.length() - 1); // Set the value as if it never had
                                                          // starting and
                                                          // ending quotes for this point on.
          LOG.debug("validate:VALUE_SET_TO_SANITIZED:preSanitizedValue,value [{}],[{}]",
              preSanitizedValue, value);
        }

        if (fieldIsEnclosedByQuotes && value.contains("\"")) {
          String message = "The field value '" + value.trim()
              + "' that starts with double quote should not contain double quote(s)";
          addTableProblem(ExceptionType.ERROR, ProblemType.INVALID_FIELD_VALUE, message,
              record.getLocation(), (i + 1));
        }

        // issue_209: fix for incorrect field number
        if (i < (fields.length - 1)) {
          if (fields[i + 1].getOffset() != fields[i].getOffset()) {
            actualFieldNumber++;
          }
        }
        // Adding debug could be time consuming for large files. Uncommenting should be
        // done by developer only for debugging.

        // Check that the length of the field value does not exceed the
        // maximum field length, if specified
        if (fields[i].getMaxLength() != -1) {
          if (value.trim().length() > fields[i].getMaxLength()) {
            String message = "The length of the value '" + value.trim()
                + "' exceeds the defined max field length (expected max " + fields[i].getMaxLength()
                + ", got " + value.trim().length() + ")";
            addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALID_TOO_LONG, message,
                record.getLocation(), (i + 1));
          }
        }

        // issue_209: when checkFieldFormat=false, it's Table_Binary
        if (checkFieldFormat) {
          // issue_56: Validate that Table_Character fields do not overlap based upon
          // field length definitions
          // Better line
          // if (((i+1)<fields.length) && (fields[i].getOffset()+fields[i].getLength() >
          // fields[i+1].getOffset()))
          // The next line is hard to read. Perhaps the parenthesis should surround the
          // 2nd > comparison.
          if (((i + 1) < fields.length)
              && (fields[i].getOffset() + fields[i].getLength()) > fields[i + 1].getOffset()) {
            int currentFieldEndsAt = fields[i].getOffset() + fields[i].getLength();
            int nextOffsetShouldBe = fields[i].getOffset() + fields[i].getLength() + 1;
            String message = "This field overlaps the next field. Current field ends at "
                + currentFieldEndsAt + ". Next field starts at " + fields[i + 1].getOffset()
                + " but should be at least at " + nextOffsetShouldBe;
            LOG.error("{}", "MESSAGE_1:" + message);
            addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_OVERLAP, message,
                record.getLocation(), (i + 1));
          }
        }

        // issue_56: Validate that fields do not overlap based upon field length
        // definitions
        if ((i + 1) < fields.length) {
          // If stopBit is set and we aren't at the end of the field,
          // we should check for overlapping bit fields
          if (fields[i].getStopBit() > 0 && fields[i].getStopBit() != fields[i].getLength() * 8) {

            // first check if the stop bit is longer than the field length
            if (fields[i + 1].getStartBit() > 1) { // only check overlap is next start bit
              // Next, if next startBit > -1 we know we have another bit field to check
              // Let's check the bit fields aren't overlapping
              if (fields[i].getStopBit() >= fields[i + 1].getStartBit() + fields[i+1].getOffset()) {
                String message = "The bit field overlaps the next field. "
                    + "Current stop_bit_location: " + (fields[i].getStopBit() + 1)
                    + ". Next start_bit_location: " + (fields[i + 1].getStartBit() + 1);
                addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_OVERLAP, message,
                    record.getLocation(), actualFieldNumber);
                // (i+1));
                fatalError = true;
              }
            }
            // Otherwise, we are just reading a normal Field_Character or Field_Binary
          } else if ((fields[i].getOffset() > fields[i + 1].getOffset()) || (!fieldIsBitStringFlag
              && ((fields[i].getOffset() + fields[i].getLength()) > fields[i + 1].getOffset()))) {
            int currentFieldEndsAt = fields[i].getOffset() + fields[i].getLength();
            int nextOffsetShouldBe = fields[i].getOffset() + fields[i].getLength() + 1;
            String message = "This field overlaps the next field. Current field ends at "
                // + (fields[i].getOffset()+fields[i].getLength()+1)
                + currentFieldEndsAt + ". Next field starts at " + (fields[i + 1].getOffset() + 1)
                + " but should be at least at " + nextOffsetShouldBe;
            LOG.error("{}", "MESSAGE_2:" + message);
            addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_OVERLAP, message,
                record.getLocation(), (i + 1));
            fatalError = true;
          }
          // Adding debug could be time consuming for large files. Uncommenting should be
          // done by developer only for debugging.
          // else {
          // LOG.debug("validate:column valid i {}",i);
          // }
        }

        // Per the DSV standard in section 4C.1 of the Standards Reference,
        // empty fields are ok for DelimitedTableRecord and space-padded empty fields
        // are ok for FixedTableRecord

        // https://github.com/NASA-PDS/validate/issues/345 validate incorrectly flags
        // integers bounded by "" in a .csv
        // Remove leading or trailing quotes if there are any.
        // Because DSV can have quotes around the value, the value should have been
        // stripped of any double quotes above.

        if (value.isEmpty() || (value.trim().isEmpty() && record instanceof FixedTableRecord)) {
          LOG.debug("VALUE_IS_EMPTY_OR_VALUE_TRIM_IS_EMPTY_AND_FIXED_TABLE_RECORD_IS_OK [{}][{}]",
              value, record.getClass().getName());
          addTableProblem(ExceptionType.DEBUG, ProblemType.BLANK_FIELD_VALUE, "Field is blank.",
              record.getLocation(), (i + 1));
        } else if (!value.trim().isEmpty()) { // Check that the value of the field matches the
                                              // defined data type
          try {
            checkType(value.trim(), fields[i].getType());
            addTableProblem(ExceptionType.DEBUG, ProblemType.FIELD_VALUE_DATA_TYPE_MATCH,
                "Value '" + value.trim() + "' matches its data type '"
                    + fields[i].getType().getXMLType() + "'.",
                record.getLocation(), (i + 1));
          } catch (InvalidTableException e) {
            if (!SpecialConstantChecker.isNonConformantSpecialConstant(value.trim(), fields[i].getSpecialConstants())) {
              String message = "Value does not match its data type '"
                  + fields[i].getType().getXMLType() + "': " + e.getMessage();
              LOG.debug("recordLocation.getLabel: " + record.getLocation().getLabel());
              addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH,
                  message, record.getLocation(), (i + 1));
            } else {
              addTableProblem(ExceptionType.DEBUG, ProblemType.FIELD_VALUE_DATA_TYPE_MATCH,
                  "Value '" + value.trim() + "' matches a special constant.",
                  record.getLocation(), (i + 1));
            }
          }
          // Check that the format of the field value in the table matches
          // the defined formation of the field
          if (checkFieldFormat) {
            // Due to CCB-214, the tool should validate against the
            // validation_format field for Character Tables.
            if (record instanceof FixedTableRecord && (!fields[i].getValidationFormat().isEmpty() || !fields[i].getFieldFormat().isEmpty())) {
              boolean asError = true;
              String format = fields[i].getValidationFormat();
              if (format.isEmpty()) {
                asError = false;
                format = fields[i].getFieldFormat();
              }

              checkFormat(value, format, i + 1, record.getLocation(), asError);
            }
            if (record instanceof DelimitedTableRecord && !fields[i].getFieldFormat().isEmpty()) {
              checkFormat(value, fields[i].getFieldFormat(), i + 1, record.getLocation(), false);
            }
          }
          // Check that the field value is within the defined min/max values
          boolean minmax = fields[i].getSpecialConstants() != null
              && (fields[i].getSpecialConstants().getValidMaximum() != null
                  || fields[i].getSpecialConstants().getValidMinimum() != null);
          if (fields[i].getMinimum() != null || fields[i].getMaximum() != null || minmax) {
            checkSpecialMinMax(value.trim(), fields[i].getSpecialConstants(),
                fields[i].getMinimum(), fields[i].getMaximum(), i + 1, record.getLocation(),
                fields[i].getType());
          }
        } else {
          try {
            checkType(value, fields[i].getType());
            addTableProblem(ExceptionType.DEBUG, ProblemType.BLANK_FIELD_VALUE, "Field is blank.",
                record.getLocation(), (i + 1));
          } catch (Exception e) {
            if (!SpecialConstantChecker.isNonConformantSpecialConstant(value.trim(), fields[i].getSpecialConstants())) {
              String message = "Value does not match its data type '"
                  + fields[i].getType().getXMLType() + "': " + e.getMessage();
              addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_DATA_TYPE_MISMATCH,
                  message, record.getLocation(), (i + 1));
            } else {
              addTableProblem(ExceptionType.DEBUG, ProblemType.BLANK_FIELD_VALUE, "Field matches special constant.",
                  record.getLocation(), (i + 1));
            }
          }
        }
      } catch (Exception e) {
        // The value of i starts at 0 so field number is i + 1
        LOG.error("Error while getting field value: {} at field {}", e.getMessage(), (i + 1));
        addTableProblem(ExceptionType.ERROR, ProblemType.BAD_FIELD_READ,
            "Error while getting field value: " + e.getMessage(), record.getLocation(), (i + 1));
        fatalError = true;

        // Print the stack trace to an external file for inspection.
        FileService.printStackTraceToFile(null, e);

        break; // This error is fatal, get out of for loop since no need to continue.
      }
    }

    // Raise exception if we get a fatal error to avoid overflow of error messages
    // for every records
    if (fatalError) {
      LOG.error("Fatal field content read error. Discontinue reading records.  Last read record {}",
          record.getLocation().getRecord());
      throw new FieldContentFatalException(
          "Fatal field content read error. Discontinue reading records.");
    }
  }

  /**
   * Checks that the given value is within the min/max range.
   * 
   * @param value The field value to validate.
   * @param minimum The minimum value.
   * @param maximum The maximum value.
   * @param recordLocation The record location where the field is located.
   * @param type The field type of the column value to validate.
   */
  private void checkSpecialMinMax(String value, SpecialConstants specialConstants, Double minimum,
      Double maximum, int fieldIndex, RecordLocation recordLocation, FieldType type) {
    final List<FieldType> dateTimes = Arrays.asList(
        FieldType.ASCII_DATE, FieldType.ASCII_DATE_DOY, FieldType.ASCII_DATE_YMD,
        FieldType.ASCII_DATE_TIME, FieldType.ASCII_DATE_TIME_DOY, FieldType.ASCII_DATE_TIME_DOY_UTC,
        FieldType.ASCII_DATE_TIME_UTC, FieldType.ASCII_DATE_TIME_YMD, FieldType.ASCII_DATE_TIME_YMD_UTC);
    final int radix = type == FieldType.ASCII_NUMERIC_BASE2 ? 2 : 
      (type == FieldType.ASCII_NUMERIC_BASE8 ? 8 :
        (type == FieldType.ASCII_NUMERIC_BASE16 ? 16 : 10));
    value = value.trim();
    // https://github.com/NASA-PDS/validate/issues/297 Content validation of
    // ASCII_Integer field does not accept value with leading zeroes
    // Some values may start with a zero but the user may not intend for it to be an
    // Octal so
    // the leading zeros have to be removed:
    //
    // "000810" gets corrected to "810"
    // "-00810" gets corrected to "-810"
    //
    // Only perform the leading zeros correction for these field types:
    //
    // FieldType.ASCII_REAL
    // FieldType.ASCII_INTEGER
    // FieldType.ASCII_NONNEGATIVE_INTEGER
    //
    boolean issueWithLeadingZerosRemovalFlag = false;
    if ((type == FieldType.ASCII_REAL || type == FieldType.ASCII_INTEGER
        || type == FieldType.ASCII_NONNEGATIVE_INTEGER)
        && (value.startsWith("0") || value.startsWith("-0"))) {
      String originalValue = value;
      try {
        // Attempt to convert from string to double, then back to string: "000810" >>
        // 810.0 >> "810.0"
        value = Double.toString(Double.parseDouble(value));
      } catch (java.lang.NumberFormatException ex) {
        // If there is problem with the value because it contains letters, set
        // issueWithLeadingZerosRemovalFlag to true
        // so we know not to make another attempt with NumberUtils.isCreatable() below.
        issueWithLeadingZerosRemovalFlag = true;
      }

      LOG.debug("checkMinMax:originalValue,value,minimum,maximum {},{},{},{}", originalValue, value,
          minimum, maximum);
    }

    if (radix != 10) { // Long.valueOf does not like leading zeros
      while (value.startsWith("0")) value = value.substring(1);
    }
    LOG.debug("checkMinMax:FIELD_VALUE,FIELD_LENGTH [{}],{}", value, value.length());

    // if (NumberUtils.isCreatable(value))
    if ((!issueWithLeadingZerosRemovalFlag && NumberUtils.isCreatable(value)) || radix != 10) {
      // In comparing double or floats, it is important how these values are built.
      // Since the values of 'minimum' and 'maximum' variables are both of types
      // Double,
      // it may be best to convert the String variable 'value' to Double as well.
      //
      // Note that is OK to use Number number = NumberUtils.createNumber(value) but
      // some precision is lost even
      // when both values 0.12345 (one built with createDouble() and one built with
      // createNumber()) should be identical:
      //
      // Field has a value '0.12345' that is greater than the defined maximum value
      // '0.12345'.
      //
      // The below line is commented out and kept for education purpose.
      //
      // Number number = NumberUtils.createNumber(value);
      BigDecimal number = radix == 10 ? NumberUtils.createBigDecimal(value) : BigDecimal.valueOf(Long.valueOf(value, radix));

      boolean isSpecialConstant = false;
      if (specialConstants != null) {
        FieldProblemReporter reporter = new FieldProblemReporter(this, ExceptionType.WARNING,
            ProblemType.FIELD_VALUE_OUT_OF_SPECIAL_CONSTANT_MIN_MAX_RANGE, recordLocation, fieldIndex);
        try {
          isSpecialConstant = SpecialConstantChecker.isConformantSpecialConstant(number.stripTrailingZeros(),
              specialConstants, reporter, radix);
        } catch (NumberFormatException nfe) {
          addTableProblem(ExceptionType.WARNING, ProblemType.FIELD_INVALID_SPECIAL_CONSTANT,
              "One of the special constants could not be converted to the numeric data type of the table cell",
              recordLocation, fieldIndex);
        }
      }
      if (!isSpecialConstant) {
        Double dnumber = number.doubleValue();
        if (minimum != null) {
          if (dnumber.doubleValue() < minimum.doubleValue()) {
            String message = "Field has a value '" + value
                + "' that is less than the defined minimum value '" + minimum.toString() + "'. ";
            addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_OUT_OF_MIN_MAX_RANGE,
                message, recordLocation, fieldIndex);
          } else {
            String message = "Field has a value '" + value
                + "' that is greater than the defined minimum value '" + minimum.toString() + "'. ";
            addTableProblem(ExceptionType.DEBUG, ProblemType.FIELD_VALUE_IN_MIN_MAX_RANGE, message,
                recordLocation, fieldIndex);
          }
        }
        if (maximum != null) {
          if (dnumber.doubleValue() > maximum.doubleValue()) {
            String message = "Field has a value '" + value
                + "' that is greater than the defined maximum value '" + maximum.toString() + "'. ";
            addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_OUT_OF_MIN_MAX_RANGE,
                message, recordLocation, fieldIndex);
          } else {
            String message = "Field has a value '" + value
                + "' that is less than the defined maximum value '" + maximum.toString() + "'. ";
            addTableProblem(ExceptionType.DEBUG, ProblemType.FIELD_VALUE_IN_MIN_MAX_RANGE, message,
                recordLocation, fieldIndex);
          }
        }
      } else {
        addTableProblem(ExceptionType.INFO, ProblemType.FIELD_VALUE_IS_SPECIAL_CONSTANT,
            "Value is a special constant defined in the label: " + value.toString(), recordLocation,
            fieldIndex);
      }
    } else if (dateTimes.contains (type)) {
      if (specialConstants != null) {
        FieldProblemReporter reporter = new FieldProblemReporter(this, ExceptionType.WARNING,
            ProblemType.FIELD_VALUE_OUT_OF_SPECIAL_CONSTANT_MIN_MAX_RANGE, recordLocation, fieldIndex);
        try { 
          SpecialConstantChecker.isTemporalSpecialConstant(DateTimeValidator.toInstant(type, value), specialConstants, reporter);
        } catch (Exception e) {
          String message = "Cannot cast field value '" + value
              + "' to a java.time.Instant from " + type.getXMLType() + " data type.";
          addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_NOT_A_DATETIME, message,
              recordLocation, fieldIndex);
        }
      }
    } else {
      // Value cannot be converted to a number
      String message = "Cannot cast field value '" + value
          + "' to a Number data type or Time data type to validate against the min/max"
          + " values defined in the label.";
      addTableProblem(ExceptionType.ERROR, ProblemType.FIELD_VALUE_NOT_A_NUMBER, message,
          recordLocation, fieldIndex);
    }
  }

  /**
   * Checks that the given value matches its defined field type.
   * 
   * @param value The field value to validate.
   * @param type The field type to check against.
   * 
   * @throws Exception If the value was found to be invalid.
   */
  private void checkType(String value, FieldType type) throws InvalidTableException {
    // File and directory naming rules are checked in the
    // FileAndDirectoryNamingRule class

    LOG.debug("checkType:value,type:afor [{}],[{}]", value, type);

    // https://github.com/NASA-PDS/validate/issues/345 validate incorrectly flags
    // integers bounded by "" in a .csv
    // Remove leading or trailing quotes if there are any.
    // Note that the Utility.removeQuotes() function is taking [ ] and change to []
    // which is not what we want.
    // The space should stay as is since the checking of the type is depending on
    // it.
    // What is in value should have been removed of any leading or trailing quotes
    // already no need to do it again.

    LOG.debug("checkType:value,type:after [{}],[{}]", value, type);

    if (SpecialConstantChecker.isInfOrNan(value) && !realTypes.contains(type)) {
      throw new InvalidTableException(value + " is not allowed");
    }
    if (FieldType.ASCII_INTEGER.getXMLType().equals(type.getXMLType())) {
      if (!asciiIntegerPattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiIntegerPattern.toString() + "'");
      }
      try {
        Long.parseLong(value);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to long: " + value);
      }
    } else if (FieldType.ASCII_NONNEGATIVE_INTEGER.getXMLType().equals(type.getXMLType())) {
      if (!asciiNonNegativeIntPattern.matcher(value).matches()) {
        throw new InvalidTableException("'" + value + "' does not match the pattern '"
            + asciiNonNegativeIntPattern.toString() + "'");
      }
      try {
        UnsignedLong.valueOf(value);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to an unsigned long: " + value);
      }
    } else if (FieldType.ASCII_REAL.getXMLType().equals(type.getXMLType())) {
      if (!asciiReal.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiReal.toString() + "'");
      }
      try {
        Double.parseDouble(value);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to a double: " + value);
      }
    } else if (FieldType.ASCII_NUMERIC_BASE2.getXMLType().equals(type.getXMLType())) {
      if (!asciiNumericBase2Pattern.matcher(value).matches()) {
        throw new InvalidTableException("'" + value + "' does not match the pattern '"
            + asciiNumericBase2Pattern.toString() + "'");
      }
      try {
        new BigInteger(value, 2);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to a base-2 integer: " + value);
      }
    } else if (FieldType.ASCII_NUMERIC_BASE8.getXMLType().equals(type.getXMLType())) {
      if (!asciiNumericBase8Pattern.matcher(value).matches()) {
        throw new InvalidTableException("'" + value + "' does not match the pattern '"
            + asciiNumericBase8Pattern.toString() + "'");
      }
      try {
        new BigInteger(value, 8);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to a base-8 integer: " + value);
      }
    } else if (FieldType.ASCII_NUMERIC_BASE16.getXMLType().equals(type.getXMLType())) {
      if (!asciiNumericBase16Pattern.matcher(value).matches()) {
        throw new InvalidTableException("'" + value + "' does not match the pattern '"
            + asciiNumericBase16Pattern.toString() + "'");
      }
      try {
        new BigInteger(value, 16);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to a base-16 integer: " + value);
      }
    } else if (FieldType.ASCII_MD5_CHECKSUM.getXMLType().equals(type.getXMLType())) {

      if (!asciiMd5ChecksumPattern.matcher(value).matches()) {
        throw new InvalidTableException("'" + value + "' does not match the pattern '"
            + asciiMd5ChecksumPattern.toString() + "'");
      }
      try {
        new BigInteger(value, 16);
      } catch (NumberFormatException e) {
        throw new InvalidTableException("Could not convert to a base-16 integer: " + value);
      }
    } else if (FieldType.ASCII_ANYURI.getXMLType().equals(type.getXMLType())) {
      try {
        @SuppressWarnings("unused") // looking for exception side effect
        URI uri = new URI(value);
      } catch (URISyntaxException e) {
        throw new InvalidTableException(e.getMessage());
      }
    } else if (FieldType.ASCII_DOI.getXMLType().equals(type.getXMLType())) {
      if (!asciiDoiPattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiDoiPattern.toString() + "'");
      }
    } else if (FieldType.ASCII_LID.getXMLType().equals(type.getXMLType())) {
      if (!asciiLidPattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiLidPattern.toString() + "'");
      }
    } else if (FieldType.ASCII_LIDVID.getXMLType().equals(type.getXMLType())) {
      if (!asciiLidVidPattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiLidVidPattern.toString() + "'");
      }
    } else if (FieldType.ASCII_LIDVID_LID.getXMLType().equals(type.getXMLType())) {
      // Can accept a LID or LIDVID?
      if (!asciiLidVidLidPattern.matcher(value).matches()) {
        if (!asciiLidPattern.matcher(value).matches()) {
          throw new InvalidTableException("'" + value + "' does not match the patterns '"
              + asciiLidVidPattern.toString() + "' or '" + asciiLidPattern.toString() + "'");
        }
      }
    } else if (FieldType.ASCII_VID.getXMLType().equals(type.getXMLType())) {
      if (!asciiVidPattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiVidPattern.toString() + "'");
      }
    } else if (FieldType.ASCII_STRING.getXMLType().equals(type.getXMLType())) {
      StringBuffer buffer = new StringBuffer(value);
      for (int i = 0; i < buffer.length(); i++) {
        if (buffer.charAt(i) > 127) {
          if (value.length() > 100) {
            value = value.substring(0, 100) + "...";
          }
          throw new InvalidTableException(
              "'" + value + "' contains non-ASCII character: " + buffer.charAt(i));
        }
      }
    } else if (FieldType.UTF8_STRING.getXMLType().equals(type.getXMLType())) {
      if (value.contains("\\s")) {
        if (value.length() > 100) {
          value = value.substring(0, 100) + "...";
        }
        throw new InvalidTableException("'" + value + "' contains whitespace character(s)");
      }
    } else if (FieldType.ASCII_DATE_DOY.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_DATE_TIME_DOY.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_DATE_TIME_DOY_UTC.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_DATE_TIME_YMD.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_DATE_TIME_YMD_UTC.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_DATE_YMD.getXMLType().equals(type.getXMLType())
        || FieldType.ASCII_TIME.getXMLType().equals(type.getXMLType())) {
      try {
        if (!DateTimeValidator.isValid(type, value)) {
          throw new Exception();
        }
      } catch (Exception e) {
        throw new InvalidTableException("Could not parse " + value + " using these patterns '"
            + DateTimeValidator.DATE_TIME_FORMATS.get(type.getXMLType()) + "'");
      }
    } else if (FieldType.ASCII_DIRECTORY_PATH_NAME.getXMLType().equals(type.getXMLType())) {
      String[] dirs = value.split("/");
      for (String dir : dirs) {
        if (!asciiDirPathNamePattern.matcher(dir).matches()) {
          throw new InvalidTableException(
              dir + " does not match the pattern '" + asciiDirPathNamePattern.toString() + "'");
        }
        if (dir.length() > 255) {
          throw new InvalidTableException(dir + " is longer than 255 characters");
        }
      }
    } else if (FieldType.ASCII_FILE_NAME.getXMLType().equals(type.getXMLType())) {
      if (!asciiFileNamePattern.matcher(value).matches()) {
        throw new InvalidTableException(
            value + " does not match the pattern '" + asciiFileNamePattern.toString() + "'");
      }
      if (value.length() > 255) {
        throw new InvalidTableException(value + " is longer than 255 characters");
      }
    } else if (FieldType.ASCII_FILE_SPECIFICATION_NAME.getXMLType().equals(type.getXMLType())) {
      String dir = FilenameUtils.getFullPath(value);
      if (!dir.isEmpty()) {
        if (dir.length() > 255) {
          throw new InvalidTableException(
              "The directory spec '" + dir + "' is longer than 255 characters");
        }
        if (!dirPattern.matcher(dir).matches()) {
          throw new InvalidTableException(
              "The directory spec '" + dir + "' does not match the pattern '" + dirPattern + "'");
        }
      }
      String name = FilenameUtils.getName(value);
      if (name.isEmpty()) {
        throw new InvalidTableException("No filename spec found in '" + value + "'.");
      }
      if (!asciiFileNamePattern.matcher(name).matches()) {
        throw new InvalidTableException("The filename spec '" + name
            + "' does not match the pattern '" + asciiFileNamePattern.toString() + "'");
      }
      if (name.length() > 255) {
        throw new InvalidTableException(
            "The filename spec '" + name + "' is longer than 255 characters");
      }
    } else if (FieldType.ASCII_BIBCODE.getXMLType().equals(type.getXMLType())) {
      if (!asciiBibCodePattern.matcher(value).matches()) {
        throw new InvalidTableException(
            "'" + value + "' does not match the pattern '" + asciiBibCodePattern.toString() + "'");
      }
    }
  }

  /**
   * Check that the given value matches the defined field format.
   * 
   * @param value The value to check.
   * @param format The defined field format.
   * @param fieldIndex Where the field value is located.
   * @param recordLocation The record location where the field is located.
   */
  private void checkFormat(String value, String format, int fieldIndex, RecordLocation recordLocation, boolean asError) {
    Matcher matcher = formatPattern.matcher(format);
    int precision = -1;
    boolean isValid = true;
    if (matcher.matches()) {
      int width = Integer.parseInt(matcher.group(2));
      if (matcher.group(4) != null) {
        precision = Integer.parseInt(matcher.group(4));
      }
      String specifier = matcher.group(5);
      if (matcher.group(1) != null) {
        String justified = matcher.group(1);
        if ("+".equals(justified)) {
          // check if there is trailing whitespace
          if (trailingWhiteSpacePattern.matcher(value).matches()) {
            addTableProblem(asError ? ExceptionType.ERROR : ExceptionType.WARNING,
                asError ? ProblemType.FIELD_VALID_NOT_RIGHT_JUSTIFIED : ProblemType.FIELD_VALUE_NOT_RIGHT_JUSTIFIED,
                "The value '" + value + "' is not right-justified.", recordLocation, fieldIndex);
            isValid = false;
          }
        } else if ("-".equals(justified)) {
          if (leadingWhiteSpacePattern.matcher(value).matches()) {
            addTableProblem(asError ? ExceptionType.ERROR : ExceptionType.WARNING,
                asError ? ProblemType.FIELD_VALID_NOT_LEFT_JUSTIFIED : ProblemType.FIELD_VALUE_NOT_LEFT_JUSTIFIED,
                "The value '" + value + "' is not left-justified.", recordLocation, fieldIndex);
            isValid = false;
          }
        }
      }
      try {
        if (specifier.matches("[eE]")) {
          String p = "(\\+|-)?([0-9](\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)";
          if (!value.trim().matches(p)) {
            throw new NumberFormatException("Value does not match pattern.");
          }
          Double.parseDouble(value.trim());
        } else if (specifier.equals("f")) {
          String p = "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)";
          if (!value.trim().matches(p)) {
            throw new NumberFormatException("Value does not match pattern.");
          }
          Double.parseDouble(value.trim());
        } else if (specifier.equals("d")) {
          @SuppressWarnings("unused") // looking for exception side effect
          BigInteger bi = new BigInteger(value.trim());
        } else if (specifier.equals("o")) {
          BigInteger bi = new BigInteger(value.trim());
          if (bi.signum() == -1) {
            throw new NumberFormatException("Value must be unsigned.");
          }
        } else if (specifier.equals("x")) {
          BigInteger bi = new BigInteger(value.trim(), 16);
          if (bi.signum() == -1) {
            throw new NumberFormatException("Value must be unsigned.");
          }
        }
      } catch (NumberFormatException e) {
        addTableProblem(asError ? ExceptionType.ERROR : ExceptionType.WARNING,
            asError ? ProblemType.FIELD_VALID_FORMAT_SPECIFIER_MISMATCH : ProblemType.FIELD_VALUE_FORMAT_SPECIFIER_MISMATCH,
            "The value '" + value.trim() + "' does not match the "
                + "defined field format specifier '" + specifier + "': " + e.getMessage(),
            recordLocation, fieldIndex);
      }
      if (value.trim().length() > width) {
        addTableProblem(asError ? ExceptionType.ERROR : ExceptionType.WARNING,
            asError ? ProblemType.FIELD_VALID_TOO_LONG : ProblemType.FIELD_VALUE_TOO_LONG,
            "The length of the value '" + value.trim() + "' exceeds the max "
                + "width set in the defined field format " + "(max " + width + ", got "
                + value.trim().length() + ").",
            recordLocation, fieldIndex);
        isValid = false;
      }
      if (precision != -1) {
        // Per StdRef 4B.1.2, field_format defines the maximum precision, not the only precision allowed:
        // For character tables, <field_format> is used to describe the maximum length and alignment of
        // the data. <field_format> also gives an indication of the maximum precision of real numbers, but
        // does not require all values to have this precision.
        if (specifier.matches("[feE]")) {
          String[] tokens = value.trim().split("[eE]", 2);
          int actual_precision = 0;
          if (tokens[0].indexOf(".") != -1) {
            actual_precision = tokens[0].substring(tokens[0].indexOf(".") + 1).length();
          }
          if (actual_precision > precision) {
            isValid = false;
            addTableProblem(asError ? ExceptionType.ERROR : ExceptionType.WARNING,
                asError ? ProblemType.FIELD_VALID_FORMAT_PRECISION_MISMATCH : ProblemType.FIELD_VALUE_FORMAT_PRECISION_MISMATCH,
                "The number of digits to the right of the decimal point " + "in the value '"
                    + value.trim() + "' should be <= the precision set in the defined field format '"
                    + format+ "' (Expected: <=" + precision + ", Actual: " + actual_precision + ").",
                    recordLocation, fieldIndex);
          }
        }
      }
      if (isValid) {
        addTableProblem(ExceptionType.DEBUG, ProblemType.FIELD_VALUE_FORMAT_MATCH,
            "Value '" + value + "' conforms to the defined field format '" + format + "'",
            recordLocation, fieldIndex);
      }
    }
  }

  /**
   * Adds a TableContentException to the Exception Container.
   * 
   * @param exceptionType The severity.
   * @param message The exception message.
   * @param recordLocation The record location where the field is located.
   * @param field The index of the field.
   */
  void addTableProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      RecordLocation recordLocation, int field) {
    String id = Integer.toString(recordLocation.getDataObjectLocation().getDataObject());
    if (this.parentName != null && 0 < this.parentName.strip().length()) {
      id = this.parentName + " or index " + id;
    }
    listener.addProblem(new TableContentProblem(exceptionType, problemType, message,
        recordLocation.getDataFile(), ruleContext.getTarget(),
        id, recordLocation.getRecord(), field));
  }
}
