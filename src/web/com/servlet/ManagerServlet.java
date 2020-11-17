package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import com.google.protobuf.StringValue;
import com.google.protobuf.StringValueOrBuilder;

import web.com.bean.Member;
import web.com.dao.ManagerDao;
import web.com.impl.ManagerDao_Impl;
import web.com.util.ImageUtil;

/**
 * 類別說明：管理者Servlet
 * 
 * @author zhitin
 * @version 建立時間:Nov 13, 2020
 * 
 */
@WebServlet("/ManagerServlet")
public class ManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ManagerDao managerDao = null;
	List<Member> memberList = null ;

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
		if (managerDao == null) {
			managerDao = new ManagerDao_Impl();
		}
//----------------------------------------------------------------------------------
		String action = jsonObject.get("action").getAsString();
		System.out.println("action::" + action);

//管理者登入		 
		 if(action.equals("getManagerList")) {
			memberList = new ArrayList<Member>();
			memberList = managerDao.getAll();
			writeText(response, gson.toJson(memberList));
			}
		 else if(action.equals("deleteManager")) {
				String memberIdStr = jsonObject.get("managerId").getAsString();
				int memberId = Integer.parseInt(memberIdStr);
				int count  = managerDao.deleteManager(memberId);
				writeText(response, String.valueOf(count));
			}
			

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

		if (managerDao == null) {
			managerDao= new ManagerDao_Impl();
		}
//		List<Member> members = memberDao.selectAll();
//		writeText(response, new Gson().toJson(members));
	}
}
