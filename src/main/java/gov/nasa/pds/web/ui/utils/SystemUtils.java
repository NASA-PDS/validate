package gov.nasa.pds.web.ui.utils;

/*
 * This class is primarily for checking that we have enough memory to continue a process. However,
 * the information provided isn't currently useful on windows due to how memory allocation works.
 * Forums indicate that you'll need to use an OS specific program to get this information. It may be
 * enough to have a flag for doing this check and only implementing something that would work on the
 * deployed environments. HOWEVER, some people may choose to install locally and we can't control
 * what environment they're on... More research should happen before this gets implemented in the
 * app.
 */
public class SystemUtils {

  private static final long MEGABYTE = 1024L * 1024L;

  public static long bytesToMeg(long bytes) {
    return bytes / MEGABYTE;
  }

  public static long megsToBytes(long megs) {
    return megs * MEGABYTE;
  }

  public static long getTotalMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  public static long getAvailableMemory() {
    Runtime r = Runtime.getRuntime();
    return r.freeMemory() + (r.maxMemory() - r.totalMemory());
  }

  public static double getPercentMemoryFree() {
    return (double) getAvailableMemory() / (double) getTotalMemory() * 100;
  }

  public static void checkAvailableMemory() throws RuntimeException {
    checkAvailableMemory(8);
    checkAvailableMemory(10.0);
  }

  @SuppressWarnings("nls")
  public static void checkAvailableMemory(final long megsFree) throws RuntimeException {
    final long availableMemory = bytesToMeg(getAvailableMemory());
    if (availableMemory < megsFree) {
      throw new RuntimeException(
          "Insufficient memory for process. " + availableMemory + " megs are free.");
    }
  }

  @SuppressWarnings("nls")
  public static void checkAvailableMemory(final double percentFree) throws RuntimeException {
    final double availPercentFree = getPercentMemoryFree();
    if (availPercentFree < percentFree) {
      throw new RuntimeException(
          "Insufficient memory for process. " + availPercentFree + " percent free.");
    }
  }
}
