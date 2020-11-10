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
import com.google.gson.JsonObject;

import web.com.bean.Member;
import web.com.bean.TripGroupMember;
import web.com.bean.Trip_Group;
import web.com.dao.Trip_Group_Dao;
import web.com.impl.Trip_Group_Dao_Impl;
import web.com.util.SettingUtil;

/**
 * 類別說明：Trip_Group_Servlet檔
 *
 * @author cooper
 * @version 建立時間:Sep 5, 2020
 * 
 */

@WebServlet("/Trip_Group_Servlet")
public class Trip_Group_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Trip_Group_Dao tripGroupDao = null;
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(request.getReader());
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}

		System.out.println("jsonIn input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		if (tripGroupDao == null) {
			tripGroupDao = new Trip_Group_Dao_Impl();
		}

		int count = 0;
		if (action.equals("update")) {
			List<Trip_Group> tripGroups = tripGroupDao.getAll();
			writeText(response, gson.toJson(tripGroups));
//若找得到自己ID 便把想參加按鈕gone掉
		} else if(action.equals("getMyGroup")) {
			String tripId = jsonObject.get("trip_Id").getAsString();
			String memberId = jsonObject.get("memberId").getAsString();
			int checkCount = tripGroupDao.selectMyGroup(tripId, memberId);
			writeText(response, String.valueOf(checkCount));		
		}else if(action.equals("getGroupCount")) {
			String trip_Id = jsonObject.get("trip_Id").getAsString();
			System.out.println("trip_Id ::" + trip_Id);
			int mcount = tripGroupDao.selectMCountByTripID(trip_Id);
			writeText(response, String.valueOf(mcount));
		}else if(action.equals("getGroupMbrList")) {
			String tripId = jsonObject.get("tripId").getAsString();
			List<Member> mbrList = tripGroupDao.getMbrList(tripId);
			writeText(response, gson.toJson(mbrList));
		}else if(action.equals("getApplicationList")) {
			String tripId = jsonObject.get("tripId").getAsString();
			List<Member> mbrList = tripGroupDao.getApplicationList(tripId);
			writeText(response, gson.toJson(mbrList));
		}		
		else {
			writeText(response, "");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (tripGroupDao == null) {
			tripGroupDao = new Trip_Group_Dao_Impl();
		}
		List<Trip_Group> tripGroups = tripGroupDao.getAll();
		writeText(response, new Gson().toJson(tripGroups));
	}

	private void writeText(HttpServletResponse response, String json) throws IOException {
		response.setContentType(SettingUtil.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(json);
		// TODO for debug
		System.out.println("response json: " + json);
		out.close();
	}

}
