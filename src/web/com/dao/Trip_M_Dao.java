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

	List<Trip_M> getTripId(String tripId);

	List<Trip_M> getAll();

	byte[] getImage(int id);

}
