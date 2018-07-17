package com.dpp.rent.service.dao.user;

import com.dpp.rent.app.api.domain.UserBuyHouse;

public interface UserBuyHouseDao {
	
	/**
	 * description: 新增用户求购，出售意向记录
	 * retrun_type:void
	 * date: 2018年7月5日
	 * author:jpg
	 */
	void addUserBuyHouse(UserBuyHouse userBuyHouse);
	
	/**
	 * description: 获取house最大id
	 * retrun_type:int
	 * date: 2018年7月5日
	 * author:jpg
	 */
	String getMaxHouseId();
}
