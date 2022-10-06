package gov.nasa.pds.web.ui.utils;

import java.util.Properties;
import gov.nasa.pds.web.ui.constants.ApplicationConstants;
import gov.nasa.pds.web.ui.managers.PropsManager;

public class ApplicationProperties {

  private final Properties props;

  public ApplicationProperties() {
    this.props = PropsManager.getApplicationProperties();
  }

  public String getDataSetRoot() {
    return this.props.getProperty(ApplicationConstants.DATA_SET_DIRECTORY_KEY);
  }

  public String getMysqlUser() {
    return this.props.getProperty(ApplicationConstants.MYSQL_USER_KEY,
        ApplicationConstants.MYSQL_USER_DEFAULT);
  }

  public String getMysqlPass() {
    return this.props.getProperty(ApplicationConstants.MYSQL_PASSWORD_KEY,
        ApplicationConstants.MYSQL_PASSWORD_DEFAULT);
  }

  public String getMysqlPort() {
    return this.props.getProperty(ApplicationConstants.MYSQL_PORT_KEY,
        ApplicationConstants.MYSQL_PORT_DEFAULT);
  }

  public String getMysqlDB() {
    return this.props.getProperty(ApplicationConstants.MYSQL_DATABASE_KEY,
        ApplicationConstants.MYSQL_DATABASE_DEFAULT);
  }

  public String getMysqlServer() {
    return this.props.getProperty(ApplicationConstants.MYSQL_SERVER_KEY,
        ApplicationConstants.MYSQL_SERVER_DEFAULT);
  }

  public boolean exists() {
    return this.props != null;
  }

}
