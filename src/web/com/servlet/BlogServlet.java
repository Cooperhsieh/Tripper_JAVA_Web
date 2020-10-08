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
import io.grpc.netty.shaded.io.netty.channel.unix.Buffer;

import web.com.bean.Blog;
import web.com.bean.Blog_Note;
import web.com.bean.Blog_Pic;
import web.com.bean.BlogD;
import web.com.bean.BlogM;

import web.com.bean.Explore;
import web.com.dao.BlogDao;
import web.com.impl.BlogImpl;
import web.com.util.ImageUtil;


@WebServlet("/BlogServlet")
public class BlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final static String CONTENT_TYPE = "text/html; charset=utf-8"; 
    BlogDao blogDao = null;
    
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
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

//			List<Blog> blogs = blogDao.getAll;
//			writeText(response, gson.toJson(blogs));
//網誌新增註解
		}else if(action.equals("insertBlogNote")) {
			String b_noteJson = jsonObject.get("blog_note").getAsString();
			System.out.println("Blog_Note ::"+ b_noteJson);
			Blog_Note blog_Note = gson.fromJson(b_noteJson, Blog_Note.class);
			int count = 0 ;
			count = blogDao.insertB_Note(blog_Note);
			writeText(response, String.valueOf(count));
		}	
//檢查是否有上傳圖片
		
		else if (action.equals("imageUpdate")) {
			String blogPic_json = jsonObject.get("blogPic").getAsString();
			System.out.println("BlogPic ::"+ blogPic_json);
			Blog_Pic blog_Pic = gson.fromJson(blogPic_json, Blog_Pic.class);			
			int count = 0 ;
			byte[] image1 = null,image2 = null ,image3 = null ,image4 = null ;
			if(blog_Pic.getPic1() != null && !blog_Pic.getPic1().isEmpty()) {
				 image1 = Base64.getMimeDecoder().decode(blog_Pic.getPic1());
			}
			if(blog_Pic.getPic2() != null && !blog_Pic.getPic2().isEmpty()) {
				 image2 = Base64.getMimeDecoder().decode(blog_Pic.getPic2());
			}
			if(blog_Pic.getPic3() != null && !blog_Pic.getPic3().isEmpty()) {
				 image3 = Base64.getMimeDecoder().decode(blog_Pic.getPic3());
			}
			if(blog_Pic.getPic4() != null && !blog_Pic.getPic4().isEmpty()) {
				 image4 = Base64.getMimeDecoder().decode(blog_Pic.getPic4());
			}
			count = blogDao.updateImage(image1,image2,image3,image4,blog_Pic.getBlogId(),blog_Pic.getLocId());
			writeText(response, String.valueOf(count));
			
		}
		else if(action.equals("getImage")){

//			List<BlogD> blogs = blogDao.findBlogById(id);
//			writeText(response, gson.toJson(blogs));
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
//		}else if(action.equals("findById")) {
//			int id = jsonObject.get("id").getAsInt();
//			List<BlogD> blist = blogDao.findById(id);
//			writeText(response, gson.toJson(blist));
//		}else if(action.equals("findDateId")) {
//			int id = jsonObject.get("id").getAsInt();
////			List<BlogD> blist = blogDao.findById(id);
//			writeText(response, gson.toJson(blist));}
		else {
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
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (blogDao == null) {
			blogDao = new BlogImpl();
		}
//		List<BlogD> blogs = blogDao.getAll();
//		writeText(response, new Gson().toJson(blogs));
	}

}
