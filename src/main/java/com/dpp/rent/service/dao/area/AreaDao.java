package com.dpp.rent.service.dao.area;

import java.util.List;

import com.dpp.rent.app.api.domain.Area;

/**
 * className:AreaDao.java
 * description: 查询所有的区域
 * date: 2018年7月11日
 * author:jpg
 */
public interface AreaDao {
	
	/**
	 * description: 获取所有的区域
	 * retrun_type:List<Area>
	 * date: 2018年7月11日
	 * author:jpg
	 */
	List<Area> getAllArea();
}
