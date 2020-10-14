package web.com.dao;

import java.util.List;


import web.com.bean.Blog;
import web.com.bean.Blog_Note;


import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Comment;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;




public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	

	Blog findById1(int id);
	
	int insertB_Comment(Blog_Comment blog_Comment);
	int insertB_Note (Blog_Note blog_Note);
	
	int updateImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String tripId);

	List <BlogD> findById(String id);
	
//	public BlogD findLocationById(int id);

	
	public List<Blog_Day> findDateById(String blodId);


	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId) ;
	
	public List<Blog_Comment> findCommentById(String blogId);

}
