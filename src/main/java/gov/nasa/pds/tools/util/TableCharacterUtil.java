package gov.nasa.pds.tools.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

/**
 * Util class to allow reading of a record and check for values between fields.
 */

public class TableCharacterUtil {
  private static final Logger LOG = LoggerFactory.getLogger(TableCharacterUtil.class);

  private static String PRODUCT_OBSERVATIONAL = "Product_Observational";
  private static String FILE_AREA_OBSERVATIONAL =
      PRODUCT_OBSERVATIONAL + "/File_Area_Observational";
  private static String TABLE_CHARACTER = FILE_AREA_OBSERVATIONAL + "/Table_Character";
  private static String RECORD_CHARACTER = TABLE_CHARACTER + "/Record_Character";
  private static String FIELD_CHARACTER = RECORD_CHARACTER + "/Field_Character";

  private static String FIELD_NUMBER_SIMPLE = "field_number"; // Used to retrieve individual field
                                                              // from node and not
                                                              // document.
  private static String FIELD_LOCATION_SIMPLE = "field_location"; // Used to retrieve individual
                                                                  // field from node and
                                                                  // not document.
  private static String FIELD_FORMAT_SIMPLE = "field_format"; // Used to retrieve individual field
                                                              // from node and not
                                                              // document.
  private static String FIELD_LENGTH_SIMPLE = "field_length"; // Used to retrieve individual field
                                                              // from node and not
                                                              // document.
  private static String DATA_TYPE_SIMPLE = "data_type"; // Used to retrieve individual field from
                                                        // node and not
                                                        // document.

  private static List<String> fieldNumberList = new ArrayList<>();
  private static List<String> fieldLocationList = new ArrayList<>();
  private static List<String> fieldTypeList = new ArrayList<>();
  private static List<String> fieldLengthList = new ArrayList<>();
  private static List<String> fieldFormatList = new ArrayList<>();

  private URL target = null;
  private ProblemListener listener = null;
  private boolean reportedErrorFlag = false;

  private void resetColumnInfoLists() {
    // Since this function can be called many times, these static lists need to be
    // cleared between each call,
    // otherwise the list will grow each time a Table_Character file is validated.
    fieldNumberList.clear();
    fieldLocationList.clear();
    fieldTypeList.clear();
    fieldLengthList.clear();
    fieldFormatList.clear();
  }

  public URL getTarget() {
    return (this.target);
  }

  public ProblemListener getListener() {
    return (this.listener);
  }

  /**
   * Creates a new instance.
   */
  public TableCharacterUtil(URL target, ProblemListener listener) {
    this.target = target;
    this.listener = listener;
  }

  /**
   * Parse the fields info for field_location, field_length and such and store them in lists.
   *
   * @param None
   * @return None
   */

