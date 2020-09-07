package web.com.dao;

import java.util.List;

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
		
		int update(Member member ,byte[] photo ) ;
		
		int delete(int id);
		
		Member findByAccount(String account) ;
		
		Member findById(int id);
		
//		List<Member> selectAll();
	//檢查帳號是否存在	
		int selectAccount(Member member);
		
		byte[] getP_picById(int id);
		
		byte[] getB_picById(int id);
	//檢查密碼
		int selectAandP(Member member);
}
