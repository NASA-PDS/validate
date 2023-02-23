package gov.nasa.pds.validate.ri;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Cylinder implements Runnable {
  final private AuthInformation registry;
  final private AuthInformation search;
  final private CamShaft cam;
  final private Logger log = LogManager.getLogger(Cylinder.class);
  final public Logger reporter = LogManager.getLogger("Reference Integrity");
  final private String lidvid;

  public Cylinder (String lidvid, AuthInformation registry, AuthInformation search, CamShaft cam) {
    this.cam = cam;
    this.lidvid = lidvid;
    this.registry = registry;
    this.search = search;
  }

  private boolean has_children(DocumentInfo search) {
    boolean result = false;
    if (search.exists(this.lidvid)) {
      String type = search.getProductTypeOf(this.lidvid);
      result = "Product_Bundle".equalsIgnoreCase(type) || "Product_Collection".equalsIgnoreCase(type);
    }
    return result;
  }

  public void run() {
    try {
      ArrayList<String> referenced_valid_lidvids = new ArrayList<String>();
      DocumentInfo registry = new RegistryDocument(this.registry);
      DocumentInfo search = new OpensearchDocument(this.search);
    
      if (search.exists(this.lidvid)) {
        this.log.info("The lidvid '" + this.lidvid + "' is of type: " + search.getProductTypeOf(this.lidvid));
        for (String reference : search.getReferencesOf(this.lidvid)) {
          if (search.exists(reference)) referenced_valid_lidvids.add(reference);
          else this.reporter.error ("In the search the lidvid '" + this.lidvid + "' references '" + reference + "' that is missing in the database.");
        }
      } else this.reporter.error("The given lidvid '" + this.lidvid + "' is missing from the database.");

      if (!AuthInformation.NO_AUTH.equals(this.registry)) {
        if (registry.exists(this.lidvid)) {
          this.log.info("In the registry-api the lidvid '" + this.lidvid + "' is of type: " + registry.getProductTypeOf(this.lidvid));
          List<String> members = registry.getReferencesOf(this.lidvid);
          for (String member : members) {
            if (!referenced_valid_lidvids.contains(member))
              this.reporter.error("For the lidvid '" + this.lidvid + "' the registry-api erroneously references the lidvid '" + member + "'.");
          }
          for (String reference : referenced_valid_lidvids) {
            if (!members.contains(reference))
              this.reporter.error("For the lidvid '" + this.lidvid + "' the registry-api erroneously failed to reference the lidvid '" + reference + "'.");
          }
        } else this.reporter.error("The given lidvid '" + this.lidvid + "' is missing from the registry");
      }

      if (this.has_children (search)) this.cam.addAll(referenced_valid_lidvids);
    }
    finally { this.cam.replace(this); }
  }
}
