package gov.nasa.pds.tools.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gov.nasa.pds.tools.validate.CrossLabelFileAreaReferenceChecker;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.RuleContext;

/**
 * Referential integrity state must survive labels being validated on several threads, and two
 * validation runs in one JVM must not see each other's state. See NASA-PDS/validate#1619.
 */
class ReferentialIntegrityConcurrencyTest {
  private static final int THREADS = 16;
  private ExecutorService pool;

  @BeforeEach
  void setUp() {
    pool = Executors.newFixedThreadPool(THREADS);
  }

  @AfterEach
  void tearDown() {
    pool.shutdownNow();
  }

  private static LabelCacheEntry entry(String lid) {
    return new LabelCacheEntry(List.of(lid), List.of(), List.of(), List.of());
  }

  @Test
  void observationalFileIsClaimedByExactlyOneLabel() throws Exception {
    for (int round = 0; round < 300; round++) {
      ReferentialIntegrityUtil run = new ReferentialIntegrityUtil();
      CrossLabelFileAreaReferenceChecker checker = run.getCrossLabelFileAreaReferenceChecker();
      List<ValidationTarget> labels = new ArrayList<>();
      for (int i = 0; i < THREADS; i++) {
        URL url = new URL("file:/race/r" + round + "/label_" + i + ".xml");
        run.cacheLabelIdentifiers(url, entry("urn:nasa:pds:race:data:p" + i + "::1.0"));
        labels.add(ValidationTarget.build(url));
      }
      CountDownLatch start = new CountDownLatch(1);
      List<Future<Boolean>> results = new ArrayList<>();
      for (ValidationTarget label : labels) {
        results.add(pool.submit(() -> {
          start.await();
          return checker.add("shared.dat", label, true);
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
      ReferentialIntegrityUtil run = new ReferentialIntegrityUtil();
      CountDownLatch start = new CountDownLatch(1);
      List<Future<?>> done = new ArrayList<>();
      final int r = round;
      for (int t = 0; t < THREADS; t++) {
        final int thread = t;
        done.add(pool.submit(() -> {
          start.await();
          for (int i = 0; i < 500; i++) {
            run.cacheLabelIdentifiers(
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
          if (run.getCachedLabelIdentifiers(
              new URL("file:/cache/r" + r + "/t" + t + "/l" + i + ".xml")) == null) {
            missing++;
          }
        }
      }
      assertEquals(0, missing, "round " + round + ": cache entries lost");
    }
  }

  @Test
  void separateRunsDoNotShareState() throws Exception {
    URL url = new URL("file:/runA/label.xml");
    ReferentialIntegrityUtil runA = new ReferentialIntegrityUtil();
    ReferentialIntegrityUtil runB = new ReferentialIntegrityUtil();
    runA.cacheLabelIdentifiers(url, entry("urn:nasa:pds:a:b:c"));
    runB.reset();
    assertNotNull(runA.getCachedLabelIdentifiers(url), "run B must not wipe run A");
    assertNull(runB.getCachedLabelIdentifiers(url), "run B must not see run A");
  }

  @Test
  void childContextSharesTheRunState() {
    RuleContext root = new RuleContext();
    ReferentialIntegrityUtil run = new ReferentialIntegrityUtil();
    root.setReferentialIntegrityUtil(run);
    assertSame(run, root.getReferentialIntegrityUtil());
    assertNotSame(run, new RuleContext().getReferentialIntegrityUtil(),
        "a fresh context starts with its own state");
    assertTrue(new RuleContext().getReferentialIntegrityUtil().getContextReferenceCheckFlag());
  }
}
