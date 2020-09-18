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
import web.com.bean.Blog;
import web.com.dao.BlogDao;
import web.com.util.ServiceLocator;

public class BlogImpl implements BlogDao{
	DataSource dataSource;
	
	public BlogImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public List<Blog> getAll() {
		
		String sql = "SELECT Blog_M.BLOG_TITLE,Blog_M.BLOG_DESC,Blog_D.BLOG_NOTE,Location.NAME ,Trip_M.D_COUNT,Blog_M.LOC_ID, Blog_M.BLOG_ID FROM Blog_M \n" + 
				"LEFT JOIN Blog_D ON Blog_D.LOC_ID = Blog_M.LOC_ID\n" + 
				"LEFT JOIN Location ON Blog_M.LOC_ID = Location.LOC_ID\n" + 
				"LEFT JOIN Trip_M ON Blog_M.TRIP_ID = Trip_M.TRIP_ID";
		
		List<Blog> bList = new ArrayList<>();
		try(
				
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
	
				){
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String blogTittle = rs.getString(1);
				String blogDesc = rs.getString(2);
				String blogNote = rs.getString(3);
				String locName = rs.getString(4);
			    int dayCount = rs.getInt(5);
			    int locId= rs.getInt(6);
			    int blogID= rs.getInt(7);
			    
			    Blog blog = new Blog(blogTittle, blogDesc, blogNote, locName, dayCount,locId,blogID);
			    bList.add(blog);
				
			}
			return bList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		 
		return bList;
	}

	@Override
	public byte[] getImage(int id) {
	    String sql = "SELECT PIC FROM Blog_D_Pic WHERE PIC_ID = ?;";
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
	public Blog findById(int id) {
		String sql = "";
		Blog blog = null;
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return blog;
	}

}
