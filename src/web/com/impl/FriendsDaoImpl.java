package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.firebase.database.Transaction.Result;

import web.com.bean.Friends;
import web.com.bean.Member;
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
	private static final int NOT_FRIEND = 0; 
	private static final int CHECKING = 1; // 邀請好友待確認
	private static final int FRIEND = 2; // 已是好友
	public FriendsDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();	
	}
	
	@Override
	public List<Member> getAll(int key) {
		String sql = " select b.* "
				+ " from FRIENDS a inner join MEMBER b "
				+ " where ( a.FRIEND_ID = b.MEMBER_ID ) "
				+ " and a.MEMBER_ID = ? "
				+ " and a.F_STATUS = ? ;";
		List<Member> friends = new ArrayList<Member>();
		Member member = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, key);
			ps.setInt(2, FRIEND);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int memberId = rs.getInt("member_id");
				String accountId = rs.getString("account_id");
				String email = rs.getString("email");
				String nickname = rs.getString("nickname");
				int loginType = rs.getInt("login_type");
				String tokenId = rs.getString("token_id");
				member = new Member(memberId, accountId, email, nickname, loginType, tokenId);
				friends.add(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friends;
	}

	@Override
	public Friends findSearchFriend(int memberId, String account) {
		Friends friend = null;
		int friendMemeberId = 0;
		String friendAccount = "";
		String mail = "";
		String nickName = "";
		int loginType = 0;
		String token = "";
		int status = 0;
		String sql = " select * from MEMBER  "
				   + " where UPPER(ACCOUNT_ID) = ? ;";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, account);
			System.out.println("getSearchFriend sql::" + ps.toString() );
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//int id, String account, String mail, String nickName, int loginType, String token, int status)
				friendMemeberId = rs.getInt("member_id");
				friendAccount = rs.getString("account_id");
				mail = rs.getString("email");
				nickName = rs.getString("nickname");
				loginType = rs.getInt("login_type");
				token = rs.getString("token_id");
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sql = " select F_STATUS from FRIENDS " 
			+ " where MEMBER_ID = ? "
			+ " and FRIEND_ID = ? ;";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, memberId);
			ps.setInt(2, friendMemeberId );
			System.out.println("getSearchFriend sql::" + ps.toString() );
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				status = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		friend = new Friends(friendMemeberId, friendAccount, mail, nickName, loginType, token, status);
		return friend;
	}

	@Override
	public int insert(int memberId, int friendId) {
		int count = 0;
		String sql = " insert into FRIENDS "
				   + " ( "
				   + " MEMBER_ID, FRIEND_ID, F_STATUS "
				   + " ) values ( "
				   + " ?, ? ,? "
				   + " ) ;";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, memberId);
			ps.setInt(2, friendId);
			ps.setInt(3, CHECKING);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}

}
