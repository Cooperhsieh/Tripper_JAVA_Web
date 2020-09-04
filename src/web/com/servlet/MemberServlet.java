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

import web.com.bean.Member;
import web.com.dao.MemberDao;
import web.com.dao.MemberDaoImpl;


/**
* 類別說明：會員Servlet
* @author zhitin 
* @version 建立時間:Sep 3, 2020 
* 
*/
@WebServlet("/MemberServlet")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	MemberDao memberDao = null ;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		// 將輸入資料列印出來除錯用
		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (memberDao == null) {
			memberDao = new MemberDaoImpl();
		}

		String action = jsonObject.get("action").getAsString();

		if (action.equals("selectAll")) {
			List<Member> books = memberDao.selectAll();
			writeText(response, gson.toJson(books));
			
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = memberDao.getphoto(id);
//			if (image != null) {
//				image = Image.shrink(image, imageSize);
//				response.setContentType("image/jpeg");    //傳送圖片格式
//				response.setContentLength(image.length);  //圖片佔的大小
//				os.write(image);
//			}
		} else if (action.equals("memberInsert") || action.equals("bookUpdate")) {
			String bookJson = jsonObject.get("member").getAsString();
			System.out.println("spotJson = " + bookJson);     //先get外部的json，再get內部的json取得spot物件
			Member member = gson.fromJson(bookJson,Member.class);
			byte[] image = null;
			// 檢查是否有上傳圖片
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			
			if (action.equals("memberInsert")) {
				count = memberDao.insert(member);
			
//			} else if (action.equals("bookUpdate")) {
//				count = bookDao.update(book, image);
//			}
			writeText(response, String.valueOf(count));
			}
			
//		} else if (action.equals("bookDelete")) {
//			int bookId = jsonObject.get("bookId").getAsInt();
//			int count = bookDao.delete(bookId);
//			writeText(response, String.valueOf(count));
//			
//		} else if (action.equals("findById")) {
//			int id = jsonObject.get("id").getAsInt();
//			Book book = bookDao.findById(id);
//			writeText(response, gson.toJson(book));
//		} 
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
		// System.out.println("output: " + outText);
	}
	
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if (memberDao == null) {
			memberDao = new MemberDaoImpl();
		}
		List<Member> members = memberDao.selectAll();
		writeText(response, new Gson().toJson(members));
	}
}

	



