package web.com.dao;

import java.util.List;


import web.com.bean.BlogD;
import web.com.bean.BlogM;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;



public interface BlogDao {
	
//	List <BlogD> getAll();

	byte[] getImage(int id);
	
	List <BlogD> findById(int id);
	
//	public BlogD findLocationById(int id);

	
	public List<Blog_Day> findDateById(int blodId);

	public List<Blog_SpotInformation> getSpotName(String s_Date, int blogId) ;
}
