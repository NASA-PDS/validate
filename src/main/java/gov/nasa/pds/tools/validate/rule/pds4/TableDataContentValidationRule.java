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
import java.net.URISyntaxException;
import java.net.URL;
import net.sf.saxon.tree.tiny.TinyNodeImpl;
import java.nio.BufferUnderflowException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import gov.nasa.arc.pds.xml.generated.Array;
import gov.nasa.arc.pds.xml.generated.FileArea;
import gov.nasa.arc.pds.xml.generated.FileAreaAncillary;
import gov.nasa.arc.pds.xml.generated.FileAreaBrowse;
import gov.nasa.arc.pds.xml.generated.FileAreaInventory;
import gov.nasa.arc.pds.xml.generated.FileAreaObservational;
import gov.nasa.arc.pds.xml.generated.FileAreaSIPDeepArchive;
import gov.nasa.arc.pds.xml.generated.FileAreaTransferManifest;
import gov.nasa.arc.pds.xml.generated.Header;
import gov.nasa.arc.pds.xml.generated.InformationPackageComponent;
import gov.nasa.arc.pds.xml.generated.Inventory;
import gov.nasa.arc.pds.xml.generated.Product;
import gov.nasa.arc.pds.xml.generated.ProductAIP;
import gov.nasa.arc.pds.xml.generated.ProductAncillary;
import gov.nasa.arc.pds.xml.generated.ProductBrowse;
import gov.nasa.arc.pds.xml.generated.ProductCollection;
import gov.nasa.arc.pds.xml.generated.ProductObservational;
import gov.nasa.arc.pds.xml.generated.ProductSIPDeepArchive;
import gov.nasa.arc.pds.xml.generated.TableBinary;
import gov.nasa.arc.pds.xml.generated.TableCharacter;
import gov.nasa.arc.pds.xml.generated.TableDelimited;
import gov.nasa.pds.label.object.TableRecord;
import gov.nasa.pds.objectAccess.ObjectAccess;
import gov.nasa.pds.objectAccess.ObjectProvider;
import gov.nasa.pds.objectAccess.ParseException;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.objectAccess.TableReader;
import gov.nasa.pds.objectAccess.DataType.NumericDataType;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.SourceLocation;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.XPaths;
import gov.nasa.pds.tools.validate.content.table.FieldContentFatalException;
import gov.nasa.pds.tools.validate.content.table.FieldValueValidator;
import gov.nasa.pds.tools.validate.content.table.RawTableReader;
import gov.nasa.pds.tools.validate.content.table.TableContentProblem;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import gov.nasa.pds.validate.constants.Constants;

 /**
  * Class that does data content validation of tables. 
  * 
  * @author mcayanan
  *
  */
public class TableDataContentValidationRule extends AbstractValidationRule {
  private static final Logger LOG = LoggerFactory.getLogger(TableDataContentValidationRule.class);
  private int PROGRESS_COUNTER = 0;

  /** Used in checking for ASCII String related fields: should not have '+' in the format. */
  private static final  Set<String> ASCII_STRING_TYPE_LIST = new HashSet<String>(); 
  static {
      ASCII_STRING_TYPE_LIST.add("ASCII_Short_String_Collapsed".toUpperCase());
      ASCII_STRING_TYPE_LIST.add("ASCII_Short_String_Preserved".toUpperCase());
      ASCII_STRING_TYPE_LIST.add("ASCII_String"                .toUpperCase());
      ASCII_STRING_TYPE_LIST.add("ASCII_Text_Collapsed"        .toUpperCase());
      ASCII_STRING_TYPE_LIST.add("ASCII_Text_Preserved"        .toUpperCase());
  }

  /** Used in checking ASCII Number related fields: should not have '-' in the format. */
  private static final Set<String> ASCII_NUMBER_TYPE_LIST = new HashSet<String>();
  static {
      ASCII_NUMBER_TYPE_LIST.add("ASCII_NonNegative_Integer".toUpperCase());
      ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base16".toUpperCase());
      ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base2".toUpperCase());
      ASCII_NUMBER_TYPE_LIST.add("ASCII_Numeric_Base8".toUpperCase());
      ASCII_NUMBER_TYPE_LIST.add("ASCII_Real".toUpperCase());
      ASCII_NUMBER_TYPE_LIST.add("ASCII_Integer".toUpperCase());
  }

  private static String PRODUCT_OBSERVATIONAL = "Product_Observational";
  private static String TABLE_CHARACTER = "Product_Observational/File_Area_Observational/Table_Character";
  private static String RECORD_CHARACTER = "Product_Observational/File_Area_Observational/Table_Character/Record_Character";
  private static String RECORD_CHARACTER_FIELDS      = RECORD_CHARACTER + "/fields";
  private static String FIELD_CHARACTER              = RECORD_CHARACTER + "/Field_Character";
  private static String FIELD_CHARACTER_FIELD_FORMAT = FIELD_CHARACTER + "/field_format";
  private static String FIELD_CHARACTER_DESCRIPTION  = FIELD_CHARACTER + "/description";
  private static String FIELD_CHARACTER_TYPE         = FIELD_CHARACTER + "/data_type";
  private static String FIELD_CHARACTER_NUMBER       = FIELD_CHARACTER + "/field_number";

