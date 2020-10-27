package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import web.com.bean.Blog_Note;
import web.com.bean.Blog_Pic;
import web.com.bean.Blog;
import web.com.bean.BlogD;
import web.com.bean.Blog_Comment;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInformation;

import com.fasterxml.jackson.core.TSFBuilder;
import com.fasterxml.jackson.core.TSFBuilder;
import com.google.cloud.Date;
import web.com.bean.Blog;
import web.com.bean.BlogD;
import web.com.bean.BlogFinish;
import web.com.bean.BlogM;
import web.com.bean.Blog_Comment;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;
import web.com.bean.Trip_M;

import web.com.dao.BlogDao;
import web.com.util.ImageUtil;
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
	public byte[] getImage(String id) {
	    String sql = "SELECT PIC FROM Tripper.Blog_M where BLOG_ID = ?;";
	    byte [] pic = null;
	    try (
	    	Connection connection = dataSource.getConnection();
	    	PreparedStatement ps = connection.prepareStatement(sql);    		
	    		){
	    	ps.setString(1, id);
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
	public List<BlogD> findById(String id) {
		String sql = "SELECT \n" + 
				"    a.Trip_ID,\n" + 
				"    a.LOC_ID,\n" + 
				"    (select b.loc_note from blog_d b where Blog_iD = trip_id and b.loc_id = a.loc_id ) loc_note,\n" + 
				"   a.S_DATE,\n" + 
				"    a.SEQ_NO,\n" + 
				"    c.name\n" + 
				"FROM\n" + 
				"    Tripper.Trip_D a\n" + 
				"        inner JOIN\n" + 
				"   Location c ON a.LOC_ID = c.LOC_ID\n" + 
				" where a.TRIP_ID = ? ;";
		
		List<BlogD> bList = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String blodId=  rs.getString(1);
				String locationId = rs.getString(2);
				String locationName  =rs.getString(6);
				String blogNote = rs.getString(3);
				String s_Date = rs.getString(4);
				String tripId= rs.getString(1);
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
	public List<Blog_Day> findDateById(String blodId) {
		String sql = " SELECT DISTINCT S_DATE FROM Trip_D  \n" + 
				"												LEFT JOIN Blog_M ON Blog_M.BLOG_ID = Trip_D.TRIP_ID  \n" + 
				"												LEFT JOIN Location ON Location.LOC_ID = Trip_D.LOC_ID \n" + 
				"											\n" + 
				"												WHERE Trip_D.TRIP_ID = ? \n" + 
				"				                            \n" + 
				"												Order By \n" + 
				"										 S_DATE asc ;";
		
		List<Blog_Day> blogDays = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setString(1,blodId);
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
	public List<Blog_SpotInformation> getSpotName(String s_Date, String blogId) {
		List<Blog_SpotInformation> spotNames = new ArrayList<>();		
		String sql = "SELECT Location.NAME,Trip_D.S_DATE,SEQ_NO FROM Trip_D \n" + 
				"		LEFT JOIN Location \n" + 
				"        ON Location.LOC_ID = Trip_D.LOC_ID  \n" + 
				"	    WHERE Trip_ID = ? and Trip_D.S_DATE = ?\n" + 
				"        order by\n" + 
				"        SEQ_NO ,S_DATE;";
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, blogId);
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

//上傳留言內容
	@Override
	public int insertB_Comment(Blog_Comment blog_Comment) {
		int count = 0;
		String sql = "";
		sql = "insert into Comment (Blog_ID, Member_ID,Com_Note) values (? , ? , ? ) ;"; 
		try (Connection connection = dataSource.getConnection();
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
	public int insertImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String locId) {
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
//update 網誌 image
	@Override
	public int updateImage(Blog_Pic blog_Pic , byte[] image1,byte[] image2,byte[] image3,byte[] image4) {
		int count = 0;
		String sql = "update  Blog_Spot_Pic Set LOC_ID= ?,BLOG_ID=?,PIC1= ?,PIC2=? ,PIC3 =? ,PIC4 = ? where Blog_Spot_Pic.LOC_ID=? and BLOG_ID =? ;";
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				){
			ps.setString(1, blog_Pic.getLocId());
			ps.setString(2,blog_Pic.getBlogId());
			ps.setBytes(3, image1);
			ps.setBytes(4, image2);
			ps.setBytes(5, image3);
			ps.setBytes(6, image4);
			ps.setString(7, blog_Pic.getLocId());
			ps.setString(8,blog_Pic.getBlogId());
		
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
//發布網誌後將相關資料存進DB
	@Override
	public int insetBlog_M(BlogFinish blogFinish,byte[] b_Pic) {
		int count = 0;
		String sql = "";
		if (b_Pic != null) {
			sql = "insert into Blog_M " + "( BLOG_ID, BLOG_TITLE, BLOG_DESC, PIC, USER_ID) "
					+ "values (? ,? ,? ,?, ? );";
		}else {
			sql = "insert into Blog_M " + "( BLOG_ID, BLOG_TITLE, BLOG_DESC, USER_ID) "
					+ "values (? , ? , ? , ? );";
		}
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, blogFinish.getTrip_Id());
			ps.setString(2, blogFinish.getBlog_title());
			ps.setString(3, blogFinish.getBlog_Info());
			if (b_Pic != null) {
				ps.setBytes(4, b_Pic);
				ps.setString(5, blogFinish.getMemberId());
			}else {
				ps.setString(4, blogFinish.getMemberId());
			}
				
			System.out.println("insert blog_M sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<byte[]> getSpotImages(String loc_Id, String blog_id) {
		List<byte[]> spotImages = new ArrayList<byte[]>();
		String sql = "SELECT * FROM Tripper.Blog_Spot_Pic where LOC_ID = ? and BLOG_ID = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, loc_Id);
			ps.setString(2, blog_id);
			System.out.println("getSpotImages :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				byte[] pic1 = rs.getBytes(5);
				byte[] pic2 = rs.getBytes(6);
				byte[] pic3 = rs.getBytes(7);
				byte[] pic4 = rs.getBytes(8);
				
				spotImages.add(pic1);
				spotImages.add(pic2);
				spotImages.add(pic3);
				spotImages.add(pic4);
			}
			return spotImages;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spotImages;

	}

	@Override
	public List<BlogFinish> getMyBlog(String memberId) {
		List<BlogFinish> blogMList = new ArrayList<BlogFinish>();
		String sql = "SELECT BLOG_ID,BLOG_TITLE,BLOG_DESC,Trip_M.S_DATE,Trip_M.S_TIME FROM TRIPPER.Blog_M\n" + 
				"Left join Trip_M on Trip_M.TRIP_ID = Blog_M.BLOG_ID\n" + 
				"Where Blog_M.USER_ID = ?" ;

		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, memberId);
			System.out.println("getMyBlog sql :" + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String blogTitle = rs.getString(3);
				String blogInfo = rs.getString(4);
				String blogId = rs.getString(2);
				String sDate = rs.getString(1);
				String sTime = rs.getString(5);
				
				BlogFinish blogFinish = new BlogFinish(sDate, blogId,blogTitle , memberId,blogInfo,sTime);
				blogMList.add(blogFinish);
			}
			return blogMList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return blogMList;
	}


	@Override
	public List<Blog_Comment> findCommentById(String blogId) {
		
		String sql ="select BLOG_ID,Member.NICKNAME,Com_Note,Comment.Member_ID, SEQ,Com_Date from Comment\n" + 
				"				left join Member on Member.Member_ID = Comment.Member_ID\n" + 
				"				where Comment.BLOG_ID = ? \n" + 
				"				order by \n" + 
				"				Com_Date Desc ";
		
		List<Blog_Comment> blogComments = new ArrayList<>();
		
		
		try(
				Connection connection  = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				) {
			ps.setString(1,blogId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				String name = rs.getString(2);
				String comment = rs.getString(3);
				String memberID = rs.getString(4);
				String date = rs.getString(6);
				
				Blog_Comment blog_Comment = new Blog_Comment(name,comment,memberID,date);
				blogComments.add(blog_Comment);
		}
			return blogComments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return blogComments;
	}
//更新網誌標題 描述 照片
	@Override
	public int update(BlogFinish blog_id, byte[] b_Pic) {
	  int count = 0 ;
	  String sql = "";
	  if (b_Pic != null) {
			sql = "UPDATE Blog_M SET  BLOG_TITLE = ?, BLOG_DESC= ?, PIC= ? WHERE BLOG_ID = ?  ";

		}else {
			sql = "UPDATE Blog_M SET  BLOG_TITLE=?, BLOG_DESC=? WHERE BLOG_ID = ?  ";
		}
	  try(
			  Connection connection = dataSource.getConnection();
			  PreparedStatement ps = connection.prepareStatement(sql);
			  ) {
		  ps.setString(1, blog_id.getBlog_title());
		  ps.setString(2, blog_id.getBlog_Info());
		
		  if(b_Pic != null) {
			  ps.setBytes(3, b_Pic);
			  ps.setString(4, blog_id.getTrip_Id());  
		  }else {
			ps.setString(3, blog_id.getTrip_Id());
		}
		count = ps.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  return count;
		
	}
//刪除網誌
	@Override
	public int delete(String blog_id) {
		int count = 0;
		String sql = "DELETE FROM Blog_M WHERE BLOG_ID = ?";
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				
				){
			ps.setString(1, blog_id);
			count= ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public BlogFinish findBlogById(String blogId) {
		String sql = "select BLOG_TITLE,BLOG_DESC  from Blog_M where Blog_ID = ? ;";
	    BlogFinish blogFinish = null;
	    try (
	    		Connection connection = dataSource.getConnection();
	    		PreparedStatement ps = connection.prepareStatement(sql);
	    	
	    		){
	    	ps.setString(1,blogId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String title = rs.getString(1);
				String desc = rs. getString(2);
				 blogFinish  = new BlogFinish(title, desc);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return blogFinish;
	}

	@Override
	public int updateB_Note(Blog_Note blog_Note) {
		int count = 0 ;
		String sql = "Update Blog_D Set LOC_ID= ? ,LOC_NOTE= ?,BLOG_ID= ? where Blog_D.LOC_ID = ? and Blog_D.BLOG_ID = ?;";
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				
				){
			ps.setString(1,blog_Note.getLoc_Id());
			ps.setString(2, blog_Note.getLoc_Note());
			ps.setString(3, blog_Note.getBlog_Id());
			ps.setString(4, blog_Note.getLoc_Id());
			ps.setString(5, blog_Note.getBlog_Id());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 return count;
	}

	
	


}
