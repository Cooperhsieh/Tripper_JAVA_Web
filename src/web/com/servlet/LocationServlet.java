package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import web.com.bean.Location;
import web.com.dao.LocationDao;
import web.com.impl.LocationImpl;
import web.com.util.ImageUtil;
import web.com.util.SettingUtil;

/**
* 
* @author Connor Fan
* @version 建立時間:Sep 2, 2020 12:57:32 PM
* 
*/
@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    LocationDao locDao = null; 
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(request.getReader());
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		// TODO when finish remember to mark
		System.out.println("jsonIn input: " + jsonIn);
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		
		if( locDao == null) {
			locDao = new LocationImpl();
		}	
		String action = jsonObject.get("action").getAsString();
		if(action.equals("getAll")) {
			List<Location> locations = locDao.getAll();
			writeText(response, gson.toJson(locations));
		}else if(action.equals("getImage")) {
			System.out.println("enter getImage");
			OutputStream os = response.getOutputStream();
			String locId = jsonObject.get("id").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			System.out.println("imageSize::" + imageSize);
			byte[] image = locDao.getImageById(locId);






			System.out.println("image size ::" + image.length);
			System.out.println("1111 locId::" + locId);


			if(image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType(SettingUtil.IMAGE_JPEG);
				response.setContentLength(image.length);
				os.write(image);
			}
		}else if(action.equals("locationInsert") || action.equals("locationUpdate"))	{
			System.out.println("enter insert or update");
			String locationJson = jsonObject.get("location").getAsString();
			// TODO when finish remember to mark
			System.out.println("insert or update locJson: " + locationJson);
			Location loc = gson.fromJson(locationJson, Location.class);
			byte[] locImage = null;
			// 檢查是否有圖片
			if( jsonObject.get("imageBase64") != null) {
				System.out.println("enter get base64");
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				// 確認有圖片decode
				if(imageBase64 != null && !imageBase64.isEmpty()) {
					locImage = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if(action.equals("locationInsert")) {
				System.out.println("enter insert");
				count = locDao.insert(loc, locImage);
			}else if(action.equals("locationUpdate")){
				System.out.println("enter update");
				count = locDao.update(loc, locImage);
			}
			writeText(response, String.valueOf(count));
		}else if(action.equals("locationDelete")) {
			String locId = jsonObject.get("id").getAsString();
			int count = locDao.delete(locId);
			writeText(response, String.valueOf(count));
		}else if(action.equals("locationFindById")) {
			String locId = jsonObject.get("id").getAsString();
			Location loc = locDao.getLocById(locId);
			writeText(response, gson.toJson(loc));
		}else {
			writeText(response, "");
		}
		
	}
	
	private void writeText(HttpServletResponse response, String json) throws IOException {
		response.setContentType(SettingUtil.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(json);
		// TODO for debug 
		System.out.println("response json: " +  json);
		out.close();
	}

	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if( locDao == null) {
			locDao =  new LocationImpl();
		}
		List<Location> locations = locDao.getAll();
		writeText(response, new Gson().toJson(locations));
	}
	
	
}
