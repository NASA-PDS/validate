package gov.nasa.pds.validate.ri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.nasa.pds.registry.common.RestClient;

public class OpensearchDocument implements DocumentInfo {
  protected final int PAGE_SIZE = 5000;
  final private AuthInformation context;
  final private HashMap<String, Map<String, Object>> documents =
      new HashMap<String, Map<String, Object>>();
  final private HashMap<String, List<String>> references = new HashMap<String, List<String>>();
  final private LidvidComparator lidvid_compare = new LidvidComparator();
  final private Logger log = LogManager.getLogger(OpensearchDocument.class);
  private RestClient client = null;
  private void load(String lidvid) {
    if (!this.documents.containsKey(lidvid)) {
      try {
        synchronized (this) {
          if (this.client == null) this.client = this.context.getConnectionFactory().createRestClient();
        }
        List<Map<String,Object>> docs = this.client.performRequest(
            this.client.createSearchRequest()
              .buildTermQuery(lidvid.contains("::") ? "lidvid" : "lid", lidvid)
              .setIndex(this.context.getConnectionFactory().getIndexName())
              .setSize(this.PAGE_SIZE)).documents();
        if (docs.size() == 1) {
          this.documents.put(lidvid, docs.get(0));
        } else {
          ArrayList<String> ids = new ArrayList<String>();
          for (Map<String,Object> doc : docs) {
            ids.add(doc.get("lidvid").toString());
          }
          ids.sort(lidvid_compare);
          for (Map<String,Object> doc : docs) {
            if (doc.get("lidvid").toString().equals(ids.get(ids.size() - 1))) {
              this.documents.put(lidvid, doc);
              this.documents.put(ids.get(ids.size() - 1), doc);
            }
          }
        }
      } catch (Exception e) {
        this.log.fatal("Error reading from URL: " + this.context.getURL(), e);    
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void load_refs(String lidvid) {
    if (!this.references.containsKey(lidvid)) {
      try {
        synchronized (this) {
          if (this.client == null) this.client = this.context.getConnectionFactory().createRestClient();
        }
        HashSet<String> newbies = new HashSet<String>();
        List<Map<String,Object>> docs = this.client.performRequest(
            this.client.createSearchRequest()
              .buildTermQuery(lidvid.contains("::") ? "collection_lidvid" : "collection_lid", lidvid)
              .setIndex(this.context.getConnectionFactory().getIndexName() + "-refs")
              .setSize(this.PAGE_SIZE)).documents();
            for (Map<String,Object> doc : docs) {
              newbies.addAll((List<String>)doc.get("product_lid"));
              newbies.addAll((List<String>)doc.get("product_lidvid"));
            }
          this.references.put(lidvid, new ArrayList<String>(newbies));
      } catch (Exception ioe) {
        this.log.fatal("Error reading from URL: " + this.context.getURL(), ioe);
      }
    }
  }

  public OpensearchDocument(AuthInformation context) {
    this.context = context;
  }

  @Override
  public boolean exists(String lidvid) {
    this.load(lidvid);
    return this.documents.containsKey(lidvid);
  }

  @Override
  public String getProductTypeOf(String lidvid) {
    String result = "";
    if (this.exists(lidvid)) {
      if (this.documents.get(lidvid).containsKey("product_class"))
        result = this.documents.get(lidvid).get("product_class").toString();
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<String> getReferencesOf(String lidvid) {
    ArrayList<String> references = new ArrayList<String>();
    if (this.exists(lidvid)) {
      if (this.getProductTypeOf(lidvid).equalsIgnoreCase("Product_Collection")) {
        load_refs(lidvid);
        references.addAll(this.references.get(lidvid));
      } else {
        for (String key : this.documents.get(lidvid).keySet()) {
          if (key.startsWith("ref_lid_")) {
            Object value = this.documents.get(lidvid).get(key);
            if (value instanceof String)
              references.add((String) value);
            else if (value instanceof List)
              references.addAll((List<String>) value);
          }
        }
      }
    }
    return references;
  }
}
