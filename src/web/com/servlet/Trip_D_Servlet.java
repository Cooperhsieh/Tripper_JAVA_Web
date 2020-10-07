package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.StringValue;

import web.com.bean.Blog_SpotInfo;
import web.com.bean.DateAndId;
import web.com.bean.Location_D;
import web.com.bean.Trip_D;
import web.com.bean.Trip_LocInfo;
import web.com.dao.Trip_D_Dao;
import web.com.impl.Trip_D_Dao_Impl;
import web.com.util.SettingUtil;

/**
 * 類別說明：Trip_D_Servlet檔
 *
 * @author cooper
 * @version 建立時間:Sep 5, 2020
 * 
 */

@WebServlet("/Trip_D_Servlet")
public class Trip_D_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Trip_D_Dao tripDDao = null;

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

		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);

		if (tripDDao == null) {
			tripDDao = new Trip_D_Dao_Impl();
		}

		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Trip_D> tripDs = tripDDao.getAll();
			writeText(response, gson.toJson(tripDs));

		} else if (action.equals("tripDInsert") || action.equals("tripDUpdate")) {
			String tripDJson = jsonObject.get("tripD").getAsString();

			System.out.println("tripD" + tripDJson);

			Trip_D tripD = gson.fromJson(tripDJson, Trip_D.class);
			writeText(response, String.valueOf(tripD));
//抓取景點名稱 建立網誌用
		} else if (action.equals("getSpotName")) {
			String dateId = jsonObject.get("dateAndId").getAsString();
			System.out.println("dateAndId" + dateId);
			DateAndId dateAndId = gson.fromJson(dateId, DateAndId.class);

			List<Blog_SpotInfo> spotNames = new ArrayList<Blog_SpotInfo>();

			spotNames = tripDDao.getSpotName(dateAndId);
			System.out.println("spotNames::" + spotNames);
			writeText(response, gson.toJson(spotNames));

//抓取行程完整的景點資料
		} else if (action.equals("getLocName")) {
			String dateId = jsonObject.get("dateAndId").getAsString();
			System.out.println("dateAndId1: " + dateId);
			DateAndId dateAndId = gson.fromJson(dateId, DateAndId.class);

			List<Trip_LocInfo> LocNames = new ArrayList<Trip_LocInfo>();

			LocNames = tripDDao.getLocName(dateAndId);
			System.out.println("LocNames::" + LocNames);
			writeText(response, gson.toJson(LocNames));
			
//編輯行程頁面顯示景點			
		} else if (action.equals("showLocName")) {
			String tripId = jsonObject.get("tripId").getAsString();
			System.out.println("編輯頁面秀行程ID: " + tripId);
					
			Map<String, List<Trip_LocInfo>> map = new TreeMap<String, List<Trip_LocInfo>>();
			map = (Map<String, List<Trip_LocInfo>>) tripDDao.showLocName(tripId);	
			writeText(response, gson.toJson(map));
		}

		else if (action.equals("tripDDelete")) {
			String tripId = jsonObject.get("tripId").getAsString();
			int count = tripDDao.delete(tripId);
			writeText(response, gson.toJson(count));

		} else if (action.equals("findTransId")) {
			String tripId = jsonObject.get("tripId").getAsString();
			// Trip_D tripD = tripDDao.findTransId(tripId);
			// writeText(response, gson.toJson(tripD));

		} else {
			writeText(response, "");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (tripDDao == null) {
			tripDDao = new Trip_D_Dao_Impl();
		}

		List<Trip_D> tripDs = tripDDao.getAll();
		writeText(response, new Gson().toJson(tripDs));
	}

	private void writeText(HttpServletResponse response, String json) throws IOException {
		response.setContentType(SettingUtil.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(json);

		System.out.println("response json: " + json);
		out.close();
	}

}
