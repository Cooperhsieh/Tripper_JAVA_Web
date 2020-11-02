package web.com.dao;

import java.util.List;

import web.com.bean.Goal;

/**
 * 類別說明：成就資料Dao檔
 * 
 * @author Weixiang
 * @version 建立時間:Oct 27, 2020
 * 
 */

public interface GoalDao {
	List<Goal> getGoalByMember(int memberId);
}
