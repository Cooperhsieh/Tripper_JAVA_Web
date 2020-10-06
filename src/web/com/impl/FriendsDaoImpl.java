package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Friends;
import web.com.dao.FriendsDao;
import web.com.util.ServiceLocator;

/**
* 類別說明：好友資料DaoImpl檔
* 
* @author Weixiang
* @version 建立時間:Sep 4, 2020 17:16 PM
* 
*/
public class FriendsDaoImpl implements FriendsDao {
	DataSource dataSource ;
	
	public FriendsDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();	
	}

	@Override
	public int insert(Friends friends) {
		int count = 0;
		String sql = "insert into Friends" +
				"(MEMBER_ID, FRIEND_ID) " + // TODO
				"values(?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, friends.getMemberId());
			ps.setInt(2, friends.getFriendId());
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int memberId, int friendId) {
		int count = 0;
		String sql = "delete from Friends where MEMBER_ID = ?, FRIEND_ID = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, memberId);
			ps.setInt(2, friendId);
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Friends findFriendTransId(String friendTransId) {
		String sql = "select MEMBER_ID, FRIEND_ID, F_STATUS, DATE_TIME from Friends where TRANS_ID = ?";
		Friends friends = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, friendTransId);
			/* 當Statement關閉，ResultSet也會自動關閉，
			 * 可以不需要將ResultSet宣告置入try with resources小括號內，參看ResultSet說明
			 */
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int memberId = rs.getInt(1);
				int friendId = rs.getInt(2);
				int friendStatus = rs.getInt(3);
				Timestamp applyDateTime = rs.getTimestamp(4); // TODO
				friends = new Friends(memberId, friendId, friendStatus, applyDateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friends;
	}

	@Override
	public List<Friends> getAll() {
		String sql = "select Trans_ID, MEMBER_ID, FRIEND_ID, F_STATUS, DATE_TIME "
				+ "from Friends order by DATE_TIME desc";
		List<Friends> friendsList = new ArrayList<Friends>();
		Friends friends = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String friendTransId = rs.getString(1);
				int memberId = rs.getInt(2);
				int friendId = rs.getInt(3);
				int friendStatus = rs.getInt(4);
				Timestamp applyDateTime = rs.getTimestamp(5);
				friends = new Friends(friendTransId, memberId, friendId, friendStatus, applyDateTime);
				friendsList.add(friends);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendsList;
	}

	// TODO
	@Override
	public byte[] getPhoto(int id) {
		String sql = "select P_PIC from Member where MEMBER_ID = ?;";
		byte[] photo = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				photo = rs.getBytes(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photo;
	}
	
}
