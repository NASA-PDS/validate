package gov.nasa.pds.tools.util;

import java.util.HashMap;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to encapsulate a home grown table to retrieve a mime type given a file name.
 */
public class MimeTable {
  private static final Logger LOG = LoggerFactory.getLogger(MimeTable.class);
  private static HashMap<String,String> mimetypesFileTypeMap = new HashMap<String,String>();

  public MimeTable() {
  }

  /**
   * Given a default mime types table, load the table into a HashMap.
   * @param defaultMimeTypesFileName The file name of the default mime types table 
   * @return None
   */

  public void loadMimeTable(String defaultMimeTypesFileName) throws IOException {
      LOG.debug("loadMimeTable:defaultMimeTypesFileName {}",defaultMimeTypesFileName);
      Scanner inFile = null;
      try {
          inFile = new Scanner(new File(defaultMimeTypesFileName));
      } catch (FileNotFoundException ex) {
          LOG.error("loadMimeTable:Cannot load file {} into memory {}",defaultMimeTypesFileName);
          throw new IOException("Cannot load file {} into memory " + defaultMimeTypesFileName);
      }

      String nextLine = null;
      String[] tokens = null;
      int tokenIndex = 0;
      while (inFile.hasNext()) {
          nextLine = inFile.nextLine();
          if (!nextLine.startsWith("#")) {
              LOG.debug("loadMimeTable:APPENDING_LINE [{}]",nextLine);
              // Parse the line for the mime type and the file extension.
              // Each file extension becomes a key and the first token (mime type) becomes the value
              // 7-Bit_ASCII_Text txt text TXT TEXT
              tokens = nextLine.split("\\s+");

              LOG.debug("loadMimeTable:nextLine,tokens.length [{}],{}",nextLine,tokens.length);

              tokenIndex = 1;
              while (tokenIndex < tokens.length) {
                  LOG.debug("loadMimeTable:APPENDING_KEY {},{},[{}],[{}]",tokenIndex,tokens.length,tokens[tokenIndex],tokens[0]);
                  mimetypesFileTypeMap.put(tokens[tokenIndex],tokens[0]);  // Each file extension becomes a key and the first token (mime type) becomes the value
                  tokenIndex += 1;
              }
             
          }
      }
      LOG.debug("loadMimeTable:defaultMimeTypesFileName,mimetypesFileTypeMap.size {},{}",defaultMimeTypesFileName,mimetypesFileTypeMap.size());
      LOG.debug("loadMimeTable:mimetypesFileTypeMap.size {}",mimetypesFileTypeMap.size());
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

     int lastDot = documentRef.lastIndexOf(".");
     String fileExtension = "";

     LOG.debug("getContentType:documentRef,lastDot {},{}",documentRef,lastDot);
     if (lastDot >= 0) {
         // Check to make sure there's something after the dot.
         // Example: The length of pccref. is 7, lastDot is 6
         // So must compare 7 greater than 6 + 1 to be able to get something after the dot.
         if (documentRef.length() > (lastDot+1)) {
             fileExtension = documentRef.substring(lastDot+1);  // Get the file extension minus the dot '.'
         } else {
             LOG.error("Cannot extract file extension from documentRef {}, expecting at least one character after the dot '.'",documentRef);
         }
     } else {
         LOG.error("Cannot extract file extension from documentRef {}, expecting a dot '.' in the file name",documentRef);
     }
     LOG.debug("getContentType:documentRef,fileExtension {},{}",documentRef,fileExtension);
     if (this.mimetypesFileTypeMap.containsKey(fileExtension)) {
         mimeType = mimetypesFileTypeMap.get(fileExtension);
     } else {
         LOG.warn("No mime type found for file {}",documentRef);
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
      if (this.mimetypesFileTypeMap != null) { 
          //contentType = this.mimetypesFileTypeMap.getContentType(documentRef);
          contentType = this.getContentType(documentRef);
          LOG.warn("isMimeTypeCorrect:documentRef,contentType,idToMatch {},{},{}",documentRef,contentType,idToMatch);
          if (contentType.equals(idToMatch)) {
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
      } else {
          LOG.error("isMimeTypeCorrect:Object mimetypesFileTypeMap is null.  Cannot get the mime type for file {}",documentRef);
      }

      LOG.debug("isMimeTypeCorrect:documentRef,documentStandardId,mimeTypeIsCorrectFlag {},{},{}",documentRef,documentStandardId,mimeTypeIsCorrectFlag);
      return(mimeTypeIsCorrectFlag);
  }
}
