package gov.nasa.pds.tools.util;

public class EveryNCounter {
  private static EveryNCounter myself = null;
 private static Object lock = new Object();
  private int current = -1;
  private EveryNCounter(){
	}
  public static EveryNCounter getInstance(){
	synchronized (lock) {
      if (myself == null) myself = new EveryNCounter();
	}
	return myself;
  }
  public int getValue() { return this.current; }
  public void increment() { this.current++; }
}

