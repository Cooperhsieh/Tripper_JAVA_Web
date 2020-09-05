package web.com.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* 類別說明：好友資料Bean檔
* 
* @author Weixiang
* @version 建立時間:Sep 4, 2020 15:45 PM
* 
*/
public class Friends implements Serializable {
	private static final long serialVersionUID = 1L;
	private String friendTransId; // 好友資料流水編號
	private	int memberId; // 會員ID
	private int friendId; // 好友ID
	private int friendStatus; // 好友狀態碼
	private Timestamp applyDateTime; // 申請日期 // TODO
	private Timestamp modifyDateTime; // 資料修改日期 // TODO
	
	public Friends(String friendTransId, int memberId, int friendId, int friendStatus, Timestamp applyDateTime) {
		super();
		this.friendTransId = friendTransId;
		this.memberId = memberId;
		this.friendId = friendId;
		this.friendStatus = friendStatus;
		this.applyDateTime = applyDateTime;
	}

	public Friends(int memberId, int friendId, int friendStatus, Timestamp applyDateTime) {
		super();
		this.memberId = memberId;
		this.friendId = friendId;
		this.friendStatus = friendStatus;
		this.applyDateTime = applyDateTime;
	}
		
	public String getFriendTransId() {
		return friendTransId;
	}
	public void setFriendTransId(String friendTransId) {
		this.friendTransId = friendTransId;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public int getFriendStatus() {
		return friendStatus;
	}
	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}
	public Timestamp getApplyDateTime() {
		return applyDateTime;
	}
	public void setApplyDateTime(Timestamp applyDateTime) {
		this.applyDateTime = applyDateTime;
	}
	public Timestamp getModifyDateTime() {
		return modifyDateTime;
	}
	public void setModifyDateTime(Timestamp modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

}
