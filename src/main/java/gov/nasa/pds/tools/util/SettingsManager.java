package gov.nasa.pds.tools.util;

/**
 * Implements a class that manages settings for an application.
 */
public enum SettingsManager {

	INSTANCE;
	
	/**
	 * Gets a string settings identified by a key, or a default
	 * value if the setting has no specified value.
	 * 
	 * @param key the key for the setting
	 * @param defaultValue the default value
	 * @return the setting value, or the default if not specified
	 */
	public String getString(String key, String defaultValue) {
		String value = System.getProperty(key);
		return (value==null ? defaultValue : value);
	}
	
}
