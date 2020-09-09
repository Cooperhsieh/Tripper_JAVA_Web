package web.com.dao;

import java.util.List;

import web.com.bean.Explore;
import web.com.bean.Member;



public interface ExploreDao {
	
	

	int delete(int id);

	Explore findById(int id);

	List<Explore> getAll();

	byte[] getImage(int id);

	List<Member> selectAll();

	

}
