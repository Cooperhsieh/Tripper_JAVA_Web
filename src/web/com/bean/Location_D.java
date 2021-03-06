package web.com.bean;

import java.io.Serializable;

public class Location_D extends Location implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tripId;
    private String memos;
    private String stayTimes;
    private String transId;
    private String startDate;
    private String startTime;

    public Location_D(String name, String address, String locId,  String memos, String stayTimes,  String startDate) {
        super(name, address, locId);
        this.memos = memos;
        this.stayTimes = stayTimes;
        this.startDate = startDate;
    }
    
    
    
    
    public Location_D(String name, String address, String locId, String memos, String stayTimes) {
		super(name, address, locId);
		this.memos = memos;
		this.stayTimes = stayTimes;
		
	}




	public String getTrip() {
        return tripId;
    }

    public void setTrip(String tripId) {
        this.tripId = tripId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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

    public String getStayTimes() {
        return stayTimes;
    }

    public void setStayTimes(String stayTimes) {
        this.stayTimes = stayTimes;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }
}
