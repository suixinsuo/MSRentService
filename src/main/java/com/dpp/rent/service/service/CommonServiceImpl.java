package com.dpp.rent.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpp.rent.app.api.constant.MSConstant;
import com.dpp.rent.app.api.domain.Area;
import com.dpp.rent.app.api.domain.TradeArea;
import com.dpp.rent.app.api.model.request.SmsForm;
import com.dpp.rent.app.api.model.response.AreaDto;
import com.dpp.rent.app.api.model.response.AreaResponse;
import com.dpp.rent.app.api.model.response.TradeAreaDto;
import com.dpp.rent.app.api.model.response.TradeAreaResponse;
import com.dpp.rent.app.api.service.CommonService;
import com.dpp.rent.app.api.service.user.UserService;
import com.dpp.rent.service.dao.area.AreaDao;
import com.dpp.rent.service.dao.area.TradeAreaDao;

/**
 * className:CommonServiceImpl.java
 * description: 公共业务处理
 * date: 2018年6月24日
 * author:jpg
 */
@Service
public class CommonServiceImpl implements CommonService{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private TradeAreaDao tradeAreaDao;
	
	
	/**
	 * 发送短信
	 */
	public void sendSms(SmsForm smsForm) {
		userService.sendUserSmsCode(smsForm.getId(),smsForm.getSendType());
	}
	
	/**
	 * 获取区域
	 */
	public AreaResponse getAllArea() {
		AreaResponse response = new AreaResponse();
		List<AreaDto> areaDtoList = new ArrayList<AreaDto>();
		List<Area> list = areaDao.getAllArea();
		for (Area area: list) {
			AreaDto areaDto = new AreaDto();
			areaDto.setAreaId(area.getAreaId());
			areaDto.setAreaName(area.getAreaName());
			areaDtoList.add(areaDto);
		}
		response.setAreaResponse(areaDtoList);
		return  response;
	}
	
	/**
	 * 根据区域id获取商圈
	 */
	public TradeAreaResponse getTradeArea(String areaId) {
		TradeAreaResponse response = new TradeAreaResponse();
		List<TradeAreaDto> areaDtoList = new ArrayList<TradeAreaDto>();
		List<TradeArea> list  = tradeAreaDao.getTradeAreaByAreaId(areaId);
		for (TradeArea area: list) {
			TradeAreaDto tradeAreaDto = new TradeAreaDto();
			tradeAreaDto.setTradeId(area.getTradeId());
			tradeAreaDto.setAreaId(area.getAreaId());
			tradeAreaDto.setTradeName(area.getTradeName());
			areaDtoList.add(tradeAreaDto);
		}
		response.setTradeAreaResponse(areaDtoList);
		return  response;
	}
	
	public TradeAreaResponse getAllTradeArea() {
		TradeAreaResponse response = new TradeAreaResponse();
		List<TradeAreaDto> list  = tradeAreaDao.getAllTradeArea();
		response.setTradeAreaResponse(list);
		return  response;
	}
}
