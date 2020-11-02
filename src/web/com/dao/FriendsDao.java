package web.com.dao;

import java.util.List;

import web.com.bean.Friends;
import web.com.bean.Member;

/**
* 類別說明：好友資料Dao檔
* 
* @author Weixiang
* @version 建立時間:Sep 4, 2020 16:10 PM
* 
*/
public interface FriendsDao {
	List<Member> getAll(int memberId);
	Friends findSearchFriend(int memberId, String account);
	int insert(int memberId, int friendId, int status);
	int update(int friendId, int memberId, int status);
	int delete(int friendId, int memberId);
}
