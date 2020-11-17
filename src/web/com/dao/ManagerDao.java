package web.com.dao;

import java.util.List;

import web.com.bean.Member;

/**
* 類別說明：管理者Dao檔
* @author zhitin 
* @version 建立時間:Nov 13, 2020 
* 
*/
public interface ManagerDao {

		//取得所有管理者資料	
		List<Member> getAll();
		//刪除該管理者
		int deleteManager(int memberId);
	
}
