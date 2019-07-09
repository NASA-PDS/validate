package gov.nasa.pds.tools.web.ui.constants;

import java.io.File;
import java.util.Locale;

@SuppressWarnings("nls")
public interface ApplicationConstants {

	public static final String DEFAULT_LOCALE_KEY = "en_US";

	public static final String DEFAULT_LANGUAGE = "en";

	public static final Locale DEFAULT_LOCALE = new Locale(DEFAULT_LOCALE_KEY);

	public static final String DATA_SET_DIRECTORY_KEY = "dataSetRoot";

	public static final String APPLICATION_PROPERTIES_FILENAME = "config.properties";

	public static final String SETUP_SQL_FILENAME = "setup_db.sql";

	public static final String SQL_SCHEMA_VERSION_ROOT = "sql_schema_";

	public static final String MYSQL_USER_KEY = "mysql.user";

	public static final String MYSQL_USER_DEFAULT = "root";

	public static final String MYSQL_PASSWORD_KEY = "mysql.pass";

	public static final String MYSQL_PASSWORD_DEFAULT = "";

	public static final String MYSQL_PORT_KEY = "mysql.port";

	public static final String MYSQL_PORT_DEFAULT = "3306";

	public static final String MYSQL_DATABASE_KEY = "mysql.db";

	public static final String MYSQL_DATABASE_DEFAULT = "pds_web";

	public static final String MYSQL_SERVER_KEY = "mysql.server";

	public static final String MYSQL_SERVER_DEFAULT = "127.0.0.1";

	public static final String WEB_INF = File.separatorChar + "WEB-INF"
			+ File.separatorChar;
	
	public static final String VALIDATION_RESULT_FILE = "validation-results.dat";
	public static final String VALIDATION_REPORT_FILE = "validation-report.dat";
	
	public static final String APPLICATION_URL_KEY = "application.url";
	public static final String SERVER_SCHEME_DEFAULT = "http";
	public static final String SERVER_HOSTNAME_DEFAULT = "localhost";
	public static final String APPLICATION_CONTEXT_DEFAULT = "pdsWeb";

}
