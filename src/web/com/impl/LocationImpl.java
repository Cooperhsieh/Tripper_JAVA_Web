package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Location;
import web.com.dao.LocationDao;
import web.com.util.ServiceLocator;

/**
* 類別說明：Location Dao 實作類別
* @author Connor Fan
* @version 建立時間:Sep 2, 2020 1:39:31 PM
* 
*/
public class LocationImpl implements LocationDao{
	DataSource dataSource;
	
	public LocationImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	
	@Override
	public int insert(Location loc, byte[] image) {		
		int count = 0;
		String sql = "insert into LOCATION ( " +
				"LOC_ID, NAME, ADDRESS, LOC_PIC, LOC_TYPE, " // 5
				+ "CITY, INFO, LONGITUDE, LATITUDE, CREATE_ID, " // 10
				+ "M_USER_ID" + 
				") values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, loc.getLocId());
			ps.setString(2, loc.getName());
			ps.setString(3, loc.getAddress());
			ps.setBytes(4, image);
			ps.setString(5, loc.getLocType());
			ps.setString(6, loc.getCity());
			ps.setString(7, loc.getInfo());
			ps.setDouble(8, loc.getLongitude());
			ps.setDouble(9, loc.getLatitude());
			ps.setInt(10, loc.getCreateId());
			ps.setInt(11, loc.getUseId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Location loc, byte[] image) {
		int count = 0;
		String sql = "";
		// 更新image欄位
		if(image != null) {
			sql = " update LOCATION set " +
						"NAME       = ?, " +
						"ADDRESS    = ?, " +
						"LOC_TYPE   = ?, " +
						"CITY       = ?, " +
						"INFO       = ?, " + // 5
						"LONGITUDE  = ?, " +
						"LATITUDE   = ?, " +
						"M_USER_ID  = ?, " + 
						"LOC_PIC    = ?" + 
				   " where LOC_ID = ? ;" ; // 10
		}else {
			// 不更新image
			sql = " update LOCATION set " +
					"NAME       = ?, " +
					"ADDRESS    = ?, " +
					"LOC_TYPE   = ?, " +
					"CITY       = ?, " +
					"INFO       = ?, " + // 5
					"LONGITUDE  = ?, " +
					"LATITUDE   = ?, " +
					"M_USER_ID  = ?" + 
			   " where LOC_ID = ? ;" ; // 9
		}
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, loc.getName());
			ps.setString(2, loc.getAddress());
			ps.setString(3, loc.getLocType());
			ps.setString(4, loc.getCity());
			ps.setString(5, loc.getInfo());
			ps.setDouble(6, loc.getLongitude());
			ps.setDouble(7, loc.getLatitude());
			ps.setInt(8, loc.getUseId());
			if(image != null) {
				ps.setBytes(9, image);
				ps.setString(10, loc.getLocId());
			}else {
				ps.setString(9, loc.getLocId());
			}
			System.out.println("Location Update:: " + ps.toString());
			count  = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(String locId) {
		int count = 0;
		String sql = " delete from LOCATION where LOC_ID = ? ;";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, locId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Location getLocById(String locId) {
		Location loc = null;
		String sql = " select " +
			" NAME, ADDRESS, LOC_TYPE, CITY, INFO," + // 5
			" LONGITUDE, LATITUDE, CREATE_ID, M_USER_ID, C_DATETIME " + // 10
			" where LOC_ID = ? ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, locId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String name = rs.getString(1);
				String address = rs.getString(2);
				String locType = rs.getString(3);
				String city = rs.getString(4);
				String info = rs.getString(5);
				double longitude = rs.getDouble(6);
				double latitude = rs.getDouble(7);
				int createId = rs.getInt(8);
				int useId = rs.getInt(9);
				Timestamp createDateTime = rs.getTimestamp(10);
				loc = new Location(name, address, locType, city, info, longitude, latitude, createId, useId, createDateTime);	
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loc;
	}

	@Override
	public List<Location> getAll() {
		List<Location> locations = new ArrayList<Location>();
		Location loc = null;
		String sql = " select " +
				" LOC_ID, NAME, ADDRESS, LOC_TYPE, CITY, " + // 5
				" INFO, LONGITUDE, LATITUDE, CREATE_ID, M_USER_ID,  " + // 10
				" C_DATETIME from LOCATION order by M_DATETIME desc ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String locId = rs.getString(1);
				String name = rs.getString(2);
				String address = rs.getString(3);
				String locType = rs.getString(4);
				String city = rs.getString(5);
				String info = rs.getString(6);
				double longitude = rs.getDouble(7);
				double latitude = rs.getDouble(8);
				int createId = rs.getInt(9);
				int useId = rs.getInt(10);
				Timestamp createDateTime = rs.getTimestamp(11);
				loc = new Location(locId, name, address, locType, city, info, longitude, latitude, createId, useId, createDateTime);
				locations.add(loc);
			}
			return locations;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return locations;
	}

	@Override
	public byte[] getImageById(String locId) {
		byte[] image = null;
		String sql = " select LOC_PIC from LOCATION where LOC_ID = ? ;" ;
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, locId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	

}
