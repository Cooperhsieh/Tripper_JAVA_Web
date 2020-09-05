package web.com.dao;

import java.util.List;

import web.com.bean.Friends;

/**
* 類別說明：好友資料Dao檔
* 
* @author Weixiang
* @version 建立時間:Sep 4, 2020 16:10 PM
* 
*/
public interface FriendsDao {
	
	int insert(Friends friends);
	
//	int update(Friends friends, byte[] photo);
	
	int delete(int memberId, int friendId);
	
	Friends findFriendTransId(String friendTransId);
	
	List<Friends> getAll();
	
	byte[] getPhoto(int id); // TODO
}
