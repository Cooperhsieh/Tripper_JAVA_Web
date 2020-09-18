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

import io.grpc.netty.shaded.io.netty.channel.unix.Buffer;
import web.com.bean.Blog;
import web.com.bean.Explore;
import web.com.dao.BlogDao;
import web.com.impl.BlogImpl;
import web.com.impl.ExploreImpl;
import web.com.util.ImageUtil;


@WebServlet("/BlogServlet")
public class BlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final static String CONTENT_TYPE = "text/html; charset=utf-8"; 
    BlogDao blogDao = null;
    
    
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
		if(blogDao == null) {
			blogDao = new BlogImpl();
			
		}
		String action = jsonObject.get("action").getAsString();
		
		if(action.equals("getAll")) {
			List<Blog> blogs = blogDao.getAll();
			writeText(response, gson.toJson(blogs));
		}else if(action.equals("getImage")){
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = blogDao.getImage(id);
			if(image !=null) {
				image =ImageUtil.shrink(image, imageSize);
				response.setContentLength(image.length);
				response.setContentType("image/jpeg");
				os.write(image);
		}
		}else if(action.equals("findById")) {
			int id = jsonObject.get("id").getAsInt();
			Blog blog = blogDao.findById(id);
			writeText(response, gson.toJson(blog));
		}else {
				writeText(response, "");
			}
	}
   
		
		
	
	

	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		 System.out.println("output: " + outText);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (blogDao == null) {
			blogDao = new BlogImpl();
		}
		List<Blog> blogs = blogDao.getAll();
		writeText(response, new Gson().toJson(blogs));
	}

}
