package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Trip_M_Impl implements Trip_M_Dao {
	DataSource dataSource;

	public Trip_M_Impl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
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
	public List<Trip_M> getall() {
		List<Trip_M> tripMs= new ArrayList<Trip_M>();
		Trip_M tripM = null;
		String sql = "select" +
		"TRIP_ID, MEMBER_ID, TRIP_TITLE, S_DATE, S_TIME," + //5
		"D_COUNT, C_DATETIME, P_MAX, STATUS " + //4
		"C_DATETIME order by C_DATETIME desc ";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tripId = rs.getString(1);
				int memberId = rs.getInt(2);
				String tripTitle = rs.getString(3);
				String startDate = String.valueOf((rs.getDate(4)));
				String startTime = String.valueOf((rs.getTime(5)));
				int dayCount = rs.getInt(6);
				String createDateTime = String.valueOf((rs.getDate(7)));
				int pMax = rs.getInt(8);
				int status = rs.getInt(9);
				
				tripM = new Trip_M(tripId, memberId, tripTitle, startDate, 
						startTime, dayCount, createDateTime, pMax, status);
				tripMs.add(tripM);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripMs;
	}

}
