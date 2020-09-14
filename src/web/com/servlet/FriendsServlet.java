package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import web.com.bean.Friends;
import web.com.dao.FriendsDao;
import web.com.impl.FriendsDaoImpl;
import web.com.util.SettingUtil;

/**
 * 類別說明：FriendsServlet檔
 *
 * @author Weixiang
 * @version 建立時間:Sep 6, 2020
 * 
 */
@WebServlet("/FriendsServlet")
public class FriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
    FriendsDao friendsDao = null;   

    @Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = new BufferedReader(request.getReader());
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		System.out.println("input: " + jsonIn);
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		
		if (friendsDao == null) {
			friendsDao = new FriendsDaoImpl();
		}
		String action = jsonObject.get("action").toString();
		if (action.equals("getAll")) {
			List<Friends> friends = friendsDao.getAll();
			writeText(response, gson.toJson(friends));
			
		} else if (action.equals("friendsInsert")) {
			String friendsJson = jsonObject.get("Friends").getAsString();
			
			System.out.println("friends: " + friendsJson);
			
			Friends friends = gson.fromJson(friendsJson, Friends.class);
			writeText(response, String.valueOf(friends));
		// TODO	
		} else if (action.equals("friendsDelete")) {
			int memberId = jsonObject.get("memberId").getAsInt();
			int friendId = jsonObject.get("friendId").getAsInt();
			int count = friendsDao.delete(memberId, friendId);
			
		} else if (action.equals("findFriendTransId")) {
			String friendTransId = jsonObject.get("friendTransId").getAsString();
			Friends friends = friendsDao.findFriendTransId(friendTransId);
			writeText(response, gson.toJson(friends));
			
		} else {
			writeText(response, "");
		}
	}
    
    private void writeText(HttpServletResponse response, String json) throws IOException {
		response.setContentType(SettingUtil.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(json);
		// TODO for debug
		System.out.println("response json: " + json);
		out.close();
	}
    
    @Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (friendsDao == null) {
			friendsDao = new FriendsDaoImpl();
		}
		List<Friends> friends = friendsDao.getAll();
		writeText(response, new Gson().toJson(friends));
	}
}
