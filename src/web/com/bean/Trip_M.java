package web.com.bean;

import java.io.Serializable;

/**
 * 類別說明：行程主檔_Master
 * 
 * @author Cooper Hsieh
 * @version 建立時間:Sep 3, 2020
 * 
 */

public class Trip_M implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tripId;
	private int memberId;
	private String tripTitle;
	private String startDate;
	private String startTime;
	private int dayCount;
	private String createDateTime;
	private int pMax;
	private int status;
	private byte[] bPic;
	private int mcount ;
	

	public Trip_M(String tripId, int memberId, String tripTitle, String startDate, String startTime, int dayCount,
			int pMax, int status, byte[] bPic) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dayCount = dayCount;
		this.pMax = pMax;
		this.status = status;
		this.bPic = bPic;
	}

	public Trip_M(int memberId, String tripTitle, String startDate, String startTime, int dayCount, int pMax,
			int status) {
		super();
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dayCount = dayCount;
		this.pMax = pMax;
		this.status = status;
	}

	public Trip_M(String tripId, int memberId, String tripTitle, String startDate, int pMax, int mCount) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
		this.startDate = startDate;
		this.tripTitle = tripTitle;
		this.pMax = pMax;
	}

	public Trip_M(String tripId, int memberId, String tripTitle, int dayCount, int pMax, int status) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.dayCount = dayCount;
		this.pMax = pMax;
		this.status = status;
	}

	public Trip_M(int memberId, String tripTitle, String startDate, String startTime, int dayCount, int pMax) {
		super();
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dayCount = dayCount;
		this.pMax = pMax;
	}
	
	

	public Trip_M(String tripId, int memberId, String tripTitle, String startDate, String startTime, int pMax,
			int status, int mcount) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.pMax = pMax;
		this.status = status;
		this.mcount = mcount;
	}

	public Trip_M(String tripId, int memberId, String tripTitle, String startDate, String startTime, int dayCount,
			String createDateTime, int pMax, int status) {
		super();
		this.tripId = tripId;
		this.memberId = memberId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dayCount = dayCount;
		this.createDateTime = createDateTime;
		this.pMax = pMax;
		this.status = status;
	}

	public Trip_M(String tripId, String tripTitle, String startDate, String startTime, int dayCount, int pMax,
			int status) {
		super();
		this.tripId = tripId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dayCount = dayCount;
		this.pMax = pMax;
		this.status = status;
	}

	public Trip_M(int status) {
		this.status = status;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getTripTitle() {
		return tripTitle;
	}

	public void setTripTitle(String tripTitle) {
		this.tripTitle = tripTitle;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getpMax() {
		return pMax;
	}

	public void setpMax(int pMax) {
		this.pMax = pMax;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte[] getbPic() {
		return bPic;
	}

	public void setbPic(byte[] bPic) {
		this.bPic = bPic;
	}
	public int getMcount() {
		return mcount;
	}

	public void setMcount(int mcount) {
		this.mcount = mcount;
	}

}
