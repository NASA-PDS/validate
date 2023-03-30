package gov.nasa.pds.validate.ri;

import java.util.List;

public interface DocumentInfo {
  boolean exists(String lidvid);

  String getProductTypeOf(String lidvid);

  List<String> getReferencesOf(String lidvid);
}
