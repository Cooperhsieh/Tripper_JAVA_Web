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
import web.com.bean.Member;
import web.com.dao.FriendsDao;
import web.com.dao.MemberDao;
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
    FriendsDao friendsDao = null;
    MemberDao memberDao = null;
    private static final int NOT_FRIEND = 0; 
	private static final int CHECKING = 1; // 邀請好友待確認
	private static final int FRIEND = 2; // 已是好友

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
		
		String action = jsonObject.get("action").getAsString();
		if (action.equals("getAll")) {
			int MemberId = jsonObject.get("memberId").getAsInt();
			List<Member> friends = friendsDao.getAll(MemberId);
			writeText(response, gson.toJson(friends));
		} else if (action.equals("search")) {
			int memberId = jsonObject.get("memberId").getAsInt();
			String account = jsonObject.get("account").getAsString();	
			Friends friend = friendsDao.findSearchFriend(memberId, account);
			writeText(response, gson.toJson(friend));
		} else if(action.equals("insert")) {
			// 加入好友
			int memberId = jsonObject.get("memberId").getAsInt();
			int friendId = jsonObject.get("friendId").getAsInt();
			int count = friendsDao.insert(memberId, friendId, CHECKING);
			writeText(response, gson.toJson(count));
		}else if(action.equals("apply")) {
			// 按下同意
			int memberId = jsonObject.get("memberId").getAsInt();
			int friendId = jsonObject.get("friendId").getAsInt();
			int count = friendsDao.insert(memberId, friendId, FRIEND);
				count = friendsDao.update(friendId, memberId, FRIEND);
			writeText(response, gson.toJson(count));
		}else if(action.equals("delete")) {
			
			// 按拒絕的人
			int memberId = jsonObject.get("memberId").getAsInt();
			// 原申請的人
			int friendId = jsonObject.get("friendId").getAsInt();
			// 刪除原申請好友的資料，由於是被加好友的人按拒絕，所以刪除的時候須相反過來
			int count = friendsDao.delete(friendId, memberId);
		}
	}
    
    private void writeText(HttpServletResponse response, String json) throws IOException {
		response.setContentType(SettingUtil.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(json);
		System.out.println("response json: " + json);
		out.close();
	}
    
    @Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (friendsDao == null) {
			friendsDao = new FriendsDaoImpl();
		}
		JsonObject jsonObject = new JsonObject();
		int MemberId = jsonObject.get("MemberId").getAsInt();
		List<Member> friends = friendsDao.getAll(MemberId);
		writeText(response, new Gson().toJson(friends));
	}
}
