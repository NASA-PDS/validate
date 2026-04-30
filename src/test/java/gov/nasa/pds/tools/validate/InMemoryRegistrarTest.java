package gov.nasa.pds.tools.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link InMemoryRegistrar}, focusing on the parent-child index
 * and count-by-type caching introduced to eliminate O(n) scans.
 */
class InMemoryRegistrarTest {

  private InMemoryRegistrar registrar;

  // Use file:// URLs to match the format used in production
  private static final String BUNDLE = "file:///data/bundle";
  private static final String COLLECTION_A = "file:///data/bundle/collection_a";
  private static final String COLLECTION_B = "file:///data/bundle/collection_b";
  private static final String PRODUCT_1 = "file:///data/bundle/collection_a/product1.xml";
  private static final String PRODUCT_2 = "file:///data/bundle/collection_a/product2.xml";
  private static final String PRODUCT_3 = "file:///data/bundle/collection_b/product3.xml";

  @BeforeEach
  void setUp() {
    registrar = new InMemoryRegistrar();
    ValidationTarget.clearCache();
  }

  @Test
  void testGetChildTargetsMultiLevelHierarchy() {
    // Build a 3-level hierarchy: bundle -> collections -> products
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.COLLECTION, COLLECTION_A);
    registrar.addTarget(BUNDLE, TargetType.COLLECTION, COLLECTION_B);
    registrar.addTarget(COLLECTION_A, TargetType.FILE, PRODUCT_1);
    registrar.addTarget(COLLECTION_A, TargetType.FILE, PRODUCT_2);
    registrar.addTarget(COLLECTION_B, TargetType.FILE, PRODUCT_3);

    // Bundle's children should be only the two collections (not grandchildren)
    ValidationTarget bundleTarget = registrar.getRoot();
    Collection<ValidationTarget> bundleChildren = registrar.getChildTargets(bundleTarget);
    List<String> bundleChildLocations = bundleChildren.stream()
        .map(ValidationTarget::getLocation)
        .collect(Collectors.toList());

    assertEquals(2, bundleChildLocations.size(),
        "Bundle should have exactly 2 direct children (collections)");
    assertTrue(bundleChildLocations.contains(COLLECTION_A));
    assertTrue(bundleChildLocations.contains(COLLECTION_B));

    // Collection A's children should be product1 and product2
    ValidationTarget collATarget = registrar.getTargets().get(COLLECTION_A);
    Collection<ValidationTarget> collAChildren = registrar.getChildTargets(collATarget);
    List<String> collAChildLocations = collAChildren.stream()
        .map(ValidationTarget::getLocation)
        .collect(Collectors.toList());

    assertEquals(2, collAChildLocations.size(),
        "Collection A should have exactly 2 direct children (products)");
    assertTrue(collAChildLocations.contains(PRODUCT_1));
    assertTrue(collAChildLocations.contains(PRODUCT_2));

    // Collection B's children should be only product3
    ValidationTarget collBTarget = registrar.getTargets().get(COLLECTION_B);
    Collection<ValidationTarget> collBChildren = registrar.getChildTargets(collBTarget);
    List<String> collBChildLocations = collBChildren.stream()
        .map(ValidationTarget::getLocation)
        .collect(Collectors.toList());

    assertEquals(1, collBChildLocations.size(),
        "Collection B should have exactly 1 direct child");
    assertTrue(collBChildLocations.contains(PRODUCT_3));
  }

  @Test
  void testGetChildTargetsLeafNodeReturnsEmpty() {
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1);

    // Leaf node should have no children
    ValidationTarget product = registrar.getTargets().get(PRODUCT_1);
    Collection<ValidationTarget> children = registrar.getChildTargets(product);
    assertTrue(children.isEmpty(), "Leaf node should have no children");
  }

  @Test
  void testDuplicateAddTargetDoesNotCreateDuplicateChildren() {
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1);
    // Add the same target again
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1);

    ValidationTarget bundleTarget = registrar.getRoot();
    Collection<ValidationTarget> children = registrar.getChildTargets(bundleTarget);
    assertEquals(1, children.size(),
        "Duplicate addTarget should not create duplicate children");
  }

  @Test
  void testTargetCountByType() {
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.COLLECTION, COLLECTION_A);
    registrar.addTarget(BUNDLE, TargetType.COLLECTION, COLLECTION_B);
    registrar.addTarget(COLLECTION_A, TargetType.FILE, PRODUCT_1);
    registrar.addTarget(COLLECTION_A, TargetType.FILE, PRODUCT_2);
    registrar.addTarget(COLLECTION_B, TargetType.FILE, PRODUCT_3);

    assertEquals(1, registrar.getTargetCount(TargetType.BUNDLE));
    assertEquals(2, registrar.getTargetCount(TargetType.COLLECTION));
    assertEquals(3, registrar.getTargetCount(TargetType.FILE));
    assertEquals(0, registrar.getTargetCount(TargetType.DIRECTORY));
  }

  @Test
  void testDuplicateAddTargetDoesNotInflateCount() {
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1); // duplicate

    assertEquals(1, registrar.getTargetCount(TargetType.BUNDLE));
    assertEquals(1, registrar.getTargetCount(TargetType.FILE),
        "Duplicate addTarget should not inflate count");
  }

  @Test
  void testLabelCount() {
    registrar.addTarget(null, TargetType.BUNDLE, BUNDLE);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_1);
    registrar.addTarget(BUNDLE, TargetType.FILE, PRODUCT_2);

    assertEquals(0, registrar.getLabelCount());

    registrar.setTargetIsLabel(PRODUCT_1, true);
    assertEquals(1, registrar.getLabelCount());

    registrar.setTargetIsLabel(PRODUCT_2, true);
    assertEquals(2, registrar.getLabelCount());

    // Setting the same target as label again should not double-count
    registrar.setTargetIsLabel(PRODUCT_1, true);
    assertEquals(2, registrar.getLabelCount(),
        "Re-setting same label should not increase count");
  }
}
