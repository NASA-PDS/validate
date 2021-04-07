package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.label.ExceptionType;

import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.rule.ValidationTest;


// TODO: Find exact problem.
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;

import javax.xml.xpath.XPathFactory;

import net.sf.saxon.tree.tiny.TinyNodeImpl;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a validation rule that checks that fields of tables
 * are defined in order, that they do not overlap, and that the
 * fields do not extend outside the record.
 */
public class TableFieldDefinitionRule extends AbstractValidationRule {
    private static final Logger LOG = LoggerFactory.getLogger(TableFieldDefinitionRule.class);
    private static final String PDS4_NS = "http://pds.nasa.gov/pds4/pds/v1";

    private static final String NS_TEST = "[namespace-uri()='" + PDS4_NS + "']";

    private static final String TEXT_TABLE_PATH
        = "//*:Table_Character" + NS_TEST;

    private static final String TEXT_TABLE_RECORD_LENGTH_PATH
        = "*:Record_Character" + NS_TEST
        + "/*:record_length" + NS_TEST;

    private static final String TEXT_FIELD_PATH
        = "*:Record_Character" + NS_TEST
        + "/*:Field_Character" + NS_TEST;

    private static final String TEXT_GROUP_FIELD_PATH
        = "*:Record_Character" + NS_TEST
        + "/*:Group_Field_Character" + NS_TEST;

    private static final String BINARY_TABLE_PATH
        = "//*:Table_Binary" + NS_TEST;

    private static final String BINARY_TABLE_RECORD_LENGTH_PATH
        = "*:Record_Binary" + NS_TEST
        + "/*:record_length" + NS_TEST;

    private static final String BINARY_FIELD_PATH
        = "*:Record_Binary" + NS_TEST
        + "/*:Field_Binary" + NS_TEST;

    private static final String BINARY_GROUP_FIELD_PATH
        = "*:Record_Binary" + NS_TEST
        + "/*:Group_Field_Binary" + NS_TEST;

    private XPathFactory xPathFactory;


    // Used in checking for ASCII String related fields: should not have '+' in the format.
    private static final  Set<String> ASCII_STRING_TYPE_LIST = new HashSet<String>();
    static {
        ASCII_STRING_TYPE_LIST.add("ASCII_Short_String_Collapsed".toUpperCase());
        ASCII_STRING_TYPE_LIST.add("ASCII_Short_String_Preserved".toUpperCase());
        ASCII_STRING_TYPE_LIST.add("ASCII_String"                .toUpperCase());
        ASCII_STRING_TYPE_LIST.add("ASCII_Text_Collapsed"        .toUpperCase());
        ASCII_STRING_TYPE_LIST.add("ASCII_Text_Preserved"        .toUpperCase());
    }

    // Used in checking ASCII Number related fields: should not have '-' in the format.
    private static final Set<String> ASCII_NUMBER_TYPE_LIST = new HashSet<String>();
    static {
        ASCII_NUMBER_TYPE_LIST.add("ASCII_NonNegative_Integer".toUpperCase());
        ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base16".toUpperCase());
        ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base2".toUpperCase());
        ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base8".toUpperCase());
        ASCII_NUMBER_TYPE_LIST.add("ASCII_Real".toUpperCase());
        ASCII_NUMBER_TYPE_LIST.add("ASCII_Integer".toUpperCase());
    }

    private static String PRODUCT_OBSERVATIONAL   = "Product_Observational";
    private static String FILE_AREA_OBSERVATIONAL = PRODUCT_OBSERVATIONAL + "/File_Area_Observational";
    private static String TABLE_CHARACTER         = FILE_AREA_OBSERVATIONAL + "/Table_Character";
    private static String RECORD_CHARACTER        = TABLE_CHARACTER + "/Record_Character";
    private static String RECORD_CHARACTER_FIELDS      = RECORD_CHARACTER + "/fields";
    private static String FIELD_CHARACTER              = RECORD_CHARACTER + "/Field_Character";
    private static String FIELD_CHARACTER_FIELD_FORMAT = FIELD_CHARACTER + "/field_format";
    private static String FIELD_CHARACTER_DESCRIPTION  = FIELD_CHARACTER + "/description";
    private static String FIELD_CHARACTER_TYPE         = FIELD_CHARACTER + "/data_type";
    private static String FIELD_CHARACTER_NUMBER       = FIELD_CHARACTER + "/field_number";

    private static String FIELD_FORMAT_SIMPLE = "field_format";  // Used to retrieve individual field from node and not document.
    private static String DATA_TYPE_SIMPLE    = "data_type";     // Used to retrieve individual field from node and not document.
    private static String FIELD_NUMBER_SIMPLE = "field_number";  // Used to retrieve individual field from node and not document.


