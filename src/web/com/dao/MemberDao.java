package web.com.dao;

import web.com.bean.Member;

/**
* 類別說明：會員Dao檔
* @author zhitin 
* @version 建立時間:Sep 3, 2020 
* 
*/

public interface MemberDao {
	//新增帳號
		int insert(Member member ) ;
	//新增第三方的帳號	
		int insertGB(Member member);
	//修改會員資料	
		int update(Member member ,byte[] photo ) ;
	//透過帳號查找資訊			
		Member findByAccount(String account) ;
	//透過ID找本帳號資訊(ID,ACCOUNT,PASSWORD,NICKNAME)			
		Member findById(int id);		
	//檢查帳號是否存在	
		int selectAccount(Member member);
	//透過id抓大頭貼照	
		byte[] getP_picById(int id);
	//透過id抓封面照	
		byte[] getB_picById(int id);
	//檢查密碼
		int selectAandP(Member member);
	//更改密碼
		int updatePassword(String account,String newPassword);
	//新增管理者帳號
		int insertManager(Member member ) ;
	//檢查管理者資訊
		//檢查密碼
		int selectManagerAandP(Member member);
	//檢查管理者帳號是否存在	
		int selectManagerAccount(Member member);	
}
