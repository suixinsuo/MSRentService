package com.dpp.rent.service.dao.agent;

import java.util.List;

import com.dpp.rent.app.api.domain.AgentAttachHouse;
import com.dpp.rent.app.api.domain.Cooperation;

/**
 * className:AgentAttachHouseDao.java
 * description: 查询房源详细信息
 * date: 2018年7月12日
 * author:jpg
 */
public interface AgentAttachHouseDao {
	
	/**
	 * description: 新增附属信息
	 * retrun_type:void
	 * date: 2018年7月13日
	 * author:jpg
	 */
	void addAgentAttachHouse(AgentAttachHouse agentAttachHouse);
	

}
