package gov.nasa.pds.validate.ri;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cylinder implements Runnable {
  final private AuthInformation search;
  final private CamShaft cam;
  final private Logger log = LogManager.getLogger(Cylinder.class);
  final public Logger reporter = LogManager.getLogger("Reference Integrity");
  final private String lidvid;
  private long broken = 0;

  public Cylinder(String lidvid, AuthInformation search, CamShaft cam) {
    this.cam = cam;
    this.lidvid = lidvid;
    this.search = search;
  }

  private boolean has_children(DocumentInfo search) {
    boolean result = false;
    if (search.exists(this.lidvid)) {
      String type = search.getProductTypeOf(this.lidvid);
      result =
          "Product_Bundle".equalsIgnoreCase(type) || "Product_Collection".equalsIgnoreCase(type);
    }
    return result;
  }

  public long getBroken() {
    return broken;
  }

  public void run() {
    try {
      ArrayList<String> referenced_valid_lidvids = new ArrayList<String>();
      DocumentInfo search = new OpensearchDocument(this.search);

      if (search.exists(this.lidvid)) {
        this.log.info(
            "The lidvid '" + this.lidvid + "' is of type: " + search.getProductTypeOf(this.lidvid));
        for (String reference : search.getReferencesOf(this.lidvid)) {
          if (search.exists(reference))
            referenced_valid_lidvids.add(reference);
          else {
            this.broken++;
            this.reporter.error("In the search the lidvid '" + this.lidvid + "' references '"
                + reference + "' that is missing in the database.");
          }
        }
      } else
        this.reporter.error("The given lidvid '" + this.lidvid + "' is missing from the database.");

      if (this.has_children(search))
        this.cam.addAll(referenced_valid_lidvids);
    } finally {
      this.cam.replace(this);
    }
  }
}
