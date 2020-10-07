package web.com.dao;

import java.util.List;


import web.com.bean.Blog;
import web.com.bean.Blog_Note;


import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;




public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	

	Blog findById1(int id);
	
	int insertB_Note (Blog_Note blog_Note);
	
	int updateImage(byte[] image,String blogId,String tripId);

	List <BlogD> findById(int id);
	
//	public BlogD findLocationById(int id);

	public List<BlogD>findLocationById(int blodId, String datetime);


}
