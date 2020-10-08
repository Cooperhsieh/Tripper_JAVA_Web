package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import web.com.bean.Blog_Note;

import com.fasterxml.jackson.core.TSFBuilder;
import com.google.cloud.Date;

import web.com.bean.Blog;
import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;

import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;

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
		String sql = "SELECT \n" + 
				"    Blog_M.BLOG_ID,\n" + 
				"    BLOG_TITLE,\n" + 
				"    BLOG_DESC,\n" + 
				"    Location.LOC_ID,\n" + 
				"    BLOG_NOTE,\n" + 
				"    NAME,\n" + 
				"    SEQ_NO,\n" + 
				"    S_DATE,\n" + 
				"    Blog_M.TRIP_ID\n" + 
				"FROM\n" + 
				"    Blog_D\n" + 
				"        LEFT JOIN\n" + 
				"    Blog_M ON Blog_M.BLOG_ID = Blog_D.BLOG_ID\n" + 
				"        LEFT JOIN\n" + 
				"    Location ON Location.LOC_ID = Blog_D.LOC_ID\n" + 
				"WHERE\n" + 
				"    Blog_D.BLOG_ID = ?\n" + 
				"ORDER BY SEQ_NO ASC";
		
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
				int tripId= rs.getInt(9);
				BlogD blogD = new BlogD(blodId,locationId, locationName, blogNote, s_Date,tripId);
				bList.add(blogD);
		}
			return bList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return bList;
	}
	
	@Override
	public List<Blog_Day> findDateById(int blodId) {
		String sql = " SELECT DISTINCT S_DATE FROM Blog_D  \n" + 
				"												LEFT JOIN Blog_M ON Blog_M.BLOG_ID = Blog_D.BLOG_ID  \n" + 
				"												LEFT JOIN Location ON Location.LOC_ID = Blog_D.LOC_ID \n" + 
				"											\n" + 
				"												WHERE Blog_D.BLOG_ID = ? \n" + 
				"				                            \n" + 
				"												Order By \n" + 
				"										 S_DATE asc ;";
		
		List<Blog_Day> blogDays = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setInt(1,blodId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				String date = rs.getString(1);
				Blog_Day  blogD= new Blog_Day(date);
				blogDays.add(blogD);
		}
			return blogDays;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return blogDays;
	}
	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId) {
		List<Blog_SpotInformation> spotNames = new ArrayList<>();		
		String sql = "SELECT NAME FROM Blog_D  \n" + 
				"	LEFT JOIN Blog_M ON Blog_M.BLOG_ID = Blog_D.BLOG_ID \n" + 
				"	LEFT JOIN Location ON Location.LOC_ID = Blog_D.LOC_ID  \n" + 
				"	WHERE Blog_D.BLOG_ID = ?  and Blog_D.S_DATE = ?\n" + 
				"	Order By \n" + 
				"	 S_DATE asc ";
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, blogId);
			ps.setString(2, s_Date);
//			System.out.println("findspotNames :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String spotName = rs.getString(1);
				Blog_SpotInformation blog_SpotInfo = new Blog_SpotInformation(spotName);			
				spotNames.add(blog_SpotInfo);
			}
			return spotNames;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spotNames;
		
	}


//上傳心得
	@Override
	public int insertB_Note(Blog_Note blog_Note) {
		int count = 0;
		String sql = "";
		sql = "insert into Blog_D (LOC_ID, LOC_NOTE,BLOG_ID) values (? , ? , ? ) ;"; 
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1, blog_Note.getLoc_Id());
			ps.setString(2, blog_Note.getLoc_Note());
			ps.setString(3, blog_Note.getBlog_Id());
			
			System.out.println("insert Blog_Note sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
//上傳景點照片
	@Override
	public int updateImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String locId) {
		int count = 0;
		String sql = "insert into Blog_Spot_Pic (LOC_ID,BLOG_ID,PIC1,PIC2,PIC3,PIC4) values (?,?,?,?,?,?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setString(1, locId);
			ps.setString(2, blogId);
			ps.setBytes(3, image1);
			ps.setBytes(4, image2);
			ps.setBytes(5, image3);
			ps.setBytes(6, image4);
			
			System.out.println("insert Blog_Spot_Pic sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Blog findById1(int id) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public BlogD findLocationById(int id) {
//		
//		return null;
//	}


}
