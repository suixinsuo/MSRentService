package com.dpp.rent.service.service.agent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpp.rent.app.api.constant.BusinessErrorCode;
import com.dpp.rent.app.api.domain.Cooperation;
import com.dpp.rent.app.api.domain.User;
import com.dpp.rent.app.api.exception.BizException;
import com.dpp.rent.app.api.model.request.agent.AddAgentForm;
import com.dpp.rent.app.api.model.request.agent.SearchAgentForm;
import com.dpp.rent.app.api.model.response.AgentResponse;
import com.dpp.rent.app.api.model.response.UserDto;
import com.dpp.rent.app.api.service.agent.AgentService;
import com.dpp.rent.service.dao.user.UserDao;

@Service
public class AgentServiceImpl implements AgentService {
	
	private static Log log = LogFactory.getLog(AgentServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	public void addAgent(AddAgentForm addAgentForm) {

		String id = addAgentForm.getMobile();
		// 判断是否注册过
		User ur = userDao.getUser(id);
		if (ur!=null) {
			log.info("用户已注册");
			throw new BizException(BusinessErrorCode.USER_HAS_REGISTED.getId());
		}
		// 生成经纪人4位推荐生成码
		String maxCode = userDao.getMaxLinkCode();
		if (StringUtils.isBlank(maxCode)) {
			// 为空则从0001开始
			maxCode = "0001";
		} else {
			if (Integer.valueOf(maxCode)>=9999) {
				log.error("新增已达上限");
				throw new BizException(BusinessErrorCode.SYSTEM_ERROR.getId());
			}
			// 则自动加1并且格式化成4位数，前面补0
			int res = Integer.valueOf(maxCode)+1;
			maxCode =  String.format("%04d", res);
		}
		User useReq = new User();
		// 经纪人则要生成注册链接以及绑定小区
		useReq.setTradeId(addAgentForm.getTradeId());
		useReq.setCooId(addAgentForm.getCooId());
		useReq.setLinkCode(maxCode);
		useReq.setId(id);
		useReq.setName(addAgentForm.getName());
		useReq.setPassword(addAgentForm.getPassword());
		useReq.setType(addAgentForm.getType());
		useReq.setStatus("1");
		useReq.setCreateDate(new Date());
		useReq.setRemark(addAgentForm.getRemark());
		userDao.saveUser(useReq);
	}

	public AgentResponse getAgent(SearchAgentForm searchAgentForm) {
		AgentResponse response = new AgentResponse();
		User userReq = new User();
		userReq.setId(searchAgentForm.getMobile());
		userReq.setName(searchAgentForm.getName());
		userReq.setType(searchAgentForm.getType());
		List<User> list = userDao.getUserByParam(userReq);
		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : list) {
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setName(user.getName());
			userDto.setType(user.getType());
			userDto.setStatus(user.getStatus());
			userDto.setRemark(user.getRemark());
			userDto.setCreateDate(user.getCreateDate());
			userDtoList.add(userDto);
		}
		response.setList(userDtoList);
		return response;
		
	}

}
