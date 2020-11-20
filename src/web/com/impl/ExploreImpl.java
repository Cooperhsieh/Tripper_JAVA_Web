package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.catalina.valves.rewrite.InternalRewriteMap.Escape;

import com.fasterxml.jackson.core.TSFBuilder;

import web.com.bean.Explore;
import web.com.bean.Like;
import web.com.bean.Location;
import web.com.bean.Member;
import web.com.dao.ExploreDao;
import web.com.util.ServiceLocator;

public class ExploreImpl implements ExploreDao{
	DataSource dataSource;
	public ExploreImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}


	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
//	@Override
//	public Explore findById(int id) {
//		
//		String sql = "SELECT BLOG_TITLE, BLOG_ID FROM Blog_M WHERE USER_ID = ?";
//		Explore explore = null;
//		try(
//				Connection connection  = dataSource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql);
//				) {
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				String titleName = rs.getString(3);
//				String blogID = rs.getString(6);
//				explore = new Explore(id, titleName, blogID);
//				}
//		} catch (SQLException e) {
//			
//		}
//		return explore;
//		
//	}

	@Override
	public List<Explore> getAll(String logingUser) {
       Explore explore = null;

		String sql = "SELECT distinct B.BLOG_TITLE, B.USER_ID,B.BLOG_ID, Member.NICKNAME,B.BLOG_DESC,B.C_DATETIME\n" + 
				"				 ,(select count(*) from ArticleGood AC where AC.articleId = B.BLOG_ID) as articleGoodCount\n" + 
				"                 ,(select case count(*) when 0 then 0 else 1 end from ArticleGood AG where AG.articleId = B.BLOG_ID and AG.userId = ? ) as articleGoodStatus\n" + 
				"				FROM  Blog_M B\n" + 
				"								LEFT JOIN Member ON  B.USER_ID = Member.MEMBER_ID";

		List<Explore> exploreslList = new ArrayList<>();
		try(    
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
		){
			ps.setString(1, logingUser);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				String titleName = rs.getString(1);
				String userID = rs.getString(2);
				String blogID = rs.getString(3);
			    String blogDesc = rs.getString(5);
				String nickName = rs.getString(4);
				String date = rs.getString(6);
				int articleGoodCount = rs.getInt("articleGoodCount");
				boolean articleGoodStatus = rs.getBoolean("articleGoodStatus");
			    explore = new Explore(blogID, userID, nickName, titleName,blogDesc,date,articleGoodCount,articleGoodStatus);
				exploreslList.add(explore);
			}
			return exploreslList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exploreslList;
	}
	
	
	@Override
	public List<Like> getAllLikes(String blogId) {
		Like  like = null;
		List<Like> likes = new ArrayList<>();
		String sql = "SELECT articleId, userId,m.NICKNAME,m.account_id FROM TRIPPER.ArticleGood \n" + 
				"LEFT JOIN Member m on  m.Member_ID = userId\n" + 
				"where articleId =  ?;";
		
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				
				){
			ps.setString(1, blogId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String articleId = rs.getString(1);
				String userId = rs.getString(2);
				String name = rs.getString(3);
				String accountId = rs.getString(4);
				like = new Like(articleId, userId, name,accountId);
				likes.add(like);
			}
			return likes;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return likes;
	}

	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT PIC  FROM Blog_M WHERE BLOG_ID = ?;";
		byte [] pic = null;
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
			){
			ps.setInt(1, id);		
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
			pic = rs.getBytes(1);	
			}
		} catch (SQLException e) {
			e.printStackTrace();		
			}
		return pic;
	}


	@Override
	public List<Member> selectAll() {
		String sql = "SELECT * FROM Member;";
		List<Member> memberList = new ArrayList<Member>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String account = rs.getString(2);
				String password= rs.getString(3);
				String nickName = rs.getString(5);
				Member member = new Member(id, account,password,nickName);
				memberList.add(member);
			}
			return memberList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberList;
		
	}


	@Override
	public List<Location> getHotLocationAll() {
		
		List<Location> locations = new ArrayList<Location>();
		Location loc = null;
		String sql = " select \n" + 
				"				 LOC_ID, NAME, ADDRESS, LOC_TYPE, CITY,\n" + 
				"				 INFO, LONGITUDE, LATITUDE, CREATE_ID, M_USER_ID,  \n" + 
				"				 C_DATETIME from LOCATION \n" + 
				"                 where LOC_TYPE = 1 ;";
				               
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
	public List<Explore> getAllIos() {
       Explore explore = null;

		String sql = "	SELECT distinct Blog_M.BLOG_TITLE, Blog_M.USER_ID,Blog_M.BLOG_ID, Member.NICKNAME,Blog_M.BLOG_DESC,Blog_M.C_DATETIME FROM  Blog_M\n" + 
				"Left JOIN Member ON ( Blog_M.USER_ID = Member.MEMBER_ID ) ;" ;

		List<Explore> exploreslList = new ArrayList<>();
		try(    
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
		){
	
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				String titleName = rs.getString(1);
				String userID = rs.getString(2);
				String blogID = rs.getString(3);
			    String blogDesc = rs.getString(5);
				String nickName = rs.getString(4);
				String date = rs.getString(6);
				
			    explore = new Explore(blogID, userID, nickName, titleName,blogDesc,date);
				exploreslList.add(explore);
			}
			return exploreslList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exploreslList;
	}
	
	
}
