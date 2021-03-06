package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import web.com.bean.Member;
import web.com.dao.MemberDao;
import web.com.impl.MemberDaoImpl;
import web.com.util.ImageUtil;

/**
 * 類別說明：會員Servlet
 * 
 * @author zhitin
 * @version 建立時間:Sep 3, 2020
 * 
 */
@WebServlet("/MemberServlet")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	MemberDao memberDao = null;

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
		// 將輸入資料列印出來除錯用
		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (memberDao == null) {
			memberDao = new MemberDaoImpl();
		}
//----------------------------------------------------------------------------------
		String action = jsonObject.get("action").getAsString();
		System.out.println("action::" + action);
//取得大頭貼
		 if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] photo = memberDao.getP_picById(id);
			if (photo != null) {
				photo = ImageUtil.shrink(photo, imageSize);
				response.setContentType("image/jpeg"); // 傳送圖片格式
				response.setContentLength(photo.length); // 圖片佔的大小
				os.write(photo);
			}
		}
//登入帳號
		else if(action.equals("logIn")) {
			String memberJson = jsonObject.get("member").getAsString();
			System.out.println("memberJson = " + memberJson);
			Member member = gson.fromJson(memberJson, Member.class);
			int count = 0 ;
			if(action.equals("logIn")) {
				count = memberDao.selectAandP(member);
				System.out.println(count);
				writeText(response, String.valueOf(count));
			}
			else {
				writeText(response, "");
			}
		}
//取得此帳號資訊		
		else if(action.equals("getProfile")) {
			String account = jsonObject.get("account").getAsString();		
			Member member = memberDao.findByAccount(account);
			writeText(response, gson.toJson(member));
		}
		 
		else if (action.equals("getProfileById")){
			String memberId = jsonObject.get("memberId").getAsString();
			Member member = memberDao.findById(Integer.parseInt(memberId));
			writeText(response, gson.toJson(member));
		}
		
//新增帳號or修改帳號
		else if (action.equals("memberInsert") || action.equals("memberUpdate")||action.equals("memberGBInsert")) {
			String memberJson = jsonObject.get("member").getAsString();
			System.out.println("memberJson = " + memberJson); // 先get外部的json，再get內部的json取得spot物件
			Member member = gson.fromJson(memberJson, Member.class);
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

			} else if (action.equals("memberUpdate")) {
				count = memberDao.update(member, image);

			}else if (action.equals("memberGBInsert")) {
				count = memberDao.insertGB(member);
			}
			writeText(response, String.valueOf(count));
			
		}
//管理者新增帳號or修改帳號
		else if (action.equals("managerInsert") || action.equals("managerUpdate")) {
			String memberJson = jsonObject.get("member").getAsString();
			System.out.println("memberJson = " + memberJson); // 先get外部的json，再get內部的json取得spot物件
			Member member = gson.fromJson(memberJson, Member.class);
			byte[] image = null;
			
			// 檢查是否有上傳圖片
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;

			if (action.equals("managerInsert")) {
				count = memberDao.insertManager(member);

			} else if (action.equals("managerUpdate")) {
				count = memberDao.update(member, image);
			}
			writeText(response, String.valueOf(count));
		}
//管理者登入		 
		else if(action.equals("managerlogIn")) {
			String memberJson = jsonObject.get("member").getAsString();
			System.out.println("memberJson = " + memberJson);
			Member member = gson.fromJson(memberJson, Member.class);
			int count = 0 ;
			if(action.equals("managerlogIn")) {
				count = memberDao.selectManagerAandP(member);
				System.out.println(count);
				writeText(response, String.valueOf(count));
			}
			else {
				writeText(response, "");
			}
		}
//更改密碼		
		else if(action.equals("UpdatePassword")){
			String account = jsonObject.get("account").getAsString();
			String newPassword = jsonObject.get("newPassword").getAsString();
			int count = 0 ;
			count = memberDao.updatePassword(account, newPassword);
			writeText(response, String.valueOf(count));
		}
			
//		} else if (action.equals("findById")) {
//			int id = jsonObject.get("id").getAsInt();
//			Book book = bookDao.findById(id);
//			writeText(response, gson.toJson(book));
//		} 
		else {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (memberDao == null) {
			memberDao = new MemberDaoImpl();
		}
//		List<Member> members = memberDao.selectAll();
//		writeText(response, new Gson().toJson(members));
	}
}
