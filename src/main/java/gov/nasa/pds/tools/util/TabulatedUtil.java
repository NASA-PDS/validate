package gov.nasa.pds.tools.util;

import java.lang.StringBuffer;

import java.net.URL;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class that inspect a tabulated content from a table file for if a field starts with a double quote or not. 
 * 
 */

public class TabulatedUtil {
  private static final Logger LOG = LoggerFactory.getLogger(TabulatedUtil.class);
  private static URL contextValue = null;
  private static int numFields = 0;
  private static String fieldDelimiter = null; // How the fields are separated by.  Possible value 'Comma'
  private static final String TEMP_SEPARATOR = "TEMP_SEPARATOR"; // Use to separate columns into individual tokens.

  public TabulatedUtil(URL contextValue) {
      this.contextValue = contextValue;
  }

  public void setNumFields(int numFields) {
      this.numFields = numFields;
  }

  public void setFieldDelimiter(String fieldDelimiter) {
      this.fieldDelimiter = fieldDelimiter;
  }

  public URL getTarget() {
    return(this.contextValue); 
  }

  public String [] smartSplit(String line) {
      // Special note: Some fields that starts with a double quote can contain commas inside which makes the logic of splitting using comma more complex.
      //
      // A normal comma delimited record:
      //
      //     1998_1187,1998_1187-0,One possible explanation is that bulk densities of soils (perhaps like Scooby Doo) at depth are larger than those at the surface.
      //
      // A comma delimited with field starts with a double quote record (with comma inside).
      //
      //     1998_1186,1998_1186-0,", Scooby Doo) may be an indurated soil, because its composition is similar to soils elsewhere at the site [7]."

      String tokens[] = new String[0];
      StringBuffer stringBuffer = new StringBuffer();
      // Check to see if a field starts with a double quote.  The double quote can be in the first field or subsequent fields.
      // If the double quote is in the first field, the line would starts with '"' whereas in subsequent field it would have ',"' in the string.
      //
      // 1998_1803,1998_1803-3,"Similar ratios for other ferric oxides such as hematite and maghemite occur in the direction of the ""Yogi"" soils and the bright soil near ""Mermaid"", but are far off the scale of Fig."^M

      // For developer only: Set the value of runSmallSampleTest to true and run for one label only.
      boolean runSmallSampleTest = false;
      //boolean runSmallSampleTest = true;
      if (runSmallSampleTest) {
          // Developer test cases:
          //line = "1,0,2021-05-01T00:00:00.000,2459335.5000000,,,FIELD ONE,,FIELD2,";
          //line = "#             OBT Integer_value";
          // line = "1998_1803,1998_1803-3,\"Similar ratios for other ferric oxides such as hematite and maghemite occur in the direction of the \"\"Yogi\"\" soils and the bright soil near \"\"Mermaid\"\", but are far off the scale of Fig.";
          //line = "1998_1803,1998_1803-3,\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\"";
          //line = "1998_1803,1998_1803-3,\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\",Now is the time for all good men";
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.,Now is the time for all good men";
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.,Now is the time for all good men";
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\",Now is the time for all good men";
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\",Now is the time for all good men\"";
          // 1998_1803,1998_1803-3,"Similar ratios ""Yogi"" soils and ""Mermaid"", but are far."
          // 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 
          // Known test cases:
          line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\"";  // Case 01; start with double quote, 2 double quotes inside, with ending quote
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.";    // Case 02; start with double quote, 2 double quotes inside, with no ending quote
          //line = "Similar ratios Yogi soils and Mermaid  but are far.";      // Case 03; No double quotes inside, with no ending quote
          //line = "Similar ratios Yogi soils and Mermaid  but are far.\"";    // Case 04; No double quotes inside, with ending quote
          //line = "Similar ratios Yogi soils and Mermaid  but are far., but are far";   // Case 05; No double quotes inside, with comma separator
          //line = "\"Similar ratios Yogi soils and Mermaid, but are far.";    // Case 06: no 2 double quotes inside, no ending quote
          //line = "\"Similar ratios Yogi soils and Mermaid, but are far.\"";  // Case 07: no 2 double quotes inside, with ending quote
          //line = "\"Similar ratios \"\"Yogi\"\" soils and \"\"Mermaid\"\", but are far.\",\"More fields\"";    // Case 08; start with double quote, 2 double quotes inside, with ending quote, then more fields
      }
      //LOG.debug("line [{}]",line);

      // Start of with value of line:
      int runningPosition = 0;
      String reducedLine = line;

      boolean doneFlag = false;
      int fieldsAdded = 0;
      int loopCount = 1;

      // Parse the line until get to the end and moving runningPosition to the beginning of next field for each loop.
      while (doneFlag == false) {
          reducedLine = reducedLine.substring(runningPosition);
          // Only add a separator if this is not the first column.
          if (fieldsAdded > 0) {
              stringBuffer.append(this.TEMP_SEPARATOR);  // Append made up separator.
          }
          //LOG.debug("smartSplit:loopCount,runningPosition,reducedLine.length {},{},{}",loopCount,runningPosition,reducedLine.length());
          //LOG.debug("smartSplit:loopCount,runningPosition,reducedLine {},{},{}",loopCount,runningPosition,reducedLine);

          // "Similar ratios for other ferric oxides such as hematite and maghemite occur in the direction of the ""Yogi"" soils and the bright soil near ""Mermaid"", but are far off the scale of Fig."
          if (reducedLine.startsWith("\"")) {
              // Check first for false end of field: bright soil near ""Mermaid"", but are far off the scale of Fig."
              // The 'Mermaid"",' does not end the field because there are still token more.
              int posDoubleDoubleQuoteComma = reducedLine.indexOf("\"\",",1);
              int posEndField = -1;
              if (posDoubleDoubleQuoteComma > 0) {
                  // [bright soil near ""Mermaid"", but are far off the scale of Fig."]
                  //                               3
                  // Note that the ", after Mermaid is not the end of the field yet.
                  posEndField = reducedLine.indexOf("\",",posDoubleDoubleQuoteComma + 3);
              } else {
                  // Look for where the field ends with '",' if there are more fields or '"' as the last character.
                  posEndField = reducedLine.indexOf("\",",1);
              }
              //LOG.debug("smartSplit:loopCount,posEndField {},{}",loopCount,posEndField);
              //LOG.debug("smartSplit:loopCount,posEndField,reducedLine {},{},[{}]",loopCount,posEndField,reducedLine);
              if (posEndField > 0) {
                  // Extract starting from beginning next double quote and comma.
                  stringBuffer.append(reducedLine.substring(0,posEndField+1));  // The posEndField+1 ensure getting the last " of \"Field one\"
                  // Case 08 ["Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.","More fields"];  start with double quote, 2 double quotes inside, with ending quote, then more fields
                  //LOG.debug("TABULATED_FILE_CASE_08");
                  fieldsAdded += 1;
                  runningPosition = posEndField + 2;  // Move the pointer to one character after ", which is "Fields 2"
              } else {
                  // There is no double quote and comma, we are at the last field, so just get to the end
                  // There may be a case where the field contains two double quotes together.  If that is the case
                  // look at the last two double quotes together and then add one to look for the next double quote.
                  // 1998_1803,1998_1803-3,"Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.
                  // 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
                  // "Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.",Now is the time for all good men
                  // 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
                  //                                             44
                  //                                               46
                  // For loopCount at 3, the value of lastIndexOfTwoDoubleQuotes should be 44 and lastIndexOfTwoDoubleQuotes+2 = 46
         
                  int lastIndexOfTwoDoubleQuotes = reducedLine.lastIndexOf("\"\"");
                  //LOG.debug("smartSplit:loopCount,lastIndexOfTwoDoubleQuotes {},{}",loopCount,lastIndexOfTwoDoubleQuotes);
                  //LOG.debug("smartSplit:loopCount,MAKE_COPY {},[{}]",loopCount,reducedLine.substring(0,reducedLine.lastIndexOf("\"")));
                  if (lastIndexOfTwoDoubleQuotes > 0) {
                      // Copy from beginning to the to the first double quote after the last of the two double quote.
                      //    "Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.
                      // Note that the above string has a starting quote but no ending quote.  Need to check for a double quote
                      // after the lastIndexOfTwoDoubleQuotes
                      int indexOfLastDoubleQuote = reducedLine.indexOf("\"",lastIndexOfTwoDoubleQuotes+2);
                      if (indexOfLastDoubleQuote >= 0) {
                          //  "Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.",ZZZ   << Note that this field has an ending quote before [,ZZZ].
                          // Case 01 ["Similar ratios ""Yogi"" soils and ""Mermaid"", but are far."];  // Case 01; 2 double quotes inside, with ending quote
                          stringBuffer.append(reducedLine.substring(0,reducedLine.indexOf("\"",lastIndexOfTwoDoubleQuotes+2)));   // Copy from ["Similar] to the last [far."]
                          // LOG.debug("TABULATED_FILE_CASE_01");
                      } else {
                          //  "Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.        << Note that this field has no ending quote.
                          // Copy from ["Similar] to the end since there is no ending quote 
                          // Case 02 ["Similar ratios ""Yogi"" soils and ""Mermaid"", but are far.];  // Case 02; 2 double quotes inside, with no ending quote
                          stringBuffer.append(reducedLine.substring(0));
                          //LOG.debug("TABULATED_FILE_CASE_02");
                      }
                  } else {
                      // There is no two double quotes together, just copy from the beginning of the field toward the last double quote.
                      int indexOfLastDoubleQuote = reducedLine.indexOf("\"",1);
                      if (indexOfLastDoubleQuote >= 0) {
                          // Case 06 ["Similar ratios Yogi soils and Mermaid, but are far.]
                          stringBuffer.append(reducedLine.substring(0,reducedLine.indexOf("\"",1)+1));   // Include the last quote as well.
                          //LOG.debug("TABULATED_FILE_CASE_06");
                      } else {
                          // Case 07 ["Similar ratios Yogi soils and Mermaid, but are far."]
                          stringBuffer.append(reducedLine.substring(0));
                          //LOG.debug("TABULATED_FILE_CASE_07");
                      }
                  }
                  fieldsAdded += 1;
                  runningPosition = reducedLine.length() + 1;
              }
          } else {
              // If there are more comma to see fetch from beginning to comma.
              //LOG.debug("smartSplit:loopCount,nextComma {},[{}]",loopCount,reducedLine.indexOf(","));

              if (reducedLine.indexOf(",") >= 0) {
                  stringBuffer.append(reducedLine.substring(0,reducedLine.indexOf(",")));
                  //LOG.debug("TABULATED_FILE_CASE_05");
                  //LOG.debug("smartSplit:loopCount,NORMAL_APPEND {},[{}]",loopCount,reducedLine.substring(0,reducedLine.indexOf(",")));
                  // Case 05 [Similar ratios Yogi soils and Mermaid  but are far., but are far]; No double quotes inside, with comma separator
                  runningPosition = reducedLine.indexOf(",") + 1;
              } else {
                  // There are no more comma, we are at the last field get everything.
                  //LOG.debug("smartSplit:loopCount,NORMAL_APPEND_LAST {},[{}]",loopCount,reducedLine.substring(0));
                  // Both of these cases acceptable.  There is no rule against a field having a double quote randomly inside if it does not start with a double quote..
                  // Case 03 [Similar ratios Yogi soils and Mermaid  but are far.]  No double quotes inside, with no ending quote
                  // Case 04 [Similar ratios Yogi soils and Mermaid  but are far."] No double quotes inside, with ending quote
                  stringBuffer.append(reducedLine.substring(0));
                  //LOG.debug("TABULATED_FILE_CASE_03_04");
                  runningPosition = reducedLine.length() + 1;
              }
              fieldsAdded += 1;
          }

          //LOG.debug("smartSplit:loopCount,stringBuffer.toString() {},{}",loopCount,stringBuffer.toString());
          loopCount += 1;

          // We are done with this loop if running position is at length or goes pass the length of reducedLine.length()
          // It is important not to index pass the length of a string variable in Java otherwise an exception will arise
          // during a regression test and it will be difficult to know where the error occurred.
          if (runningPosition >= reducedLine.length()) {
              doneFlag = true;
          }
          //LOG.debug("smartSplit:loopCount,runningPosition,reducedLine {},{},[{}]",loopCount,runningPosition,reducedLine);
          //LOG.debug("smartSplit:loopCount,runningPosition,reducedLine.length {},{},{}",loopCount,runningPosition,reducedLine.length());
      }

      // Now that the stringBuffer contains all the fields with TEMP_SEPARATOR as a field separator,
      // we can safely split the string into individual tokens.
      tokens = stringBuffer.toString().split(TEMP_SEPARATOR);
      //LOG.debug("smartSplit:loopCount,tokens {},{}",loopCount,tokens);
      //LOG.debug("smartSplit:loopCount,tokens.length {},{}",loopCount,tokens.length);

      //runSmallSampleTest = true;
      // If running a small sample test, print all the tokens that was split, the number of tokens and exit for review.
      if (runSmallSampleTest) {
          for (int ii=0; ii < tokens.length; ii++) {
              LOG.info("smartSplit:ii,token[ii] {},[{}]",ii,tokens[ii]);
          }
          LOG.info("smartSplit:line [{}]",line);
          LOG.info("smartSplit:tokens.length {}",tokens.length);
          System.out.println("TabulatedUtil:EARLY_EXIT_BY_DEVELOPER:early#exit#0002");
          System.exit(0);
      }

      return(tokens);
  }

