package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verapdf.gf.foundry.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * A class that validate if a PDF file conforms to PDF/A standard.
 */
public class PDFUtil {
  private static final Logger LOG = LoggerFactory.getLogger(PDFUtil.class);
  private String externalErrorFilename = null;
  private String parserFlavor = null;
  private String errorMessage = null;

  public PDFUtil() {
    VeraGreenfieldFoundryProvider.initialise(); // Should only do this once.
  }

  /**
   * Returns the name of the external error filename. Can be null.
   *
   */
  public synchronized String getExternalErrorFilename() {
    return (this.externalErrorFilename);
  }

  private synchronized void writeErrorToFile(String baseDir, String pdfFullName, ValidationResult result, String flavor) {
    // Ignore the write if baseDir has not been given
    if (baseDir.isEmpty()) return;

    // Write to an external file with ".error" appended to file name in user's
    // default directory the content of result.

    // Build the external filename and save it for other to access.
    this.externalErrorFilename = baseDir + File.separator + Paths.get(pdfFullName).getFileName() + "." + flavor + ".error.csv";

    FileWriter myWriter = null;
    try {
      myWriter = new FileWriter(this.externalErrorFilename);
      myWriter.write("PDF_FILE " + pdfFullName + " PARSER_FLAVOR " + this.parserFlavor + "\n");
      String headerMessage = "getRuleId, getStatus, getMessage, getLocation";
      myWriter.write(headerMessage + "\n");
    } catch (IOException e) {
      LOG.error("Cannot open file {} for writing", this.externalErrorFilename);
      this.externalErrorFilename = null; // Reset back to null since writing of file was not
                                         // successful.
      e.printStackTrace();
    }

    for (TestAssertion error : result.getTestAssertions()) {
      try {
        String errorMessage =
            "\"" + error.getRuleId() + "\"" + "," + "\"" + error.getStatus() + "\"" + "," + "\""
                + error.getMessage() + "\"" + "," + "\"" + error.getLocation() + "\"";
        myWriter.write(errorMessage + "\n");
      } catch (IOException e) {
        LOG.error("Cannot write to file {}", this.externalErrorFilename);
        this.externalErrorFilename = null; // Reset back to null since writing of file was not
                                           // successful.
        e.printStackTrace();
      }
    } // end for (TestAssertion error : result.getTestAssertions())

    try {
      myWriter.close();
    } catch (IOException e) {
      LOG.error("Cannot close file {}", this.externalErrorFilename);
      e.printStackTrace();
      this.externalErrorFilename = null; // Reset back to null since writing of file was not
                                         // successful.
    }
  }

  private boolean validatePDF(String baseDir, URI uri, String pdfRef) throws IOException {
    boolean pdfValidateFlag = false;

    try {
      // Create a parser and auto-detect flavour
      PDFAParser parser = Foundries.defaultInstance().createParser(new FileInputStream(pdfRef));
      PDFAFlavour detectedFlavour = parser.getFlavour();
      LOG.debug("validatePDF: parser.getFlavour() [{}]", detectedFlavour);

      // First, check the flavour is valid 1a or 1b
      if (!detectedFlavour.equals(PDFAFlavour.PDFA_1_A)
          && !detectedFlavour.equals(PDFAFlavour.PDFA_1_B)) {
        this.errorMessage = "Invalid PDF/A version detected for " + uri
            + ". Expected: 1a or 1b. Actual: " + detectedFlavour.getId();
      } else {
        // Next, check the PDF is actually a valid 1a/1b flavour
        PDFAValidator validator =
            Foundries.defaultInstance().createValidator(detectedFlavour, false);
        this.parserFlavor = parser.getFlavour().getId();
        ValidationResult result = validator.validate(parser);
        if (result.isCompliant()) {
          // File is a valid PDF
          LOG.debug("validatePDF file " + pdfRef + " is a valid PDF file with flavor "
              + parser.getFlavour().getId());
          pdfValidateFlag = true;
        } else {
          LOG.error("validatePDF file" + pdfRef + " is not valid PDF file with flavor "
              + parser.getFlavour().getId());

          // Write the result to external file so the user can look over in the validate
          // report.
          this.writeErrorToFile(baseDir, pdfRef, result, parser.getFlavour().getId());

          this.errorMessage = "Validation failed for flavour PDF/A-" + detectedFlavour.getId()
              + " in file " + Paths.get(pdfRef).getFileName() + ".";
          if (this.getExternalErrorFilename() != null) this.errorMessage += "  Detailed error output can be found at " + this.getExternalErrorFilename();
        }
      }
    } catch (Exception e) {
      this.errorMessage = " Unable to read with VeraPDF standard reader. " + e.getMessage();
      throw new IOException(this.errorMessage);
    }
    return (pdfValidateFlag);
  }

  /**
   * Validate if a PDF file conforms to PDF/A standard.
   *
   * @param pdfBase The basename of the PDF file
   * @param parentURL The URL of the parent of pdfBase.
   * @return true if the PDF is PDF/A compliant, and false otherwise
   *
   */
  public synchronized boolean validateFileStandardConformity(String baseDir, String pdfBase, URL parentURL, URL target)
      throws Exception {
    // Do the validation of the PDF document.
    // https://verapdf.org/category/software/

    boolean pdfValidateFlag = false;
    LOG.debug("validateFileStandardConformity:pdfBase {}", pdfBase);

    // Get the location of the PDF file.
    URI uri = null;
    try {
      uri = target.toURI();
    } catch (URISyntaxException e) {
      // Should never happen
      // but if it does, print an error message and returns false for pdfValidateFlag.
      // Observed in DEV that if the file name contains spaces, the getURI() results
      // in the URISyntaxException exception.
      LOG.error("validateFileStandardConformity:Cannot build URI for target {}", target);
      throw new Exception(
          "validateFileStandardConformity:Cannot build URI for target [" + target + "]");
    }

    // Get the parent from parentURL instead of using "new
    // File(uri.getPath()).getParent()" which will only get the top level directory.
    // It would be a bug if the file is in a sub directory of the top level
    // directory.
    String parent = parentURL.getFile();

    // tired of tricks to make it work everywhere so explicitly handling windoze
    if (System.getProperty("os.name").toLowerCase().startsWith("window")) {
      parent = new File(parentURL.toURI()).getAbsolutePath();
    }

    // Build the full pathname of the PDF file.
    String pdfRef = parent + (parent.endsWith(File.separator) ? "" : File.separator) + pdfBase;
    LOG.debug("validateFileStandardConformity:parent,pdfBase,pdfRef [{}],[{}],[{}]", parent,
        pdfBase, pdfRef);
    LOG.debug("validateFileStandardConformity:parent,pdfBase,pdfRef,uri [{}],[{}],[{}],{}", parent,
        pdfBase, pdfRef, uri);

    // First, validate the PDF against PDFAFlavour.PDFA_1_A, if it does not
    // validate, do it again with PDFAFlavour.PDFA_1_B
    pdfValidateFlag = this.validatePDF(baseDir, uri, pdfRef);

    LOG.debug("validateFileStandardConformity:pdfRef,pdfValidateFlag [{}],{}", parent,
        pdfValidateFlag);

    return (pdfValidateFlag);
  }

  /**
   * Returns error message from failed PDF/A validation, if one exists. Otherwise returns null.
   * 
   * @return errorMessage
   */
  public String getErrorMessage() {
    return this.errorMessage;
  }

}
