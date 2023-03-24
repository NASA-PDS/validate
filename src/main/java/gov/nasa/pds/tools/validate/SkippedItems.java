package gov.nasa.pds.tools.validate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SkippedItems {
  final private HashSet<URL> skipped = new HashSet<URL>();
  private static SkippedItems singleton = null;
  private SkippedItems() {
  }
  public static synchronized SkippedItems getInstance() {
    if (singleton == null) singleton = new SkippedItems();
    return singleton;
  }
  public synchronized void add (URL xmlfile) {
    this.skipped.add(xmlfile);
  }
  public synchronized List<URL> copy() {
    List<URL> duplicate = new ArrayList<URL>();
    duplicate.addAll(this.skipped);
    return duplicate;
  }
}
