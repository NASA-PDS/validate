package gov.nasa.pds.web.ui.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import gov.nasa.pds.web.ui.constants.ApplicationConstants;

public class PropsManager {

  private static Properties APPLICATION_PROPS;

  private static boolean inited = false;

  @SuppressWarnings("nls")
  public static Properties getApplicationProperties() {
    if (!inited) {
      inited = true;
      final InputStream is = PropsManager.class
          .getResourceAsStream("/" + ApplicationConstants.APPLICATION_PROPERTIES_FILENAME);

      try {
        APPLICATION_PROPS = new Properties();
        APPLICATION_PROPS.load(is);
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return APPLICATION_PROPS;
  }
}
