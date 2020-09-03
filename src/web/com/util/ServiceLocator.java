package web.com.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
* 類別說明：建立資料庫連線
* @author Connor Fan
* @version 建立時間:Sep 2, 2020 2:00:52 PM
* 
*/
public class ServiceLocator {
	private static final String TAG = "TAG_ServiceLocator_";
	private Context initContext;
	// 實體化
	private static ServiceLocator serviceLocator = new ServiceLocator();
	
	// 提供外部呼叫
	public static ServiceLocator getInstance() {
		return serviceLocator;
	}
	
	private ServiceLocator() {
		try {
			this.initContext = new InitialContext();
		} catch (NamingException e) {
			System.out.println(TAG + "ServiceLocator fail:" + e.getMessage());
		}
	}
	
	// 預設連至Tripper DB
	public DataSource getDataSource() {
		DataSource dataSource = null;
		try {
			Context cxt = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) cxt.lookup("jdbc/TRIPPER");
		} catch (Exception e) {
			System.out.println(TAG + "getDataSource fail:" + e.getMessage());
		}
		return dataSource;
	}
	
	// 依DB Name連線
	public DataSource getDataSource(String dataSourceName) {
		DataSource dataSource = null;
		try {
			Context cxt = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) cxt.lookup("jdbc/" + dataSourceName);
		} catch (Exception e) {
			System.out.println(TAG + "getDataSource fail:" + e.getMessage());
		}
		return dataSource;
	}
	

}
