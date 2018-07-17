package com.dpp.rent.service.service.agent;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpp.rent.app.api.domain.AgentAttachHouse;
import com.dpp.rent.app.api.domain.AgentHouse;
import com.dpp.rent.app.api.model.request.agent.AddAgenthouseForm;
import com.dpp.rent.app.api.model.request.agent.AgentHouseVo;
import com.dpp.rent.app.api.model.request.agent.AgentHouseForm;
import com.dpp.rent.app.api.model.response.AgentHouseDto;
import com.dpp.rent.app.api.model.response.AgentHouseResponse;
import com.dpp.rent.app.api.service.agent.AgentHouseService;
import com.dpp.rent.app.api.util.DateUtil;
import com.dpp.rent.service.dao.agent.AgentAttachHouseDao;
import com.dpp.rent.service.dao.agent.AgentHouseDao;
import com.dpp.rent.service.dao.area.AreaDao;
import com.dpp.rent.service.dao.area.TradeAreaDao;


/**
 * className:AgentHouseServiceImpl.java
 * description: 经纪人房源处理
 * date: 2018年7月13日
 * author:jpg
 */
@Service
public class AgentHouseServiceImpl implements AgentHouseService{
	
	private static Log log = LogFactory.getLog(AgentHouseServiceImpl.class);
	
	@Autowired
	private AgentAttachHouseDao agentAttachHouseDao;
	
	@Autowired
	private AgentHouseDao agentHouseDao;
	
	@Autowired
	private TradeAreaDao tradeAreaDao;
	
	public void addAgentHouse(AddAgenthouseForm addAgenthouseForm) {
		AgentHouse agentHouse = new AgentHouse();
		String maxCode = agentHouseDao.maxAgentHouseId();
		if (StringUtils.isBlank(maxCode)) {
			// 为空则从000000001开始
			maxCode = "000000001";
		} else {
		
			// 则自动加1并且格式化成4位数，前面补0
			int res = Integer.valueOf(maxCode)+1;
			maxCode =  String.format("%09d", res);
		}
		agentHouse.setHouseId(maxCode);
		agentHouse.setId(addAgenthouseForm.getId());
		agentHouse.setType(addAgenthouseForm.getType()); // 出售类型
		agentHouse.setCreateDate(new Date());
		agentHouse.setStatus("0"); // 发布类型
		agentHouse.setChildId(addAgenthouseForm.getChildId());
		// 主表中插入记录
		agentHouseDao.addAgentHouse(agentHouse);
		
		// 插入房源详细信息
		AgentAttachHouse agentAttachHouse = new AgentAttachHouse();
		agentAttachHouse.setHouseId(maxCode);
		agentAttachHouse.setPrice(Double.valueOf(addAgenthouseForm.getPrice()));
		agentAttachHouse.setPerPrice(Double.valueOf(addAgenthouseForm.getPerPrice()));
		agentAttachHouse.setHuxing(addAgenthouseForm.getHuxing());
		agentAttachHouse.setTing(addAgenthouseForm.getTing());
		agentAttachHouse.setRemark(addAgenthouseForm.getRemark());
		agentAttachHouse.setHouseArea(addAgenthouseForm.getHouseArea());
		agentAttachHouse.setOrientation(addAgenthouseForm.getOrientation());
		agentAttachHouse.setHouseLabel(addAgenthouseForm.getHouseLabel());
		agentAttachHouse.setHouseDate(addAgenthouseForm.getHouseDate());
		agentAttachHouse.setFloorLevel(addAgenthouseForm.getFloorLevel());
		agentAttachHouse.setTradeId(addAgenthouseForm.getTradeId());
		agentAttachHouse.setCooId(addAgenthouseForm.getCooId());
		agentAttachHouse.setElevator(addAgenthouseForm.getElevator());
		agentAttachHouse.setPurpose(addAgenthouseForm.getPurpose());
		agentAttachHouse.setPower(addAgenthouseForm.getPower());
		agentAttachHouse.setPrePay(addAgenthouseForm.getPrePay());
		agentAttachHouse.setRenovation(addAgenthouseForm.getRenovation());
		agentAttachHouse.setHouseDes(addAgenthouseForm.getHouseDes());
		agentAttachHouse.setHouseTitle(addAgenthouseForm.getHouseTitle());
		agentAttachHouse.setHousePic(addAgenthouseForm.getHousePic());
		agentAttachHouse.setTradeName(addAgenthouseForm.getTradeName());
		agentAttachHouse.setCooName(addAgenthouseForm.getCooName());
		agentAttachHouse.setCooAddress(addAgenthouseForm.getCooAddress());
		agentAttachHouseDao.addAgentAttachHouse(agentAttachHouse);
		
		log.info("经纪人新增房源成功，id:" + addAgenthouseForm.getId());
		
	}
	
	/**
	 * 查询订单
	 */
	public AgentHouseResponse getAgentHouse(AgentHouseForm agentHouseForm) {
		AgentHouseResponse response = new AgentHouseResponse();
		AgentHouseVo  agentHouseVo = new AgentHouseVo();
		agentHouseVo.setTradeId(agentHouseForm.getTradeId());
		agentHouseVo.setHuxing(agentHouseForm.getHuxing());
		agentHouseVo.setBeginPrice(agentHouseForm.getBeginPrice());
		agentHouseVo.setEndPrice(agentHouseForm.getEndPrice());
		agentHouseVo.setBeginHouseArea(agentHouseForm.getBeginHouseArea());
		agentHouseVo.setEndHouseArea(agentHouseForm.getEndHouseArea());
		agentHouseVo.setOrientation(agentHouseForm.getOrientation());
		agentHouseVo.setBeginFloorLevel(agentHouseForm.getBeginFloorLevel());
		agentHouseVo.setEndFloorLevel(agentHouseForm.getEndFloorLevel());
		agentHouseVo.setId(agentHouseForm.getMobile());
		agentHouseVo.setChildId(agentHouseForm.getChildId());
		agentHouseVo.setStatus(agentHouseForm.getStatus());
		agentHouseVo.setType(agentHouseForm.getType()); //  0.出售，1.出租
		agentHouseVo.setCooName(agentHouseForm.getCooName());
		String houseDateType = agentHouseForm.getHouseDateType();
		int beginHouseDate = 0;
		int endHouseDate = 0;
		if (StringUtils.isNotBlank(houseDateType)) {
			// 楼龄类型不为空则要做特殊处理，0. 5年以内，1.10年以内，2.15年以内  3. 20年以内  4.20年以上
			if ("0".equals(houseDateType)) {
				beginHouseDate = DateUtil.getCurrentYear()-5;
			} else if ("1".equals(houseDateType)) {
				beginHouseDate = DateUtil.getCurrentYear()-10;
			} else if ("2".equals(houseDateType)) { 
				beginHouseDate = DateUtil.getCurrentYear()-15;
			}else if ("3".equals(houseDateType)) {
				beginHouseDate = DateUtil.getCurrentYear()-20;
			}else {
				endHouseDate = DateUtil.getCurrentYear()-20;
			}
			agentHouseVo.setBeginHouseDate(beginHouseDate>0?String.valueOf(beginHouseDate):"");
			agentHouseVo.setBeginHouseDate(endHouseDate>0?String.valueOf(endHouseDate):"");
		}
		List<AgentHouseDto> list = agentHouseDao.getAgentHouse(agentHouseVo);
		response.setList(list);
		return response;
	}
}
