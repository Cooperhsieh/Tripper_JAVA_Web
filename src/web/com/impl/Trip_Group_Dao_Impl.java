package web.com.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

import web.com.bean.TripGroupMember;
import web.com.bean.Trip_Group;
import web.com.dao.Trip_Group_Dao;
import web.com.util.ServiceLocator;


/**
 * 類別說明：Trip_Group_Dao_Impl檔
 * 
 * @author cooper
 * @version 建立時間:Sep 5, 2020
 * 
 */

public class Trip_Group_Dao_Impl implements Trip_Group_Dao {
	DataSource datasource;
	
	public Trip_Group_Dao_Impl() {
		datasource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public int insert(Trip_Group tripGroup) {
		int count = 0 ;
		String sql = " insert into Trip_Group " +
		"( GROUP_TRANS_ID, TRIP_ID, C_DATETIME, MEMBER_ID )" +
		"values (? , ?, ?)" ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, tripGroup.getGroupTransId());
			ps.setString(2, tripGroup.getTripId());
			ps.setTimestamp(3, Timestamp.valueOf(tripGroup.getCreateDateTime()));
			ps.setInt(4, tripGroup.getMemberId());
			
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_Group tripGroup) {
		return 0;
//		int count = 0 ;
//		String sql = "update Trip_Group set MEMBER_ID = ?, " +
//		"where GROUP_TRANS_ID = ? ; " ;
//		
//		try (Connection connection = datasource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql); ) {
//			
//			ps.setInt(1, tripGroup.getMemberId());
//			count = ps.executeUpdate();
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return count;
	}

	@Override
	public int delete(String tripId) {
		int count = 0 ;
		String sql = "delete from Trip_Group where TRIP_ID = ? ; " ;	
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ) {	
			ps.setString(1, tripId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<TripGroupMember> findGroupTripId(String groupTripId) {
		List<TripGroupMember> tripGroups = new ArrayList<>();
		String sql = " select " +
		"a.TRIP_ID, a.MEMBER_ID, b.NICKNAME " +
		"from TRIP_GROUP a inner join MEMBER b " +
		"on a.MEMBER_ID = b.MEMBER_ID "+
		"where TRIP_ID  = ? " ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			
			ps.setString(1, groupTripId);
			System.out.println("###findGroupTripId sql:: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String tripId = rs.getString(1);
				int memberId = rs.getInt(2);
				String nickName = rs.getString(3);
				tripGroups.add(new TripGroupMember(tripId, memberId, nickName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripGroups;
	}

	@Override
	public List<Trip_Group> getAll() {
		List<Trip_Group> tripGroups = new ArrayList<Trip_Group>();
		Trip_Group tripGroup = null;
		String sql = " select " +
				"GROUP_TRANS_ID, TRIP_ID, C_DATETIME, MEMBER_ID" +
				"GROUP_TRANS_ID order by GROUP_TRANS_ID desc " ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String groupTransId = rs.getString(1);
				String tripId = rs.getString(2);
				String createDateTime = String.valueOf((rs.getDate(3)));
				int memberId = rs.getInt(4);
				
				tripGroup = new Trip_Group(groupTransId, tripId, createDateTime, memberId);
				
				tripGroups.add(tripGroup);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripGroups;
	}



}
