package web.com.dao;

import java.util.List;

import web.com.bean.Trip_D;

/**
 * 類別說明：Trip_D_Dao檔
 * 
 * @author cooper
 * @version 建立時間:Sep 4, 2020
 * 
 */

public interface Trip_D_Dao {

	int insert(Trip_D tripD);

	int update(Trip_D tripD);

	int delete(String transId);

	Trip_D findTransId(String transId);

	List<Trip_D> getall();

}
