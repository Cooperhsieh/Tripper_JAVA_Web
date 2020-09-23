package web.com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Base64;
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

import web.com.bean.Location_D;
import web.com.bean.Trip_D;
import web.com.bean.Trip_Group;
import web.com.bean.Trip_M;
import web.com.dao.Trip_D_Dao;
import web.com.dao.Trip_Group_Dao;
import web.com.dao.Trip_M_Dao;
import web.com.impl.Trip_D_Dao_Impl;
import web.com.impl.Trip_Group_Dao_Impl;
import web.com.impl.Trip_M_Dao_Impl;
import web.com.util.SettingUtil;

/**
* 
* @author Connor Fan
* @version 建立時間:Sep 20, 2020 12:57:32 PM
* 
*/
@WebServlet("/TripServlet")
public class TripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Trip_M_Dao tripMasterDao = null;
	private Trip_D_Dao tripDetailDao = null;
	//private Location_D locationDetailDao = null;
	private Trip_Group_Dao tripGroupDao = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(request.getReader());
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while( (line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("jsonIn input: " + jsonIn);
		// TODO 判斷dao是否存在
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		tripMasterDao = new Trip_M_Dao_Impl();
		tripGroupDao = new Trip_Group_Dao_Impl();
		
		//取得該會員所有行程資料,
		if (action.equals("getAll")) {
			String tripId = jsonObject.get("tripId").getAsString();
			List<Trip_M> tripMasters = tripMasterDao.getTripId(tripId);
			writeText(response, gson.toJson(tripMasters));
		} else if(action.equals("insert") || action.equals("update") ) {
			
			// 主檔 
			String tripMasterJson = jsonObject.get("tripM").getAsString();
			System.out.println("tripMasterJson:: " + tripMasterJson);
			
			// 附檔
			String locationDJson = jsonObject.get("locationD").getAsString();
			System.out.println("locationDJson:: " + locationDJson);
			Type type = new TypeToken<Map<String, List<Location_D>>>(){}.getType();
			
			Map<String, List<Location_D>> maps = new Gson().fromJson(jsonIn.toString(), type);
			Trip_M tripMaster = gson.fromJson(tripMasterJson, Trip_M.class);
			
			 int groupStat = tripMaster.getStatus();
			
			// 確認是否有圖片
			String imageBase64 = jsonObject.get("imageBase64").getAsString();
			byte[] backgroudPic = null;
			if(imageBase64 != null && !imageBase64.isEmpty()) {
				backgroudPic = Base64.getMimeDecoder().decode(imageBase64);
			}
			int count = 0;
			// 新增
			if(action.equals("insert")) {
				// 主檔資料
				count = tripMasterDao.insert(tripMaster, backgroudPic);
				// insert 失敗直接傳回null
				if(count <= 0) {
					writeText(response, String.valueOf(count));
				}
				// 判斷是否有開啟揪團，0 未開放, 1  開放, 2 已結束, 3 取消
				if(groupStat == 1) {
					Trip_Group tripGroup = new Trip_Group(SettingUtil.getTransId(), tripMaster.getTripId(), tripMaster.getMemberId());
					count = tripGroupDao.insert(tripGroup);
					if(count <= 0) {
						writeText(response, String.valueOf(count));
					}
				}
				
				// 附檔資料
				tripDetailDao = new Trip_D_Dao_Impl();
				List<Location_D> locationDs = null;
				for(int i = 1; i <= maps.size(); i++) {
					int seq = 1;
					locationDs = maps.get(i + "");
					for(Location_D locD: locationDs) { 
						Trip_D tripD =  new Trip_D(
								locD.getTransId(), tripMaster.getTripId(), seq, locD.getLocId(), locD.getStartDate(),
								locD.getStartTime(), locD.getStayTimes(), locD.getMemos());
						count = tripDetailDao.insert(tripD);
						seq++;
					}
					// insert 失敗直接傳回null
					if(count <= 0) {
						writeText(response, String.valueOf(count));
					}
				}
				
				
			// 修改
			}else if (action.equals("update")) {
				// 主檔修改
				count = tripMasterDao.update(tripMaster, backgroudPic);
				if(groupStat != 1) {
					count = tripGroupDao.delete(tripMaster.getTripId());
					if(count <= 0) {
						writeText(response, String.valueOf(count));
					}
				}
				
				// 副檔修改
				List<Location_D> locationDs = null;
				for(int i = 1; i <= maps.size(); i++) {
					int seq = 1;
					locationDs = maps.get(i + "");
					for(Location_D locD: locationDs) { 
						Trip_D tripD =  new Trip_D(
								locD.getTransId(), tripMaster.getTripId(), seq, locD.getLocId(), locD.getStartDate(),
								locD.getStartTime(), locD.getStayTimes(), locD.getMemos());
						count = tripDetailDao.update(tripD);
						seq++;
					}
					// insert 失敗直接傳回null
					if(count <= 0) {
						writeText(response, String.valueOf(count));
					}
				}
				
			}
			writeText(response, String.valueOf(count));
		} 
			
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void writeText(HttpServletResponse response, String json) throws IOException {
        response.setContentType(SettingUtil.CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.print(json);
        // TODO for debug 
        System.out.println("response json: " +  json);
        out.close();
    }
	

}
