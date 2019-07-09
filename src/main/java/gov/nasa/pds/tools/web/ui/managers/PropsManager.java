package gov.nasa.pds.tools.web.ui.managers;

import gov.nasa.pds.tools.web.ui.constants.ApplicationConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsManager {

	private static Properties APPLICATION_PROPS;

	private static boolean inited = false;

	@SuppressWarnings("nls")
	public static Properties getApplicationProperties() {
		if (inited == false) {
			inited = true;
			final InputStream is = PropsManager.class.getResourceAsStream("/"
					+ ApplicationConstants.APPLICATION_PROPERTIES_FILENAME);

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
