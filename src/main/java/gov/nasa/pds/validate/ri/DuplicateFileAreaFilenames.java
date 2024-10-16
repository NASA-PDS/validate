package gov.nasa.pds.validate.ri;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.nasa.pds.registry.common.RestClient;


class DuplicateFileAreaFilenames extends OpensearchDocument implements Runnable {
  final private AuthInformation search;
  final private HashMap<String, List<String>> duplicates = new HashMap<String, List<String>>();
  final private Logger log = LogManager.getLogger(CommandLineInterface.class);
  private boolean done = true;

  public DuplicateFileAreaFilenames(AuthInformation search) {
    super(search);
    this.search = search;    
  }
  public void findDuplicatesInBackground() {
    this.done = false;
    Thread t = new Thread(this);
    t.setDaemon(true);
    t.start();
  }
  public Map<String, List<String>> getResults() {
    return this.duplicates;
  }
  @Override
  public void run() {
    findDuplicates();
    this.done = true;
    synchronized(this) {
      this.notifyAll();
    }
  }
  public void findDuplicates() {
    try {
      RestClient client = this.search.getConnectionFactory().createRestClient();
      for (String file_ref : client.performRequest(client.createSearchRequest()
          .buildFindDuplicates(PAGE_SIZE)
          .setIndex(this.search.getConnectionFactory().getIndexName())).bucketValues()) {
        HashSet<String> lids = new HashSet<String>();
        lids.addAll(client.performRequest(client.createSearchRequest()
            .buildTermQuery("ops:Data_File_Info/ops:file_ref", file_ref)
            .setReturnedFields(Arrays.asList("lid"))).fields());
        if (1 < lids.size()) {
          this.log.error("Found duplicate file: " + file_ref);
          this.duplicates.put (file_ref, new ArrayList<String>(lids));
        }
      }
    } catch (Exception e) {
      this.log.error("Had an error communicating with opensearch", e);
    }
  }
  public synchronized void waitTillDone() {
    while (!this.done) {
      try {
        this.wait();
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }
}
