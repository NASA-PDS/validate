package gov.nasa.pds.tools.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to encapsulate a home grown table to retrieve a mime type given a file name.
 */
public class MimeTable {
  private static final Logger LOG = LoggerFactory.getLogger(MimeTable.class);
  private static HashMap<String,String> mimetypesFileTypeMap = new HashMap<String,String>();

  // The file "validate_default_mime_types.txt" is expected to be in src/main/resources directory.
  // The value of the mime type contains underscore since the space is used as a separator in the file.
  private static String DEFAULT_MIME_TYPES_FILE_NAME = "validate_default_mime_types.txt";

  static {
    try {
        URL propertyFile = MimeTable.class.getResource(DEFAULT_MIME_TYPES_FILE_NAME);
        InputStream in = propertyFile.openStream();
        Scanner inFile = new Scanner(in);

        String nextLine = null;
        String[] tokens = null;
        int tokenIndex = 0;
        while (inFile.hasNext()) {
            nextLine = inFile.nextLine();
            if (!nextLine.startsWith("#")) {
                LOG.debug("loadMimeTable:APPENDING_LINE [{}]",nextLine.replaceAll("[\r\n]",""));
                // Parse the line for the mime type and the file extension.
                // Each file extension becomes a key and the first token (mime type) becomes the value
                // 7-Bit_ASCII_Text txt text TXT TEXT
                tokens = nextLine.split("\\s+");

                LOG.debug("loadMimeTable:nextLine,tokens.length [{}],{}",nextLine.replaceAll("[\r\n]",""),tokens.length);

                tokenIndex = 1;
                while (tokenIndex < tokens.length) {
                    LOG.debug("loadMimeTable:APPENDING_KEY {},{},[{}],[{}]",tokenIndex,tokens.length,tokens[tokenIndex],tokens[0]);
                    // Each token may potentially has a carriage return, replace them with empty string.
                    mimetypesFileTypeMap.put(tokens[tokenIndex].replaceAll("[\r\n]",""),tokens[0]);  // Each file extension becomes a key and the first token (mime type) becomes the value
                    tokenIndex += 1;
                }
               
            }
        }
        LOG.debug("loadMimeTable:defaultMimeTypesFileName,mimetypesFileTypeMap.size {},{}", DEFAULT_MIME_TYPES_FILE_NAME, mimetypesFileTypeMap.size());
        LOG.debug("loadMimeTable:mimetypesFileTypeMap.size {}",mimetypesFileTypeMap.size());
        inFile.close(); // Closes this scanner and release the resource.
    } catch (IOException io) {
        throw new RuntimeException(io.getMessage());
    }
}
  
  public MimeTable() {
  }

  /**
   * Given a default mime types table, load the table into a HashMap.
   * @param defaultMimeTypesFileName The file name of the default mime types table 
   * @return None
   */

