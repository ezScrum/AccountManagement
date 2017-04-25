package SQLService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;


public class MySQLControl {
	String mHost;
	String mPort;
	String mDbName;
	String mUser;
	String mPassword;
	Configuration mConfig = null;

	Connection mConnection = null;
	DataSource mDataSource = null;

	String[] mKeys;
	public MySQLControl(Configuration config) {
		mConfig = config;
		mHost = config.getServerUrl();
		mPort = "3306";
		mUser = config.getDBAccount();
		mPassword = config.getDBPassword();
		mDbName = config.getDBName();
		loadDriver();
	}
	
	public MySQLControl(String host, String port, String dbName) {
		mHost = host;
		mPort = port;
		mDbName = dbName;
		loadDriver();
	}
	private void loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void setUser(String user) {
		mUser = user;
	}

	public void setPassword(String password) {
		mPassword = password;
	}
	
	private void setKeys(Statement statement, String query) throws SQLException {
		statement.execute(query, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = statement.getGeneratedKeys();

		if (keys.next()) {
			ResultSetMetaData _metadata = keys.getMetaData();
			int columnCount = _metadata.getColumnCount();
			mKeys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				mKeys[i - 1] = keys.getString(i);
			}
		} else {
			mKeys = new String[0];
		}
	}
	
	public String[] getKeys() {
		return mKeys;
	}
	private String getURL() {
		return "jdbc:mysql://" + mHost + ":" + mPort + "/" + mDbName + "?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
	}
	private String getServerURL() {
		return "jdbc:mysql://" + mHost + ":" + mPort;
	}
	public Connection getconnection() {
		return mConnection;
	}
	
	public void connect() {
		try {
			if (mDataSource == null) {
				mDataSource = ConnectionPoolManager.getInstance().getConnectionPool("com.mysql.jdbc.Driver", getURL(), mUser, mPassword);
			}

			// �u���bConnection��null�Ϊ̬OConnection�w�gClose�����p�U�~�i��Connection
			if (mConnection == null || mConnection.isClosed())
				mConnection = mDataSource.getConnection();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			mDataSource = null;
			ConnectionPoolManager.getInstance().RemoveConnectionPool(getURL());
		}
	}
	
	public void reconnect() {
		try {
			mDataSource = ConnectionPoolManager.getInstance().getConnectionPool("com.mysql.jdbc.Driver", getURL(), mUser, mPassword);
			mConnection = mDataSource.getConnection();
		} catch(SQLException e) {
			mDataSource = null;
			ConnectionPoolManager.getInstance().RemoveConnectionPool(getURL());
		}
	}
	
	public void connectToServer() {
		try {
			mConnection = DriverManager.getConnection(getServerURL(), mUser, mPassword);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Long executeInsert(String query) {
		execute(query, true);
		return Long.parseLong(mKeys[0]);
	}
	
	public boolean execute(String query) {
		return execute(query, false);
	}
	public boolean execute(String query, boolean returnKeys) {
		try {
			Statement statement = mConnection.createStatement();
			if (returnKeys) {
				setKeys(statement, query);
			} else {
				return statement.execute(query);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	/**
	 * for insert, update, delete
	 */
	public boolean executeUpdate(String query) {
		return executeUpdate(query, false);
	}

	/**
	 * for insert, update, delete
	 */
	public boolean executeUpdate(String query, boolean returnKeys) {
		try {
			Statement statement = mConnection.createStatement();
			if (returnKeys) {
				setKeys(statement, query);
			} else {
				if (statement.executeUpdate(query) > 0) {
					return true;
				}
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public ResultSet executeQuery(String query) {
		ResultSet result = null;
		
		// �i�� MySQL connection ����
		try {
			Statement statement = mConnection.createStatement();
			statement.execute("Select 1;");
			statement.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			reconnect();
		}
		
		try {
			Statement statement = mConnection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return result;
	}
	public void close() {
		try {
			mConnection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
