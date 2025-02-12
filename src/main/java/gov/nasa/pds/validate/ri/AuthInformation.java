package gov.nasa.pds.validate.ri;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.NotImplementedException;
import gov.nasa.pds.registry.common.ConnectionFactory;
import gov.nasa.pds.registry.common.EstablishConnectionFactory;

public class AuthInformation {
  final private String apiAuthFile;
  final private String osAuthFile;
  final private String regConn;
  private transient ConnectionFactory factory = null;
  private AuthInformation(String a, String A, String r) {
    this.apiAuthFile = A;
    this.osAuthFile = a;
    this.regConn = r;
  }
  public static AuthInformation buildFrom(CommandLine cl) {
    return new AuthInformation(
        cl.getOptionValue("a",""),
        cl.getOptionValue("A",""),
        cl.getOptionValue("r","app://connections/cognito/registry.xml"));
  }
  public synchronized ConnectionFactory getConnectionFactory() throws Exception {
    if (this.factory == null) {
      if (!this.apiAuthFile.isBlank()) {
        throw new NotImplementedException();
      }
      if (!this.osAuthFile.isBlank()) {
        this.factory = EstablishConnectionFactory.from(this.regConn, this.osAuthFile);
      }
      if (this.factory == null) {
        throw new IllegalArgumentException("did not supply necessary arguments on the CLI");
      }
    }
    return this.factory;
  }
  public String getURL() {
    return factory != null ? this.factory.toString() : "uninitialized connection factory";
  }
}
