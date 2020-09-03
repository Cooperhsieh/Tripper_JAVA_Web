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
		int insert(Member member ,byte[] photo , byte[] backgroundImage) ;
		
		int update(Member member ,byte[] photo , byte[] backgroundImage) ;
		
		int delete(int id);
		
		Member findByKey(String account,String password) ;
		
		List<Member> selectAll();
		
		byte[] getphoto(int id);
		
		byte[] getbackground(int id);
}
