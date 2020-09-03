package web.com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.util.ServiceLocator;
import web.com.bean.Member;

/**
* 類別說明：會員DaoImpl檔
* @author zhitin 
* @version 建立時間:Sep 3, 2020 
* 
*/
public class MemberDaoImpl implements MemberDao{
	DataSource dataSource ;
	
	public MemberDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();	
	}

	@Override
	public int insert(Member member, byte[] photo, byte[] backgroundImage) {
		int count = 0;
		String sql = "INSERT INTO Member"+"(MEMBER_ID,ACCOUNT_ID,PASSWORD,EMAIL,NICKNAME,LOGIN_TYPE,P_PIC,B_PIC,TOKEN_ID,C_DATETIME,L_DATETIME)"
		+"VALUES(?,?,?,?,?,?,?,?,?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1,member.getId());
			ps.setString(2,member.getAccount());
			ps.setString(3,member.getPassword());
			ps.setString(4,member.getMail());
			ps.setString(5,member.getNickName());
			ps.setInt(6,member.getLoginType());
			ps.setBytes(7, photo);
			ps.setBytes(8, backgroundImage);
			ps.setString(9, member.getToken());
			
			count = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Member member, byte[] photo, byte[] backgroundImage) {
		int count = 0;
		String sql = "";
		if (photo != null) {
			sql = "UPDATE Member SET PASSWORD = ?, NICKNAME = ?,P_PIC = ?, "
					+ "WHERE MEMBER_ID = ?;";
		} else {
			sql = "UPDATE Member SET PASSWORD = ?, NICKNAME = ?,"
					+ "WHERE MEMBER_ID = ?;";     //不分支可能導致新增時沒加照片，導致原本的圖檔被覆蓋成空值
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

	@Override
	public Member findByKey(String account, String password) {
		// TODO Auto-generated method stub
		return null;
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
}
