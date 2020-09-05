package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.util.ServiceLocator;
import web.com.bean.Member;
import web.com.dao.MemberDao;

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

	@Override
	public int update(Member member, byte[] photo) {
		int count = 0;
		String sql = "";
		if (photo != null) {
			sql = "UPDATE Member SET PASSWORD = ?, NICKNAME = ?,P_PIC = ?, " + "WHERE MEMBER_ID = ?;";
		} else {
			sql = "UPDATE Member SET PASSWORD = ?, NICKNAME = ?," + "WHERE MEMBER_ID = ?;"; // 不分支可能導致新增時沒加照片，導致原本的圖檔被覆蓋成空值
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, member.getPassword());
			ps.setString(2, member.getNickName());

			if (photo != null) {
				ps.setBytes(3, photo);
				ps.setInt(4, member.getId());
			} else {
				ps.setInt(3, member.getId());
			}
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
//透過帳號資訊
	
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
				member  = new Member(id,accountid,password,nickname);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	@Override
	public List<Member> selectAll() {
		String sql = "SELECT * FROM Member;";
		List<Member> memberList = new ArrayList<Member>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String account = rs.getString(2);
				Member member = new Member(id, account);
				memberList.add(member);
			}
			return memberList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberList;
	}

	@Override
	public byte[] getphoto(int id) {
		String sql = "SELECT P_PIC FROM Member WHERE MEMBER_ID = ?;";
		byte[] photo = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				photo = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return photo;
	}

	@Override
	public byte[] getbackground(int id) {
		String sql = "SELECT B_PIC FROM Member WHERE MEMBER_ID = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
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
