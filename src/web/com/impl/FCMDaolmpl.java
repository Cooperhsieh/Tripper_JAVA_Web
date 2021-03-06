package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.api.Usage;

import web.com.bean.AppMessage;
import web.com.bean.Notify;
import web.com.dao.FCMDao;
import web.com.util.ServiceLocator;

/**
* 類別說明：
* @author Connor Fan
* @version 建立時間:Sep 29, 2020 7:07:02 PM
* 
*/
public class FCMDaolmpl implements FCMDao{
	DataSource dataSource;
	private static final int NOT_READ = 0;
	private static final int READ = 1;
	public FCMDaolmpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	@Override
	public int update(String token, int memberId) {
		int count = 0;
		String sql = " update MEMBER set TOKEN_ID = ? "+
					 " where MEMBER_ID = ?; ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, token);
			ps.setInt(2, memberId);
			System.out.println("FCMDaolmpl update sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public String getToken(int memberId) {
		String token = "";
		String sql = " select TOKEN_ID from MEMBER "
				   + " where MEMBER_ID = ?; ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, memberId);
			System.out.println("##getToken::" + ps.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				token = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return token;
	}
	@Override
	public int insertMsg(AppMessage msg) {
		int count = 0;
		String sql = " insert into MESSAGE ( " 
				   + " msg_type, member_id, msg_title, msg_body, msg_stat, send_id, reciver_id " // 7
				   + " ) values ( "
				   + " ?, ?, ?, ?, ?, ?, ? "
				   + " ); ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, msg.getMsgType());
			ps.setInt(2, msg.getMemberId());
			ps.setString(3, msg.getMsgTitle());
			ps.setString(4, msg.getMsgBody());
			ps.setInt(5, msg.getMsgStat());
			ps.setInt(6, msg.getSendId());
			ps.setInt(7, msg.getReciverId());
			System.out.println("insertMsg sql :: " + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	@Override
	public List<Notify> getAllMsg(int memberId) {
		List<Notify> notifies = new ArrayList<Notify>();
		String sql = " select "
				   + " MSG_TYPE, MSG_TITLE, MSG_BODY, MSG_STAT, SEND_ID, RECIVER_ID, "
				   + " ( select NICKNAME from MEMBER a where a.MEMBER_ID = b.MEMBER_ID ) NICKNAME, "
				   + " DATE_TIME "
				   + " from MESSAGE b " 
				   + " where RECIVER_ID = ? "
				   + " and MSG_STAT = ? "
				   + " order by DATE_TIME desc ; ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, memberId);
			ps.setInt(2, NOT_READ);
			System.out.println("## get Message sql:: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				// String msgType, int memberId, String msgTitle, String msgBody, int msgStat,
				// int sendId, int reciverId, String nickname, String notifyDateTime
				String msgType = rs.getString("MSG_TYPE");
				String msgTitle = rs.getString("MSG_TITLE");
				String msgBody = rs.getString("MSG_BODY");
				int msgStat = rs.getInt("MSG_STAT");
				int sendId = rs.getInt("SEND_ID");
				int reciverId = rs.getInt("RECIVER_ID");
				String nickname = rs.getString("NICKNAME");
				String notifyDateTime = rs.getString("DATE_TIME");
				Notify notify = new Notify(msgType, memberId, msgTitle, msgBody, msgStat, 
											sendId, reciverId, nickname, notifyDateTime);
				
				notifies.add(notify);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notifies;
	}
	@Override
	public int updateMsg(AppMessage msg) {
		String sql = " update MESSAGE "
				   + " set MSG_STAT = 1 "
				   + " where RECIVER_ID = ? "
				   + " and SEND_ID = ? "
				   + " and MSG_STAT = 0 "
				   ;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, msg.getReciverId());
			ps.setInt(2, msg.getSendId());
			System.out.println("updateMsg sql :" + ps.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	
	
	
	@Override
	public int insertChatMsg(AppMessage msg) {
		int count = 0;
		String sql = " insert into CHAT ( " 
				   + " msg_type, member_id, msg_title, msg_body, msg_stat, send_id, reciver_id ,uptime " // 7
				   + " ) values ( "
				   + " ?, ?, ?, ?, ?, ?, ? ,?"
				   + " ); ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, msg.getMsgType());
			ps.setInt(2, msg.getMemberId());
			ps.setString(3, msg.getMsgTitle());
			ps.setString(4, msg.getMsgBody());
			ps.setInt(5, msg.getMsgStat());
			ps.setInt(6, msg.getSendId());
			ps.setInt(7, msg.getReciverId());
			ps.setString(8,msg.getUpTime());
			System.out.println("insertChatMsg sql :: " + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public List<Notify> getChat(int memberId, int recierverId) {
		List<Notify> notifies = new ArrayList<Notify>();
		String sql = "select \n" + 
				"				    MSG_TYPE, MSG_TITLE, MSG_BODY, MSG_STAT, SEND_ID, RECIVER_ID, " + 
				"				    ( select NICKNAME from MEMBER a where a.MEMBER_ID = b.MEMBER_ID ) NICKNAME, " + 
				"				    UPTIME " + 
				"				    from Chat b " + 
				"				    where (SEND_ID = ? and RECIVER_ID = ?) or " + 
				"                    (SEND_ID = ? and RECIVER_ID = ?) " + 
				"				    and MSG_STAT = ?" + 
				"				    order by DATE_TIME asc ; ";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, memberId);
			ps.setInt(2, recierverId);
			ps.setInt(3, recierverId);
			ps.setInt(4, memberId);
			ps.setInt(5, NOT_READ);
			System.out.println("## get Message sql:: " + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				// String msgType, int memberId, String msgTitle, String msgBody, int msgStat,
				// int sendId, int reciverId, String nickname, String notifyDateTime
				String msgType = rs.getString("MSG_TYPE");
				String msgTitle = rs.getString("MSG_TITLE");
				String msgBody = rs.getString("MSG_BODY");
				int msgStat = rs.getInt("MSG_STAT");
				int sendId = rs.getInt("SEND_ID");
				int reciverId = rs.getInt("RECIVER_ID");
				String nickname = rs.getString("NICKNAME");
				String notifyDateTime = rs.getString("UPTIME");
				Notify notify = new Notify(msgType, memberId, msgTitle, msgBody, msgStat, 
											sendId, reciverId, nickname, notifyDateTime);
				
				notifies.add(notify);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notifies;
	}
	
	@Override
	public String getSenderName(AppMessage msg) {
		String senderNameString = "" ;
		String sql = "SELECT NICKNAME FROM Tripper.Member where MEMBER_ID = ?;";
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, msg.getMemberId());			
			System.out.println("getSenderName sql :: " + ps.toString());
			ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			senderNameString = rs.getString("NICKNAME");
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return senderNameString;
	}
	@Override
	public byte[] getSpotImage(String locId) {
		String sql = "SELECT LOC_PIC FROM TRIPPER.Location where LOC_ID = ?;";
		byte [] image = null;
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
			){
			ps.setString(1, locId);
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
