package gov.nasa.pds.validate.ri;

import gov.nasa.pds.harvest.cfg.ConfigManager;
import gov.nasa.pds.harvest.cfg.RegistryType;
import gov.nasa.pds.registry.common.ConnectionFactory;

public class AuthInformation {
  final private RegistryType registry;
  private AuthInformation(RegistryType registry) {
    this.registry = registry;
  }
  public static AuthInformation buildFrom(String filename) {
    return new AuthInformation(ConfigManager.read(filename).getRegistry());
  }
  public ConnectionFactory getConnectionFactory() throws Exception {
    return ConfigManager.exchangeRegistry(registry);
  }
  public String getURL() {
    return this.registry.getValue();
  }
}
