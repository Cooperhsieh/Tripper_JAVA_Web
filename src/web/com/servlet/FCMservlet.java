package web.com.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import web.com.bean.AppMessage;
import web.com.dao.FCMDao;
import web.com.impl.FCMDaolmpl;
import web.com.util.SettingUtil;

@WebServlet("/FCMServlet")
public class FCMservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userToken = "";
	private FCMDao fcmDao = null;
//	@Override
//	public void init() throws ServletException {		
//		FileInputStream serviceAccount = null;
//		try {
//			serviceAccount = new FileInputStream("/google-services-2.json");
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//					  .setDatabaseUrl("https://myfirebasemessage-ba28b.firebaseio.com")
//					  .build();
//			FirebaseApp.initializeApp(options);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			
//	}
	
	
	@Override
	public void init() throws ServletException {		
		try (InputStream in = getServletContext().getResourceAsStream("/firebaseServlet.json")) {
				FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(in))
					.build();
			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = "";
		while( (line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("FCM jasonIn::" + jsonIn.toString());
		if(fcmDao == null) {
			fcmDao = new FCMDaolmpl();
		}
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		if(action.equals("register")) {
			userToken = jsonObject.get("userToken").getAsString();
			int memberId = jsonObject.get("memberId").getAsInt();
			if(!userToken.isEmpty() && memberId > 0) {
				int count = 0;
				count = fcmDao.update(userToken, memberId);
				writeText(response, count + "");
			}
		}else if(action.equals("sendMsg")) {
			String messageStr = jsonObject.get("message").getAsString();
			AppMessage message = gson.fromJson(messageStr, AppMessage.class);
			// 取得token發出推播
			String token = fcmDao.getToken(message.getReciverId());
			// 將message寫入DB
			int count = fcmDao.insertMsg(message);
			sendSingleFcm(message, token);
			writeText(response, count + "");
		}
		

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	private void writeText(HttpServletResponse response, String json) throws IOException {
        response.setContentType(SettingUtil.CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.print(json);
        // TODO for debug 
        System.out.println("response json: " +  json);
        out.close();
    }
	
	private void sendSingleFcm(AppMessage msg, String token) {
		String title = msg.getMsgTitle();
		String body = msg.getMsgBody();
		String data = "Notify";
		
		// 主要設定訊息標題跟內容，client app一定要在背景時才會自動顯示
		Notification notification = Notification.builder()
									.setTitle(title)
									.setBody(body)
									.build();
		// 發送notification message
		Message message = Message.builder()
				.setNotification(notification)
				.putData("data", data)
				.setToken(token)
				.build();
		String msgCode = "";
		try {
			msgCode = FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
		System.out.println("### msgCode:: " + msgCode);
	}

}