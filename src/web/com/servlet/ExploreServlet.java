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

import web.com.bean.Explore;
import web.com.bean.Like;
import web.com.bean.Location;
import web.com.bean.Member;
import web.com.dao.ExploreDao;
import web.com.impl.ExploreImpl;
import web.com.util.ImageUtil;




@WebServlet("/ExploreServlet")
public class ExploreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
    ExploreDao exploreDao = null;
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
	    BufferedReader br = request.getReader();
	    StringBuilder jsonIn = new StringBuilder();
	    String line = null;
	    while ((line = br.readLine()) != null) {
		   jsonIn.append(line);
	    }
		System.out.print("input" + jsonIn);
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if(exploreDao == null) {
			exploreDao = new ExploreImpl();
		}
		String action = jsonObject.get("action").getAsString();
		
		if (action.equals("getAll")) {
			String loginUser = jsonObject.get("loginUserId").getAsString();
			List<Explore> explores = exploreDao.getAll(loginUser);
			writeText(response, gson.toJson(explores));
		}else if(action.equals("getAllIos")){ 
			List<Explore> explores = exploreDao.getAllIos();
			writeText(response, gson.toJson(explores));
		}else if(action.equals("getLikes")) {
			String blogId = jsonObject.get("blogId").getAsString();
			List<Like> likes = exploreDao.getAllLikes(blogId);
			writeText(response,gson.toJson(likes));
		}else if(action.equals("getHotSpot")) {
				List<Location> locations = exploreDao.getHotLocationAll();
				writeText(response, gson.toJson(locations));
		}else if(action.equals("getImage")) {
				OutputStream os = response.getOutputStream();
				int id = jsonObject.get("id").getAsInt();
//				String blogId = jsonObject.get("blogId").getAsString();
				int imageSize = jsonObject.get("imageSize").getAsInt();
				byte[] image = exploreDao.getImage(id);
				if(image != null) {
					image = ImageUtil.shrink(image, imageSize);
					response.setContentLength(image.length);
					response.setContentType("image/jpeg");
					os.write(image);
					
				}else {
					writeText(response, "");
				}
				
			
		}
		
	}
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		 System.out.println("output: " + outText);
	}

//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		if (exploreDao == null) {
//			exploreDao = new ExploreImpl();
//		}
//		String loginUser
//		List<Explore> explores = exploreDao.getAll();
//		writeText(response, new Gson().toJson(explores));
//	}
	

}
