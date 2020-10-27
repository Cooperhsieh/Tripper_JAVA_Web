package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.opencensus.common.ServerStatsFieldEnums.Id;
import web.com.bean.Blog_Note;

import web.com.bean.Blog_Pic;

import web.com.bean.BlogD;
import web.com.bean.BlogFinish;
import web.com.bean.BlogM;
import web.com.bean.Blog_Comment;
import web.com.bean.Blog_Day;
import web.com.bean.Blog_SpotInfo;
import web.com.bean.Blog_SpotInformation;
import web.com.bean.DateAndId;
import web.com.bean.Explore;
import web.com.bean.Trip_M;
import web.com.dao.BlogDao;
import web.com.impl.BlogImpl;
import web.com.util.ImageUtil;

@WebServlet("/BlogServlet")
public class BlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	BlogDao blogDao = null;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		if (blogDao == null) {
			blogDao = new BlogImpl();

		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {

//			List<Blog> blogs = blogDao.getAll;
//			writeText(response, gson.toJson(blogs));
//網誌新增註解
		} else if (action.equals("insertBlogNote")) {
			String b_noteJson = jsonObject.get("blog_note").getAsString();
			System.out.println("Blog_Note ::" + b_noteJson);
			Blog_Note blog_Note = gson.fromJson(b_noteJson, Blog_Note.class);
			int count = 0;
			count = blogDao.insertB_Note(blog_Note);
			writeText(response, String.valueOf(count));

		}
//網誌新增留言
	       else if(action.equals("insertBlogComment")) {
		     String b_commentJson= jsonObject.get("blog_comment").getAsString();
		     System.out.println("Blog_Comment ::"+ b_commentJson);
		     Blog_Comment blog_Comment = gson.fromJson(b_commentJson, Blog_Comment.class);
		     int count = 0 ;
		     count = blogDao.insertB_Comment(blog_Comment);
		     writeText(response, String.valueOf(count));
	       }

		 else if (action.equals("getSpotImage")) {
			String blog_Id = jsonObject.get("blog_Id").getAsString();
			String loc_Id = jsonObject.get("loc_Id").getAsString();
			List<byte[]> spotImages = new ArrayList<byte[]>();
			Blog_Pic blog_Pic = new Blog_Pic();
			Encoder encoder = Base64.getEncoder();
			spotImages = blogDao.getSpotImages(loc_Id, blog_Id);
			if (spotImages.size() != 0) {
				if (spotImages.get(0) != null) {
					String img1 = encoder.encodeToString(spotImages.get(0));
					blog_Pic.setPic1(img1);
				}
				if (spotImages.get(1) != null) {
					String img2 = encoder.encodeToString(spotImages.get(1));
					blog_Pic.setPic2(img2);
				}
				if (spotImages.get(2) != null) {
					String img3 = encoder.encodeToString(spotImages.get(2));
					blog_Pic.setPic3(img3);
				}
				if (spotImages.get(3) != null) {
					String img4 = encoder.encodeToString(spotImages.get(3));
					blog_Pic.setPic4(img4);
				}
			}
			System.out.println("spotImages::" + blog_Pic);
			writeText(response, gson.toJson(blog_Pic));
		}

//檢查是否有上傳圖片

