package web.com.dao;

import java.util.List;
import java.util.Map;

import web.com.bean.Blog_SpotInfo;
import web.com.bean.DateAndId;
import web.com.bean.Location_D;
import web.com.bean.Trip_D;
import web.com.bean.Trip_LocInfo;

/**
 * 類別說明：Trip_D_Dao檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public interface Trip_D_Dao {

	int insert(Trip_D tripD);

	int update(Trip_D tripD);

	int delete(String tripId);

	List<Trip_D> findTransId(String tripId);

	List<Trip_D> getAll();

	List<Blog_SpotInfo> getSpotName(DateAndId dateAndId);
	
	List<Trip_LocInfo> getLocName(DateAndId dateAndId);
	
	Map<String, List<Location_D>> showLocName(String tripId);
	

}
