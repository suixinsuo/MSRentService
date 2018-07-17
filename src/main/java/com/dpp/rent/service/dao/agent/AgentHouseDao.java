package com.dpp.rent.service.dao.agent;

import java.util.List;

import com.dpp.rent.app.api.domain.AgentHouse;
import com.dpp.rent.app.api.model.request.agent.AgentHouseForm;
import com.dpp.rent.app.api.model.request.agent.AgentHouseVo;
import com.dpp.rent.app.api.model.response.AgentHouseDto;


/**
 * className:AgentHouseDao.java
 * description: 查询房源
 * date: 2018年7月11日
 * author:jpg
 */
public interface AgentHouseDao {
	
	/**
	 * description: 查询符合条件的小区
	 * retrun_type:List<AgentHouse>
	 * date: 2018年7月13日
	 * author:jpg
	 */
	List<AgentHouseDto> getAgentHouse(AgentHouseVo agentHouseVo);
	
	
	void addAgentHouse(AgentHouse agentHouse);
	
	/**
	 * description: 获取房源最大id
	 * retrun_type:String
	 * date: 2018年7月13日
	 * author:jpg
	 */
	String maxAgentHouseId();
}
