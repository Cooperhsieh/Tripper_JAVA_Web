package web.com.dao;

import java.util.List;

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

	int delete(String tripId, int memberId);

	List<TripGroupMember> findGroupTripId(String groupTripId);

	List<Trip_Group> getAll();

}
