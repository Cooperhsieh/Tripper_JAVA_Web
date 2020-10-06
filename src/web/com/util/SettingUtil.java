package web.com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
* 類別說明：設定常數設定檔
* @author Connor Fan
* @version 建立時間:Sep 3, 2020 1:32:54 PM
* 
*/
public class SettingUtil {
	public static final String CONTENT_TYPE = "text/html; charset=utf-8";
	public static final String IMAGE_JPEG= "image/jpeg";
	
	public static String getTransId(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssss");
        String transId = dateFormat.format(System.currentTimeMillis());
        return transId;
    }
}
