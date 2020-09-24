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
	Trip_Group_Dao tripGroupDao = null;
	

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

		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (tripGroupDao == null) {
			tripGroupDao = new Trip_Group_Dao_Impl();
		}

		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Trip_Group> tripGroups = tripGroupDao.getAll();
			writeText(response, gson.toJson(tripGroups));

		} else if (action.equals("tripGroupInsert") || action.equals("tripGroupUpdate")) {
			String tripGroupJson = jsonObject.get("tripGroup").getAsString();

			System.out.println("tripGroup" + tripGroupJson);

			Trip_Group tripGroup = gson.fromJson(tripGroupJson, Trip_Group.class);
			writeText(response, String.valueOf(tripGroup));

		} else if (action.equals("tripGroupDelete")) {
			String tripId = jsonObject.get("tripId").getAsString();
			int memberId = jsonObject.get("memberId").getAsInt();
			int count = tripGroupDao.delete(tripId);
			writeText(response, String.valueOf(count));

		} else if (action.equals("findGroupTripId")) {
			String tripId = jsonObject.get("tripId").getAsString();
			List<TripGroupMember> tripGroups = tripGroupDao.findGroupTripId(tripId);
			writeText(response, gson.toJson(tripGroups));

		} else {
			writeText(response, "");
		}

	}

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
