package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.TSFBuilder;

import web.com.bean.Explore;
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
	public List<Explore> getAll() {
		String sql = "SELECT distinct Blog_M.BLOG_TITLE, Blog_M.USER_ID,Blog_M.BLOG_ID, Member.NICKNAME,Blog_M.BLOG_DESC,Blog_M.C_DATETIME FROM  Blog_M\n" + 
				"LEFT JOIN Member ON  Blog_M.USER_ID = Member.MEMBER_ID";
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
				
				
	
				Explore explore = new Explore(blogID, userID, nickName, titleName,blogDesc,date);
				exploreslList.add(explore);
			}
			return exploreslList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exploreslList;
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
	
}