  public void loadMimeTable() throws IOException {
      LOG.debug("loadMimeTable:defaultMimeTypesFileName {}", DEFAULT_MIME_TYPES_FILE_NAME);
      // Note: The function FilenameUtils.getPath() doesn't seem to work correctly.
      // It returns the path without the leading slash '/':  
      //
      // For this URI
      //
      //      file:/home/qchau/sandbox/validate/src/test/resources/github367/document/
      //
      // The FilenameUtils.getPath(getTarget().getPath()) returns
      //
      //     home/qchau/sandbox/validate/src/test/resources/github367/document/
      //
      // which is missing the leading slash.
      //

//      Scanner inFile = null;
//      try {
//          String parent = defaultMimeTypesFileName.substring(0,defaultMimeTypesFileName.lastIndexOf(File.separator));
//          LOG.debug("loadMimeTable:FilenameUtils.getName(defaultMimeTypesFileName) {}",FilenameUtils.getName(defaultMimeTypesFileName));
//          LOG.debug("loadMimeTable:FilenameUtils.getPath(defaultMimeTypesFileName) {}",FilenameUtils.getPath(defaultMimeTypesFileName));
//          LOG.debug("loadMimeTable:parent {}",parent);
//          inFile = new Scanner(new File(parent + File.separator + FilenameUtils.getName(defaultMimeTypesFileName)));
//      } catch (FileNotFoundException ex) {
//          LOG.error("loadMimeTable:Cannot load file {} into memory",defaultMimeTypesFileName);
//          throw new IOException("Cannot load file " + defaultMimeTypesFileName + " into memory ");
//      }

//      String nextLine = null;
//      String[] tokens = null;
//      int tokenIndex = 0;
//      while (inFile.hasNext()) {
//          nextLine = inFile.nextLine();
//          if (!nextLine.startsWith("#")) {
//              LOG.debug("loadMimeTable:APPENDING_LINE [{}]",nextLine.replaceAll("[\r\n]",""));
//              // Parse the line for the mime type and the file extension.
//              // Each file extension becomes a key and the first token (mime type) becomes the value
//              // 7-Bit_ASCII_Text txt text TXT TEXT
//              tokens = nextLine.split("\\s+");
//
//              LOG.debug("loadMimeTable:nextLine,tokens.length [{}],{}",nextLine.replaceAll("[\r\n]",""),tokens.length);
//
//              tokenIndex = 1;
//              while (tokenIndex < tokens.length) {
//                  LOG.debug("loadMimeTable:APPENDING_KEY {},{},[{}],[{}]",tokenIndex,tokens.length,tokens[tokenIndex],tokens[0]);
//                  // Each token may potentially has a carriage return, replace them with empty string.
//                  mimetypesFileTypeMap.put(tokens[tokenIndex].replaceAll("[\r\n]",""),tokens[0]);  // Each file extension becomes a key and the first token (mime type) becomes the value
//                  tokenIndex += 1;
//              }
//             
//          }
//      }
//      LOG.debug("loadMimeTable:defaultMimeTypesFileName,mimetypesFileTypeMap.size {},{}", DEFAULT_MIME_TYPES_FILE_NAME, mimetypesFileTypeMap.size());
//      LOG.debug("loadMimeTable:mimetypesFileTypeMap.size {}",mimetypesFileTypeMap.size());
//      inFile.close(); // Closes this scanner and release the resource.
  }



  //https://www.javacodeexamples.com/check-if-string-is-uppercase-in-java-example/614
  private boolean isStringUpperCase(String str) {
      char[] charArray = str.toCharArray();
        
      for(int i=0; i < charArray.length; i++) {
            // If any character is not in upper case, return false
          if( !Character.isUpperCase( charArray[i] ))
              return false;
      }
      return true;
  }


  /**
   * Given a file name, returns the mime type.
   * @param documentRef The file name of the document
   * @return mimeType The mime type
   */
  public String getContentType(String documentRef) {
     // Given a file name uses the file extension and returns the mime type.
     // Note that this is an alternative to using the MimetypesFileTypeMap to get the mime type.
     String mimeType = null; 

     int index = documentRef.lastIndexOf(".");
     String fileExtension = "";

     LOG.debug("getContentType:documentRef,lastDot {},{}",documentRef, index);
     if (index >= 0) {
         // Check to make sure there's something after the dot.
         // Example: The length of pccref. is 7, lastDot is 6
         // So must compare 7 greater than 6 + 1 to be able to get something after the dot.
         if (documentRef.length() > (index+1)) {
             fileExtension = documentRef.substring(index + 1);  // Get the file extension minus the dot '.'
         } else {
             LOG.error("Cannot extract file extension from documentRef {}, expecting at least one character after the dot '.'",documentRef);
         }
     } else {
         LOG.error("Cannot extract file extension from documentRef {}, expecting a dot '.' in the file name",documentRef);
     }
     LOG.debug("getContentType:documentRef,fileExtension {},{}",documentRef,fileExtension);
     if (mimetypesFileTypeMap.containsKey(fileExtension)) {
         mimeType = mimetypesFileTypeMap.get(fileExtension);
     } else {
         // If did not find the value of fileExtension and it was uppercase, look for the lowercase.
         if (this.isStringUpperCase(fileExtension) && mimetypesFileTypeMap.containsKey(fileExtension.toLowerCase())) {
             mimeType = mimetypesFileTypeMap.get(fileExtension.toLowerCase());
         } else {
             LOG.warn("No mime type found for file {}",documentRef);
         }
     }
     LOG.debug("getContentType:documentRef,fileExtension,mimeType {},{},{}",documentRef,fileExtension,mimeType);
     return(mimeType);
  }

    /**
     * Given a document file name, check for the mime type loaded into the hashmap.
     * @param documentRef The file name of the document
     * @param documentStandardId The document standard id as defined by the PDS Information Model document. 
     * @return true if the mime type matches with was defined, false otherwise.
     */

