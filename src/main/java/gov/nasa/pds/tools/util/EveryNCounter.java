package gov.nasa.pds.tools.util;

public class EveryNCounter {
  private static EveryNCounter myself = null;
  private static Integer lock = new Object();
  private int current = 0;
  private EveryNCounter(){
	}
  public static EveryNCounter getInstance(){
	synchronized (lock) {
      if (myself == null) myself = new EveryNCounter();
	}
	return myself;
  }
  public int getValueThenIncrement() { return this.current++; }
}

