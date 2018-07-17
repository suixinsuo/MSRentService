package com.dpp.rent.service.dao.area;

import java.util.List;
import java.util.Map;

import com.dpp.rent.app.api.domain.Cooperation;

/**
 * className:CooperationDao.java
 * description: 查询小区dao
 * date: 2018年7月12日
 * author:jpg
 */
public interface CooperationDao {
	
	/**
	 * description: 通过商圈id获取所有小区
	 * retrun_type:List<Cooperation>
	 * date: 2018年7月12日
	 * author:jpg
	 */
	List<Cooperation> getAllCooperation(Map<String,String> map);
	
	/**
	 * description: 保存小区
	 * retrun_type:void
	 * date: 2018年7月12日
	 * author:jpg
	 */
	void saveCooperation(Cooperation cooperation);
	
	
	/**
	 * description: 获取最大id
	 * retrun_type:String
	 * date: 2018年7月12日
	 * author:jpg
	 */
	String getMaxCooId();
}