		else if (action.equals("imageUpdate")) {
			String blogPic_json = jsonObject.get("blogPic").getAsString();
			System.out.println("BlogPic ::" + blogPic_json);
			Blog_Pic blog_Pic = gson.fromJson(blogPic_json, Blog_Pic.class);
			int count = 0;
			byte[] image1 = null, image2 = null, image3 = null, image4 = null;
			if (blog_Pic.getPic1() != null && !blog_Pic.getPic1().isEmpty()) {
				image1 = Base64.getMimeDecoder().decode(blog_Pic.getPic1());
			}
			if (blog_Pic.getPic2() != null && !blog_Pic.getPic2().isEmpty()) {
				image2 = Base64.getMimeDecoder().decode(blog_Pic.getPic2());
			}
			if (blog_Pic.getPic3() != null && !blog_Pic.getPic3().isEmpty()) {
				image3 = Base64.getMimeDecoder().decode(blog_Pic.getPic3());
			}
			if (blog_Pic.getPic4() != null && !blog_Pic.getPic4().isEmpty()) {
				image4 = Base64.getMimeDecoder().decode(blog_Pic.getPic4());
			}
			count = blogDao.insertImage(image1, image2, image3, image4, blog_Pic.getBlogId(), blog_Pic.getLocId());
			writeText(response, String.valueOf(count));
		}else if (action.equals("UpdateImage")) {
			String blogPic_json = jsonObject.get("blogPic").getAsString();
			System.out.println("BlogPic ::" + blogPic_json);
			Blog_Pic blog_Pic = gson.fromJson(blogPic_json, Blog_Pic.class);
			int count = 0;
			byte[] image1 = null, image2 = null, image3 = null, image4 = null;
			if (blog_Pic.getPic1() != null && !blog_Pic.getPic1().isEmpty()) {
				image1 = Base64.getMimeDecoder().decode(blog_Pic.getPic1());
			}
			if (blog_Pic.getPic2() != null && !blog_Pic.getPic2().isEmpty()) {
				image2 = Base64.getMimeDecoder().decode(blog_Pic.getPic2());
			}
			if (blog_Pic.getPic3() != null && !blog_Pic.getPic3().isEmpty()) {
				image3 = Base64.getMimeDecoder().decode(blog_Pic.getPic3());
			}
			if (blog_Pic.getPic4() != null && !blog_Pic.getPic4().isEmpty()) {
				image4 = Base64.getMimeDecoder().decode(blog_Pic.getPic4());
			}
			count = blogDao.updateImage(blog_Pic,image1,image2,image3,image4);
			writeText(response, String.valueOf(count));
		} else if (action.equals("updateBlog")|| action.equals("blogUpdate")) {
			String blogFinish = jsonObject.get("blogFinish").getAsString();
			System.out.println("BlogFinish ::" + blogFinish);
			BlogFinish blogFinsh = gson.fromJson(blogFinish, BlogFinish.class);
			
			byte[] image = null;
			// 檢查是否有上傳圖片
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if(action.equals("updateBlog")) {
				count = blogDao.insetBlog_M(blogFinsh, image);
			}else if (action.equals("blogUpdate")) {
				count = blogDao.update(blogFinsh, image);
			}
			
			writeText(response, String.valueOf(count));

		} else if (action.equals("getImage")) {

			OutputStream os = response.getOutputStream();
			String id = jsonObject.get("id").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = blogDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentLength(image.length);
				response.setContentType("image/jpeg");
				os.write(image);
			}
		}else if(action.equals("findById")) {
			String id = jsonObject.get("id").getAsString();
			List<BlogD> blist = blogDao.findById(id);
			writeText(response, gson.toJson(blist));
			//編輯網誌抓BlogM 資料
		}else if(action.equals("findBlogById")) {
			String id = jsonObject.get("id").getAsString();
			BlogFinish blogFinish = blogDao.findBlogById(id);
			writeText(response, gson.toJson(blogFinish));
		}else if(action.equals("blogDelete")) {
			 String blogId= jsonObject.get("blogId").getAsString();
			 int count = blogDao.delete(blogId);
			 writeText(response, String.valueOf(count));
		}
//抓取個人的Blog		
		else if (action.equals("getMyBlog")) {
			String memberId = jsonObject.get("memberId").getAsString();
			List<BlogFinish> blogMs = blogDao.getMyBlog(memberId);
			writeText(response, gson.toJson(blogMs));
		
		} else if (action.equals("findDateById")) {
			String id = jsonObject.get("id").getAsString();
			List<Blog_Day> blogDays = blogDao.findDateById(id);

			writeText(response, gson.toJson(blogDays));

		} else if (action.equals("getSpotName")) {
			String id = jsonObject.get("id").getAsString();
<<<<<<< HEAD



=======
>>>>>>> 0db230fc3faa002e11c6b4987109f3328ae16b77
			String date = jsonObject.get("dateD").getAsString();		
			List<Blog_SpotInformation> spotNames = blogDao.getSpotName(date,id);
//			System.out.println("spotNames:" + spotNames);
			writeText(response,gson.toJson(spotNames));
		}else if(action.equals("getCommentNote")) {
			String id = jsonObject.get("id").getAsString();
			List<Blog_Comment> comments = blogDao.findCommentById(id);
			writeText(response, gson.toJson(comments));
		}else if(action.equals("getUpdateNote")){
			String spotNote = jsonObject.get("spotNoteUpadte").getAsString();
			System.out.println("blogNote"+spotNote);
			Blog_Note blog_Note  = gson.fromJson(spotNote, Blog_Note.class);
			int count = 0;
			count=blogDao.updateB_Note(blog_Note);
			writeText(response, String.valueOf(count));
		}else {

				writeText(response, "");
			}
		


		
		

//		else {

//		}else if(action.equals("findById")) {
//			int id = jsonObject.get("id").getAsInt();
//			List<BlogD> blist = blogDao.findById(id);
//			writeText(response, gson.toJson(blist));
//		}else if(action.equals("findDateId")) {
//			int id = jsonObject.get("id").getAsInt();
////			List<BlogD> blist = blogDao.findById(id);
//			writeText(response, gson.toJson(blist));}

//		
//
//				writeText(response, "");
//			}
//		}
//	


}
	

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		System.out.println("output: " + outText);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (blogDao == null) {
			blogDao = new BlogImpl();
		}
//		List<BlogD> blogs = blogDao.getAll();
//		writeText(response, new Gson().toJson(blogs));
	}

}
