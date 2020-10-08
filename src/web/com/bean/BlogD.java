package web.com.bean;

import java.io.Serializable;

public class BlogD implements Serializable{
	private int dayCount;
	private int locationId;
	private String locationName;
	private String blogNote;
	private String s_Date;
	private int blogId;
	private String stayTime;
	private int tripId;
	
	
	public String getS_Date() {
		return s_Date;
	}
	public void setS_Date(String s_Date) {
		this.s_Date = s_Date;
	}
	public String getStayTime() {
		return stayTime;
	}
	public void setStayTime(String stayTime) {
		this.stayTime = stayTime;
	}
	public int getTripId() {
		return tripId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}
	public int getBlogId(){
          return blogId;
  }
	
	 public BlogD(String locationName) {
		 super();
		 this.locationName = locationName;
		 
		
	}
	public BlogD(int blogId,int dayCount, int locationId, String locationName, String blogNote, String s_Date) {
		super();
		this.blogId = blogId;
		this.dayCount = dayCount;
		this.locationId = locationId;
		this.locationName = locationName;
		this.blogNote = blogNote;
		this.s_Date = s_Date;
	}
	public BlogD(int blogId,int dayCount, int locationId, String locationName, String blogNote) {
		super();
		this.blogId = blogId;
		this.dayCount = dayCount;
		this.locationId = locationId;
		this.locationName = locationName;
		this.blogNote = blogNote;
	}
	public BlogD(int blogId,int locationId, String locationName, String blogNote,String s_Date,int tripId) {
		super();
		this.blogId = blogId;
	    this.locationId = locationId;
		this.locationName = locationName;
		this.blogNote = blogNote;
		this.s_Date = s_Date;
		this.tripId = tripId;
	}
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getBlogNote() {
		return blogNote;
	}
	public void setBlogNote(String blogNote) {
		this.blogNote = blogNote;
	}
	
	
	

}
