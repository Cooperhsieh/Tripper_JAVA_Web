package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Blog_Comment;
import web.com.bean.Member;
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
		"( GROUP_TRANS_ID, TRIP_ID, MEMBER_ID ,STATUS )" +
		"values (? , ?, ? , ?)" ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, tripGroup.getGroupTransId());
			ps.setString(2, tripGroup.getTripId());
			ps.setInt(3, tripGroup.getMemberId());
			ps.setInt(4, 1);
			
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Trip_Group tripGroup) {
		int count = 0 ;
//		String sql = "update Trip_Group STATUS  = 2 " +
//		"where GROUP_TRANS_ID = ? ; " ;
//		
//		try (Connection connection = datasource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql); ) {
//	
//			ps.setInt(1, tripGroup.getMemberId());
//			System.out.println("tripGroup:: " + ps.toString());
//			count = ps.executeUpdate();
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return count;
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

	@Override
	public int selectMCountByTripID(String trip_ID) {
		int count = 0 ;
		String sql = "SELECT count(*) FROM Tripper.Trip_Group where TRIP_ID = ? and STATUS = 2 ;";
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1,trip_ID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int selectMyGroup(String trip_Id, String memberId) {
		int count = 0 ;
		String sql = "SELECT STATUS FROM Tripper.Trip_Group where Trip_ID = ? and MEMBER_ID = ?;" ;
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1,trip_Id);
			ps.setString(2, memberId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
				}					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int deleteGroup(Trip_Group tripGroup) {
		int count = 0 ;
		String sql = "delete from Trip_Group where TRIP_ID = ? and MEMBER_ID = ? ;" ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, tripGroup.getTripId());
			ps.setInt(2, tripGroup.getMemberId());
			
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Member> getMbrList(String tripId) {
		String sql = "select b.* " + 
					"from Trip_Group a inner join MEMBER b " + 	
					"where ( a.MEMBER_ID = b.MEMBER_ID ) " + 
					"and a.Trip_ID = ? and a.STATUS = 2;";
		List<Member> friends = new ArrayList<Member>();
		Member member = null;
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, tripId);
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
	public List<Member> getApplicationList(String tripId) {
		String sql = "select b.* " + 
				"from Trip_Group a inner join MEMBER b " + 	
				"where ( a.MEMBER_ID = b.MEMBER_ID ) " + 
				"and a.Trip_ID = ? and a.STATUS = 1;";
	List<Member> friends = new ArrayList<Member>();
	Member member = null;
	try (Connection connection = datasource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);) {
		ps.setString(1, tripId);
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
	public int agreeJoinGroup(Trip_Group tripGroup) {
		int count = 0 ;
		String sql = "update Trip_Group set STATUS = 2 where TRIP_ID = ? and MEMBER_ID = ? ;" ;
		
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, tripGroup.getTripId());
			ps.setInt(2, tripGroup.getMemberId());
			
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int insertB_Comment(Blog_Comment blog_Comment) {
		int count = 0;
		String sql = "";
		sql = "insert into GroupComment (Group_ID, Member_ID,Com_Info) values (? , ? , ? ) ;"; 
		try (Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1, blog_Comment.getName());
			ps.setString(2, blog_Comment.getMember_ID());
			ps.setString(3, blog_Comment.getContent());

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int deleteComment(int comID) {
		int count = 0 ;
		String sql = "DELETE FROM GroupComment WHERE Com_ID = ?";
		try (
				Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setInt(1, comID);
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int updateComment(Blog_Comment blog_Comment) {
		int count = 0 ;
		String sql = "UPDATE GroupComment set Com_ID = ?, Group_ID = ?,Member_ID = ?,Com_Info = ? ,Com_Date = ?" + 
				"where Com_ID = ?;";
		try(
				Connection connection = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				
				){
			ps.setInt(1, blog_Comment.getComId());
			ps.setString(2, blog_Comment.getBlogId());
			ps.setString(3, blog_Comment.getMember_ID());
			ps.setString(4,blog_Comment.getContent());
			ps.setString(5, blog_Comment.getDate());
			ps.setInt(6, blog_Comment.getComId());
		
			count = ps.executeUpdate();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Blog_Comment> findCommentById(String blogId) {
		String sql ="select GroupComment.Group_ID,Member.NICKNAME,GroupComment.Com_Info,GroupComment.Member_ID, GroupComment.SEQ,Com_Date,Com_ID from GroupComment\n" + 
				"								left join Member on Member.Member_ID = GroupComment.Member_ID \n" + 
				"								where GroupComment.Group_ID = ? \n" + 
				"								order by   \n" + 
				"								Com_Date Desc ";
		
		List<Blog_Comment> blogComments = new ArrayList<>();
		
		
		try(
				Connection connection  = datasource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setString(1,blogId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String blogID = rs.getString(1);
				String name = rs.getString(2);
				String comment = rs.getString(3);
				String memberID = rs.getString(4);
				String date = rs.getString(6);
				int comID = rs.getInt(7);
				Blog_Comment blog_Comment = new Blog_Comment(blogID,name,comment,memberID,date,comID);
				blogComments.add(blog_Comment);
		}
			return blogComments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return blogComments;
	}

}
