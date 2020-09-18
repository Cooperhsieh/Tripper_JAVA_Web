package web.com.dao;

import java.util.List;

import web.com.bean.Blog;



public interface BlogDao {
	
	List <Blog> getAll();

	byte[] getImage(int id);
	
	Blog findById(int id);

}