    /**
     * Creates a new instance.
     */
    public TableFieldDefinitionRule() {
        xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
    }

    @Override
    public boolean isApplicable(String location) {
        // The rule is applicable if a label has been parsed.
        return getContext().containsKey(PDS4Context.LABEL_DOCUMENT);
    }

  private void validateAsciiStringFieldsFormat(List<String> fieldFormatList, List<String> fieldTypeList, List<String> fieldNumberList) {
    // ASCII String related fields should not have '+' in the format.

    int indexToList = 0;
    // Loop through all elements fieldFormatList.  If field is in ASCII_STRING_TYPE_LIST, check if prohibited '+' is in the format.
    for (String oneFieldFormat : fieldFormatList) {
        //LOG.debug("validateAsciiStringFieldsFormat:indexToList,oneFieldFormat,fieldTypeList.get(indexToList)  {},{},{}",indexToList,oneFieldFormat,fieldTypeList.get(indexToList));
        // Check for prohibited plus symbol '+' in format field : %+8s
        if (ASCII_STRING_TYPE_LIST.contains(fieldTypeList.get(indexToList).toUpperCase())) {
            if (oneFieldFormat.contains("%+")) {
                LOG.error("ASCII String related fields should not contain '+' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList));
                addTableProblem(ExceptionType.ERROR, 
                    ProblemType.INVALID_LABEL,
                    "ASCII String related fields should not contain '+' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList),
                    getTarget(),
                    -1,
                    -1);
            } 
       }
       indexToList += 1;
    }
  }

  private void validateAsciiNumberFieldsFormat(List<String> fieldFormatList, List<String> fieldTypeList, List<String> fieldNumberList) {
    // ASCII Number related fields should not have '-' in the format.

    int indexToList = 0;
    // Loop through all elements fieldFormatList.  If field is in ASCII_NUMBER_TYPE_LIST, check if prohibited '-' is in the format.
    for (String oneFieldFormat : fieldFormatList) {
        //LOG.debug("validateAsciiNumberFieldsFormat:indexToList,oneFieldFormat,fieldTypeList.get(indexToList)  {},{},{}",indexToList,oneFieldFormat,fieldTypeList.get(indexToList));
        // Check for prohibited minus symbol '-' in format field : %-2d
        if (ASCII_NUMBER_TYPE_LIST.contains(fieldTypeList.get(indexToList).toUpperCase())) {
            if (oneFieldFormat.contains("%-")) {
                LOG.error("ASCII Number related fields should not contain '-' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList));
                addTableProblem(ExceptionType.ERROR, 
                    ProblemType.INVALID_LABEL,
                    "ASCII Number related fields should not contain '-' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList),
                    getTarget(),
                    -1,
                    -1);
            }
       }
       indexToList += 1;
    }
  }

  private void validateFieldFormats() {
     // The field 'field_format' need to checked for conformant to standard:
     // Example of valid field_format for ASCII_String (left justified):
     //
     //     <data_type>ASCII_String</data_type>
     //     <field_length unit="byte">26</field_length>
     //     <field_format>%-26s</field_format>
     //
     // Example of an invalid field_format for ASCII_String (right justified):
     //          <data_type>ASCII_String</data_type>
     //          <field_length unit="byte">8</field_length>
     //          <field_format>%+8s</field_format>
     //          <description>Number of the asteroid.</description>
     int numFields = 0;

     try {
        XMLExtractor extractor = new XMLExtractor(getTarget());
        TinyNodeImpl productObservationalNode = extractor.getNodeFromDoc(PRODUCT_OBSERVATIONAL);
        TinyNodeImpl tableCharacterNode       = extractor.getNodeFromDoc(TABLE_CHARACTER);
        TinyNodeImpl recordCharacterNode      = extractor.getNodeFromDoc(RECORD_CHARACTER); 

        // If any of the nodes are null, cannot continue.  Not all labels are expected to contain the PRODUCT_OBSERVATIONAL nodes.
        if (productObservationalNode == null ||
            tableCharacterNode       == null ||
            recordCharacterNode      == null) { 
            LOG.info("Label " + getTarget() + " does not contain any fields pertaining to " + TABLE_CHARACTER + " or " + RECORD_CHARACTER + " to valid ASCII field formats on");
            return;
        }

        // At this point, all nodes are valid and should be able to perform validation on. 

        numFields = Integer.parseInt(extractor.getValueFromDoc(RECORD_CHARACTER_FIELDS));
        //LOG.debug("numFields,getTarget() {}",numFields,getTarget());
        LOG.info("validateFieldFormats:target,recordCharacterNode {},{}",getTarget(),recordCharacterNode);

        // New way of extracting fields in the 'Field_Character' node since not every tag is required to be present for every node.
        // This new way guarantees all the lists are the same size make it easy to index between lists.

        List<String> fieldFormatList = new ArrayList<String>();
        List<String> fieldTypeList   = new ArrayList<String>();
        List<String> fieldNumberList = new ArrayList<String>();
        List<TinyNodeImpl> FieldCharacterNodeList = extractor.getNodesFromDoc(FIELD_CHARACTER);
        String fieldValue = ""; 

        // Note that the function getValueFromItem() returns an empty string if the field we seek is not present in the node.
        for (TinyNodeImpl node : FieldCharacterNodeList) {
            fieldValue = extractor.getValueFromItem(FIELD_NUMBER_SIMPLE,node);
            fieldNumberList.add(fieldValue);
            fieldValue = extractor.getValueFromItem(FIELD_FORMAT_SIMPLE,node);
            fieldFormatList.add(fieldValue);
            fieldValue = extractor.getValueFromItem(DATA_TYPE_SIMPLE,node);
            fieldTypeList.add(fieldValue);
        }

        LOG.debug("validateFieldFormats:fieldFormatList {}",fieldFormatList);
        LOG.debug("validateFieldFormats:fieldTypeList {}",fieldTypeList);
        LOG.debug("validateFieldFormats:fieldNumberList{}",fieldNumberList);

        // The field 'field_format' is optional so the value of fieldFormatList.size() can be zero.
        // Only perform a check on the field format if it is available and everything matches.

        if (fieldFormatList.size() > 0) {
            // Check to make sure all arrays are the same size as numFields.
            if (numFields != fieldFormatList.size() ||
                numFields != fieldTypeList.size()   ||
                numFields != fieldNumberList.size()) {
              String errorMessage = ("The number of fields " + Integer.toString(numFields) + " do not match with fields provided: fieldFormatList.size() " + 
                         Integer.toString(fieldFormatList.size()) + ",fieldTypeList.size() " + Integer.toString(fieldTypeList.size()) + ",fieldNumberList.size() " +
                         Integer.toString(fieldNumberList.size()));
              LOG.error(errorMessage);
              getListener().addProblem(
                  new ValidationProblem(new ProblemDefinition(
                      ExceptionType.ERROR,
                          ProblemType.INVALID_LABEL,
                      errorMessage),
                  getTarget()));
              return;
            }

            // Validate both ASCIII String and ASCII Number fields format.
            this.validateAsciiStringFieldsFormat(fieldFormatList, fieldTypeList, fieldNumberList);
            this.validateAsciiNumberFieldsFormat(fieldFormatList, fieldTypeList, fieldNumberList);
        }

     } catch (Exception e) {
        LOG.error("Cannot extract {} from label {}",RECORD_CHARACTER,getTarget());
          getListener().addProblem(
              new ValidationProblem(new ProblemDefinition(
                  ExceptionType.ERROR,
                  ProblemType.INVALID_LABEL,
                  "Cannot extract " + RECORD_CHARACTER + " from label"),
              getTarget()));
     }

  }

    @ValidationTest
    public void testTableDefinitions() {
        testTableDefinition(TEXT_TABLE_PATH, TEXT_FIELD_PATH, TEXT_GROUP_FIELD_PATH);
        testTableDefinition(BINARY_TABLE_PATH, BINARY_FIELD_PATH, BINARY_GROUP_FIELD_PATH);

        //   
        // Validate all fields to make sure they are not using prohibited '+' or '-' in certain fields.
        //   

        this.validateFieldFormats();

    }

    private void testTableDefinition(String tablePath, String fieldPath, String groupFieldPath) {
        LOG.debug("testTableDefinition:tablePath,fieldPath,groupFieldPath {},{},{}",tablePath,fieldPath,groupFieldPath);
    }

  /**
   * Adds a table-related exception to the ProblemListener.
   * 
   * @param exceptionType The exception type.
   * @param message The message.
   * @param dataFile The data file associated with the exception.
   * @param table The index of the table associated with the exception.
   * @param record The index of the record associated with the exception.
   */
  private void addTableProblem(ExceptionType exceptionType,
      ProblemType problemType, String message, URL dataFile, int table,
      int record) {
    addTableProblem(exceptionType, problemType, message, dataFile, table,
        record, -1);
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
  private void addTableProblem(ExceptionType exceptionType,
      ProblemType problemType, String message,
      URL dataFile, int table, int record, int field) {
    getListener().addProblem(
        new TableContentProblem(exceptionType,
            problemType,
            message,
            dataFile,
            getTarget(),
            table,
            record,
            field));
  }
}
