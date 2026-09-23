package gov.nasa.pds.tools.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationTargetTest {

  private static final String TARGET = "file:///data/test/product.xml";

  @BeforeEach
  void setUp() {
    ValidationTarget.clearCache();
  }

  @Test
  void buildReturnsCachedTargetForSameUrl() throws MalformedURLException {
    URL url = new URL(TARGET);

    ValidationTarget first = ValidationTarget.build(url);
    ValidationTarget second = ValidationTarget.build(url);

    assertSame(first, second,
        "Building the same URL should return the cached ValidationTarget");
  }

  @Test
  void buildIsThreadSafeForSameUrl() throws Exception {
    URL url = new URL(TARGET);

    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch start = new CountDownLatch(1);
    List<ValidationTarget> results = new ArrayList<>();

    try {
      List<Future<ValidationTarget>> futures = new ArrayList<>();

      for (int i = 0; i < threadCount; i++) {
        futures.add(executor.submit(() -> {
          start.await();
          return ValidationTarget.build(url);
        }));
      }

      start.countDown();

      for (Future<ValidationTarget> future : futures) {
        results.add(future.get());
      }
    } finally {
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    ValidationTarget first = results.get(0);

    for (ValidationTarget result : results) {
      assertSame(first, result,
          "Concurrent builds for the same URL should return the same cached instance");
    }

    assertEquals(threadCount, results.size());
  }

  @Test
  void buildWithDifferentSourceProducesDifferentInstances() throws MalformedURLException {
    URL target = new URL(TARGET);
    URL labelA = new URL("file:///data/test/label_a.xml");
    URL labelB = new URL("file:///data/test/label_b.xml");

    ValidationTarget withLabelA = ValidationTarget.build(target, labelA);
    ValidationTarget withLabelB = ValidationTarget.build(target, labelB);

    assertNotSame(withLabelA, withLabelB,
        "Same target URL with different source labels should produce distinct cached instances");
  }
}