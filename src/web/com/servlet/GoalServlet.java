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

import web.com.bean.Goal;
import web.com.dao.GoalDao;
import web.com.impl.GoalDaoImpl;
import web.com.util.SettingUtil;

/**
 * 類別說明：GoalServlet檔
 * 
 * @author Weixiang
 * @version 建立時間:Oct 27, 2020
 * 
 */

@WebServlet("/GoalServlet")
public class GoalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GoalDao goalDao = null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
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
		
		if (goalDao == null) {
			goalDao = new GoalDaoImpl();
		}
		
		String action = jsonObject.get("action").getAsString();
		if (action.equals("getGoalByMember")) {
			int memberId = jsonObject.get("memberId").getAsInt();
			List<Goal> goals = goalDao.getGoalByMember(memberId);
			writeText(response, gson.toJson(goals));
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (goalDao == null) {
			goalDao = new GoalDaoImpl();
		}
		JsonObject jsonObject = new JsonObject();
		int memberId = jsonObject.get("memberId").getAsInt();
		List<Goal> goals = goalDao.getGoalByMember(memberId);
		writeText(response, new Gson().toJson(goals));
	}
}
