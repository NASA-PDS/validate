package gov.nasa.pds.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nasa.pds.tools.util.EncodingMimeMapping;
import org.junit.jupiter.api.Test;

public class EncodingMimeMappingTest {

  @Test
  public void testMsssOdrMiniHeaderMapsToUnknownExtension() {
    assertEquals(EncodingMimeMapping.UNKNOWN_EXTENSION,
        EncodingMimeMapping.find("MSSS Original Data Record Mini Header"));
  }

  @Test
  public void testMsssOdrPayloadDataMapsToUnknownExtension() {
    assertEquals(EncodingMimeMapping.UNKNOWN_EXTENSION,
        EncodingMimeMapping.find("MSSS Original Data Record Payload Data"));
  }

  @Test
  public void testNikonNefMapsToNef() {
    assertEquals(EncodingMimeMapping.NEF,
        EncodingMimeMapping.find("Nikon Electronic Format (NEF)"));
  }

  @Test
  public void testNefAllowedExtension() {
    assertTrue(EncodingMimeMapping.NEF.contains("nef"));
  }

  @Test
  public void testUnknownExtensionAllowedIsEmpty() {
    assertTrue(EncodingMimeMapping.UNKNOWN_EXTENSION.allowed().isEmpty());
  }

  @Test
  public void testFindThrowsOnUnknownEncoding() {
    assertThrows(IllegalArgumentException.class,
        () -> EncodingMimeMapping.find("Not A Real Encoding"));
  }

  @Test
  public void testFindThrowsOnNull() {
    assertThrows(IllegalArgumentException.class,
        () -> EncodingMimeMapping.find(null));
  }
}
