package web.com.dao;

import java.util.List;


import web.com.bean.Blog;
import web.com.bean.Blog_Note;


import web.com.bean.BlogD;
import web.com.bean.BlogFinish;
import web.com.bean.BlogM;
import web.com.bean.Blog_Comment;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;
import web.com.bean.Trip_M;




public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	
	Blog findById1(int id);
<<<<<<< HEAD
	
	int insertB_Comment(Blog_Comment blog_Comment);
=======
//將發布網誌的資料存進DB
	int insetBlog_M (BlogFinish blogFinish,byte[] b_Pic);
//將網誌景點的心得存進DB
>>>>>>> dfb9f9cd750918df6c9ec48809bba15112800a9c
	int insertB_Note (Blog_Note blog_Note);
//將網誌景點的照片存進DB	
	int updateImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String tripId);

	List <BlogD> findById(String id);
	
//	public BlogD findLocationById(int id);

	
	public List<Blog_Day> findDateById(String blodId);


<<<<<<< HEAD
	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId) ;
	
	public List<Blog_Comment> findCommentById(String blogId);
=======
	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId);
	
	List<byte[]> getSpotImages (String loc_Id,String blog_id);
>>>>>>> dfb9f9cd750918df6c9ec48809bba15112800a9c

	public List<BlogFinish> getMyBlog(String memberId); 
}
