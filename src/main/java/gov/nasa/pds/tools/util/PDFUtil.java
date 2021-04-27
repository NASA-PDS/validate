package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.FileInputStream;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that validate if a PDF file conforms to PDF/A standard.
 */
public class PDFUtil {
  private static final Logger LOG = LoggerFactory.getLogger(PDFUtil.class);
  private URL target = null;

  public PDFUtil(URL target) {
      this.target = target;
      PdfBoxFoundryProvider.initialise();  // Should only do this once.
  }

  /**
   * Returns the URL of the target.
   *
   */
  public URL getTarget() {
      return(this.target);
  }

  /**
   * Validate if a PDF file conforms to PDF/A standard.
   *
   * @param pdfBase The basename of the PDF file
   * @return true if the PDF is PDF/A compliant, and false otherwise
   *
   */
  public boolean validateFileStandardConformity(String pdfBase) {
    // Do the validation of the PDF document.
    // https://verapdf.org/category/software/

    boolean pdfValidateFlag = false;
    LOG.debug("pdfBase {}",pdfBase);

    // Get the location of the PDF file.
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
    }

    File file = new File(uri.getPath());
    String parent = file.getParent();

    // Build the full pathname of the PDF file.
    String pdfRef = parent + File.separator + pdfBase;

    try (PDFAParser parser = Foundries.defaultInstance().createParser(new FileInputStream(pdfRef))) {
        PDFAValidator validator = Foundries.defaultInstance().createValidator(parser.getFlavour(), false);
        ValidationResult result = validator.validate(parser);
        if (result.isCompliant()) {
            // File is a valid PDF/A
            LOG.debug("The file " + pdfRef + " is a valid PDF/A file");
            pdfValidateFlag = true;
        } else {
            LOG.error("The file" + pdfRef + " is not valid PDF/A file");
        }
    } catch (Exception e) {
        LOG.error("Cannot parse PDF file " + pdfRef);
        LOG.error("Exception is " + e.getMessage());
    }

    return(pdfValidateFlag); 
  }
}
