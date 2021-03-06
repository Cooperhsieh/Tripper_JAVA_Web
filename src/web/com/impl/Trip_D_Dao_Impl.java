package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.activation.MailcapCommandMap;
import javax.sql.DataSource;

import web.com.bean.Blog_SpotInfo;
import web.com.bean.DateAndId;
import web.com.bean.Location_D;
import web.com.bean.Trip_D;
import web.com.bean.Trip_LocInfo;
import web.com.bean.Trip_M;
import web.com.dao.Trip_D_Dao;
import web.com.util.ServiceLocator;
import web.com.util.SettingUtil;

/**
 * 類別說明：Trip_D_Impl檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public class Trip_D_Dao_Impl implements Trip_D_Dao {
	DataSource dataSource;

	public Trip_D_Dao_Impl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public int insert(Trip_D tripD) {
		int count = 0;
		String sql = "";
		sql = " insert into Trip_D " + "( TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID, S_DATE, S_TIME,  STAYTIME, MEMO ) "
				+ "values (? , ? , ? , ? , ? , ? , ? ,?) ";
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
			System.out.println("insert tripD sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_D tripD) {
		int count = 0;
		String sql = " update Trip_D set " 
				+ " TRIP_ID = ?, " 
				+ " SEQ_NO = ?, " 
				+ " LOC_ID = ? ,"
				+ " S_DATE = ? , " //5
				+ " S_TIME = ? , " 
				+ " STAYTIME = ? ," 
				+ " MEMO = ? " 
				+ " where TRIP_ID = ?; ";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripD.getTripId());
			ps.setInt(2, tripD.getSeqNo());
			ps.setString(3, tripD.getLocId());
			ps.setString(4, tripD.getStartDate());
			ps.setString(5, tripD.getStartTime());
			ps.setString(6, tripD.getStayTime());
			ps.setString(7, tripD.getMemo());
			ps.setString(8, tripD.getTripId());
			System.out.println("update Trip_D sql :: " + ps.toString());
			
			count = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(String tripId) {
		int count = 0;
		String sql = " delete from Trip_D where TRIP_ID = ? ;";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			System.out.println("Delete Trip_D sql :: " + ps.toString());
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
		String sql = " select " 
					+ " TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID ,STAY_TIME, MEMO " 
				    + " from trip_d "
				    + " where TRIP_ID = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
			System.out.println("findTransId :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
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
		String sql = " select " + "TRANS_ID, TRIP_ID, SEQ_NO, LOC_ID, S_DATE," + // 5
				"S_TIME, STAYTIME , MEMO" + // 8
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

				tripD = new Trip_D(transId, tripId, seqNo, locId, startDate, startTime, stayTime, memo);
				tripDs.add(tripD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tripDs;
	}

	// 創建網誌
	@Override
	public List<Blog_SpotInfo> getSpotName(DateAndId dateAndId) {
		List<Blog_SpotInfo> spotNames = new ArrayList<Blog_SpotInfo>();
		String sql = "SELECT   Location.LOC_ID,Trip_D.TRIP_ID,NAME ,Blog_D.LOC_NOTE FROM Location \n" + 
				"left join Trip_D on Location.LOC_ID = Trip_D.LOC_ID \n" + 
				"left join Blog_D on Location.LOC_ID = Blog_D.LOC_ID and Blog_D.BLOG_ID = Trip_D.TRIP_ID\n" + 
				"where Trip_D.S_DATE = ? and Trip_D.TRIP_ID = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, dateAndId.getS_Date());
			ps.setString(2, dateAndId.getTrip_Id());
			System.out.println("findspotNames :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String loc_Id = rs.getString(1);
				String trip_Id = rs.getString(2);
				String spotName = rs.getString(3);
				String loc_Note= rs.getString(4);

				Blog_SpotInfo blog_SpotInfo = new Blog_SpotInfo(loc_Id, trip_Id, spotName,loc_Note);
				spotNames.add(blog_SpotInfo);
			}
			return spotNames;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spotNames;

	}

	@Override
	//透過行程日期取得景點資料
	public List<Trip_LocInfo> getLocName(DateAndId dateAndId) {
		List<Trip_LocInfo> locNames = new ArrayList<Trip_LocInfo>();
		String sql = " SELECT Location.LOC_ID, "
				+ " Trip_D.TRIP_ID,NAME,ADDRESS,STAYTIME,MEMO "
				+ " FROM Location "
				+ " left join Trip_D on Location.LOC_ID = Trip_D.LOC_ID " 
				+ " where S_DATE = ? and TRIP_ID = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, dateAndId.getS_Date());
			ps.setString(2, dateAndId.getTrip_Id());
			System.out.println("findLocNames :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String loc_Id = rs.getString(1);
				String trip_Id = rs.getString(2);
				String locName = rs.getString(3);
				String address = rs.getString(4);
				String staytime = rs.getString(5);
				String memo = rs.getString(6);

				Trip_LocInfo trip_LocInfo = new Trip_LocInfo(loc_Id, trip_Id, locName, address, staytime, memo);
				locNames.add(trip_LocInfo);
			}
			return locNames;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locNames;
	}
	
	//點擊編輯按鈕回到編輯行程，撈已存的景點資料
	@Override
	public Map<String, List<Location_D>> showLocName(String tripId) {
		
		String sql = " SELECT * " 
				+ " FROM Trip_D "
				+ " LEFT JOIN Location "
				+ " ON Location.LOC_ID = Trip_D.LOC_ID " 
				+ " WHERE TRIP_ID = ? "
				+ " ORDER BY S_DATE, SEQ_NO; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, tripId);
			System.out.println("ShowTripUpdate's ID :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			Map<String, List<Location_D>> map = new TreeMap<>();
			List<Location_D> showLocNames = null;
			int count = 1;
			while (rs.next()) {
				if(rs.getInt("seq_No") == 1) {
					if (showLocNames != null) {
						System.out.println("rs next inside count: " + count);
						map.put(count + "", showLocNames);
						count++;
					}
					showLocNames = new ArrayList<Location_D>();
				}
				String locName = rs.getString("NAME");
				String address = rs.getString("ADDRESS");
				String locId = rs.getString("LOC_ID");
				String memo = rs.getString("MEMO");
				String staytime = rs.getString("STAYTIME");
//				String trip_Id = rs.getString("trip_Id");
				System.out.println(locName + " " + address + " " + locId + " " + memo + " " + staytime + " " + tripId);
				Location_D trip_LocInfo = new Location_D(locName, address, locId, memo, staytime);
				showLocNames.add(trip_LocInfo);
			}
			System.out.println("outside count:" + count );
			map.put(count + "", showLocNames);
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return showLocName(tripId);
	}
	}
		

	


