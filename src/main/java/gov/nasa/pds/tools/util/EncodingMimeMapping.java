package gov.nasa.pds.tools.util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public enum EncodingMimeMapping {
  GIF(Arrays.asList("gif")),
  J2C(Arrays.asList("j2c", "mj2", "mjp2")),
  JPEG(Arrays.asList("jpg", "jpeg")),
  MP4(Arrays.asList("mp4")),
  PDF(Arrays.asList("pdf")),
  PDFA(Arrays.asList("pdf", "pdfa")),
  PNG(Arrays.asList("png")),
  SEED(Arrays.asList("mseed")),
  TIFF(Arrays.asList("tif", "tiff")),
  WAV(Arrays.asList("wav"));
  final private List<String> extensions;
  EncodingMimeMapping (List<String> extensions){
    this.extensions = extensions;
  }
  public boolean contains (String extension) {
    return this.extensions.contains (extension.toLowerCase());
  }
  public List<String> allowed() {
    return new ArrayList<String>(this.extensions);
  }
  public static EncodingMimeMapping find (String encoding) throws IllegalArgumentException {
    if (encoding != null) {
      if (encoding.equalsIgnoreCase("GIF")) return GIF;
      if (encoding.equalsIgnoreCase("J2C")) return J2C;
      if (encoding.equalsIgnoreCase("JPEG")) return JPEG;
      if (encoding.equalsIgnoreCase("MP4/H.264")) return MP4;
      if (encoding.equalsIgnoreCase("MP4/H.264/AAC")) return MP4;
      if (encoding.equalsIgnoreCase("PDF")) return PDF;
      if (encoding.equalsIgnoreCase("PDFA")) return PDFA;
      if (encoding.equalsIgnoreCase("PDF/A")) return PDFA;
      if (encoding.equalsIgnoreCase("PNG")) return PNG;
      if (encoding.startsWith("SEED 2.")) return SEED;
      if (encoding.equalsIgnoreCase("TIFF")) return TIFF;
      if (encoding.equalsIgnoreCase("WAV")) return WAV;
      throw new IllegalArgumentException("encoding parameter '" + encoding + "' is not known to this version of validate.");
    } else {
      throw new IllegalArgumentException("encoding parameter cannot be null");
    }
  }
}
