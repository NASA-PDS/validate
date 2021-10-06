package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.PdfBoxFoundryProvider;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.TestAssertion;

import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that validate if a PDF file conforms to PDF/A standard.
 */
public class PDFUtil {
  private static final Logger LOG = LoggerFactory.getLogger(PDFUtil.class);
  private URL target = null;
  private String externalErrorFilename = null;
  private String parserFlavor = null;

  public PDFUtil(URL target) {
      this.target = target;
      PdfBoxFoundryProvider.initialise();  // Should only do this once.
  }

  /**
   * Returns the name of the external error filename.  Can be null.
   *
   */
  public synchronized String getExternalErrorFilename() {
      return(this.externalErrorFilename);
  }

  /**
   * Returns the URL of the target.
   *
   */
  public URL getTarget() {
      return(this.target);
  }

  private synchronized void writeErrorToFile(URI uri, ValidationResult result, String flavor) {
      // Write to an external file with ".error" appended to file name in user's default directory the content of result.

      // Build the external filename and save it for other to access.
      this.externalErrorFilename = System.getProperty("user.dir") + File.separator + FilenameUtils.getName(uri.getPath()) + "." + flavor + ".error";

      LOG.debug("writeErrorToFile:uri,this.externalErrorFilename {},{}",uri,this.externalErrorFilename);

      FileWriter myWriter = null;
      try {
          myWriter = new FileWriter(this.externalErrorFilename);
          //myWriter.write("Error messages for PDF file " + uri.getPath() + " using flavor " + this.parserFlavor + "\n") ;
          myWriter.write("PDF_FILE " + uri.getPath() + " PARSER_FLAVOR " + this.parserFlavor + "\n") ;
          String headerMessage = "getRuleId, getStatus, getMessage, getLocation";
          myWriter.write(headerMessage + "\n");
      } catch (IOException e) {
          LOG.error("Cannot open file {} for writing",this.externalErrorFilename);
          this.externalErrorFilename = null;  // Reset back to null since writing of file was not successful.
          e.printStackTrace();
      }

      for (TestAssertion error : result.getTestAssertions()) {
          try {
              String errorMessage = error.getRuleId() + "," + error.getStatus() + "," + error.getMessage() + "," + error.getLocation();
              myWriter.write(errorMessage + "\n");
          } catch (IOException e) {
              LOG.error("Cannot write to file {}",this.externalErrorFilename);
              this.externalErrorFilename = null;  // Reset back to null since writing of file was not successful.
              e.printStackTrace();
         }
      } // end for (TestAssertion error : result.getTestAssertions())

      try {
          myWriter.close();
      } catch (IOException e) {
          LOG.error("Cannot close file {}",this.externalErrorFilename);
          e.printStackTrace();
          this.externalErrorFilename = null;  // Reset back to null since writing of file was not successful.
     }
  }

  private boolean validateAgainstSpecificFlavor(URI uri, String pdfRef, PDFAFlavour specificFlavor) {
    // Create a specific flavor parser and validate the PDF against it.

    boolean pdfValidateFlag = false;

    try (PDFAParser parser = Foundries.defaultInstance().createParser(new FileInputStream(pdfRef), specificFlavor)) {
        PDFAValidator validator = Foundries.defaultInstance().createValidator(parser.getFlavour(), false);
        LOG.debug("validateAgainstSpecificFlavor: specificFlavor,parser.getFlavour() [{}][{}]",specificFlavor, parser.getFlavour());
        this.parserFlavor = parser.getFlavour().getId();
        ValidationResult result = validator.validate(parser);
        if (result.isCompliant()) {
            // File is a valid PDF
            LOG.debug("validateAgainstSpecificFlavor:The file " + pdfRef + " is a valid PDF file with flavor " + parser.getFlavour().getId());
            pdfValidateFlag = true;
        } else {
            LOG.error("validateAgainstSpecificFlavor:The file" + pdfRef + " is not valid PDF file with flavor " + parser.getFlavour().getId());

            // Write the result to external file so the user can look over in the validate report.
            this.writeErrorToFile(uri, result, parser.getFlavour().getId());
        }
    } catch (Exception e) {
        LOG.error("validateAgainstSpecificFlavor:Cannot parse PDF file " + pdfRef);
        LOG.error("validateAgainstSpecificFlavor:Exception is " + e.getMessage());
    }
    return(pdfValidateFlag);
  }

  /**
   * Validate if a PDF file conforms to PDF/A standard.
   *
   * @param pdfBase The basename of the PDF file
   * @return true if the PDF is PDF/A compliant, and false otherwise
   *
   */
  public synchronized boolean validateFileStandardConformity(String pdfBase) throws Exception {
    // Do the validation of the PDF document.
    // https://verapdf.org/category/software/

    boolean pdfValidateFlag = false;
    LOG.debug("validateFileStandardConformity:pdfBase {}",pdfBase);

    // Get the location of the PDF file.
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
      // but if it does, print an error message and  returns false for pdfValidateFlag.
      // Observed in DEV that if the file name contains spaces, the getURI() results in the URISyntaxException exception.
      LOG.error("validateFileStandardConformity:Cannot build URI for target {}",getTarget());
      throw new Exception("validateFileStandardConformity:Cannot build URI for target [" + getTarget() + "]");
    }

    File file = new File(uri.getPath());
    String parent = file.getParent();

    // Build the full pathname of the PDF file.
    String pdfRef = parent + File.separator + pdfBase;
    LOG.debug("validateFileStandardConformity:parent,pdfBase,pdfRef [{}],[{}],[{}]",parent,pdfBase,pdfRef);

    // First, validate the PDF against PDFAFlavour.PDFA_1_A, if it does not validate, do it again with PDFAFlavour.PDFA_1_B
    pdfValidateFlag = this.validateAgainstSpecificFlavor(uri, pdfRef, PDFAFlavour.PDFA_1_A);
    if (pdfValidateFlag == false) {
        pdfValidateFlag = this.validateAgainstSpecificFlavor(uri, pdfRef, PDFAFlavour.PDFA_1_B);
    }

    LOG.debug("validateFileStandardConformity:pdfRef,pdfValidateFlag [{}],{}",parent,pdfValidateFlag);

    return(pdfValidateFlag); 
  }
}
