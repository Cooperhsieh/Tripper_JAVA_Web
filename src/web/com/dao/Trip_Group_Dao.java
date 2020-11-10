package web.com.dao;

import java.util.List;

import web.com.bean.Member;
import web.com.bean.TripGroupMember;
import web.com.bean.Trip_Group;

/**
 * 類別說明：Trip_Group_Dao檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public interface Trip_Group_Dao {

	int insert(Trip_Group tripGroup);

	int update(Trip_Group tripGroup);

	int delete(String tripId);

	List<TripGroupMember> findGroupTripId(String groupTripId);

	List<Trip_Group> getAll();
//透過Trip_ID取得該揪團人數	
	int selectMCountByTripID(String Trip_ID);
//查找該行程是否有該會員已參加
	int selectMyGroup(String trip_Id,String memberId);
//允許加入揪團
	int agreeJoinGroup(Trip_Group tripGroup);
//退出揪團
	int deleteGroup(Trip_Group tripGroup);
//取得揪團成員列表
	List<Member> getMbrList(String tripId);
//取得成員申請列表
	List<Member> getApplicationList(String tripId);	
	
}
