package web.com.dao;

import java.util.List;

import web.com.bean.Trip_M;

/**
 * 類別說明：Trip_M_Dao檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public interface Trip_M_Dao {

	int insert(Trip_M tripM, byte[] image);

	int update(Trip_M tripM, byte[] image);

	int delete(String tripId);

	List<Trip_M> getTripId(String memberId);

	List<Trip_M> getAll();
	
	List<Trip_M> getMyTrip(String memberId);

	byte[] getImage(String id);
	
	//透過會員ID 抓取是否為主揪人
	Trip_M getStatusById (String memberId);
	
	int changeBlogStatus (String tripId);

}
