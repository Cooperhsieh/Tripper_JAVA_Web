package web.com.impl;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.cloud.Date;

import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;
import web.com.dao.BlogDao;
import web.com.util.ServiceLocator;

public class BlogImpl implements BlogDao{
	DataSource dataSource;
	
	public BlogImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

//	@Override
//	public List<BlogD> getAll() {
//		
//		String sql = "SELECT Blog_M.BLOG_TITLE,Blog_M.BLOG_DESC,Blog_D.BLOG_NOTE,Location.NAME ,Trip_M.D_COUNT,Blog_M.LOC_ID, Blog_M.BLOG_ID FROM Blog_M \n" + 
//				"LEFT JOIN Blog_D ON Blog_D.LOC_ID = Blog_M.LOC_ID\n" + 
//				"LEFT JOIN Location ON Blog_M.LOC_ID = Location.LOC_ID\n" + 
//				"LEFT JOIN Trip_M ON Blog_M.TRIP_ID = Trip_M.TRIP_ID";
//		
//		List<BlogD> bList = new ArrayList<>();
//		try(
//				
//				Connection connection = dataSource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql);
//	
//				){
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				String blogTittle = rs.getString(1);
//				String blogDesc = rs.getString(2);
//				String blogNote = rs.getString(3);
//				String locName = rs.getString(4);
//			    int dayCount = rs.getInt(5);
//			    int locId= rs.getInt(6);
//			    int blogID= rs.getInt(7);
//			    
//			    BlogM blog = new BlogM(blogTittle, blogDesc);
//			    bList.add(blog);
//				
//			}
//			return bList;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//		 
//		return bList;
//	}

	@Override
	public byte[] getImage(int id) {
	    String sql = ";";
	    byte [] pic = null;
	    try (
	    	Connection connection = dataSource.getConnection();
	    	PreparedStatement ps = connection.prepareStatement(sql);
	    		
	    		){
	    	ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				pic = rs.getBytes(1);
			}
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return pic;
	}

	@Override
	public List<BlogD> findById(int id) {
		String sql = "SELECT Blog_M.BLOG_ID,BLOG_TITLE,BLOG_DESC,Location.LOC_ID,BLOG_NOTE,NAME,SEQ_NO,S_DATE FROM Blog_D  \n" + 
				"				LEFT JOIN Blog_M ON Blog_M.BLOG_ID = Blog_D.BLOG_ID \n" + 
				"				LEFT JOIN Location ON Location.LOC_ID = Blog_D.LOC_ID\n" + 
				"				WHERE Blog_D.BLOG_ID = ? " + 
				"				Order By \n" + 
				"			SEQ_NO asc";
		
		List<BlogD> bList = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int blodId=  rs.getInt(1);
				int locationId = rs.getInt(4);
				String locationName  =rs.getString(6);
				String blogNote = rs.getString(5);
				String s_Date = rs.getString(8);
				BlogD blogD = new BlogD(blodId,locationId, locationName, blogNote, s_Date);
				bList.add(blogD);
		}
			return bList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return bList;
	}
	@Override
	public List<BlogD> findLocationById(int blodId, String datetime) {
		String sql = "SELECT NAME,SEQ_NO,S_DATE FROM Blog_D  \n" + 
				"								LEFT JOIN Blog_M ON Blog_M.BLOG_ID = Blog_D.BLOG_ID  \n" + 
				"								LEFT JOIN Location ON Location.LOC_ID = Blog_D.LOC_ID \n" + 
				"                                \n" + 
				"								WHERE Blog_D.BLOG_ID =?  and Blog_D.S_DATE = ? \n" + 
				"                                \n" + 
				"								Order By \n" + 
				"							SEQ_NO and S_DATE asc ;";
		
		List<BlogD> bList = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setInt(1,blodId);
			ps.setString(2, datetime);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				String s_Name = rs.getString(1);
				BlogD  blogD= new BlogD(s_Name);
				bList.add(blogD);
		}
			return bList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return bList;
	}

//	@Override
//	public BlogD findLocationById(int id) {
//		
//		return null;
//	}

}
