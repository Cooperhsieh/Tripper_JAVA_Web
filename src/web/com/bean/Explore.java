package web.com.bean;

public class Explore {
	private int blogId;
	private int userId;
	private String nickName;
    private String tittleName;
	public Explore(int blogId, int userId, String nickName, String tittleName) {
		super();
		this.blogId = blogId;
		this.userId = userId;
		this.nickName = nickName;
		this.tittleName = tittleName;
	}
	public int getBlogId() {
		return blogId;
	}
	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTittleName() {
		return tittleName;
	}
	public void setTittleName(String tittleName) {
		this.tittleName = tittleName;
	}
   
    


   

}
