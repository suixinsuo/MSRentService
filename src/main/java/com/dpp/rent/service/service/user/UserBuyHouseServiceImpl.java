package com.dpp.rent.service.service.user;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpp.rent.app.api.domain.UserBuyHouse;
import com.dpp.rent.app.api.model.request.user.UserBuyHouseForm;
import com.dpp.rent.app.api.model.request.user.UserLeaseHouseForm;
import com.dpp.rent.app.api.model.request.user.UserRentHouseForm;
import com.dpp.rent.app.api.model.request.user.UserSellHouseForm;
import com.dpp.rent.app.api.service.user.UserBuyHouseService;
import com.dpp.rent.service.dao.user.UserBuyHouseDao;

/**
 * className:UserBuyHouseServiceImpl.java
 * description: 用户出售求购意向业务处理
 * date: 2018年7月5日
 * author:jpg
 */
@Service
public class UserBuyHouseServiceImpl implements UserBuyHouseService{
	
	private static Log log = LogFactory.getLog(UserBuyHouseServiceImpl.class);

	@Autowired
	private UserBuyHouseDao userBuyHouseDao;
	
	public void addUserSellHouse(UserSellHouseForm userSellHouseForm) {
		log.info("user add house that is sold,paramter:");
		String maxHouseId = userBuyHouseDao.getMaxHouseId();
		int houseId = null==maxHouseId?0:Integer.valueOf(maxHouseId);
		UserBuyHouse userBuyHouse = new UserBuyHouse();
		userBuyHouse.setHouseId(houseId);
		// 赋值id
		userBuyHouse.setId(userSellHouseForm.getId());
		// 价格
		userBuyHouse.setExpectPrice(Double.valueOf(userSellHouseForm.getExpectPrice()));
		userBuyHouse.setHuxing(userSellHouseForm.getHuxing());
		userBuyHouse.setHousePhone(userSellHouseForm.getHousePhone());
		userBuyHouse.setRemark(userSellHouseForm.getRemark());
		userBuyHouse.setHouseArea(Double.valueOf(userSellHouseForm.getHouseArea()));
		userBuyHouse.setCommunityName(userSellHouseForm.getCommunityName());
		userBuyHouse.setAddress(userSellHouseForm.getAddress());
		userBuyHouse.setPedestal(userSellHouseForm.getPedestal());
		userBuyHouse.setUnit(userSellHouseForm.getUnit());
		userBuyHouse.setRoomNum(userSellHouseForm.getRoomNum());
		
		userBuyHouse.setType("0"); // 0.出售
		userBuyHouse.setStatus("0"); // 待抢单
		userBuyHouse.setCreateDate(new Date()); // 创建时间为当前时间
		userBuyHouseDao.addUserBuyHouse(userBuyHouse);
		log.info("id:" + userSellHouseForm.getId() + ",add sell house success");
	}

	public void addUserBuyHouse(UserBuyHouseForm userBuyHouseForm) {
		String maxHouseId = userBuyHouseDao.getMaxHouseId();
		int houseId = null==maxHouseId?0:Integer.valueOf(maxHouseId);
		UserBuyHouse userBuyHouse = new UserBuyHouse();
		userBuyHouse.setHouseId(houseId);
		// 赋值id
		userBuyHouse.setId(userBuyHouseForm.getId());
		// 价格
		userBuyHouse.setExpectPrice(Double.valueOf(userBuyHouseForm.getExpectPrice()));
		userBuyHouse.setHuxing(userBuyHouseForm.getHuxing());
		userBuyHouse.setHousePhone(userBuyHouseForm.getHousePhone());
		userBuyHouse.setRemark(userBuyHouseForm.getRemark());
		userBuyHouse.setName(userBuyHouseForm.getName());
		userBuyHouse.setAreaType(userBuyHouseForm.getAreaType());
		userBuyHouse.setArea1(userBuyHouseForm.getArea1());
		userBuyHouse.setArea2(userBuyHouseForm.getArea2());
		
		userBuyHouse.setType("1"); // 0.出售类型
		userBuyHouse.setStatus("0"); // 待抢单
		userBuyHouse.setCreateDate(new Date()); // 创建时间为当前时间
		userBuyHouseDao.addUserBuyHouse(userBuyHouse);
		log.info("id:" + userBuyHouseForm.getId() + ",add buy house success");
	}

	public void addUserLeaseHouse(UserLeaseHouseForm userLeaseHouseForm) {
		log.info("user add house that is lease,paramter:");
		String maxHouseId = userBuyHouseDao.getMaxHouseId();
		int houseId = null==maxHouseId?0:Integer.valueOf(maxHouseId);
		
		UserBuyHouse userBuyHouse = new UserBuyHouse();
		userBuyHouse.setHouseId(houseId);
		// 赋值id
		userBuyHouse.setId(userLeaseHouseForm.getId());
		// 价格
		userBuyHouse.setExpectPrice(Double.valueOf(userLeaseHouseForm.getExpectPrice()));
		userBuyHouse.setHuxing(userLeaseHouseForm.getHuxing());
		userBuyHouse.setHousePhone(userLeaseHouseForm.getHousePhone());
		userBuyHouse.setRemark(userLeaseHouseForm.getRemark());
		userBuyHouse.setHouseArea(Double.valueOf(userLeaseHouseForm.getHouseArea()));
		userBuyHouse.setCommunityName(userLeaseHouseForm.getCommunityName());
		userBuyHouse.setAddress(userLeaseHouseForm.getAddress());
		userBuyHouse.setPedestal(userLeaseHouseForm.getPedestal());
		userBuyHouse.setUnit(userLeaseHouseForm.getUnit());
		userBuyHouse.setRoomNum(userLeaseHouseForm.getRoomNum());
		
		userBuyHouse.setType("2"); // 0.出售
		userBuyHouse.setStatus("0"); // 待抢单
		userBuyHouse.setCreateDate(new Date()); // 创建时间为当前时间
		userBuyHouseDao.addUserBuyHouse(userBuyHouse);
		log.info("id:" + userLeaseHouseForm.getId() + ",add lease house success");
	
		
	}

	public void addUserRentHouse(UserRentHouseForm userRentHouseForm) {

		String maxHouseId = userBuyHouseDao.getMaxHouseId();
		int houseId = null==maxHouseId?0:Integer.valueOf(maxHouseId);
		
		UserBuyHouse userBuyHouse = new UserBuyHouse();
		userBuyHouse.setHouseId(houseId);
		// 赋值id
		userBuyHouse.setId(userRentHouseForm.getId());
		// 价格
		userBuyHouse.setExpectPrice(Double.valueOf(userRentHouseForm.getExpectPrice()));
		userBuyHouse.setHuxing(userRentHouseForm.getHuxing());
		userBuyHouse.setHousePhone(userRentHouseForm.getHousePhone());
		userBuyHouse.setRemark(userRentHouseForm.getRemark());
		userBuyHouse.setName(userRentHouseForm.getName());
		userBuyHouse.setAreaType(userRentHouseForm.getAreaType());
		userBuyHouse.setArea1(userRentHouseForm.getArea1());
		userBuyHouse.setArea2(userRentHouseForm.getArea2());
		
		userBuyHouse.setType("3"); // 0.出售类型
		userBuyHouse.setStatus("0"); // 待抢单
		userBuyHouse.setCreateDate(new Date()); // 创建时间为当前时间
		userBuyHouseDao.addUserBuyHouse(userBuyHouse);
		log.info("id:" + userRentHouseForm.getId() + ",add rent house success");
	}

}
