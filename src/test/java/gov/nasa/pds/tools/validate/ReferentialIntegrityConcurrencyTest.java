package gov.nasa.pds.tools.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import gov.nasa.pds.tools.util.LabelCacheEntry;
import gov.nasa.pds.tools.util.ReferentialIntegrityUtil;

/**
 * The label identifier cache and the cross-label file area checker are written from label worker
 * threads, so they must not lose writes or let two labels claim the same observational file. See
 * NASA-PDS/validate#1619.
 */
@Timeout(value = 2, unit = TimeUnit.MINUTES)
class ReferentialIntegrityConcurrencyTest {
  private static final int THREADS = 16;
  private ExecutorService pool;

  @BeforeEach
  void setUp() {
    ReferentialIntegrityUtil.reset();
    CrossLabelFileAreaReferenceChecker.reset();
    pool = Executors.newFixedThreadPool(THREADS);
  }

  @AfterEach
  void tearDown() {
    pool.shutdownNow();
    ReferentialIntegrityUtil.reset();
    CrossLabelFileAreaReferenceChecker.reset();
    ValidationTarget.clearCache();
  }

  private static LabelCacheEntry entry(String lid) {
    return new LabelCacheEntry(List.of(lid), List.of(), List.of(), List.of());
  }

  @Test
  void observationalFileIsClaimedByExactlyOneLabel() throws Exception {
    for (int round = 0; round < 300; round++) {
      List<ValidationTarget> labels = new ArrayList<>();
      for (int i = 0; i < THREADS; i++) {
        URL url = new URL("file:/race/r" + round + "/label_" + i + ".xml");
        ReferentialIntegrityUtil.cacheLabelIdentifiers(url,
            entry("urn:nasa:pds:race:data:p" + i + "::1.0"));
        labels.add(ValidationTarget.build(url));
      }
      CountDownLatch start = new CountDownLatch(1);
      List<Future<Boolean>> results = new ArrayList<>();
      for (ValidationTarget label : labels) {
        results.add(pool.submit(() -> {
          start.await();
          return CrossLabelFileAreaReferenceChecker.add("shared.dat", label, true);
        }));
      }
      start.countDown();
      int owners = 0;
      for (Future<Boolean> f : results) {
        if (f.get()) {
          owners++;
        }
      }
      assertEquals(1, owners, "round " + round + ": one observational file, one owner");
    }
  }

  @Test
  void concurrentCacheWritesAreNotLost() throws Exception {
    for (int round = 0; round < 50; round++) {
      CountDownLatch start = new CountDownLatch(1);
      List<Future<?>> done = new ArrayList<>();
      final int r = round;
      for (int t = 0; t < THREADS; t++) {
        final int thread = t;
        done.add(pool.submit(() -> {
          start.await();
          for (int i = 0; i < 500; i++) {
            ReferentialIntegrityUtil.cacheLabelIdentifiers(
                new URL("file:/cache/r" + r + "/t" + thread + "/l" + i + ".xml"),
                entry("urn:nasa:pds:c:d:p" + thread + "_" + i));
          }
          return null;
        }));
      }
      start.countDown();
      for (Future<?> f : done) {
        f.get();
      }
      int missing = 0;
      for (int t = 0; t < THREADS; t++) {
        for (int i = 0; i < 500; i++) {
          if (ReferentialIntegrityUtil.getCachedLabelIdentifiers(
              new URL("file:/cache/r" + r + "/t" + t + "/l" + i + ".xml")) == null) {
            missing++;
          }
        }
      }
      assertEquals(0, missing, "round " + round + ": cache entries lost");
    }
  }
}
