package gov.nasa.pds.validate.ri;

import java.util.List;

public class RegistryDocument implements DocumentInfo {
  final private AuthInformation context;
  
  public RegistryDocument(AuthInformation context) {
    this.context = context;
  }

  @Override
  public boolean exists(String lidvid) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getProductTypeOf(String lidvid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getReferencesOf(String lidvid) {
    // TODO Auto-generated method stub
    return null;
  }

}