  /** Used in evaluating xpath expressions. */
  private XPathFactory xPathFactory;
  
  /** XPath to find descendant Packed_Data_Fields elements 
   *  from a given node. 
   */
  private static final String PACKED_DATA_FIELD_XPATH = 
      "descendant::*[name()='Packed_Data_Fields']";
  
  /** XPath to find child Table_Binary elements from a given node. */
  private static final String CHILD_TABLE_BINARY_XPATH = 
      "child::*[name()='Table_Binary']";
  
  /**
   * Creates a new instance.
   */
  public TableDataContentValidationRule() {
    xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
  }

  @Override
  public boolean isApplicable(String location) {
  	if (!getContext().getCheckData()) {
      return false;
    }
    boolean isApplicable = false;
    // The rule is applicable if a label has been parsed and tables exist
    // in the label.
    if (getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT,
          Document.class);
      
      DOMSource source = new DOMSource(label);      
      try {
        NodeList tables = (NodeList) xPathFactory.newXPath().evaluate(
            XPaths.TABLE_TYPES, source, XPathConstants.NODESET);
        if (tables.getLength() > 0) {
          isApplicable = true;
        }
      } catch (XPathExpressionException e) {
        //Ignore
      }
      
    }
    return isApplicable;
  }

  private void validateAsciiStringFieldsFormat(List<String> fieldFormatList, List<String> fieldTypeList, List<String> fieldNumberList) {
    // ASCII String related fields should not have '+' in the format.

    int indexToList = 0;
    // Loop through all elements fieldFormatList.  If field is in ASCII_STRING_TYPE_LIST, check if prohibited '+' is in the format.
    for (String oneFieldFormat : fieldFormatList) {
        //LOG.debug("validateAsciiStringFieldsFormat:indexToList,oneFieldFormat,fieldTypeList.get(indexToList)  {},{},{}",indexToList,oneFieldFormat,fieldTypeList.get(indexToList));
        // Check for prohibited plus symbol '+' in format field : %+8s
        if (this.ASCII_STRING_TYPE_LIST.contains(fieldTypeList.get(indexToList).toUpperCase())) {
            if (oneFieldFormat.contains("%+")) {
                LOG.error("ASCII String related fields should not contain '+' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList));
                addTableProblem(ExceptionType.ERROR, 
                    ProblemType.TABLE_FILE_READ_ERROR,
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
        if (this.ASCII_NUMBER_TYPE_LIST.contains(fieldTypeList.get(indexToList).toUpperCase())) {
            if (oneFieldFormat.contains("%-")) {
                LOG.error("ASCII Number related fields should not contain '-' symbol in field_format [" + oneFieldFormat + "] in field_number " + fieldNumberList.get(indexToList));
                addTableProblem(ExceptionType.ERROR, 
                    ProblemType.TABLE_FILE_READ_ERROR,
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
        TinyNodeImpl productObservationalNode = extractor.getNodeFromDoc(this.PRODUCT_OBSERVATIONAL);
        TinyNodeImpl tableCharacterNode       = extractor.getNodeFromDoc(this.TABLE_CHARACTER);
        TinyNodeImpl recordCharacterNode      = extractor.getNodeFromDoc(this.RECORD_CHARACTER); 

        // If any of the nodes are null, cannot continue.  Not all labels are expected to contain the PRODUCT_OBSERVATIONAL nodes.
        if (productObservationalNode == null ||
            tableCharacterNode       == null ||
            recordCharacterNode      == null) { 
            LOG.info("Label " + getTarget() + " does not contain any fields pertaining to " + this.TABLE_CHARACTER + " or " + this.RECORD_CHARACTER + " to valid ASCII field formats on");
            return;
        }

        // At this point, all nodes are valid and should be able to perform validation on. 

        numFields = Integer.parseInt(extractor.getValueFromDoc(this.RECORD_CHARACTER_FIELDS));
        //LOG.debug("numFields,getTarget() {}",numFields,getTarget());
        LOG.info("validateFieldFormats:target,recordCharacterNode {},{}",getTarget(),recordCharacterNode);

        List<String> fieldFormatList = extractor.getValuesFromItem(this.FIELD_CHARACTER_FIELD_FORMAT,recordCharacterNode.getRoot());
        //List<String> fieldDescriptionList = extractor.getValuesFromItem(this.FIELD_CHARACTER_DESCRIPTION,recordCharacterNode.getRoot());
        List<String> fieldTypeList = extractor.getValuesFromItem(this.FIELD_CHARACTER_TYPE,recordCharacterNode.getRoot());
        List<String> fieldNumberList = extractor.getValuesFromItem(this.FIELD_CHARACTER_NUMBER,recordCharacterNode.getRoot());

        //LOG.debug("fieldFormatList {},{}",fieldFormatList,fieldFormatList.size());
        //LOG.debug("fieldDescriptionList {},{}",fieldDescriptionList,fieldDescriptionList.size());
        //LOG.debug("fieldTypeList {},{}",fieldTypeList,fieldTypeList.size());
        //LOG.debug("fieldNumberList {},{}",fieldNumberList,fieldNumberList.size());

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

     } catch (Exception e) {
        LOG.error("Cannot extract {} from label {}",this.RECORD_CHARACTER,getTarget());
          getListener().addProblem(
              new ValidationProblem(new ProblemDefinition(
                  ExceptionType.ERROR,
                  ProblemType.INVALID_LABEL,
                  "Cannot extract " + this.RECORD_CHARACTER + " from label"),
              getTarget()));
     }
  }
   
  @ValidationTest
  public void validateTableDataContents() throws MalformedURLException, 
  URISyntaxException {
	  
    LOG.debug("Entering validateTableDataContents");
    LOG.debug("validateTableDataContents:getTarget() {}",getTarget());
    ObjectProvider objectAccess = null;
    objectAccess = new ObjectAccess(getTarget());
    int spotCheckData = getContext().getSpotCheckData();
    List<FileArea> tableFileAreas = new ArrayList<FileArea>();
    try {
      tableFileAreas = getTableFileAreas(objectAccess);
    } catch (ParseException pe) {
      getListener().addProblem(
          new ValidationProblem(new ProblemDefinition(
              ExceptionType.ERROR,
              ProblemType.INVALID_LABEL,
              "Error occurred while trying to get the table file areas: "
                  + pe.getCause().getMessage()),
          getTarget()));
      return;
    }
    LOG.debug("validateTableDataContents:tableFileAreas.size() {}",tableFileAreas.size());
    NodeList fileAreaNodes = null;
    XPath xpath = xPathFactory.newXPath();
    LOG.debug("validateTableDataContents:xpath {}",xpath);
    try {
      fileAreaNodes = (NodeList) xpath.evaluate(
        XPaths.TABLE_FILE_AREAS, 
        new DOMSource(getContext().getContextValue(
            PDS4Context.LABEL_DOCUMENT, Document.class)), 
        XPathConstants.NODESET);
        LOG.debug("validateTableDataContents:fileAreaNodes {}",fileAreaNodes);
        LOG.debug("validateTableDataContents:fileAreaNodes.getLength() {}",fileAreaNodes.getLength());
    } catch (XPathExpressionException e) {
      addXPathException(null, XPaths.TABLE_FILE_AREAS, e.getMessage());
    }

    //
    // Validate all fields to make sure they are not using prohibited '+' or '-' in certain fields.
    //

    this.validateFieldFormats();

    Map<String, Integer> numTables = scanTables(tableFileAreas, objectAccess);
    Map<URL, Integer> tableIndexes = new LinkedHashMap<URL, Integer>();
    LOG.debug("validateTableDataContents:numTables {}",numTables.size());
    LOG.debug("validateTableDataContents:tableIndexes {}",tableIndexes.size());
    int fileAreaObserveIndex = 0;
    for (FileArea fileArea : tableFileAreas) {
      int tableIndex = -1;
      String fileName = getDataFile(fileArea);
      URL dataFile = new URL(Utility.getParent(getTarget()), fileName);
      LOG.debug("validateTableDataContents:fileAreaObserveIndex,fileName,dataFile {},{},{}",fileAreaObserveIndex,fileName,dataFile);
      if (tableIndexes.containsKey(dataFile)) {
        tableIndex = tableIndexes.get(dataFile).intValue() + 1;
        tableIndexes.put(dataFile, new Integer(tableIndex));
      } else {
        tableIndex = 1;
        tableIndexes.put(dataFile, new Integer(1));
      }
      LOG.debug("validateTableDataContents:tableIndex,fileAreaObserveIndex,fileName,dataFile {},{},{}",tableIndex,fileAreaObserveIndex,fileName,dataFile);
      NodeList binaryTableNodes = null;
      try {
        binaryTableNodes = (NodeList) xpath.evaluate(CHILD_TABLE_BINARY_XPATH, 
            fileAreaNodes.item(fileAreaObserveIndex), XPathConstants.NODESET);
      } catch (XPathExpressionException xe) {
        addXPathException(fileAreaNodes.item(fileAreaObserveIndex), 
            CHILD_TABLE_BINARY_XPATH, xe.getMessage());
      }
      LOG.debug("validateTableDataContents:binaryTableNodes.getLength() {}",binaryTableNodes.getLength());
      
      // issue_234: Validate gives incorrect records mismatch WARNING for interleaved data objects 
      // need to subtract many header objects from total filesize 
      List<Object> headerObjects = objectAccess.getHeaderObjects(fileArea);
      int headerSize = 0;
      LOG.debug("validateTableDataContents:headerObjects.size {}",headerObjects.size());
      for (Object header: headerObjects) {
    	  Header hdr = (Header)header;
    	  headerSize += hdr.getObjectLength().getValue().intValueExact();
          LOG.debug("validateTableDataContents:headerObjects.size(),headerSize,hdr.getObjectLength().getValue().intValueExact() {},{},{}",headerObjects.size(),headerSize,hdr.getObjectLength().getValue().intValueExact());
      }
      LOG.debug("validateTableDataContents:headerSize {}",headerSize);
 	  // issue_233 Product validation does not detect the number of table records correctly for Table + Array object
      List<Array> arrayObjects = objectAccess.getArrays(fileArea);
      long arraySize = 1;
      LOG.debug("validateTableDataContents:arrayObjects.size {}",arrayObjects.size());
      if (arrayObjects.size()>0) {
    	  for (Array array : arrayObjects) {
    		  String dataTypeStr = "";
    		  try {
    			  dataTypeStr = array.getElementArray().getDataType();
    			  NumericDataType dataType = Enum.valueOf(NumericDataType.class, dataTypeStr);
    			  // element size = bits/8
    			  arraySize = dataType.getBits()/8;
                  LOG.debug("validateTableDataContents:dataTypeStr,dataType,arraySize {},{},{}",dataTypeStr,dataType,arraySize);
    		  } catch (IllegalArgumentException a) {
    			  throw new IllegalArgumentException(dataTypeStr + " is not supported at this time.");
    		  }
          
    		  /* total size = number of lines * number of samples
    		   *             * number of bands * element size)
    		   */
    		  int[] dimensions = new int[array.getAxisArraies().size()];
    		  for (int i = 0; i < dimensions.length; i++) {
    			  dimensions[i] = array.getAxisArraies().get(i).getElements().intValueExact();
                  LOG.debug("validateTableDataContents:i,dimensions[i],arraySize {},{},{}",i,dimensions[i],arraySize);
    			  arraySize *= dimensions[i];
    		  } 
              LOG.debug("validateTableDataContents:post_loop:arraySize {}",arraySize);
    	  }
      }
      else arraySize = 0;

      List<Object> tableObjects = objectAccess.getTableObjects(fileArea);
      FieldValueValidator fieldValueValidator = new FieldValueValidator(getListener());  
      long definedTotalRecords = 0, actualTotalRecords = 0, actualRecordNumber = 0, recordsToRemove = 0;
      
      // Get total record size first before validation
      // issue_220 & issue_219
      try {
    	  TableReader tmpReader = new TableReader(tableObjects.get(0), dataFile, false, false);
      	  // issue_233 Product validation does not detect the number of table records correctly for Table + Array object
    	  // set records size with the table index 1
    	  actualTotalRecords = actualRecordNumber = tmpReader.getRecordSize(dataFile, tableObjects.get(0));
      }
      catch (InvalidTableException ex) {
    	 //ex.printStackTrace();
      }
      catch (Exception ex) { 
    	 //ex.printStackTrace();
      }

      LOG.debug("validateTableDataContents:arraySize,actualTotalRecords {},{}",arraySize,actualTotalRecords);
      LOG.debug("validateTableDataContents:dataFile,tableObjects.size() {},{}",dataFile,tableObjects.size());
      for (Object table : tableObjects) {
        RawTableReader reader = null;
        try {
          reader = new RawTableReader(table, dataFile, getTarget(), 
              tableIndex, false);
        } 
        catch (InvalidTableException ex) {
        	addTableProblem(ExceptionType.ERROR, 
                    ProblemType.TABLE_FILE_READ_ERROR,
                    "" + ex.getMessage(),
                    dataFile,
                    tableIndex,
                    -1);
                tableIndex++;
                continue;
        }
        catch (Exception ex) { 
          addTableProblem(ExceptionType.ERROR, 
              ProblemType.TABLE_FILE_READ_ERROR,
              "Error occurred while trying to read table: " + ex.getMessage(),
              dataFile,
              tableIndex,
              -1);
          tableIndex++;
          continue;
        }
        int recordLength = -1;
        int recordMaxLength = -1;
        int definedNumRecords = -1;
        boolean inventoryTable = false;
        LOG.debug("validateTableDataContents:tableIndex {}",tableIndex);
        //Check if record length is equal to the defined record length in
        // the label (or maximum length)
        if (table instanceof TableDelimited) {
          LOG.debug("validateTableDataContents:table instanceof TableDelimited");
          TableDelimited td = (TableDelimited) table;
          if (td.getRecordDelimited() != null &&
              td.getRecordDelimited().getMaximumRecordLength() != null) {
            recordMaxLength = td.getRecordDelimited()
                .getMaximumRecordLength().getValue().intValueExact();
          }
          definedNumRecords = td.getRecords().intValueExact();
          recordsToRemove = (long)definedNumRecords;
          if (td instanceof Inventory) {
                LOG.debug("validateTableDataContents:table instanceof Inventory");
        	    inventoryTable = true;
          }
          actualRecordNumber = actualTotalRecords;
        } else if (table instanceof TableBinary) {
          LOG.debug("validateTableDataContents:table instanceof TableBinary");
          TableBinary tb = (TableBinary) table;
          if (tb.getRecordBinary() != null &&
              tb.getRecordBinary().getRecordLength() != null) {
            recordLength = tb.getRecordBinary().getRecordLength().getValue().intValueExact();
          }
          definedNumRecords = tb.getRecords().intValueExact();
          if (binaryTableNodes != null) {
            validatePackedFields(binaryTableNodes.item(tableIndex-1));
          }
          LOG.debug("validateTableDataContents:recordLength,definedNumRecords,binaryTableNodes,tableObjects.size() {},{},{},{}",recordLength,definedNumRecords,binaryTableNodes,tableObjects.size());
          if (tableObjects.size()==1) {
        	 actualTotalRecords = actualTotalRecords - arraySize - headerSize;
        	 actualRecordNumber = actualTotalRecords/recordLength;
              LOG.debug("validateTableDataContents:actualTotalRecords,actualRecordNumber {},{}",actualTotalRecords,actualRecordNumber);
          }
          else {
        	  // issue_243: Fix for choke on a probably good file
        	  // record size for the table index i
        	  long actualRecordSize = actualTotalRecords - reader.getOffset();
        	  actualRecordNumber = actualRecordSize/recordLength;
          }
        } else {
          LOG.debug("table instanceof TableCharacter: else");
          TableCharacter tc = (TableCharacter) table;
          LOG.debug("tc.getRecordCharacter() {}",tc.getRecordCharacter());
          LOG.debug("tc.getRecordCharacter().getRecordLength() {}",tc.getRecordCharacter().getRecordLength());
          LOG.debug("tc.getRecordDelimiter() [{}]",tc.getRecordDelimiter());
          if (tc.getRecordCharacter() != null && 
              tc.getRecordCharacter().getRecordLength() != null) {
            recordLength = tc.getRecordCharacter().getRecordLength().getValue().intValueExact();
          }
          definedNumRecords = tc.getRecords().intValueExact();
          recordsToRemove = (long)definedNumRecords;
          try {
             actualRecordNumber = reader.getRecordSize(dataFile, table);
          }
          catch (Exception e) {}        
          LOG.debug("recordLength,definedNumRecords,recordsToRemove,actualRecordNumber {},{},{},{}",recordLength,definedNumRecords,recordsToRemove,actualRecordNumber);
        } 
        
        { // issue_220
        	definedTotalRecords += definedNumRecords;  
        	if (tableObjects.size()==tableIndex) {   		
        		String message = "Number of records read is not equal "
        				+ "to the defined number of records in the label (expected "
        				+ definedNumRecords + ", got " + actualRecordNumber + ").";
        		if (actualRecordNumber<definedNumRecords) {
        			addTableProblem(ExceptionType.ERROR,
        					ProblemType.RECORDS_MISMATCH,
        					message,
        					dataFile,
        					tableIndex,
        					-1);  
        			break;
        		}
        		/*
        		 * issue_234: Validate gives incorrect records mismatch WARNING for interleaved data objects 
        		else {
        		  if (actualRecordNumber>definedNumRecords) {
        			addTableProblem(ExceptionType.WARNING,
        					ProblemType.RECORDS_MISMATCH,
        					message,
        					dataFile,
        					tableIndex,
        					-1);  
        		  }
        		}   
        		*/		
        	}
        }
   
        TableRecord record = null;
        try {
          if (table instanceof TableBinary) {
            try {
              record = reader.readNext();
              while (record != null) {
                progressCounter();

                try {
                    fieldValueValidator.validate(record, reader.getFields(), false);
                } catch (FieldContentFatalException e) {
                    // If we get a fatal error, we can avoid an overflow of error output
                    // by killing the loop through all the table records
                    break;
                }
                if (spotCheckData != -1) {
                  try {
                    record = reader.getRecord(reader.getCurrentRow() + spotCheckData);
                  } catch (IllegalArgumentException iae) {
                    record = null;
                  } catch (IOException io) {
                    throw new IOException("Error occurred "
                        + "while reading table '" + tableIndex + "', "
                        + "record '"
                        + (reader.getCurrentRow() + spotCheckData) + "'");
                  }
                } else {
                  record = reader.readNext();
                }
              }
            } catch (BufferUnderflowException be) {
              throw new IOException(
                  "Unexpected end-of-file reached while reading table '"
                      + tableIndex
                      + "', record '" + reader.getCurrentRow() + "'");
            }
          } else {  // We have either a character or delimited table
            boolean manuallyParseRecord = false;
            String line = reader.readNextLine();
            while (line != null) {
              progressCounter();
              
              if (!line.endsWith("\r\n")) {
                addTableProblem(ExceptionType.ERROR,
                    ProblemType.MISSING_CRLF,
                    "Record does not end in carriage-return line feed.", 
                    dataFile, tableIndex, reader.getCurrentRow());
                manuallyParseRecord = true;
              } else {
                addTableProblem(ExceptionType.DEBUG,
                    ProblemType.CRLF_DETECTED,
                    "Record ends in carriage-return line feed.",
                    dataFile,
                    tableIndex,
                    reader.getCurrentRow());              
              }
              if (recordLength != -1) {
                if (line.length() != recordLength) {
                  addTableProblem(ExceptionType.ERROR,
                      ProblemType.RECORD_LENGTH_MISMATCH,
                      "Record does not equal the defined record length "
                          + "(expected " + recordLength
                          + ", got " + line.length() + ").",
                      dataFile,
                      tableIndex,
                      reader.getCurrentRow()); 
                  manuallyParseRecord = true;
                } else {
                  addTableProblem(ExceptionType.DEBUG,
                      ProblemType.RECORD_MATCH,
                      "Record equals the defined record length "
                          + "(expected " + recordLength
                          + ", got " + line.length() + ").",
                      dataFile,
                      tableIndex,
                      reader.getCurrentRow());              
                }
              }
              if (recordMaxLength != -1) {
                if (line.length() > recordMaxLength) {
                  addTableProblem(ExceptionType.ERROR,
                      ProblemType.RECORD_LENGTH_MISMATCH,
                      "Record length exceeds the max defined record length "
                          + "(expected " + recordMaxLength
                          + ", got " + line.length() + ").",
                      dataFile,
                      tableIndex,
                      reader.getCurrentRow());
                  manuallyParseRecord = true;
                } else {
                  addTableProblem(ExceptionType.DEBUG,
                      ProblemType.GOOD_RECORD_LENGTH,
                      "Record length is less than or equal to the max "
                          + "defined record length "
                          + "(max " + recordMaxLength
                          + ", got " + line.length() + ").",
                      dataFile,
                      tableIndex,
                      reader.getCurrentRow());
                }
              }
              // Need to manually parse the line if we're past the defined
              // number of records. The PDS4-Tools library won't let us
              // read past the defined number of records.
              if (reader.getCurrentRow() > definedNumRecords) {
                manuallyParseRecord = true;
              }
              try {
                if (manuallyParseRecord && !(table instanceof TableDelimited)) {
                  record = reader.toRecord(line, reader.getCurrentRow());
                } else {
                	//System.out.println("TableDataContentValidationRule...... reader.getCurrentRow() = " + reader.getCurrentRow());
                  record = reader.getRecord(reader.getCurrentRow());
                }
                //Validate fields within the record here
                try {
                    fieldValueValidator.validate(record, reader.getFields());
                } catch (FieldContentFatalException e) {
                    // If we get a fatal error, we can avoid an overflow of error output
                    // by killing the loop through all the table records
                    break;
                }

                //Validate collection inventory member status
                if (inventoryTable) {
                	  Map<String, Integer> fieldMap = reader.getFieldMap();
                	  String memberStatus = null;
                	  if (fieldMap.containsKey("Member_Status")) {
                	    memberStatus = record.getString(
                	        fieldMap.get("Member_Status").intValue());
                	  }else if (fieldMap.containsKey("Member Status")) {
                	    memberStatus = record.getString(
                	        fieldMap.get("Member Status").intValue());
                	  }
                	  if (!memberStatus.startsWith("P") &&
                      !memberStatus.startsWith("S")) {
                    addTableProblem(ExceptionType.ERROR,
                        ProblemType.INVALID_MEMBER_STATUS,
                        "Invalid member status " + memberStatus +
                            ".  It should begin with 'P' or 'S'.",
                        dataFile,
                        tableIndex,
                        reader.getCurrentRow());
                	  }
                }
              } catch (IOException io) {
                //An exception here means that the number of fields read in
                //did not equal the number of fields exepected
                addTableProblem(ExceptionType.ERROR,
                    ProblemType.FIELDS_MISMATCH,
                    io.getMessage(),
                    dataFile,
                    tableIndex,
                    reader.getCurrentRow());                
              }
              
              if (spotCheckData > 1) {
                  // If spot checking is turned on, we want to skip to
                  // the record just before the one we want to read
                  try {
                    record = reader.getRecord(reader.getCurrentRow() + (spotCheckData - 1));
                  } catch (IllegalArgumentException iae) {
                    line = null;
                    continue;
                  } catch (IOException io) {
                    throw new IOException("Error occurred "
                        + "while reading table '" + tableIndex + "', "
                        + "record '"
                        + (reader.getCurrentRow() + spotCheckData) + "'");
                  }
              }
              
              // WARNING: All checks must happen before this in case we
              // reach end of the table
              if (table instanceof TableDelimited && 
                  definedNumRecords == reader.getCurrentRow()) {
                break;
              }
              line = reader.readNextLine();
            }
            // only give error message when the actual record number is smaller than the defined in the label
            if (definedNumRecords != -1 && 
                (definedNumRecords>reader.getCurrentRow()) && spotCheckData == -1) {
              String message = "Number of records read is not equal "
                + "to the defined number of records in the label (expected "
                + definedNumRecords + ", got " + reader.getCurrentRow() + ").";
              addTableProblem(ExceptionType.ERROR,
                  ProblemType.RECORDS_MISMATCH,
                  message,
                  dataFile,
                  tableIndex,
                  -1);
            } 
          }
        } catch (IOException io) {
          // Error occurred while reading the data file
          addTableProblem(ExceptionType.FATAL,
              ProblemType.TABLE_FILE_READ_ERROR,
              "Error occurred while reading the data file: " + io.getMessage(),
              dataFile,
              -1,
              -1);
        }
        actualRecordNumber -= recordsToRemove;
        tableIndex++;
        LOG.debug("recordLength,definedNumRecords,recordsToRemove,actualRecordNumber {},{},{},{}",recordLength,definedNumRecords,recordsToRemove,actualRecordNumber);
      }
      fileAreaObserveIndex++;
    }
    LOG.debug("Leaving validateTableDataContents");
//System.exit(0);
  }

  /**
   * Creates a mapping to detect the number of tables found in
   * each data file defined in the label.
   *
   * @param fileAreas The file areas.
   * @param objectAccess An ObjectProvider.
   * 
   * @return A mapping of data files to number of tables.
   */
  private Map<String, Integer> scanTables(List<FileArea> fileAreas, 
      ObjectProvider objectAccess) {
    Map<String, Integer> results = new LinkedHashMap<String, Integer>();
    for (FileArea fileArea : fileAreas) {
      String dataFile = getDataFile(fileArea);
      List<Object> tables = objectAccess.getTableObjects(fileArea);
      Integer numTables = results.get(dataFile);
      if (numTables == null) {
        results.put(dataFile, new Integer(tables.size()));
      } else {
        numTables = results.get(dataFile);
        numTables = new Integer(numTables.intValue() + tables.size());
        results.put(dataFile, numTables);
      }
    }
    return results;
  }
  
  /**
   * Gets the FileArea(s) found within the given Object.
   * 
   * @param objectAccess The object containing the FileArea(s).
   * 
   * @return A list of the FileAreas found.
   * 
   * @throws ParseException If an error occured while getting the Product.
   */
  private List<FileArea> getTableFileAreas(ObjectProvider objectAccess) 
      throws ParseException {
    List<FileArea> results = new ArrayList<FileArea>();
    Product product = objectAccess.getProduct(getTarget(), Product.class);
    if (product instanceof ProductObservational) {
      results.addAll(((ProductObservational) product)
          .getFileAreaObservationals());
    } else if (product instanceof ProductSIPDeepArchive) {
      ProductSIPDeepArchive psda = (ProductSIPDeepArchive) product;
      if (psda.getInformationPackageComponentDeepArchive() != null && 
          psda.getInformationPackageComponentDeepArchive()
          .getFileAreaSIPDeepArchive() != null) {
        results.add(psda.getInformationPackageComponentDeepArchive()
            .getFileAreaSIPDeepArchive());
      }
    } else if (product instanceof ProductAIP) {
      ProductAIP pa = (ProductAIP) product;
      for ( InformationPackageComponent ipc : 
        pa.getInformationPackageComponents()) {
        if (ipc.getFileAreaTransferManifest() != null) {
          results.add(ipc.getFileAreaTransferManifest());
        }
      }
    } else if (product instanceof ProductCollection) {
      ProductCollection pc = (ProductCollection) product;
      if (pc.getFileAreaInventory() != null &&
        pc.getFileAreaInventory().getInventory() != null) {
        results.add(pc.getFileAreaInventory());
      }
    } else if (product instanceof ProductAncillary) {
    	ProductAncillary pa = (ProductAncillary) product;
    	if (pa.getFileAreaAncillaries() != null &&
    		pa.getFileAreaAncillaries().size()>0)
    	  results.addAll(pa.getFileAreaAncillaries());
    } else if (product instanceof ProductBrowse) {
      results.addAll(((ProductBrowse) product).getFileAreaBrowses());
    }
    return results;
  }
  
  /**
   * Gets the data file within the given FileArea.
   * 
   * @param fileArea The FileArea.
   * 
   * @return The data file name found within the given FileArea.
   */
  private String getDataFile(FileArea fileArea) {
    String result = "";
    if (fileArea instanceof FileAreaObservational) {
      result = ((FileAreaObservational) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaSIPDeepArchive) {
      result = ((FileAreaSIPDeepArchive) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaTransferManifest) {
      result = ((FileAreaTransferManifest) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaInventory) {
      result = ((FileAreaInventory) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaBrowse) {
      result = ((FileAreaBrowse) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaAncillary) {
      result = ((FileAreaAncillary) fileArea).getFile().getFileName();
    }
    return result;
  }
  
  /**
   * Checks to see that the Packed_Data_Field elements are defined correctly
   * in the label.
   * 
   * @param binaryTable The Table_Binary node.
   */
  private void validatePackedFields(Node binaryTable) {
    LOG.debug("Entering validatePackedField");
    NodeList packedDataFields = null;
    try {
      packedDataFields = (NodeList) xPathFactory.newXPath().evaluate(
          PACKED_DATA_FIELD_XPATH, binaryTable, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      addXPathException(binaryTable, PACKED_DATA_FIELD_XPATH, e.getMessage());
    }
    LOG.debug("validatePackedFields:PACKED_DATA_FIELD_XPATH,XPathConstants.NODESET,XPathConstants.NODE {},{},{}",PACKED_DATA_FIELD_XPATH,XPathConstants.NODESET,XPathConstants.NODE);
    LOG.debug("validatePackedFields:packedDataFields.getLength() {}",packedDataFields.getLength());
    for (int i = 0; i < packedDataFields.getLength(); i++) {
      Node packedDataField = packedDataFields.item(i);
      SourceLocation nodeLocation = 
          (SourceLocation) packedDataField.getUserData(
              SourceLocation.class.getName());
      try {
        NodeList fieldBits = (NodeList) xPathFactory.newXPath()
            .evaluate("child::*[name()='Field_Bit']", packedDataField, 
                XPathConstants.NODESET);
        try {
          Number definedBitFields = (Number) xPathFactory.newXPath()
              .evaluate("child::*[name()='bit_fields']/text()", 
                  packedDataField, XPathConstants.NUMBER);
          LOG.debug("validatePackedFields:i,packedDataFields.getLength(),definedBitFields.intValue(),fieldBits.getLength() {},{},{},{}",i,packedDataFields.getLength(),definedBitFields.intValue(),fieldBits.getLength());
          if ( definedBitFields.intValue() != fieldBits.getLength()) {
            getListener().addProblem(
                new ValidationProblem(new ProblemDefinition(
                    ExceptionType.ERROR,
                    ProblemType.BIT_FIELD_MISMATCH,
                    "Number of 'Field_Bit' elements does not equal the "
                        + "'bit_fields' value in the label (expected "
                        + definedBitFields.intValue()
                        + ", got " + fieldBits.getLength() + ")."),
                    getTarget(),
                    nodeLocation.getLineNumber(),
                    -1));
          } else {
            getListener().addProblem(
                new ValidationProblem(new ProblemDefinition(
                    ExceptionType.DEBUG,
                    ProblemType.BIT_FIELD_MATCH,
                    "Number of 'Field_Bit' elements equals the "
                        + "'bit_fields' value in the label (expected "
                        + definedBitFields.intValue()
                        + ", got " + fieldBits.getLength() + ")."),
                    getTarget(),
                    nodeLocation.getLineNumber(),
                    -1));            
          }
        } catch (XPathExpressionException e) {
          addXPathException(packedDataField, 
              "child::*[name()='bit_fields']/text()", e.getMessage());
        }
      } catch (XPathExpressionException xe) {
        addXPathException(packedDataField, "child::*[name()='Field_Bit']",
            xe.getMessage());       
      }
    }
    LOG.debug("Leaving validatePackedFields");
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
  
  /**
   * Adds an XPath exception to the ProblemListener.
   * 
   * @param node The node associated with the XPath error.
   * @param xpath The XPath that caused the error.
   * @param message The reason why the XPath caused the error.
   */
  private void addXPathException(Node node, String xpath, String message) {
    int lineNumber = -1;
    if (node != null) {
      SourceLocation nodeLocation = 
        (SourceLocation) node.getUserData(
            SourceLocation.class.getName());
      lineNumber = nodeLocation.getLineNumber();
    }
    getListener().addProblem(
        new ValidationProblem(new ProblemDefinition(
            ExceptionType.FATAL,
            ProblemType.TABLE_INTERNAL_ERROR,
            "Error evaluating XPath expression '" + xpath + "': "
                + message),
            getTarget(),
            lineNumber,
            -1));
  }
  
  private void progressCounter() {
      if (PROGRESS_COUNTER++ == Integer.MAX_VALUE) {
          PROGRESS_COUNTER = 0;
      } else if (PROGRESS_COUNTER % Constants.CONTENT_VAL_PROGRESS_COUNTER == 0) {
          System.out.print(".");
      }
  }
}
