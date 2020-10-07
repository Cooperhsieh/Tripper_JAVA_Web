package web.com.dao;

import web.com.bean.AppMessage;

/**
* 類別說明：
* @author Connor Fan
* @version 建立時間:Sep 29, 2020 7:04:25 PM
* 
*/
public interface FCMDao {
	
	// 更新token
	int update(String token, int memberId);
	// 取得user token
	String getToken(int memberId);
	// 寫入傳送的訊息
	int insertMsg(AppMessage msg);
}
