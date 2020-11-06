package web.com.dao;

import java.util.List;


import web.com.bean.Blog;
import web.com.bean.Blog_Note;
import web.com.bean.Blog_Pic;
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

	byte[] getImage(String id);
	
	Blog findById1(int id);

	
	int insertB_Comment(Blog_Comment blog_Comment);

//將發布網誌的資料存進DB
	int insetBlog_M (BlogFinish blogFinish,byte[] b_Pic);
//將網誌景點的心得存進DB

	int insertB_Note (Blog_Note blog_Note);
//將網誌景點的照片存進DB	
	int insertImage(byte[] image1,byte[] image2,byte[] image3,byte[] image4,String blogId,String locId);
//將網誌景點的照片從DB更改
	int updateImage(Blog_Pic blog_Pic , byte[] image1,byte[] image2,byte[] image3,byte[] image4);
	
	int deleteComment(int comID);
	List <BlogD> findById(String id);
	int updateB_Note(Blog_Note blog_Note);
//	public BlogD findLocationById(int id);
//編輯網誌的資料存進DB
	int update(BlogFinish blog_id,byte[] b_Pic);
//刪除存在DB的網誌資料	
	int delete(String blog_id);
//更新留言內容
	int updateComment(Blog_Comment blog_Comment);
    public BlogFinish findBlogById(String blogId);
	
	public List<Blog_Day> findDateById(String blodId);


	public List<Blog_SpotInformation> getSpotName(String s_Date, String blogId) ;
	
	public List<Blog_Comment> findCommentById(String blogId);
	
	List<byte[]> getSpotImages (String loc_Id,String blog_id);

	public List<BlogFinish> getMyBlog(String memberId); 
}
