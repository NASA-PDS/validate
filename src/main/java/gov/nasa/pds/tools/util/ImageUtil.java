package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.RandomAccessFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that validate if these image files {JPEG, PNG) files are valid.
 */

public class ImageUtil {
  private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);
  private URL target = null;

  private static int JPEG_FIRST_SHORT = 0xffd8;
  private static int JPEG_LAST_SHORT = 0xffd9;

  private static int PNG_SIGNATURE_FIRST_INT  = 0x89504e47;
  private static int PNG_SIGNATURE_SECOND_INT = 0x0d0a1a0a;

  public ImageUtil(URL target) {
      this.target = target;
  }

  /**
   * Returns the URL of the target.
   *
   */
  public URL getTarget() {
      return(this.target);
  }

  /**
   * Check if a JPEG file is valid by inspecting the first 2 bytes for 0xffd8 and last 2 bytes for 0xffd9.
   *
   * @param jpegBase The basename of the JPEG file
   * @return true if the JPEG file is valid false otherwise
   *
   */
  public boolean isJPEG(String jpegBase) throws Exception {

    LOG.debug("isJPEG:jpegBase {}",jpegBase);

    // Get the location of the input file.
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
      // but if it does, print an error message.
      // Observed in DEV that if the file name contains spaces, the getURI() results in the URISyntaxException exception.
      LOG.error("isJPEG:Cannot build URI for target {}",getTarget());
      throw new Exception("isJPEG:Cannot build URI for target [" + getTarget() + "]");
    }

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
    // Using alternative method to get the parent.
    String parent = ""; 
    if (getTarget().getPath().lastIndexOf(File.separator) >= 0) {
        parent = getTarget().getPath().substring(0,getTarget().getPath().lastIndexOf(File.separator)); 
    } else {
        LOG.error("isJPEG:The path does not contain a file separator {}",getTarget().getPath());
        throw new Exception("isJPEG:The path does not contain a file separator " + getTarget().getPath());
    }

    LOG.debug("isJPEG:,parent,jpegBase {},{}",parent,jpegBase);

    // Build the full pathname of the file.
    String jpegRef = parent + File.separator + jpegBase;
    LOG.debug("isJPEG:parent,jpegBase,jpegRef [{}],[{}],[{}]",parent,jpegBase,jpegRef);

    File jpegFile = new File(jpegRef);
    RandomAccessFile raf = new RandomAccessFile(jpegFile, "r");

    try {

        // Make sure there are at least 4 bytes in the file.
        if (raf.length() < 4) {
            LOG.warn("isJPEG:Expecting the file size to be at least 4 bytes, true size is {} from file {}",raf.length(),jpegRef);
            return(false);
        }

        // Read the first 2 bytes and last 2 bytes.  If they are 0xffd8 and 0xffd9 then the file is a JPEG file.
        int firstShort = 0; 
        int lastShort = 0; 

        // Seek to the beginning of the file and read 2 bytes
        raf.seek(0);
        firstShort = raf.readUnsignedShort();

        // Seek to the end of file (minus 2 bytes) and read 2 bytes.
        raf.seek(jpegFile.length() - 2);
        lastShort = raf.readUnsignedShort(); 

        LOG.debug("isJPEG:jpegRef,firstShort,lastShort {},{},{}",jpegRef,String.format("0x%04x", firstShort),String.format("0x%04x", lastShort));

        // If first 2 bytes and last 2 bytes are 0xffd8 and 0xffd9 then the file is a JPEG file.
        if ((firstShort == JPEG_FIRST_SHORT) && (lastShort == JPEG_LAST_SHORT)) {
            LOG.debug("The file " + jpegRef + " is a valid JPEG file");
            return(true);
        } else {
            LOG.warn("The file " + jpegRef + " is not a valid JPEG file");
            LOG.debug("The file " + jpegRef + " is not a valid JPEG file");
            return(false);

        }
    } finally {
        raf.close();
    }
  }

  /**
   * Check if a PNG file is valid by inspecting if the 1st group 4 bytes are 0x89504e47 and the 2nd group of 4 bytes are 0x0d0a1a0a
   *
   * @param pngBase The basename of the PNG file
   * @return true if the PNG file is valid false otherwise
   *
   */
  public boolean isPNG(String pngBase) throws Exception {

    LOG.debug("isPNG:pngBase {}",pngBase);

    // Get the location of the input file.
    URI uri = null;
    try {
      uri = getTarget().toURI();
    } catch (URISyntaxException e) {
      // Should never happen
      // but if it does, print an error message.
      // Observed in DEV that if the file name contains spaces, the getURI() results in the URISyntaxException exception.
      LOG.error("isPNG:Cannot build URI for target {}",getTarget());
      throw new Exception("isPNG:Cannot build URI for target [" + getTarget() + "]");
    }

    // Using alternative method to get the parent.
    String parent = ""; 
    if (getTarget().getPath().lastIndexOf(File.separator) >= 0) {
        parent = getTarget().getPath().substring(0,getTarget().getPath().lastIndexOf(File.separator)); 
    } else {
        LOG.error("isPNGG:The path does not contain a file separator {}",getTarget().getPath());
        throw new Exception("isPNG:The path does not contain a file separator " + getTarget().getPath());
    }

    // Build the full pathname of the file.
    String pngRef = parent + File.separator + pngBase;
    LOG.debug("isPNG:parent,pngBase,pngRef [{}],[{}],[{}]",parent,pngBase,pngRef);

    File pngFile = new File(pngRef);
    RandomAccessFile raf = new RandomAccessFile(pngFile, "r");

    try {

        // Make sure there are at least 8 bytes in the file.
        if (raf.length() < 8) {
            LOG.warn("isPNG:Expecting the file size to be at least 8 bytes, true size is {} from file {}",raf.length(),pngRef);
            return(false);
        }

        // Read the first 8 bytes.
        int firstInt = 0;
        int secondInt = 0;

        // Seek to the beginning of the file and read 4 bytes.
        raf.seek(0);
        firstInt = raf.readInt();

        // Read next 4 bytes.
        raf.seek(4);
        secondInt = raf.readInt();

        LOG.debug("isPNG:pngRef,firstInt,secondInt {},{},{}",pngRef,String.format("0x%08x", firstInt),String.format("0x%08x", secondInt));

        // If first 4 bytes matches PNG_SIGNATURE_FIRST_INT and the second 4 bytes matches PNG_SIGNATURE_SECOND_INT, then the file is a PNG file.
        if (firstInt == PNG_SIGNATURE_FIRST_INT && secondInt == PNG_SIGNATURE_SECOND_INT) {
            LOG.debug("The file " + pngRef + " is a valid PNG file");
            return true;
        } else {
            LOG.warn("The file " + pngRef + " is not a valid PNG file");
            LOG.debug("The file " + pngRef + " is not a valid PNG file");
            return false;

        }
    } finally {
        raf.close();
    }
  }

}
