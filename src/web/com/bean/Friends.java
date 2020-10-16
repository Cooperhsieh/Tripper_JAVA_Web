package web.com.bean;
/**
* 類別說明：
* @author Connor Fan
* @version 建立時間:Oct 4, 2020 9:43:53 PM
* 
*/
import java.io.Serializable;

public class Friends extends Member implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private int status;

    public Friends(int id, String account, String mail, String nickName, int loginType, String token, int status) {
        super(id, account, mail, nickName, loginType, token);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
