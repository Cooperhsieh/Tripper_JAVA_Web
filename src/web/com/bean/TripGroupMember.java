package web.com.bean;

import java.io.Serializable;

public class TripGroupMember extends Trip_Group implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private String nickName;

	public TripGroupMember(String tripId, int memberId, String nickName) {
        super(tripId, memberId);
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
