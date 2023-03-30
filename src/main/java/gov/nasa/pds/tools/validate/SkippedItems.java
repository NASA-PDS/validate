package gov.nasa.pds.tools.validate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkippedItems {
  final private HashMap<String,URL> skipped = new HashMap<String,URL>();
  private static SkippedItems singleton = null;
  private SkippedItems() {
  }
  public static synchronized SkippedItems getInstance() {
    if (singleton == null) singleton = new SkippedItems();
    return singleton;
  }
  public synchronized void add (URL xmlfile) {
    this.skipped.put(xmlfile.toString(), xmlfile);
  }
  public synchronized List<URL> copy() {
    List<URL> duplicate = new ArrayList<URL>();
    duplicate.addAll(this.skipped.values());
    return duplicate;
  }
}
