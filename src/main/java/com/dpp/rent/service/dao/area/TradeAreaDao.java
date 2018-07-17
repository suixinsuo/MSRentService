package com.dpp.rent.service.dao.area;

import java.util.List;

import com.dpp.rent.app.api.domain.TradeArea;
import com.dpp.rent.app.api.model.response.TradeAreaDto;

/**
 * className:TradeAreaDao.java
 * description: 商圈dao
 * date: 2018年7月11日
 * author:jpg
 */
public interface TradeAreaDao {
	/**
	 * description: 根据区域id获取所有的商圈
	 * retrun_type:List<TradeArea>
	 * date: 2018年7月11日
	 * author:jpg
	 */
	List<TradeArea> getTradeAreaByAreaId(String areaId);
	
	/**
	 * description: 获取所有的商圈
	 * retrun_type:List<TradeArea>
	 * date: 2018年7月13日
	 * author:jpg
	 */
	List<TradeAreaDto> getAllTradeArea();
	
	/**
	 * description: 通过自身id查对象
	 * retrun_type:TradeArea
	 * date: 2018年7月14日
	 * author:jpg
	 */
	TradeArea getTradeAreaById(String tradeId);
}