  public Boolean[] getListOfBooleanFieldsStartWithQuote(String line) {
      // For every record (or line) in the table delimited file, build a list of booleans whether the field starts with a double quote or not.
      // If fieldDelimiter is null, there is no way to split the line so the returned length of fieldsStartWithQuoteList is zero.
      // An example of lines:
      //
      // 1998_1018,1998_1018-0,The angle measuring capability  was used to determine that Sojourner could not approach Yogi from the IMP-facing side due to an overhang that is only obvious using the reprojecti on capabilities of Marsmap.
       // 1998_1228,1998_1228-1,"The Yogi images were obtained in four filters (443, 531, 671, and 967 nm) at 0740, 1500, 1640 LST over Sols 55-56 and at 0900 LST on Sol 75."
       //

      Boolean[] fieldsStartWithQuoteList = new Boolean[0]; // Start with zero elements since we don't know how many fields this will be
      Arrays.fill(fieldsStartWithQuoteList, Boolean.FALSE);  // Set all values to FALSE and set to TRUE if it starts with a double quote.
      //LOG.debug("getListOfBooleanFieldsStartWithQuote: this.fieldDelimiter,line.length() {},{}",this.fieldDelimiter,line.length()); 
      //LOG.debug("TableDataContentValidationRule:getListOfBooleanFieldsStartWithQuote:fieldDelimiter,line {},[{}]",fieldDelimiter,line);

      if (this.fieldDelimiter != null && this.fieldDelimiter.equalsIgnoreCase("comma")) {
          //String tokens[] = line.split(",");  // This is the normal split but may not work if the fields is enclosed in double quotes.
          String tokens[] = smartSplit(line);   // This is a better funtion to perform a smart split on the line for fields separated by a comma.

          // Do a sanity check to see that the number of tokens should be the same as numFields
          // It is just a warning if received more tokens than numFields.
          //LOG.debug("getListOfBooleanFieldsStartWithQuote:numFields,tokens.length {},{}",this.numFields,tokens.length);
          // Note that some tabulated file may have a Group_Field_Delimited/repetitions that would throw off the num_fields,
          /// so a more reliable value to use is tokens.length
          if (tokens.length > this.numFields) {
              //LOG.debug("The number of tokens {} from parsing line is greater than numFields {}",tokens.length,this.numFields);
              //LOG.debug("Will instantiate number of Booleans for fieldsStartWithQuoteList {}",tokens.length);
              fieldsStartWithQuoteList = new Boolean[tokens.length]; // Allocate as many elements as the tokens length.
              Arrays.fill(fieldsStartWithQuoteList, Boolean.FALSE);  // Set all values to FALSE and set to TRUE if it starts with a double quote.
          }

          //LOG.debug("TableDataContentValidationRule:getListOfBooleanFieldsStartWithQuote:tokens.length,line {},[{}]",tokens.length,line);
          for (int i = 0; i < tokens.length; i++) {
              // Check for i against tokens.length otherwise may index pass the length of tokens array.
              if (tokens[i].startsWith("\"")) {
                  fieldsStartWithQuoteList[i] = Boolean.TRUE;
              }
          }
          //LOG.debug("TableDataContentValidationRule:getListOfBooleanFieldsStartWithQuote:fieldsStartWithQuoteList.length,tokens.length {},{}",fieldsStartWithQuoteList.length,tokens.length);
      }
      //LOG.debug("TableDataContentValidationRule:getListOfBooleanFieldsStartWithQuote:fieldsStartWithQuoteList.length {}",fieldsStartWithQuoteList.length);

      //LOG.debug("TableDataContentValidationRule:getListOfBooleanFieldsStartWithQuote:fieldsStartWithQuoteList.length {}",fieldsStartWithQuoteList.length);
      return(fieldsStartWithQuoteList);
  }
}
