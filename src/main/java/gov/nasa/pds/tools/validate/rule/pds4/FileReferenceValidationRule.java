// Copyright 2006-2017, by the California Institute of Technology.
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.DocumentsChecker;
import gov.nasa.pds.tools.util.FileSizesUtil;
import gov.nasa.pds.tools.util.FlagsUtil;
import gov.nasa.pds.tools.util.FileReferencedMapList;
import gov.nasa.pds.tools.util.ImageUtil;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.MD5Checksum;
import gov.nasa.pds.tools.util.PDFUtil;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

/**
 * Implements a rule to validate file references found in a label.
 * 
 * @author mcayanan
 *
 */
public class FileReferenceValidationRule extends AbstractValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(FileReferenceValidationRule.class);
  private static final Pattern LABEL_PATTERN = Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE);

  private static final FileReferencedMapList fileReferencedMapList = new FileReferencedMapList();
  
  /**
   * XPath to the file references within a PDS4 data product label.
   */
  private final String FILE_OBJECTS_XPATH =
    "//*[starts-with(name(), 'File_Area')]/File | //Document_File";

  
  private Map<URL, String> checksumManifest;
  private PDFUtil pdfUtil = null;  // Define pdfUtil so we can reuse it for every call to validateFileReferences() function.
  private ImageUtil imageUtil = null;  // Define imageUtil so we can reuse it for every call to isJPEG() function.
  private DocumentsChecker documentsChecker = null; // Define documentsChecker so we can reuse it.
  
  public FileReferenceValidationRule() {
    checksumManifest = new HashMap<URL, String>();
  }
  
  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location) || !Utility.canRead(location)) {
      return false;
    }
    
    if(!getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      return false;
    }
    Matcher matcher = LABEL_PATTERN.matcher(FilenameUtils.getName(location));
    return matcher.matches();
  }

  @ValidationTest
  public void validateFileReferences() {
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
    }
    if (getContext().getChecksumManifest() != null) {
      checksumManifest = getContext().getChecksumManifest();
    }
    Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class);
    DOMSource source = new DOMSource(label);
    source.setSystemId(uri.toString());
    try {
      DocumentInfo xml = LabelParser.parse(source);
      LOG.debug("FileReferenceValidationRule:validateFileReferences:uri {}",uri);
      validate(xml);
    } catch (TransformerException te) {
      ProblemDefinition pd = new ProblemDefinition(ExceptionType.ERROR, 
          ProblemType.INTERNAL_ERROR, te.getMessage());
      if (te.getLocator() != null) {
        getListener().addProblem(new ValidationProblem(pd, 
            new ValidationTarget(getTarget()), 
            te.getLocator().getLineNumber(), 
            te.getLocator().getColumnNumber(), te.getMessage()));
      } else {
        getListener().addProblem(new ValidationProblem(pd, 
            new ValidationTarget(getTarget())));        
      }
    }
    LOG.debug("validateFileReferences:leaving:uri {}",uri);
  }

  private String getNameIfMatchingPattern(String[] patterns, String name, String fileType) {
      // If the name contains one of the pattern, get it, otherwise return null.
      String nameValue = null;
      boolean oneTokensMatchedFlag = false;
      for (String endToken : patterns) {
          if (endToken.isEmpty()) {
             continue;
          }
          LOG.debug("getNameIfMatchingPattern:endToken,fileType {},{}",endToken,fileType);
          if (name.toLowerCase().endsWith(endToken.toLowerCase())) { 
              oneTokensMatchedFlag = true;
          }
      }

      LOG.debug("getNameIfMatchingPattern:name,fileType,oneTokensMatchedFlag {},{},{}",name,fileType,oneTokensMatchedFlag);

      if (oneTokensMatchedFlag && FlagsUtil.getContentValidationFlag() == true) {
          nameValue = name;
      }

      LOG.debug("getNameIfMatchingPattern:name,filetype,nameValue {},{},{}",name,fileType,nameValue);

      return(nameValue);
  }

  private boolean checkGenericDocument(ValidationTarget target, URL urlRef, TinyNodeImpl fileObject, String fileName,
                                       URL parent, String directory, String documentStandardId, String documentType, List<ValidationProblem> problems, ProblemType problemType) {
      boolean passFlag = true;
      // Check for document file validity by getting the mime type associated with the file name.
      try {
          problems.addAll(handleGenericDocument(target, urlRef,
              fileObject, fileName, parent, directory, documentStandardId, problemType));
      } catch (Exception e) {
          ProblemDefinition def = new ProblemDefinition(ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
                  "Error occurred while processing " + documentType + " file content for "
                  + FilenameUtils.getName(urlRef.toString()) + ": "
                  + e.getMessage());
              problems.add(new ValidationProblem(def, target,
              fileObject.getLineNumber(), -1));
         passFlag = false;
      }
      LOG.debug("checkGenericDocument:fileName,passFlag {},{}",fileName,passFlag);
      return(passFlag);
  }
    
  private boolean validate(DocumentInfo xml) {
    boolean passFlag = true;
    List<ValidationProblem> problems = new ArrayList<ValidationProblem>();
    ValidationTarget target = new ValidationTarget(getTarget());
    try {
      // Perform checksum validation on the label itself
      problems.addAll(handleChecksum(target, new URL(xml.getSystemId())));
    } catch(Exception e) {
      ProblemDefinition pd = new ProblemDefinition(
          ExceptionType.ERROR, ProblemType.INTERNAL_ERROR, 
          "Error occurred while calculating checksum for "
          + FilenameUtils.getName(xml.getSystemId()) + ": "
          + e.getMessage());
      problems.add(new ValidationProblem(pd, target));
      passFlag = false;
    }
    try {
      LOG.debug("FileReferenceValidationRule:validate:building extractor");
      XMLExtractor extractor = new XMLExtractor(xml);
      LOG.debug("FileReferenceValidationRule:validate:extractor {}",extractor);
      URL labelUrl = new URL(xml.getSystemId());
      LOG.debug("FileReferenceValidationRule:validate:labelUrl {}",labelUrl);
      URL parent = labelUrl.toURI().getPath().endsWith("/") ?
          labelUrl.toURI().resolve("..").toURL() :
            labelUrl.toURI().resolve(".").toURL();
      LOG.debug("FileReferenceValidationRule:validate:parent {}",parent);
      try {
        // Search for "xml:base" attributes within the merged XML. This will
        // tell us if there are any xincludes.
        LOG.debug("FileReferenceValidationRule:validate:getValuesFromDoc(//@xml:base");
        List<String> xincludes = extractor.getValuesFromDoc("//@xml:base");
        LOG.debug("FileReferenceValidationRule:validate:xincludes.size {}",xincludes.size());
        for (String xinclude : xincludes) {
          URL xincludeUrl = new URL(parent, xinclude);
          LOG.debug("FileReferenceValidationRule:validate:xincludeUrl {}",xincludeUrl);
          try {
            xincludeUrl.openStream().close();
            // Check that the casing of the file reference matches the
            // casing of the file located on the file system.
            try {
              File fileRef = FileUtils.toFile(xincludeUrl);
              LOG.debug("FileReferenceValidationRule:validate:xincludeUrl,fileRef.getPath() {},{}",xincludeUrl,fileRef.getPath());
              if (fileRef != null &&
                  !fileRef.getCanonicalPath().endsWith(fileRef.getName())) {
                ProblemDefinition def = new ProblemDefinition(ExceptionType.WARNING, 
                    ProblemType.FILE_REFERENCE_CASE_MISMATCH, 
                    "File reference'" + fileRef.toString()
                      + "' exists but the case doesn't match");
                problems.add(new ValidationProblem(def, target));
              }
            } catch (IOException io) {
              ProblemDefinition def = new ProblemDefinition(ExceptionType.FATAL,
                  ProblemType.INTERNAL_ERROR,
                  "Error occurred while checking for the existence of the "
                  + "uri reference '" + xincludeUrl.toString() + "': "
                  + io.getMessage());
              problems.add(new ValidationProblem(def, target));
              passFlag = false;
            }
            try {
              // Perform checksum validation on the xincludes.
              problems.addAll(
                  handleChecksum(target, xincludeUrl)
                  );
            } catch (Exception e) {
              ProblemDefinition def = new ProblemDefinition(
                  ExceptionType.ERROR, ProblemType.INTERNAL_ERROR, 
                  "Error occurred while calculating checksum for "
                  + FilenameUtils.getName(xincludeUrl.toString()) + ": "
                  + e.getMessage());
              problems.add(new ValidationProblem(def, target));
              passFlag = false;
            }
          } catch (IOException io) {
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.ERROR, ProblemType.MISSING_REFERENCED_FILE,
                "URI reference does not exist: " + xincludeUrl.toString());
            problems.add(new ValidationProblem(def, target));
            passFlag = false;
          }
        }
        
 	    // issue_42: Add capability to ignore product-level validation
        if (!getContext().getSkipProductValidation()) {    	   
          List<TinyNodeImpl> fileObjects = extractor.getNodesFromDoc(
              FILE_OBJECTS_XPATH);
          LOG.debug("FileReferenceValidationRule:validate:fileObjects.size() {}",fileObjects.size());
          for (TinyNodeImpl fileObject : fileObjects) {
            String name = "";
            String checksum = "";
            String directory = "";
            String filesize  = "";
            String pdfName = null;
            String jpegName = null;
            String pngName = null;
            String htmlName = null;
            String textName = null;
            String msWordName = null;
            String msExcelName = null;
            String latexName = null;
            String psName = null;
            String epsName = null;
            String rtName = null;
            String gifName = null;
            String tifName = null;
            String mp4Name = null;
            String documentStandardId = null;
            List<TinyNodeImpl> children = new ArrayList<TinyNodeImpl>();
            try {
              children = extractor.getNodesFromItem("*", fileObject);
            } catch (XPathExpressionException xpe) {
              ProblemDefinition def = new ProblemDefinition(
                  ExceptionType.ERROR, ProblemType.INTERNAL_ERROR, 
                  "Problem occurred while trying to get all the children "
                      + "of the file object node: " + xpe.getMessage());
              problems.add(new ValidationProblem(def, target, 
                  fileObject.getLineNumber(), -1));
              passFlag = false;
              continue;
            }

            for (TinyNodeImpl child : children) {
              // Get the value of 'document_standard_id' tag.
              if ("document_standard_id".equals(child.getLocalPart())) {
                  documentStandardId = child.getStringValue(); 
              }
              if ("file_name".equals(child.getLocalPart())) {
                name = child.getStringValue();
                LOG.debug("FileReferenceValidationRule:validate:name {}",name);

                pdfName  = this.getNameIfMatchingPattern(DocumentsChecker.PDF_PATTERNS, name, "PDF");
                jpegName = this.getNameIfMatchingPattern(DocumentsChecker.JPEG_PATTERNS, name, "JPEG");
                pngName  = this.getNameIfMatchingPattern(DocumentsChecker.PNG_PATTERNS,  name, "PNG");
                htmlName = this.getNameIfMatchingPattern(DocumentsChecker.HTML_PATTERNS, name, "HTML");
                textName = this.getNameIfMatchingPattern(DocumentsChecker.TEXT_PATTERNS, name, "TEXT");

                msWordName = this.getNameIfMatchingPattern(DocumentsChecker.MSWORD_PATTERNS, name, "MSWORD");
                msExcelName = this.getNameIfMatchingPattern(DocumentsChecker.MSEXCEL_PATTERNS, name, "MSEXCEL");
                latexName = this.getNameIfMatchingPattern(DocumentsChecker.LATEX_PATTERNS, name, "LATEX");
                psName = this.getNameIfMatchingPattern(DocumentsChecker.POSTSCRIPT_PATTERNS, name, "POSTSCRIPT");
                epsName = this.getNameIfMatchingPattern(DocumentsChecker.POSTSCRIPT_ENCAPSULATED__PATTERNS, name, "ENCAPSULATED_POSTSCRIPT");

                rtName = this.getNameIfMatchingPattern(DocumentsChecker.RICHTEXT_PATTERNS, name, "RICHTEXT");
                gifName = this.getNameIfMatchingPattern(DocumentsChecker.GIF_PATTERNS, name, "GIF");
                tifName = this.getNameIfMatchingPattern(DocumentsChecker.TIF_PATTERNS, name, "TIFF");
                mp4Name = this.getNameIfMatchingPattern(DocumentsChecker.MP4_PATTERNS, name, "MP4");

              } else if ("md5_checksum".equals(child.getLocalPart())) {
                checksum = child.getStringValue();
                LOG.debug("FileReferenceValidationRule:validate:checksum {}",checksum);
              } else if ("directory_path_name".equals(child.getLocalPart())) {
                directory = child.getStringValue();
                LOG.debug("FileReferenceValidationRule:validate:directory {}",directory);
              } else if ("file_size".equals(child.getLocalPart())) { // Fetch the file_size value from label.
                filesize = child.getStringValue();
                LOG.debug("FileReferenceValidationRule:validate:filesize {}",filesize);
              }
            }

            if (name.isEmpty()) {
              ProblemDefinition def = new ProblemDefinition(
                  ExceptionType.ERROR, ProblemType.UNKNOWN_VALUE, 
                  "Missing 'file_name' element tag");
              problems.add(new ValidationProblem(def, target, 
                  fileObject.getLineNumber(), -1));
              passFlag = false;
            } else {
              URL urlRef = null;
              if (!directory.isEmpty()) {
                urlRef = new URL(parent, directory + "/" + name);
              } else {
                urlRef = new URL(parent, name);
              }
              try {
                urlRef.openStream().close();
                // Check that the casing of the file reference matches the
                // casing of the file located on the file system.
                try {
                  File fileRef = FileUtils.toFile(urlRef);
                  if (fileRef != null &&
                      !fileRef.getCanonicalPath().endsWith(fileRef.getName())) {
                    ProblemDefinition def = new ProblemDefinition(ExceptionType.WARNING, 
                        ProblemType.FILE_REFERENCE_CASE_MISMATCH, 
                        "File reference'" + fileRef.toString()
                          + "' exists but the case doesn't match");
                    problems.add(new ValidationProblem(def, target, 
                        fileObject.getLineNumber(), -1));
                  } else {
                      LOG.debug("FileReferenceValidationRule:validate:getTarget,name,urlRef {},{},{},",getTarget(),name,urlRef);
                      // For every label that referenced urlRef, keep track of this list of labels.
                      // If more othan one label referenced a file, this will be flagged as an error.
                      //FileReferencedMap fileReferencedMap = this.fileReferencedMapList.setLabels(urlRef,getTarget().toString());
                      //if (fileReferencedMap.getNumLabelsReferencedFile() > 1) { 
                      //    LOG.error("File " + urlRef.toString() + "  is referenced by more than one labels " + fileReferencedMap.toString());
                      //    System.exit(1);
                      //}
                 }
                } catch (IOException io) {
                  ProblemDefinition def = new ProblemDefinition(ExceptionType.FATAL,
                      ProblemType.INTERNAL_ERROR,
                      "Error occurred while checking for the existence of the "
                      + "uri reference '" + urlRef.toString() + "': "
                      + io.getMessage());
                  problems.add(new ValidationProblem(def, target, 
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }
                try {
                  problems.addAll(handleChecksum(target, urlRef,
                      fileObject, checksum));
                } catch (Exception e) {
                  ProblemDefinition def = new ProblemDefinition(
                      ExceptionType.ERROR, ProblemType.INTERNAL_ERROR, 
                      "Error occurred while calculating checksum for "
                      + FilenameUtils.getName(urlRef.toString()) + ": "
                      + e.getMessage());
                  problems.add(new ValidationProblem(def, target, 
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }

                // Check for provided file_size value and against the calculated size.
                try {
                  problems.addAll(handleFilesize(target, urlRef,
                      fileObject, filesize));
                } catch (Exception e) {
                  ProblemDefinition def = new ProblemDefinition(
                      ExceptionType.ERROR, ProblemType.INTERNAL_ERROR, 
                      "Error occurred while calculating filesize for "
                      + FilenameUtils.getName(urlRef.toString()) + ": "
                      + e.getMessage());
                  problems.add(new ValidationProblem(def, target, 
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }

                // Check for PDF file validity.
                try {
                  problems.addAll(handlePDF(target, urlRef,
                      fileObject, pdfName, parent, directory));
                } catch (Exception e) {
                  ProblemDefinition def = new ProblemDefinition(
                      ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
                      "Error occurred while processing PDF file content for "
                      + FilenameUtils.getName(urlRef.toString()) + ": "
                      + e.getMessage());
                  problems.add(new ValidationProblem(def, target,
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }


                // Check for JPEG file validity.
                try {
                  problems.addAll(handleJPEG(target, urlRef,
                      fileObject, jpegName, parent, directory));
                } catch (Exception e) {
                  ProblemDefinition def = new ProblemDefinition(
                      ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
                      "Error occurred while processing JPEG file content for "
                      + FilenameUtils.getName(urlRef.toString()) + ": "
                      + e.getMessage());
                  problems.add(new ValidationProblem(def, target,
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }

                // Check for PNG file validity.
                try {
                  problems.addAll(handlePNG(target, urlRef,
                      fileObject, pngName, parent, directory));
                } catch (Exception e) {
                  ProblemDefinition def = new ProblemDefinition(
                      ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
                      "Error occurred while processing PNG file content for "
                      + FilenameUtils.getName(urlRef.toString()) + ": "
                      + e.getMessage());
                  problems.add(new ValidationProblem(def, target,
                      fileObject.getLineNumber(), -1));
                  passFlag = false;
                }

                passFlag = this.checkGenericDocument(target, urlRef, fileObject, htmlName, parent, directory, documentStandardId, "HTML", problems, ProblemType.NON_HTML_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, textName, parent, directory, documentStandardId, "TEXT", problems, ProblemType.NON_TEXT_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, msWordName, parent, directory, documentStandardId, "MSWORD", problems, ProblemType.NON_MSWORD_FILE);

                passFlag = this.checkGenericDocument(target, urlRef, fileObject, msExcelName, parent, directory, documentStandardId, "MSEXCEL", problems, ProblemType.NON_MSWORD_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, latexName, parent, directory, documentStandardId, "LATEX", problems, ProblemType.NON_LATEX_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, psName, parent, directory, documentStandardId, "POSTSCRIPT", problems, ProblemType.NON_POSTSCRIPT_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, epsName, parent, directory, documentStandardId, "ENCAPSULATED_POSTSCRIPT", problems, ProblemType.NON_ENCAPSULATED_POSTSCRIPT_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, rtName, parent, directory, documentStandardId, "RICHTEXT", problems, ProblemType.NON_RICHTEXT_FILE);

                passFlag = this.checkGenericDocument(target, urlRef, fileObject, gifName, parent, directory, documentStandardId, "GIF", problems, ProblemType.NON_GIF_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, tifName, parent, directory, documentStandardId, "TIFF", problems, ProblemType.NON_TIFF_FILE);
                passFlag = this.checkGenericDocument(target, urlRef, fileObject, mp4Name, parent, directory, documentStandardId, "MP4", problems, ProblemType.NON_MP4_FILE);

              } catch (IOException io) {
                ProblemDefinition def = new ProblemDefinition(
                    ExceptionType.ERROR, ProblemType.MISSING_REFERENCED_FILE,
                    "URI reference does not exist: " + urlRef.toString());
                problems.add(new ValidationProblem(def, target, 
                    fileObject.getLineNumber(), -1));
                passFlag = false;
              }
            }
          }
        } // end if (!getContext().getSkipProductValidation()) {
      } catch (XPathExpressionException xpe) {
        String message = "Error occurred while evaluating the following xpath expression '"
            + FILE_OBJECTS_XPATH + "': " + xpe.getMessage();
        ProblemDefinition def = new ProblemDefinition(
            ExceptionType.FATAL, ProblemType.INTERNAL_ERROR, message);
        problems.add(new ValidationProblem(def, target));
        passFlag = false;
      }
    } catch (Exception e) {
      String message = "Error occurred while reading the uri: " + e.getMessage();
      ProblemDefinition def = new ProblemDefinition(
          ExceptionType.FATAL, ProblemType.INTERNAL_ERROR, message);
      problems.add(new ValidationProblem(def, target));
      passFlag = false;
    }
    // Add the problems to the exception container.
    for (ValidationProblem problem : problems) {
      getListener().addProblem(problem);
    }
    LOG.debug("FileReferenceValidationRule:validate:DONE: passFlag {}",passFlag);
    return passFlag;
  }

  private List<ValidationProblem> handleChecksum(ValidationTarget target, URL fileRef)
  throws Exception {
    return handleChecksum(target, fileRef, null, null);
  }

  /**
   * Method to handle checksum processing.
   *
   * @param systemId The source (product label).
   * @param urlRef The uri of the file being processed.
   * @param fileObject The Node representation of the file object.
   * @param checksumInLabel Supplied checksum in the label. Can pass in
   * an empty value. If a null value is passed instead, it tells the
   * method to not do a check to see if the generated value matches
   * a supplied value. This would be in cases where a label's own
   * checksum is being validated.
   *
   * @return The resulting checksum. This will either be the generated value,
   * the value from the manifest file (if supplied), or the value from the
   * supplied value in the product label (if provided).
   *
   * @throws Exception If there was an error generating the checksum
   *  (if the flag was on)
   */
  private List<ValidationProblem> handleChecksum(ValidationTarget target, URL urlRef,
      TinyNodeImpl fileObject, String checksumInLabel)
  throws Exception {
    LOG.debug("handleChecksum:target,urlRef,checksumInLabel {},{},{}",target,urlRef,checksumInLabel);
    if (checksumManifest.isEmpty() && (checksumInLabel == null || checksumInLabel.isEmpty())) {
        String message = "No checksum found in the manifest for '"
            + urlRef + "'";
        LOG.debug("handleChecksum:"+message);
        return new ArrayList<ValidationProblem>();
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        String generatedChecksum = MD5Checksum.getMD5Checksum(urlRef);
        int lineNumber = -1;
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }
        if (!checksumManifest.isEmpty()) {
          if (checksumManifest.containsKey(urlRef)) {
            String suppliedChecksum = checksumManifest.get(urlRef);
            String message = "";
            ProblemType type = null;
            ExceptionType severity = null;
            if (!suppliedChecksum.equals(generatedChecksum)) {
              message = "Generated checksum '" + generatedChecksum
                  + "' does not match supplied checksum '"
                  + suppliedChecksum + "' in the manifest for '"
                  + urlRef + "'";
              severity = ExceptionType.ERROR;
              type = ProblemType.CHECKSUM_MISMATCH;
            } else {
              message =  "Generated checksum '" + generatedChecksum
                  + "' matches the supplied checksum '" + suppliedChecksum
                  + "' in the manifest for '" + urlRef
                  + "'";
              severity = ExceptionType.INFO;
              type = ProblemType.CHECKSUM_MATCHES;
            }
            if (!message.isEmpty()) {
              ProblemDefinition def = new ProblemDefinition(severity, type, 
                  message);
              messages.add(new ValidationProblem(def, target, lineNumber, -1));
            }
          } else {
            String message = "No checksum found in the manifest for '"
                + urlRef + "'";
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.ERROR, ProblemType.MISSING_CHECKSUM, message);
            messages.add(new ValidationProblem(def, target, lineNumber, -1));
          }
        }
        if (checksumInLabel != null) {
          if (!checksumInLabel.isEmpty()) {
            String message = "";
            ProblemType type = null;
            ExceptionType severity = null;
            if (!generatedChecksum.equals(checksumInLabel)) {
              message = "Generated checksum '" + generatedChecksum
                  + "' does not match supplied checksum '"
                  + checksumInLabel + "' in the product label for '"
                  + urlRef + "'";
              type = ProblemType.CHECKSUM_MISMATCH;
              severity = ExceptionType.ERROR;
            } else {
              message = "Generated checksum '" + generatedChecksum
                  + "' matches the supplied checksum '" + checksumInLabel
                  + "' in the product label for '"
                  + urlRef + "'";
              type = ProblemType.CHECKSUM_MATCHES;
              severity = ExceptionType.INFO;
            }
            if (!message.isEmpty()) {
              ProblemDefinition def = new ProblemDefinition(severity, type, 
                  message);
              messages.add(new ValidationProblem(def, target, lineNumber, -1));
            }
          } else {
            String message = "No checksum to compare against in the product label "
                + "for '" + urlRef + "'";
            LOG.debug("handleChecksum:"+message);
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.INFO, ProblemType.MISSING_CHECKSUM_INFO, message);
            messages.add(new ValidationProblem(def, target, lineNumber, -1));
          }
        }
        return messages;
    }
  }  

  private List<ValidationProblem> handleFilesize(ValidationTarget target, URL fileRef)
  throws Exception {
    return handleFilesize(target, fileRef, null, null);
  }

  /**
   * Method to handle filesize processing.
   *
   * @param target The source (product label).
   * @param urlRef The uri of the file being processed.
   * @param fileObject The Node representation of the file object.
   * @param filesizeInLabel Supplied filesize in the label. Can pass in
   * an empty value. If a null value is passed instead, it tells the
   * method to not do a check to see if the generated value matches
   * a supplied value. This would be in cases where a label's own
   * filesize is being validated.
   *
   * @return The resulting list of problems with filesize processing.
   *
   * @throws Exception If there was an error generating the filesize
   *  (if the flag was on)
   */
  private List<ValidationProblem> handleFilesize(ValidationTarget target, URL urlRef,
      TinyNodeImpl fileObject, String filesizeInLabel)
  throws Exception {
    LOG.debug("handleFilesize:target,urlRef,filesizeInLabel {},{},{}",target,urlRef,filesizeInLabel);
    if (filesizeInLabel == null || filesizeInLabel.isEmpty())  {
        String message = "No filesize to compare against in the product label "
                + "for '" + urlRef + "'";
        LOG.debug("handleFilesize:"+message);
        return new ArrayList<ValidationProblem>();  // Return an empty list.
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        long fileSizeAsInt = FileSizesUtil.getExternalFilesize(urlRef);  // Get the actual file size.
        String generatedFilesize = Long.toString(fileSizeAsInt); 
        int lineNumber = -1;
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }

        // For dealing with file size, there is no manifest to check like the checksum processing.
        // If the file size is provided, compare it with the actual file size generated.

        if (filesizeInLabel != null) {
          if (!filesizeInLabel.isEmpty()) {
            String message = "";
            ProblemType type = null;
            ExceptionType severity = null;
            if (!generatedFilesize.equals(filesizeInLabel)) {
              message = "Generated filesize '" + generatedFilesize
                  + "' does not match supplied filesize '"
                  + filesizeInLabel + "' in the product label for '"
                  + urlRef + "'";
              type = ProblemType.FILESIZE_MISMATCH;
              severity = ExceptionType.ERROR;
            } else {
              message = "Generated filesize '" + generatedFilesize
                  + "' matches the supplied filesize '" + filesizeInLabel
                  + "' in the product label for '"
                  + urlRef + "'";
              type = ProblemType.FILESIZE_MATCHES;
              severity = ExceptionType.INFO;
            }
            if (!message.isEmpty()) {
              ProblemDefinition def = new ProblemDefinition(severity, type, 
                  message);
              messages.add(new ValidationProblem(def, target, lineNumber, -1));
            }
          } else {
            // The file size is not provided, report for info only.
            String message = "No filesize to compare against in the product label "
                + "for '" + urlRef + "'";
            LOG.debug("handleFilesize:"+message);
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.INFO, ProblemType.MISSING_FILESIZE_INFO, message);
            messages.add(new ValidationProblem(def, target, lineNumber, -1));
          }
        }
        return messages;
    }
  }  

  private List<ValidationProblem> handlePDF(ValidationTarget target, URL fileRef,
                                            TinyNodeImpl fileObject, String pdfName, URL parent, String directory)
  throws Exception {
    LOG.debug("handlePDF:target,fileRef,pdfName {},{},{}",target,fileRef,pdfName);
    boolean pdfValidateFlag = false;
    if ((pdfName == null) || (fileObject == null)) {
        if (pdfName == null) {
           String message = "The file is not a PDF file, no need to perform check "
                   + "for '" + fileRef + "'";
           LOG.debug("handlePDF:"+message);
        }
        if (fileObject == null) {
            String message = "The fileObject is null, no need to perform check "
                + "for '" + fileRef + "'";
            LOG.debug("handlePDF:"+message);
        }
        return new ArrayList<ValidationProblem>();  // Return an empty list.
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        int lineNumber = -1;
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }

        if (this.pdfUtil == null) {
            // Save pdfUtil so it can be reused.
            this.pdfUtil = new PDFUtil(fileRef);
        }

        pdfValidateFlag = this.pdfUtil.validateFileStandardConformity(pdfName);

        // Report a  warning if the PDF file is not PDF/A compliant.
        if (!pdfValidateFlag) {
            URL urlRef = null;
            if (!directory.isEmpty()) {
                urlRef = new URL(parent, directory + "/" + pdfName);
            } else {
                urlRef = new URL(parent, pdfName);
            }
            LOG.error("handlePDF:"+urlRef.toString() + " is not valid PDF/A file or does not exist");
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.WARNING, ProblemType.NON_PDFA_FILE,
                urlRef.toString() + " is not valid PDF/A file or does not exist");
            messages.add(new ValidationProblem(def, target,
                lineNumber, -1));
        }

        return messages;
    }
  }

  private List<ValidationProblem> handleJPEG(ValidationTarget target, URL fileRef,
                                            TinyNodeImpl fileObject, String jpegName, URL parent, String directory)
  throws Exception {
    LOG.debug("handleJPEGtarget,fileRef,jpegName {},{},{}",target,fileRef,jpegName);
    boolean jpegValidateFlag = false;
    if ((jpegName == null) || (fileObject == null)) {
        if (jpegName == null) {
           String message = "The file is not a JPEG file, no need to perform check "
                   + "for '" + fileRef + "'";
           LOG.debug("handleJPEG:"+message);
        }
        if (fileObject == null) {
            String message = "The fileObject is null, no need to perform check "
                + "for '" + fileRef + "'";
            LOG.debug("handleJPEG:"+message);
        }
        return new ArrayList<ValidationProblem>();  // Return an empty list.
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        int lineNumber = -1;
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }

        if (this.imageUtil == null) {
            // Save imageUtil so it can be reused.
            this.imageUtil = new ImageUtil(fileRef);
        }

        jpegValidateFlag = this.imageUtil.isJPEG(jpegName);

        // Report a  warning if the JPEG file is not compliant.
        if (!jpegValidateFlag) {
            URL urlRef = null;
            if (!directory.isEmpty()) {
                urlRef = new URL(parent, directory + "/" + jpegName);
            } else {
                urlRef = new URL(parent, jpegName);
            }
            LOG.error("handleJPEG:" + urlRef.toString() + " is not valid JPEG file");
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.WARNING, ProblemType.NON_JPEG_FILE,
                urlRef.toString() + " is not valid JPEG file");
            messages.add(new ValidationProblem(def, target,
                lineNumber, -1));
        }

        return messages;
    }
  }

  private List<ValidationProblem> handlePNG(ValidationTarget target, URL fileRef,
                                            TinyNodeImpl fileObject, String pngName, URL parent, String directory)
  throws Exception {
    LOG.debug("handlePNGtarget,fileRef,pngName {},{},{}",target,fileRef,pngName);
    boolean validateFlag = false;
    if ((pngName == null) || (fileObject == null)) {
        if (pngName == null) {
           String message = "The file is not a PNG file, no need to perform check "
                   + "for '" + fileRef + "'";
           LOG.debug("handlePNG:"+message);
        }
        if (fileObject == null) {
            String message = "The fileObject is null, no need to perform check "
                + "for '" + fileRef + "'";
            LOG.debug("handlePNG:"+message);
        }
        return new ArrayList<ValidationProblem>();  // Return an empty list.
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        int lineNumber = -1;
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }

        if (this.imageUtil == null) {
            // Save imageUtil so it can be reused.
            this.imageUtil = new ImageUtil(fileRef);
        }

        validateFlag = this.imageUtil.isPNG(pngName);

        // Report a  warning if the PNG file is not compliant.
        if (!validateFlag) {
            URL urlRef = null;
            if (!directory.isEmpty()) {
                urlRef = new URL(parent, directory + "/" + pngName);
            } else {
                urlRef = new URL(parent, pngName);
            }
            LOG.error("handlePNG:" + urlRef.toString() + " is not valid PNG file");
            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.WARNING, ProblemType.NON_PNG_FILE,
                urlRef.toString() + " is not valid PNG file");
            messages.add(new ValidationProblem(def, target,
                lineNumber, -1));
        }

        return messages;
    }
  }

  private List<ValidationProblem> handleGenericDocument(ValidationTarget target, URL fileRef,
                                            TinyNodeImpl fileObject, String textName, URL parent, String directory,
                                            String documentStandardId, ProblemType problemType)
  throws Exception {
    LOG.debug("handleGenericDocument:Target,fileRef,textName {},{},{}",target,fileRef,textName);
    boolean mimeTypeIsCorrectFlag = false;
    if ((textName == null) || (fileObject == null)) {
        if (textName == null) {
           String message = "The file is not a Text file, no need to perform check "
                   + "for '" + fileRef + "'";
           LOG.debug("handleGenericDocument:"+message);
        }
        if (fileObject == null) {
            String message = "The fileObject is null, no need to perform check "
                + "for '" + fileRef + "'";
            LOG.debug("handleGenericDocument:"+message);
        }
        return new ArrayList<ValidationProblem>();  // Return an empty list.
    } else {
        List<ValidationProblem> messages = new ArrayList<ValidationProblem>();
        int lineNumber = -1; 
        if (fileObject != null) {
          lineNumber = fileObject.getLineNumber();
        }

        if (this.documentsChecker == null) {
            // Save documentsChecker so it can be reused.
            this.documentsChecker = new DocumentsChecker();
        }

        mimeTypeIsCorrectFlag = this.documentsChecker.isMimeTypeCorrect(textName,documentStandardId);
        LOG.debug("handleGenericDocument:textName,documentStandardId,mimeTypeIsCorrectFlag {},{},{}",textName,documentStandardId,mimeTypeIsCorrectFlag);

        // Report a  warning if the mime type is not correct
        if (!mimeTypeIsCorrectFlag) {
            URL urlRef = null;
            if (!directory.isEmpty()) {
                urlRef = new URL(parent, directory + "/" + textName);
            } else {
                urlRef = new URL(parent, textName);
            }

            String errorMessage = urlRef.toString() + " with document standard id '" + documentStandardId + "' is not correct";
            LOG.warn("handleGenericDocument:" + errorMessage);

            ProblemDefinition def = new ProblemDefinition(
                ExceptionType.WARNING, problemType,
                errorMessage);
            messages.add(new ValidationProblem(def, target,
                lineNumber, -1));
        }

        return messages;
    }   
  }

}
