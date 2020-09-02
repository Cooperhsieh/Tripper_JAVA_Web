package web.com.dao;

import java.util.List;

import web.com.bean.Location;

/**
* 類別說明：interface for Location
* @author Connor Fan
* @version 建立時間:Sep 2, 2020 1:23:05 PM
* 
*/
public interface LocationDao {
	int insert(Location loc, byte[] image);
	int update(Location loc, byte[] image);
	int delete(String locId);
	Location getLocById(String locId);
	List<Location> getAll();
	byte[] getImageById(String locId);
}
