package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Trip_D;
import web.com.dao.Trip_D_Dao;
import web.com.util.ServiceLocator;

/**
 * 類別說明：Trip_D_Impl檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */


public class Trip_D_Dao_Impl implements Trip_D_Dao {
	DataSource dataSource;
	
	public Trip_D_Dao_Impl () {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	

	@Override
	public int insert(Trip_D tripD) {
		int count = 0;
		String sql = "";
		sql = " insert into Trip_D " +
				"( TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID, S_DATE, S_TIME,  STAYTIME, MEMO ) " + 
				"values (? , ? , ? , ? , ? , ? , ? ,?) " ; 
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1, tripD.getTransId());
			ps.setString(2, tripD.getTripId());
			ps.setInt(3, tripD.getSeqNo());
			ps.setString(4, tripD.getLocId());
			ps.setString(5, tripD.getStartDate());
			ps.setString(6, tripD.getStartTime());
			ps.setString(7, tripD.getStayTime());
			ps.setString(8, tripD.getMemo());
			System.out.println("insert tripD sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_D tripD) {
		int count = 0 ;
		String sql = "update Trip_D set " +
		"TRANS_ID = ?, " +
		"TRIP_ID = ?, "  +
		"SEQ_NO = ?, "   +
		"LOC_ID = ? ,"   +
		"S_DATE = ? , "  +  // 5
		"S_TIME = ? , "  +
		"STAYTIME = ? ," +
		"MEMO = ? "     +  
		"where TRIP_ID = ?; " ;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripD.getTransId());
			ps.setString(2, tripD.getTripId());
			ps.setInt(3, tripD.getSeqNo());
			ps.setString(4, tripD.getLocId());
			ps.setString(5, tripD.getStartDate());
			ps.setString(6, tripD.getStartTime());
			ps.setString(7, tripD.getStayTime());
			ps.setString(8, tripD.getMemo());
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(String tripId) {
		int count = 0 ;
		String sql = " delete from Trip_D where TRIP_ID = ? ;" ;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// fix by Connor 2020/09/22
	@Override
	public List<Trip_D> findTransId(String tripId) {
		List<Trip_D> tripDs = new ArrayList<Trip_D>();
		Trip_D tripD;
		String sql = " select " +
		"TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID ,STAY_TIME, MEMO " +
		"from trip_d " +
		"where TRIP_ID = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			System.out.println("findTransId :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String transId = rs.getString("TRANS_ID");
				String tripID = rs.getString("TRIP_ID");
				int seqNo = rs.getInt("SEQ_NO");
				String locId = rs.getString("LOC_ID");
				String stayTime = rs.getString("STAY_TIME");
				String memo = rs.getString("MEMO");
				tripD = new Trip_D(transId, tripID, seqNo, locId, stayTime, memo);
				tripDs.add(tripD);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripDs;
	}



	@Override
	public List<Trip_D> getAll() {
		List<Trip_D> tripDs = new ArrayList<Trip_D>();
		Trip_D tripD = null;
		String sql = " select " +
				"TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID, S_DATE," + //5
				"S_TIME, STAYTIME , MEMO" +  // 8
				"TRANS_ID order by TRANS_ID desc";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				String transId = rs.getString(1);
				String tripId = rs.getString(2);
				int seqNo = rs.getInt(3);
				String locId = rs.getString(4);
				String startDate = String.valueOf(rs.getDate(5));
				String startTime = String.valueOf(rs.getTime(6));
				String stayTime = String.valueOf(rs.getTime(7));
				String memo = rs.getString(8);
				
				tripD = new Trip_D(transId, tripId, seqNo, locId, startDate,
						startTime, stayTime, memo);
				tripDs.add(tripD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripDs;
	}

}
