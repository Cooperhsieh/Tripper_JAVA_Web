package web.com.dao;

import java.util.List;

import web.com.bean.Blog;
import web.com.bean.Blog_Note;



public interface BlogDao {
	
	List <Blog> getAll();

	byte[] getImage(int id);
	
	Blog findById(int id);
	
	int insertB_Note (Blog_Note blog_Note);

}