  public void parseFieldsInfo() {
    // Since this function can be called many times, these static lists need to be
    // cleared between each call,
    // otherwise the list will grow each time a Table_Character file is validated.

    this.resetColumnInfoLists();

    try {
      XMLExtractor extractor = new XMLExtractor(getTarget());

      List<TinyNodeImpl> FieldCharacterNodeList = extractor.getNodesFromDoc(FIELD_CHARACTER);
      String fieldValue = "";

      // Note that the function getValueFromItem() returns an empty string if the
      // field we seek is not present in the node.
      for (TinyNodeImpl node : FieldCharacterNodeList) {
        fieldValue = extractor.getValueFromItem(FIELD_NUMBER_SIMPLE, node);
        fieldNumberList.add(fieldValue);
        fieldValue = extractor.getValueFromItem(FIELD_LOCATION_SIMPLE, node);
        fieldLocationList.add(fieldValue);
        fieldValue = extractor.getValueFromItem(DATA_TYPE_SIMPLE, node);
        fieldTypeList.add(fieldValue);
        fieldValue = extractor.getValueFromItem(FIELD_LENGTH_SIMPLE, node);
        fieldLengthList.add(fieldValue);
        fieldValue = extractor.getValueFromItem(FIELD_FORMAT_SIMPLE, node);
        fieldFormatList.add(fieldValue);
      }

      LOG.debug("parseFieldsInfo:fieldNumberList {}", fieldNumberList);
      LOG.debug("parseFieldsInfo:fieldLocationList {}", fieldLocationList);
      LOG.debug("parseFieldsInfo:fieldTypeList {}", fieldTypeList);
      LOG.debug("parseFieldsInfo:fieldLengthList {}", fieldLengthList);
      LOG.debug("parseFieldsInfo:fieldFormatList {}", fieldFormatList);
    } catch (Exception e) {
      LOG.error("Cannot extract {} from label {}", RECORD_CHARACTER, getTarget());
      getListener().addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
          ProblemType.INVALID_LABEL, "Cannot extract " + RECORD_CHARACTER + " from label"),
          getTarget()));
    }

  }

  /**
   * Validate in between fields for non-blanks values. This test is optional and only call if the
   * user requested it.
   *
   * @param record The record being validated (as text)
   * @param lineNumber Where in the file is this record.
   * @return None
   */
  public void validateInBetweenFields(String record, long lineNumber) {
    LOG.debug("validateInBetweenFields:recordNumber,record {},[{}]", lineNumber, record);

    int startIndex = 0;
    int endIndex = 0;
    int previousEndIndex = 0;

    List<Boolean> columnsWarningFlagList =
        new ArrayList<>(Arrays.asList(new Boolean[fieldNumberList.size()]));
    Collections.fill(columnsWarningFlagList, Boolean.FALSE);

    for (int ii = 0; ii < fieldNumberList.size(); ii++) {
      startIndex = Integer.parseInt(fieldLocationList.get(ii)) - 1; // The location in the label
                                                                    // starts at 1.
      endIndex = startIndex + Integer.parseInt(fieldLengthList.get(ii));
      LOG.debug("validateInBetweenFields:ii,startIndex,endIndex {},{},{}", ii, startIndex,
          endIndex);

      // Do a sanity check if startIndex is larger than endIndex because the user had
      // created a bad info for the offsets.
      if (startIndex > endIndex) {
        LOG.error(
            "validateInBetweenFields:In record {}, unexpected starting location {} and length {}",
            lineNumber, fieldLocationList.get(ii), fieldLengthList.get(ii));
        return;
      }

      // Check the gap between where the previous field end and the current field
      // start for non blanks and report it.
      if (ii > 0) {
        LOG.debug("validateInBetweenFields:ii,previousEndIndex,startIndex {},{},{}", ii,
            previousEndIndex, startIndex);

        // Do a sanity check if the user has field_location of the current field before
        // the end of the previous end index.

        if (previousEndIndex > startIndex) {
          if ((columnsWarningFlagList.get(ii) == Boolean.FALSE) && !this.reportedErrorFlag) {
            String errorMessage =
                "In record " + Long.toString(lineNumber) + ", the ending location "
                    + Integer.toString(previousEndIndex) + " of field (start with 1) "
                    + fieldNumberList.get(ii - 1) + " is greater than the starting index "
                    + Integer.toString(startIndex) + " of field " + fieldNumberList.get(ii);
            LOG.error(errorMessage);
            getListener().addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR, ProblemType.GENERAL_INFO, errorMessage),
                getTarget()));
            columnsWarningFlagList.set(ii, Boolean.TRUE);
            break; // Get out of the loop since this is a serious error.
          }
        }

        String gapValue = record.substring(previousEndIndex, startIndex).trim(); // Get the value
                                                                                 // between one
                                                                                 // field ends and
                                                                                 // the next
                                                                                 // field start.
        LOG.debug("validateInBetweenFields:ii,previousEndIndex,startIndex,gapValue {},{},{},[{}]",
            ii, previousEndIndex, startIndex, gapValue);
        if (gapValue.length() > 0) {
          if (gapValue.length() == 1 && gapValue.equals(",")) {
            LOG.error(
                "validateInBetweenFields:Values in between gap of field number (starts with 1) {} and {} is a comma:",
                fieldNumberList.get(ii - 1), fieldNumberList.get(ii));
          } else {
            LOG.error(
                "validateInBetweenFields:Values in between gap of field number (starts with 1) {} and {} is non-blanks:[{}]",
                fieldNumberList.get(ii - 1), fieldNumberList.get(ii), gapValue);
          }
          if ((columnsWarningFlagList.get(ii) == Boolean.FALSE) && !this.reportedErrorFlag) {
            String errorMessage =
                "Unexpected alphanumeric characters found between fields in record "
                    + Long.toString(lineNumber) + ": " + " [" + gapValue + "]"
                    + " found between fields " + fieldNumberList.get(ii - 1) + " and "
                    + fieldNumberList.get(ii);
            LOG.error(errorMessage);
            getListener()
                .addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
                    ProblemType.CHARS_BETWEEN_FIELDS, errorMessage), getTarget()));
            columnsWarningFlagList.set(ii, Boolean.TRUE);
          }
        }
      }

      // Save where the current index ends for the next iteration.
      previousEndIndex = endIndex;
    }

    // If any of the columns had reported a warning/error, set the reportedErrorFlag
    // to true
    // so as not to overwhelm the error reporting mechanism.
    for (int ii = 0; ii < fieldNumberList.size(); ii++) {
      if (columnsWarningFlagList.get(ii) == Boolean.TRUE) {
        this.reportedErrorFlag = true;
      }
    }
  }
}