  public boolean isMimeTypeCorrect(String documentRef, String documentStandardId) {
      // Because the mime type table can have multiple mime types matching the same file extension, we have to do a little more work
      // if they don't match initially e.g.
      //      text/plain       txt text TXT TEXT
      //      7-Bit_ASCII_Text txt text TXT TEXT
      //      UTF-8_Text       txt text TXT TEXT
      // As long as 'text' is in both documentStandardId and contentType, we can consider the mime type matches.

      boolean mimeTypeIsCorrectFlag = false;
      String idToMatch = "";
      String contentType = "";  // Retrieved from map.

      LOG.debug("isMimeTypeCorrect:documentRef,documentStandardId {},[{}]",documentRef,documentStandardId);

      if (documentStandardId != null) {
          idToMatch = documentStandardId.replace(' ','_');  // Change "7-Bit ASCII Text" to "7-Bit_ASCII_Text"
      } else {
          LOG.warn("isMimeTypeCorrect:documentStandardId is null for documentRef {}",documentRef);
      }
      if (mimetypesFileTypeMap != null) { 
        contentType = this.getContentType(documentRef);

        // The value of contentType can be null, add a sanity check.
        if (contentType == null) {
          LOG.warn("isMimeTypeCorrect:The value of contentType is null from getContentType() for documentRef {}",documentRef);
        } else {
          LOG.debug("isMimeTypeCorrect:documentRef,contentType,idToMatch {},{},{}",documentRef,contentType,idToMatch);
          if (contentType.equalsIgnoreCase(idToMatch)) {
              mimeTypeIsCorrectFlag = true;
          } else {
              // Special case: Note that the txt, text file extensions can map to 3 different mime types: text/plain, 7-Bit_ASCII_Text, UTF-8_Text
              // so we will make an attempt to check if 'text' is in both the idToMatch and contentType
              if (idToMatch.toLowerCase().contains("text") && contentType.toLowerCase().contains("text")) {
                  mimeTypeIsCorrectFlag = true;
                  LOG.debug("Both mime types contains 'text'.  Provided documentStandardId '{}', retrieved from default mime type '{}'",idToMatch,contentType);
              } else {
                  LOG.warn("The two mime types do not match.  Provided documentStandardId '{}', retrieved from default mime type '{}'",idToMatch,contentType);
              }
          }
        }
      } else {
          LOG.error("isMimeTypeCorrect:Object mimetypesFileTypeMap is null.  Cannot get the mime type for file {}",documentRef);
      }

      LOG.debug("isMimeTypeCorrect:documentRef,documentStandardId,mimeTypeIsCorrectFlag {},{},{}",documentRef,documentStandardId,mimeTypeIsCorrectFlag);
      return(mimeTypeIsCorrectFlag);
  }

    /**
     * Given a document standard id, returns a list of possible file extensions associated with that id.
     * @param documentStandardId The document standard id as defined by the PDS Information Model document. 
     * @return extensionList List of file extensions allowed for that document standard id.
     */

  public ArrayList<String> getPossibleFileExtensions(String documentStandardId) {
      ArrayList<String> extensionList = new ArrayList<String>(); //
      String idToMatch = "";
       if (documentStandardId != null) {
          idToMatch = documentStandardId.replace(' ','_');  // Change "7-Bit ASCII Text" to "7-Bit_ASCII_Text"
      } else {
          LOG.warn("getPossibleFileExtensions:documentStandardId is null");
      }

      LOG.debug("getPossibleFileExtensions: this.mimetypesFileTypeMap.size() {}",mimetypesFileTypeMap.size());

      // Note: The HashMap mimetypesFileTypeMap has an unusual order.  The keys are the file extension, the values are the documentStandardId, e.g
      //       key: EPS , value: Encapsulated_Postscript
      Iterator<Map.Entry<String, String>> itr = mimetypesFileTypeMap.entrySet().iterator();
      while(itr.hasNext()) {
          Map.Entry<String, String> entry = itr.next();
          if (entry.getValue().equalsIgnoreCase(idToMatch)) {
              extensionList.add(entry.getKey());
              LOG.debug("getPossibleFileExtensions:ADDING_KEY_VALUE{},{}",entry.getKey(),entry.getValue());
          }
      }
      return(extensionList);
  }
}
