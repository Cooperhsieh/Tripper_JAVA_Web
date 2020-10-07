package web.com.dao;

import java.util.List;


import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;



public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	
	List <BlogD> findById(int id);
	
//	public BlogD findLocationById(int id);

	public List<BlogD>findLocationById(int blodId, String datetime);

}
