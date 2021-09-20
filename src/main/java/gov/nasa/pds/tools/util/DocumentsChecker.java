package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

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

  private static final String PDF_ENDS_WITH_PATTERN_1 = ".pdf";
  private static final String PDF_ENDS_WITH_PATTERN_2 = ".PDF";
  private static final String JPEG_ENDS_WITH_PATTERN_1 = ".jpg";
  private static final String JPEG_ENDS_WITH_PATTERN_2 = ".jpeg";
  private static final String PNG_ENDS_WITH_PATTERN_1 = ".png";
  private static final String PNG_ENDS_WITH_PATTERN_2 = ".PNG";
  private static final String HTML_ENDS_WITH_PATTERN_1 = ".html";
  private static final String HTML_ENDS_WITH_PATTERN_2 = ".htm";
  private static final String TEXT_ENDS_WITH_PATTERN_1 = ".txt";
  private static final String TEXT_ENDS_WITH_PATTERN_2 = ".TXT";
  private static final String TEXT_ENDS_WITH_PATTERN_3 = ".text";
  private static final String TEXT_ENDS_WITH_PATTERN_4 = ".TEXT";
  private static final String MSWORD_ENDS_WITH_PATTERN_1 = ".doc";
  private static final String MSWORD_ENDS_WITH_PATTERN_2 = ".docx";
  private static final String MSEXCEL_ENDS_WITH_PATTERN_1 = ".xls";
  private static final String MSEXCEL_ENDS_WITH_PATTERN_2 = ".xlsx";
  private static final String LATEX_ENDS_WITH_PATTERN_1 = ".tex";
  private static final String LATEX_ENDS_WITH_PATTERN_2 = ".TEX";
  private static final String POSTSCRIPT_ENDS_WITH_PATTERN_1 = ".ps";
  private static final String POSTSCRIPT_ENDS_WITH_PATTERN_2 = ".PS";
  private static final String ENCAPSULATED_POSTSCRIPT_ENDS_WITH_PATTERN_1 = ".eps";
  private static final String ENCAPSULATED_POSTSCRIPT_ENDS_WITH_PATTERN_2 = ".EPS";
  private static final String RICHTEXT_ENDS_WITH_PATTERN_1 = ".rtf";
  private static final String RICHTEXT_ENDS_WITH_PATTERN_2 = ".RTF";
  private static final String GIF_ENDS_WITH_PATTERN_1 = ".gif";
  private static final String GIF_ENDS_WITH_PATTERN_2 = ".GIF";
  private static final String TIF_ENDS_WITH_PATTERN_1 = ".tif";
  private static final String TIF_ENDS_WITH_PATTERN_2 = ".TIF";
  private static final String TIF_ENDS_WITH_PATTERN_3 = ".tiff";
  private static final String TIF_ENDS_WITH_PATTERN_4 = ".TIFF";
  private static final String MP4_ENDS_WITH_PATTERN_1 = ".mp4";
  private static final String MP4_ENDS_WITH_PATTERN_2 = ".MP4";

  public static final String[] JPEG_PATTERNS = {JPEG_ENDS_WITH_PATTERN_1, JPEG_ENDS_WITH_PATTERN_2};
  public static final String[] PDF_PATTERNS = {PDF_ENDS_WITH_PATTERN_1, PDF_ENDS_WITH_PATTERN_2};
  public static final String[] PNG_PATTERNS = {PNG_ENDS_WITH_PATTERN_1, PNG_ENDS_WITH_PATTERN_2};
  public static final String[] HTML_PATTERNS = {HTML_ENDS_WITH_PATTERN_1, HTML_ENDS_WITH_PATTERN_2};
  public static final String[] TEXT_PATTERNS = {TEXT_ENDS_WITH_PATTERN_1, TEXT_ENDS_WITH_PATTERN_2, TEXT_ENDS_WITH_PATTERN_3, TEXT_ENDS_WITH_PATTERN_4};
  public static final String[] MSWORD_PATTERNS = {MSWORD_ENDS_WITH_PATTERN_1, MSWORD_ENDS_WITH_PATTERN_2};
  public static final String[] MSEXCEL_PATTERNS = {MSEXCEL_ENDS_WITH_PATTERN_1, MSEXCEL_ENDS_WITH_PATTERN_2};
  public static final String[] LATEX_PATTERNS = {LATEX_ENDS_WITH_PATTERN_1, LATEX_ENDS_WITH_PATTERN_2};
  public static final String[] POSTSCRIPT_PATTERNS = {POSTSCRIPT_ENDS_WITH_PATTERN_1, POSTSCRIPT_ENDS_WITH_PATTERN_2};
  public static final String[] POSTSCRIPT_ENCAPSULATED__PATTERNS = {ENCAPSULATED_POSTSCRIPT_ENDS_WITH_PATTERN_1, ENCAPSULATED_POSTSCRIPT_ENDS_WITH_PATTERN_2};
  public static final String[] RICHTEXT_PATTERNS = {RICHTEXT_ENDS_WITH_PATTERN_1, RICHTEXT_ENDS_WITH_PATTERN_2};
  public static final String[] GIF_PATTERNS = {GIF_ENDS_WITH_PATTERN_1, GIF_ENDS_WITH_PATTERN_2};
  public static final String[] TIF_PATTERNS = {TIF_ENDS_WITH_PATTERN_1, TIF_ENDS_WITH_PATTERN_2, TIF_ENDS_WITH_PATTERN_3, TIF_ENDS_WITH_PATTERN_4};
  public static final String[] MP4_PATTERNS = {MP4_ENDS_WITH_PATTERN_1, MP4_ENDS_WITH_PATTERN_2};

  // The file "validate_default_mime_types.txt" is expected to be in src/main/resources directory.
  // If the environment resources.home is different, the file must be copied to that directory for this class to work.
  // The value of the mime type contains underscore since the space is used as a separator in the file.
  private String DEFAULT_MIME_TYPES_FILE_NAME =  System.getProperty("resources.home") + File.separator + "validate_default_mime_types.txt";
  private MimetypesFileTypeMap mimetypesFileTypeMap = null;

  public DocumentsChecker() {
    LOG.debug("DocumentsChecker:Loading input {}",DEFAULT_MIME_TYPES_FILE_NAME);
    try {
      // Loads the default mime type file into memory.
      this.mimetypesFileTypeMap = new MimetypesFileTypeMap(DEFAULT_MIME_TYPES_FILE_NAME);
    } catch (IOException ex) {
        LOG.error("DocumentsChecker:Cannot build object MimetypesFileTypeMap with input {}",DEFAULT_MIME_TYPES_FILE_NAME);
    }
  }

    /**
     * Given a document file name, check for the mime type defined in DEFAULT_MIME_TYPES_FILE_NAME.
     * @param documentRef The file name of the document
     * @param documentStandardId The document standard if as defined by the PDS Information Model document. 
     * @return true if the mime type DEFAULT_MIME_TYPES_FILE_NAME matches with was defined, false otherwise.
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
          contentType = this.mimetypesFileTypeMap.getContentType(documentRef);
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
