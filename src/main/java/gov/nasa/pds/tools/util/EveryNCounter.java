package gov.nasa.pds.tools.util;

public class EveryNCounter {
  private static EveryNCounter myself = null;
  private static Object lock = new Object();
  public enum Group { content, labels, references }; 
  private int n = 0;
  private int progress[] = {-1,0,0};
  private EveryNCounter(){
	}
  public static EveryNCounter getInstance(){
	synchronized (lock) {
      if (myself == null) myself = new EveryNCounter();
	}
	return myself;
  }
  public int getValue(Group of) { return this.progress[of.ordinal()]; }
  public int getProgress (Group of) { return this.progress[of.ordinal()]; }
  public void increment (Group of) {
    synchronized (of) {
      this.progress[of.ordinal()]++;
        if (n > 0 && this.progress[of.ordinal()] > 0 && (this.progress[of.ordinal()] % this.n) == 0) {
          System.out.println("Progress: processed another "+ this.n 
              + " for a total of " + this.progress[of.ordinal()] 
              + " of " + of.name());
        }
    }
  }
  public void setProgressN (int n) { this.n = n; }
}

