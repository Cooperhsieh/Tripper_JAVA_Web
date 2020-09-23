package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Trip_M;
import web.com.dao.Trip_M_Dao;
import web.com.util.ServiceLocator;

/**
 * 類別說明：Trip_M_Impl檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public class Trip_M_Dao_Impl implements Trip_M_Dao {
	DataSource dataSource;

	public Trip_M_Dao_Impl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	
	@Override
	public int insert(Trip_M tripM, byte[] image) {
		int count = 0;
		String sql = "insert into Trip_M" +
		"( TRIP_ID, MEMBER_ID, TRIP_TITLE, S_DATE, S_TIME, " + // 5
		"D_COUNT, P_MAX, STATUS, B_PIC )" + // 4
		"values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			System.out.println("insert tripM sql::" + ps.toString());
			ps.setString(1, tripM.getTripId());
			ps.setInt(2, tripM.getMemberId());
			ps.setString(3, tripM.getTripTitle());
			ps.setTimestamp(4, Timestamp.valueOf(tripM.getStartDate()));
			ps.setTimestamp(5, Timestamp.valueOf(tripM.getStartTime()));
			ps.setInt(6, tripM.getDayCount());
			ps.setInt(7, tripM.getpMax());
			ps.setInt(8, tripM.getStatus());
			ps.setBytes(9, tripM.getbPic());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_M tripM, byte[] image) {
		int count = 0;
		String sql = "";
		if(image != null) {
			sql = " update Trip_M set " + 
					"TRIP_TITLE = ?, "     +
					"S_DATE     = ?, "	   +
					"S_TIME     = ?, "     +
					"D_COUNT    = ?, "     +
					"P_MAX      = ?, "     + //5
					"STATUS     = ?, "     +
					"B_PIC      = ?, "     +
					"where TRIP_ID = ?; " ;
		}else {
			sql = " update Trip_M set " + 
					"TRIP_TITLE = ?, "     +
					"S_DATE     = ?, "	   +
					"S_TIME     = ?, "     +
					"D_COUNT    = ?, "     +
					"P_MAX      = ?, "     + //5
					"STATUS     = ?, "     +
					"where TRIP_ID = ?; " ;
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {	
			ps.setString(1, tripM.getTripTitle());
			ps.setTimestamp(2, Timestamp.valueOf(tripM.getStartDate()));
			ps.setTimestamp(3, Timestamp.valueOf(tripM.getStartTime()));
			ps.setInt(4, tripM.getDayCount());
			ps.setInt(5, tripM.getpMax());
			ps.setInt(6, tripM.getStatus());
			if(image != null) {
				ps.setBytes(7, tripM.getbPic());
				ps.setString(8, tripM.getTripId());
			}else {
				ps.setString(7, tripM.getTripId());
			}
			System.out.println("update Trip_M sql :: " + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(String tripId) {
		int count = 0;
		String sql = "delete from Trip_M where TRIP_ID = ? ; ";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Trip_M> getTripId(String tripId) {
		List<Trip_M> tripMasters = new ArrayList<Trip_M>();
		Trip_M tripM = null;
		String sql = "select" +
		"MEMBER_ID, TRIP_TITLE, S_DATE, S_TIME," + // 4
		"D_COUNT, P_MAX " + // 2
		"where TRIP_ID = ? ";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			System.out.println("getTripId sql :" + sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int memberId = rs.getInt(1);
				String tripTitle = rs.getString(2);
				String startDate = String.valueOf((rs.getDate(3)));
				String startTime = String.valueOf((rs.getTime(4)));
				int dayCount = rs.getInt(5);
				int pMax = rs.getInt(6);
				tripM = new Trip_M(memberId, tripTitle, startDate, startTime, dayCount, pMax);
				tripMasters.add(tripM);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripMasters;
	}

	@Override
	public List<Trip_M> getAll() {
		List<Trip_M> tripMs = new ArrayList<Trip_M>();

		String sql = "SELECT * FROM Trip_M left join Trip_Group on Trip_M.TRIP_ID = Trip_Group.TRIP_ID ;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tripId = rs.getString(1);
				int memberId = rs.getInt(2);
				String tripTitle = rs.getString(3);
				String startDate = String.valueOf((rs.getDate(4)));
//				String startTime = String.valueOf((rs.getTime(5)));
//				int dayCount = rs.getInt(6);
//				String createDateTime = String.valueOf((rs.getDate(7)));
				int pMax = rs.getInt(9);
//				int status = rs.getInt(9);
				int mCount = rs.getInt(16);
				
				Trip_M tripM = new Trip_M(tripId, memberId, tripTitle ,startDate, pMax,mCount);
				tripMs.add(tripM);
			}
			return tripMs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripMs;
	}

	
	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT image FROM TRIPPER.Trip_M WHERE MEMBER_ID = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return image;
	}
}
