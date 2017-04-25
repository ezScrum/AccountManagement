package SQLService;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class ConnectionPoolManager {
	private static ConnectionPoolManager mInstance = null;
	private Map<String, DataSource> mPoolMap = null;
	
	private ConnectionPoolManager() {
		mPoolMap = new HashMap<String, DataSource>();
	}

	public static ConnectionPoolManager getInstance() {
		if (mInstance == null) {
			mInstance = new ConnectionPoolManager();
		}
		return mInstance;
	}
	public DataSource getConnectionPool(String driverClass, String url, String account, String password) {
		DataSource dataSource = mPoolMap.get(url);
		if (dataSource == null)
			dataSource = createDataSource(driverClass, url, account, password);
		return dataSource;
	}
	public void RemoveConnectionPool(String url) {
		mPoolMap.remove(url);
	}
	private DataSource createDataSource(String driverClass, String url, String account, String password) {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driverClass);
			dataSource.setJdbcUrl(url);
			dataSource.setUser(account);
			dataSource.setPassword(password);

			dataSource.setMinPoolSize(10);
			dataSource.setMaxPoolSize(35);
			dataSource.setAcquireIncrement(0);
			dataSource.setMaxStatements(0);
			
			/** �̤j���\�����m�ɶ�(��) */
			dataSource.setMaxIdleTime(300);
			/** �ﶢ�m���s�u�i��Query���ճ]�m(��) */
			dataSource.setIdleConnectionTestPeriod(1800);
			
			/** Checkin connection�ɤ��ˬd�s�u�O�_���� */
			dataSource.setTestConnectionOnCheckin(false);
			/** Checkout connection���ˬd�s�u�����ĩ�*/
			dataSource.setTestConnectionOnCheckout(true);
			/** �i��test�ɨϥΪ� Query�]�w*/
			dataSource.setPreferredTestQuery("SELECT 1;");
			/** Connection���̤j���Įɼ�(��)*/
			dataSource.setMaxConnectionAge(28800);
			/** Connection checkout ���᪺���Įɼ�(�@��)*/
			dataSource.setCheckoutTimeout(28800000);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		mPoolMap.put(url, dataSource);
		return dataSource;
	}
}
