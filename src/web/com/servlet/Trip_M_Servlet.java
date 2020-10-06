package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import web.com.bean.Trip_M;
import web.com.dao.Trip_M_Dao;
import web.com.impl.Trip_M_Dao_Impl;
import web.com.util.ImageUtil;
import web.com.util.SettingUtil;

/**
 * 類別說明：Trip_M_Servlet檔
 *
 * @author cooper
 * @version 建立時間:Sep 5, 2020
 * 
 */

@WebServlet("/Trip_M_Servlet")
public class Trip_M_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Trip_M_Dao tripMDao = null;

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

		if (tripMDao == null) {
			tripMDao = new Trip_M_Dao_Impl();
		}

		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Trip_M> tripMs = tripMDao.getAll();
			writeText(response, gson.toJson(tripMs));

		} else if (action.equals("tripMInsert") || action.equals("tripMUpdate")) {
			String tripMJson = jsonObject.get("tripM").getAsString();

			System.out.println("tripMJson " + tripMJson);

			Trip_M tripM = gson.fromJson(tripMJson, Trip_M.class);

			writeText(response, String.valueOf(tripM));

		} else if (action.equals("tripMDelete")) {
			String tripId = jsonObject.get("tripId").getAsString();
			int count = tripMDao.delete(tripId);
			writeText(response, String.valueOf(count));

		} else if (action.equals("getTripId")) {
			String tripId = jsonObject.get("tripId").getAsString();
//			Trip_M tripM = tripMDao.getTripId(tripId);
//			writeText(response, gson.toJson(tripM));
		
			
				
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			String id = jsonObject.get("id").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = tripMDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");    //傳送圖片格式
				response.setContentLength(image.length);  //圖片佔的大小
				os.write(image);
			}
		}
			else {
			writeText(response, "fuck");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (tripMDao == null) {
			tripMDao = new Trip_M_Dao_Impl();
		}

		List<Trip_M> tripMs = tripMDao.getAll();
		writeText(response, new Gson().toJson(tripMs));
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
