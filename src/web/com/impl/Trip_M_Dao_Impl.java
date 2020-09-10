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
	public int insert(Trip_M tripM) {
		int count = 0;
		String sql = "insert into Trip_M" +
		"( TRIP_ID, MEMBER_ID, TRIP_TITLE, S_DATE, S_TIME, " + // 5
		"D_COUNT, C_DATETIME, P_MAX, STATUS )" + // 4
		"values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripM.getTripId());
			ps.setInt(2, tripM.getMemberId());
			ps.setString(3, tripM.getTripTitle());
			ps.setTimestamp(4, Timestamp.valueOf(tripM.getStartDate()));
			ps.setTimestamp(5, Timestamp.valueOf(tripM.getStartTime()));
			ps.setInt(6, tripM.getDayCount());
			ps.setTimestamp(7, Timestamp.valueOf(tripM.getCreateDateTime()));
			ps.setInt(8, tripM.getpMax());
			ps.setInt(9, tripM.getStatus());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_M tripM) {
		int count = 0;
		String sql = " update Trip_M set " + 
		"TRIP_TITLE = ?, "     +
		"S_DATE     = ?, "	   +
		"S_TIME     = ?, "     +
		"D_COUNT    = ?, "     +
		"P_MAX      = ?, "     + //5
		"where TRIP_ID = ?; " ;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripM.getTripTitle());
			ps.setTimestamp(2, Timestamp.valueOf(tripM.getStartDate()));
			ps.setTimestamp(3, Timestamp.valueOf(tripM.getStartTime()));
			ps.setInt(4, tripM.getDayCount());
			ps.setInt(5, tripM.getpMax());
			
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
	public Trip_M getTripId(String tripId) {
		Trip_M tripM = null;
		String sql = "select" +
		"MEMBER_ID, TRIP_TITLE, S_DATE, S_TIME," + // 4
		"D_COUNT, P_MAX " + // 2
		"where TRIP_ID = ? ";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int memberId = rs.getInt(1);
				String tripTitle = rs.getString(2);
				String startDate = String.valueOf((rs.getDate(3)));
				String startTime = String.valueOf((rs.getTime(4)));
				int dayCount = rs.getInt(5);
				int pMax = rs.getInt(6);
				
				tripM = new Trip_M(memberId, tripTitle, startDate, startTime, dayCount, pMax);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripM;
	}

	@Override
	public List<Trip_M> getAll() {
		List<Trip_M> tripMs = new ArrayList<Trip_M>();

		String sql = "SELECT * FROM TRIPPER.Trip_M;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tripId = rs.getString(1);
				int memberId = rs.getInt(2);
				String tripTitle = rs.getString(3);
//				String startDate = String.valueOf((rs.getDate(4)));
//				String startTime = String.valueOf((rs.getTime(5)));
//				int dayCount = rs.getInt(6);
//				String createDateTime = String.valueOf((rs.getDate(7)));
				int pMax = rs.getInt(9);
//				int status = rs.getInt(9);
				
				Trip_M tripM = new Trip_M(tripId, memberId, tripTitle , pMax);
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
