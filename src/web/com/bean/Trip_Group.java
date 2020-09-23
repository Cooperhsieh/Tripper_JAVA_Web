package web.com.bean;

import java.io.Serializable;

/**
 * 類別說明：行程附檔_Group揪團
 * 
 * @author Cooper
 * @version 建立時間:Sep 3, 2020
 *
 */

public class Trip_Group implements Serializable {
	private static final long serialVersionUID = 1L;
	private String groupTransId;
	private String tripId;
	private String createDateTime;
	private int memberId;

	public Trip_Group(String groupTransId, String tripId, String createDateTime, int memberId) {
		super();
		this.groupTransId = groupTransId;
		this.tripId = tripId;
		this.createDateTime = createDateTime;
		this.memberId = memberId;
	}
	
	public Trip_Group(String tripId, int memberId) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
	}
	
	public Trip_Group(String groupTransId, String tripId, int memberId) {
		super();
		this.groupTransId = groupTransId;
		this.tripId = tripId;
		this.memberId = memberId;
	}

	public String getGroupTransId() {
		return groupTransId;
	}

	

	public void setGroupTransId(String groupTransId) {
		this.groupTransId = groupTransId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

}
