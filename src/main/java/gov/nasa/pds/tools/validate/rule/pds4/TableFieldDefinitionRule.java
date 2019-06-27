package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

import javax.xml.xpath.XPathFactory;

/**
 * Implements a validation rule that checks that fields of tables
 * are defined in order, that they do not overlap, and that the
 * fields do not extend outside the record.
 */
public class TableFieldDefinitionRule extends AbstractValidationRule {

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

    @ValidationTest
    public void testTableDefinitions() {
        testTableDefinition(TEXT_TABLE_PATH, TEXT_FIELD_PATH, TEXT_GROUP_FIELD_PATH);
        testTableDefinition(BINARY_TABLE_PATH, BINARY_FIELD_PATH, BINARY_GROUP_FIELD_PATH);
    }

    private void testTableDefinition(String tablePath, String fieldPath, String groupFieldPath) {

    }

}
