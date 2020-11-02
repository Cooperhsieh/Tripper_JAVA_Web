package web.com.bean;

import java.io.Serializable;

/**
 * 類別說明：成就Bean檔
 * 
 * @author Weixiang
 * @version 建立時間:Oct 27, 2020
 * 
 */

public class Goal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int goalId; // 成就ID
	private String goalName; // 成就名稱
	private int goalCond1; // 成就條件1(建立的行程數)
	private int goalCond2; // 成就條件2(建立的網誌數)
	private int goalCond3; // 成就條件3(參與的揪團數)
	
	// 成就主頁使用
	public Goal(int goalId, int goalCond1, int goalCond2, int goalCond3) {
		super();
		this.goalId = goalId;
		this.goalCond1 = goalCond1;
		this.goalCond2 = goalCond2;
		this.goalCond3 = goalCond3;
	}
	
	// 成就建構子
	public Goal(int goalId, String goalName, int goalCond1, int goalCond2, int goalCond3) {
		super();
		this.goalId = goalId;
		this.goalName = goalName;
		this.goalCond1 = goalCond1;
		this.goalCond2 = goalCond2;
		this.goalCond3 = goalCond3;
	}

	public int getGoalId() {
		return goalId;
	}

	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public int getGoalCond1() {
		return goalCond1;
	}

	public void setGoalCond1(int goalCond1) {
		this.goalCond1 = goalCond1;
	}

	public int getGoalCond2() {
		return goalCond2;
	}

	public void setGoalCond2(int goalCond2) {
		this.goalCond2 = goalCond2;
	}

	public int getGoalCond3() {
		return goalCond3;
	}

	public void setGoalCond3(int goalCond3) {
		this.goalCond3 = goalCond3;
	}
	
}
