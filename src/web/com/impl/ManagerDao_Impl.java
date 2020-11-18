package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Member;
import web.com.bean.Trip_Group;
import web.com.dao.ManagerDao;
import web.com.dao.MemberDao;
import web.com.util.ServiceLocator;

/**
 * 類別說明：會員DaoImpl檔
 * 
 * @author zhitin
 * @version 建立時間:Nov 13, 2020
 * 
 */
public class ManagerDao_Impl implements ManagerDao {
	
		DataSource dataSource;

		public ManagerDao_Impl() {
			dataSource = ServiceLocator.getInstance().getDataSource();
		}
	

	@Override
	public List<Member> getAll() {
	
		List<Member> memberList = new ArrayList<Member>();
		Member member = null;
		String sql = "select * from MANAGER " ;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int memberId = rs.getInt(1);
				String account = rs.getString(2);
				String password =rs.getString(3);
				String nickName = rs.getString(4);
				
				member = new Member(memberId, account, password, nickName);
				memberList.add(member);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberList;
	}


	@Override
	public int deleteManager(int memberId) {
		int count = 0 ;
		String sql = "delete from MANAGER where MEMBER_ID = ? ;" ;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql); ){
			ps.setInt(1, memberId);
			count = ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	

	
	
	
		
		

}
