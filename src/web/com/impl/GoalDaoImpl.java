package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.Goal;
import web.com.dao.GoalDao;
import web.com.util.ServiceLocator;

/**
 * 類別說明：成就資料DaoImpl檔
 * 
 * @author Weixiang
 * @version 建立時間:Oct 27, 2020
 * 
 */

public class GoalDaoImpl implements GoalDao {
	DataSource dataSource;
	
	public GoalDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public List<Goal> getGoalByMember(int id) {
		String sql = " select A.* "
				+ ",IFNULL((select Goal_ID from Goal "
				+ " where A.TripCount >= GOAL_COND1 " 
				+ "	and A.BlogCount >= GOAL_COND2 " 
				+ " and A.GroupCount >= GOAL_COND3 "
				+ " order by Goal_ID desc limit 1),0) as 'Goal_ID' "
				+ " from "
				+ " (select  M.* "
				+ " ,(select COUNT(*) from Trip_M where MEMBER_ID = M.MEMBER_ID) as 'TripCount' "
				+ " ,(select COUNT(*) from Blog_M where USER_ID  = M.MEMBER_ID) as 'BlogCount' "
				+ " ,(select COUNT(*) from Trip_Group where MEMBER_ID = M.MEMBER_ID) as 'GroupCount' "
				+ " from `Member` as M "
				+ " where MEMBER_ID = ? ) as A; ";
		List<Goal> goals = new ArrayList<Goal>();
		Goal goal = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int goalId = rs.getInt("GOAL_ID");
				int goalCond1 = rs.getInt("TripCount");
				int goalCond2 = rs.getInt("BlogCount");
				int goalCond3 = rs.getInt("GroupCount");
				goal = new Goal(goalId, goalCond1, goalCond2, goalCond3);
				goals.add(goal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goals;
	}
	
}
