package gov.nasa.pds.tools.label;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import javax.xml.transform.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SchematronTransformer} cache behavior.
 *
 * <p>These tests exercise the full ISO schematron → XSLT compilation pipeline and
 * therefore require Saxon-HE on the classpath. They are slower than pure unit tests
 * on first run due to XSLT compilation overhead.
 */
@Tag("integration")
public class SchematronTransformerTest {

  /** Minimal valid ISO Schematron document. */
  private static final String MINIMAL_SCHEMATRON =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">\n"
      + "  <sch:title>Minimal test schematron</sch:title>\n"
      + "  <sch:pattern>\n"
      + "    <sch:rule context=\"/root\">\n"
      + "      <sch:assert test=\"true()\">Always passes.</sch:assert>\n"
      + "    </sch:rule>\n"
      + "  </sch:pattern>\n"
      + "</sch:schema>";

  /** A different schematron to verify distinct cache entries. */
  private static final String OTHER_SCHEMATRON =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">\n"
      + "  <sch:title>Other test schematron</sch:title>\n"
      + "  <sch:pattern>\n"
      + "    <sch:rule context=\"/other\">\n"
      + "      <sch:assert test=\"true()\">Also passes.</sch:assert>\n"
      + "    </sch:rule>\n"
      + "  </sch:pattern>\n"
      + "</sch:schema>";

  private SchematronTransformer transformer;

  @BeforeEach
  void setUp() throws Exception {
    transformer = new SchematronTransformer();
  }

  @Test
  void testCachePopulatedOnFirstCall() throws Exception {
    assertEquals(0, transformer.cacheSize(), "cache should start empty");

    Transformer t = transformer.transform(MINIMAL_SCHEMATRON);
    assertNotNull(t);
    assertEquals(1, transformer.cacheSize(), "cache should have one entry after first call");
  }

  @Test
  void testCacheHitOnRepeatedCall() throws Exception {
    Transformer t1 = transformer.transform(MINIMAL_SCHEMATRON);
    assertEquals(1, transformer.cacheSize());

    Transformer t2 = transformer.transform(MINIMAL_SCHEMATRON);
    assertEquals(1, transformer.cacheSize(), "cache size should remain 1 after repeated call");

    // Each call should return a distinct Transformer (Transformer is not thread-safe)
    assertNotSame(t1, t2, "each call should produce a new Transformer instance");
  }

  @Test
  void testDistinctSchematronsGetSeparateCacheEntries() throws Exception {
    transformer.transform(MINIMAL_SCHEMATRON);
    assertEquals(1, transformer.cacheSize());

    transformer.transform(OTHER_SCHEMATRON);
    assertEquals(2, transformer.cacheSize(), "distinct schematrons should produce separate cache entries");
  }

  @Test
  void testClearCacheRemovesAllEntries() throws Exception {
    transformer.transform(MINIMAL_SCHEMATRON);
    transformer.transform(OTHER_SCHEMATRON);
    assertEquals(2, transformer.cacheSize());

    transformer.clearCache();
    assertEquals(0, transformer.cacheSize(), "cache should be empty after clearCache()");
  }

  @Test
  void testCacheMissAfterClear() throws Exception {
    transformer.transform(MINIMAL_SCHEMATRON);
    assertEquals(1, transformer.cacheSize());

    transformer.clearCache();
    assertEquals(0, transformer.cacheSize());

    // Re-compile should repopulate the cache
    Transformer t = transformer.transform(MINIMAL_SCHEMATRON);
    assertNotNull(t);
    assertEquals(1, transformer.cacheSize(), "cache should repopulate after clear");
  }
}
