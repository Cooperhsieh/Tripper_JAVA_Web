package web.com.dao;

import java.util.List;

import web.com.bean.Blog_SpotInfo;
import web.com.bean.DateAndId;
import web.com.bean.Trip_D;

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

}
