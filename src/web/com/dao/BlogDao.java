package web.com.dao;

import java.util.List;


import web.com.bean.Blog;
import web.com.bean.Blog_Note;


import web.com.bean.BlogD;
import web.com.bean.BlogFinish;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;
import web.com.bean.Trip_M;




public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	
	Blog findById1(int id);
//將發布網誌的資料存進DB
	int insetBlog_M (BlogFinish blogFinish,byte[] b_Pic);
//將網誌景點的心得存進DB
	int insertB_Note (Blog_Note blog_Note);
//將網誌景點的照片存進DB	
	int updateImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String tripId);

	List <BlogD> findById(int id);
	
//	public BlogD findLocationById(int id);

	
	public List<Blog_Day> findDateById(int blodId);


	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId);
	
	List<byte[]> getSpotImages (String loc_Id,String blog_id);

	public List<BlogFinish> getMyBlog(String memberId); 
}
