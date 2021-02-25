package gov.nasa.pds.tools.util;

import java.lang.StringBuffer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.om.DocumentInfo;

import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabulatedUtil {
  // Class to inspect a tabulated content from a table file.

  private static final Logger LOG = LoggerFactory.getLogger(TabulatedUtil.class);
  private static URL contextValue      = null;

  public TabulatedUtil(URL contextValue) {
      this.contextValue   = contextValue;
  }

  public URL getTarget() {
    return(this.contextValue); 
  }

  public String getRecordDelimiter() {
      // Get the value of 'record_delimiter' in a label.  The returned value can be null.
      // <Table_Character>
      //     <record_delimiter>Carriage-Return Line-Feed</record_delimiter>

      String recordDelimiter = null;

      try {
          SAXSource saxSource = new SAXSource(Utility.openConnection(getTarget()));
          DocumentInfo docInfo = LabelParser.parse(saxSource); // Parses a label.
          XMLExtractor extractor = new XMLExtractor(docInfo);
          String fieldCheck = "record_delimiter";

          // Since 'record_delimiter' does not have to be in Table_Character element, prepend with "//" to get to the field's value. 
          recordDelimiter = extractor.getValueFromDoc("//" + fieldCheck);
          // If the tag does not exist, it is returned as an empty string.  Reset to null for later use.
          if (recordDelimiter.length() == 0) {
             recordDelimiter = null;
          }
      } catch (Exception e) {
          LOG.error("Exception encountered in getFieldDelimiter:getTarget() {},{}",getTarget());
          e.printStackTrace();
      }
      return(recordDelimiter);
  }
}
