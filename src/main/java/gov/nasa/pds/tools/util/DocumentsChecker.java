package gov.nasa.pds.tools.util;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that validate if a document file is valid based on the file name.
 * This class relies on the MimetypesFileTypeMap class to load a default MIME types file
 * that maps a mime type to file extensions.
 * Come time to check for the correct mime type, this map will be used to check for the
 * user provided mime type against the mime type defined in the file.
 */

public class DocumentsChecker {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentsChecker.class);

  private MimeTable mimeTable = new MimeTable();

  public DocumentsChecker() { }

    /**
     * Given a document standard id, returns a list of possible file extensions associated with that id.
     * @param documentStandardId The document standard id as defined by the PDS Information Model document. 
     * @return extensionList List of file extensions allowed for that document standard id.
     */

  public ArrayList<String> getPossibleFileExtensions(String documentStandardId) {
      ArrayList<String> extensionList = this.mimeTable.getPossibleFileExtensions(documentStandardId);
      return(extensionList);
  }

    /**
     * Given a document file name, check for the mime type matches in the defined default filename.
     * @param documentRef The file name of the document
     * @param documentStandardId The document standard id as defined by the PDS Information Model document. 
     * @return true if the mime type matches with was defined, false otherwise.
     */

  public boolean isMimeTypeCorrect(String documentRef, String documentStandardId) {
      boolean mimeTypeIsCorrectFlag = false;
      mimeTypeIsCorrectFlag = this.mimeTable.isMimeTypeCorrect(documentRef,documentStandardId);
      LOG.debug("isMimeTypeCorrect:documentRef,documentStandardId,mimeTypeIsCorrectFlag {},{},{}",documentRef,documentStandardId,mimeTypeIsCorrectFlag);
      return(mimeTypeIsCorrectFlag);
  }
}
