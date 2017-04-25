package SQLService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import ntut.csie.ezScrum.issue.sql.service.core.JettyServerXMLReader;

public class Configuration {
	private Properties properties;
	public String USER_ID = "admin";	// user ID
	private String BASEDIR_PATH = getBaseDirPath();
	private final String PERFS_FILE_NAME = ".ini";// ini file path
	private final String PERFS_FILE_PATH = BASEDIR_PATH + File.separator + PERFS_FILE_NAME;
	private final String SERVER_URL = "ServerUrl";
//	private final String SERVICE_PATH = "ServicePath";
	private final String ACCOUNT = "Account";
	private final String PASSWORD = "Password";
	private final String DATABASE_TYPE = "DatabaseType";
	private final String DATABASE_NAME = "DatabaseName";
	
	public Configuration() {
		init();
	}
	private void init() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(PERFS_FILE_PATH));
		} catch (IOException e) {
			System.out.println(
					"************ ERROR MESSAGE ************\n\n\n" +
					"Please check \"ezScrum.ini\" file exist.\n\n\n" +
					"***************************************\n\n\n"
			);
			System.exit(0);
		}
	}
	
	public String getServerUrl() {
		return properties.getProperty(SERVER_URL);
	}
	public String getDBAccount() {
		return properties.getProperty(ACCOUNT);
	}
	public String getDBPassword() {
		return properties.getProperty(PASSWORD);
	}
	public String getDBType() {
		return properties.getProperty(DATABASE_TYPE);
	}
	public String getDBName() {
		return properties.getProperty(DATABASE_NAME);
	}
	public String getBaseDirPath() { // get user current working directory
		return System.getProperty("user.dir");
	}
	/**
	 * return service host with port ex. localhost:8080
	 */
	public String getServiceHostWithPort() {
		return JettyServerXMLReader.readHostWithPort();
	}
//	public String getWebServicePath() {
//		return properties.getProperty(SERVICE_PATH);
//	}
	
}
