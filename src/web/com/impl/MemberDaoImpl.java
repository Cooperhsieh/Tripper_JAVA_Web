package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import web.com.bean.Member;
import web.com.dao.MemberDao;
import web.com.util.ServiceLocator;

/**
 * 類別說明：會員DaoImpl檔
 * 
 * @author zhitin
 * @version 建立時間:Sep 3, 2020
 * 
 */
public class MemberDaoImpl implements MemberDao {
	DataSource dataSource;

	public MemberDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
//註冊帳號
	@Override
	public int insert(Member member) {
		int confirm = selectAccount(member) ;
		int count = 0;
		String sql = "INSERT INTO Member" + "(MEMBER_ID,ACCOUNT_ID,PASSWORD,NICKNAME)" + "VALUES(?,?,?,?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			if(confirm ==0) {
			ps.setInt(1, member.getId());
			ps.setString(2, member.getAccount());
			ps.setString(3, member.getPassword());
			ps.setString(4, member.getNickName());
//			ps.setInt(6,member.getLoginType());
//			ps.setBytes(7, photo);
//			ps.setBytes(8, backgroundImage);
//			ps.setString(9, member.getToken());

			count = ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count; //成功註冊回傳1
	}
	
	//第三方註冊帳號
		@Override
		public int insertGB(Member member) {
			int count = 0;
			int confirm = selectAccount(member) ;
			String sql = "INSERT INTO Member" + "(MEMBER_ID,ACCOUNT_ID,PASSWORD,NICKNAME,LOGIN_TYPE)" + "VALUES(?,?,?,?,?);";
			try (Connection connection = dataSource.getConnection();
					PreparedStatement ps = connection.prepareStatement(sql)) {
				if(confirm ==0) {
				ps.setInt(1, member.getId());
				ps.setString(2, member.getAccount());
				ps.setString(3, member.getPassword());
				ps.setString(4, member.getNickName());
				ps.setInt(5,member.getLoginType());
				count = ps.executeUpdate();
				}
				if(confirm == 1) {
					count = 1 ;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count; //成功註冊回傳1
		}
		
//新增or修改大頭貼
	@Override
	public int update(Member member, byte[] p_pic) {
		int count = 0;
		String sql = "";
		if (p_pic != null) {
			sql = "UPDATE Member SET NICKNAME = ?,P_PIC = ? " + "WHERE MEMBER_ID = ?;";
		} else {
			sql = "UPDATE Member SET NICKNAME = ?" + "WHERE MEMBER_ID = ?;"; // 不分支可能導致新增時沒加照片，導致原本的圖檔被覆蓋成空值
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, member.getNickName());

			if (p_pic != null) {
				ps.setBytes(2, p_pic);
				ps.setInt(3, member.getId());
			} else {
				ps.setInt(2, member.getId());
			}
			System.out.println("update Member sql :: " + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
//透過帳號查找資訊	
	@Override
	public Member findByAccount(String account) {
		String sql = "SELECT * FROM Member WHERE ACCOUNT_ID = ?;";
		Member member = null ;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				String accountid = rs.getString(2);
				String password = rs.getString(3);
				String nickname = rs.getString(5);
				int Login_type = rs.getInt(6);
				member  = new Member(id,accountid,password,nickname);
				member.setLoginType(Login_type);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}
//透過ID找本帳號資訊(ID,ACCOUNT,PASSWORD,NICKNAME)	
	@Override
	public Member findById(int id) {
		String sql = "SELECT * FROM Member WHERE MEMBER_ID = ?;";
		Member member = null ;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
				String accountid = rs.getString(2);
				String password = rs.getString(3);
				String nickname = rs.getString(5);
				member  = new Member(id,accountid,password,nickname);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}


//透過id抓大頭貼照
	@Override
	public byte[] getP_picById(int id) {
		String sql = "SELECT P_PIC FROM Member WHERE MEMBER_ID = ?;";
		byte[] p_pic = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p_pic = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p_pic;
	}
//透過id抓封面照
	@Override
	public byte[] getB_picById(int id) {
		String sql = "SELECT B_PIC FROM Member WHERE MEMBER_ID = ?;";
		byte[] b_pic = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				b_pic = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b_pic;
	}
//搜尋帳號是否有重複
	@Override
	public int selectAccount(Member member) {
		String sql = "SELECT * FROM Member ;";
		int count = 0;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getString("ACCOUNT_ID").equals(member.getAccount())) {
					count = 1 ;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;  //有重複使用者回傳1
	}
//檢查密碼
	@Override
	public int selectAandP(Member member) {
		String sql = "SELECT * FROM Member ;";
		int count = 0 ;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);)
		{
			ResultSet rs = ps.executeQuery();
			while (rs.next()) { //如果使用者名稱和密碼都正確
				if(rs.getString("ACCOUNT_ID").equals(member.getAccount()) && rs.getString("PASSWORD").equals(member.getPassword())) {
					count = 1 ;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;  //帳號密碼正確回傳1
	}
	
}
