package com.dpp.rent.service.service.user;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.dpp.rent.app.api.constant.BusinessErrorCode;
import com.dpp.rent.app.api.constant.CacheConstant;
import com.dpp.rent.app.api.constant.MSConstant;
import com.dpp.rent.app.api.domain.User;
import com.dpp.rent.app.api.exception.BizException;
import com.dpp.rent.app.api.model.BaseForm;
import com.dpp.rent.app.api.model.request.LoginForm;
import com.dpp.rent.app.api.model.request.QuickLoginForm;
import com.dpp.rent.app.api.model.request.RegisterForm;
import com.dpp.rent.app.api.model.response.LoginResponse;
import com.dpp.rent.app.api.service.user.UserService;
import com.dpp.rent.app.api.util.CacheService;
import com.dpp.rent.app.api.util.UUIDTool;
import com.dpp.rent.service.dao.user.UserDao;
import com.dpp.rent.service.util.SendSmsUtil;

@Service
public class UserServiceImpl implements UserService{
	private static Log log = LogFactory.getLog(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CacheService cacheService;
	
	public LoginResponse userLogin(LoginForm loginForm) {
		// 定义返回对象
		LoginResponse loginResponse = new LoginResponse();
		String id = loginForm.getId();
		// 获取user对象
		User user = userDao.getUser(id);
		if (user == null) {
			log.error(BusinessErrorCode.USER_IS_NOT_REGISTER.getDescription());
			throw new BizException(BusinessErrorCode.USER_IS_NOT_REGISTER.getId());
		}
		// 非正常状态用户不允许登录
		if (!MSConstant.USER_NORMAL_STATUS.equals(user.getStatus())) {
			log.error(BusinessErrorCode.USER_IS_NOT_NORMAL.getDescription());
			throw new BizException(BusinessErrorCode.USER_IS_NOT_NORMAL.getId());
		}
		
		// 判断用户密码是否正确
		if (!user.getPassword().equals(loginForm.getPassword())) {
			log.error("用户：" + id + "登录密码错误");
			throw new BizException(BusinessErrorCode.USER_PASSWORD_ERROR.getId());
		}
		
		// 登录正确则返回token,redis中保存60s
		String loginCacheKeyString = CacheConstant.getLoginKey(id);
		String token = UUIDTool.getUUID();
		try {
			cacheService.set(loginCacheKeyString, 60*60*24*10, token);
		} catch (Exception e) {
			log.error("保存缓存错误");
			throw new BizException(BusinessErrorCode.SYSTEM_CACHE_ERROR.getId());
		}
		
		// 返回id和token
		loginResponse.setId(id);
		loginResponse.setToken(token);
		loginResponse.setType(user.getType());
		loginResponse.setLinkCode(user.getLinkCode());
		loginResponse.setName(user.getName());
		return loginResponse;
	}

	public void sendRegisterSmsCode(String id) {
		id = id.trim();
		User user = userDao.getUser(id);
		if (null != user) {
			log.error("手机号：" + id + "已经注册过了");
			throw new BizException(BusinessErrorCode.USER_HAS_REGISTED.getId());
		}
		// 获取注册手机验证码key
		String registerMobileKey = CacheConstant.getSMSKey(id,MSConstant.SEND_MOBILE_REGISTER);
		// 生成6位短信随机验证码
		String verifyCode = SendSmsUtil.getVerifyCode();
		// 调用发送短信组件
		SendSmsUtil.sendSms(id, verifyCode);
		// 发送成功则保存手机验证码到redis中
		try {
			cacheService.set(registerMobileKey, 60*60*24, verifyCode);
		} catch (Exception e) {
			log.error("保存缓存错误");
			throw new BizException(BusinessErrorCode.SYSTEM_CACHE_ERROR.getId());
		}
	}

	public void register(RegisterForm registerForm) {
		String id = registerForm.getId().trim();
		User userRes = userDao.getUser(id);
		if (null != userRes) {
			log.error("手机号：" + id + "已经注册过了");
			throw new BizException(BusinessErrorCode.USER_HAS_REGISTED.getId());
		}
		
		// 校验手机验证码
		String registerMobileKey = CacheConstant.getSMSKey(id,MSConstant.SEND_MOBILE_REGISTER);
		String verifyCode = (String) cacheService.get(registerMobileKey);
		
		if (!registerForm.getVerifyCode().equals(verifyCode)) {
			log.error("手机号：" + id + "注册时填写验证码错误");
			throw new BizException(BusinessErrorCode.MOBILE_VERIFYCODE_ERROR.getId());
		}
		// 校验成功插入用户记录
		User user = new User();
		// 注册码若不为空，查找该注册码所在经纪人
		String linkCode = registerForm.getLinkCode();
		User userReq = new User();
		userReq.setLinkCode(linkCode);
		List<User> userRep = userDao.getUserByParam(userReq);
		if (null!=userRep && userRep.size()>0) {
			// 查得到经纪人，则赋值经纪人
			user.setParentId(userRep.get(0).getId());
		}
		user.setId(id);
		user.setPassword(registerForm.getPassword());
		user.setCreateDate(new Date());
		user.setStatus("1"); // 1.正常状态
		user.setType("0");  // 0.用户
		userDao.saveUser(user);
	}

	public LoginResponse quickLogin(QuickLoginForm quickLoginForm) {

		// 定义返回对象
		LoginResponse loginResponse = new LoginResponse();
		String id = quickLoginForm.getId();
		// 获取user对象
		User user = userDao.getUser(id);
		if (user == null) {
			log.error(BusinessErrorCode.USER_IS_NOT_REGISTER.getDescription());
			throw new BizException(BusinessErrorCode.USER_IS_NOT_REGISTER.getId());
		}
		// 非正常状态用户不允许登录
		if (!MSConstant.USER_NORMAL_STATUS.equals(user.getStatus())) {
			log.error(BusinessErrorCode.USER_IS_NOT_NORMAL.getDescription());
			throw new BizException(BusinessErrorCode.USER_IS_NOT_NORMAL.getId());
		}
		
		// 校验短信验证码
		String quickLoginMobileKey = CacheConstant.getSMSKey(id,MSConstant.SEND_MOBILE_QUICKLOGIN);
		String verifyCode = (String) cacheService.get(quickLoginMobileKey);
		
		if (!quickLoginForm.getVerifyCode().equals(verifyCode)) {
			log.error("手机号：" + id + "快速登录时手机验证码错误");
			throw new BizException(BusinessErrorCode.MOBILE_VERIFYCODE_ERROR.getId());
		}
		// 生成token
		String token = UUIDTool.getUUID();
		// 生成快速登录缓存key
		String quickLoginKey = CacheConstant.getLoginKey(id);
		try {
			cacheService.set(quickLoginKey, 60*60*24*10, token);
		} catch (Exception e) {
			log.error("保存缓存错误");
			throw new BizException(BusinessErrorCode.SYSTEM_CACHE_ERROR.getId());
		}
		
		// 返回id和token
		loginResponse.setId(id);
		loginResponse.setToken(token);
		loginResponse.setType(user.getType());
		loginResponse.setLinkCode(user.getLinkCode());
		loginResponse.setName(user.getName());
		return loginResponse;

	}

	public void sendQuickLoginSmsCode(String id) {
		id = id.trim();
		User user = userDao.getUser(id);
		if (null != user) {
			log.error("手机号：" + id + "已经注册过了");
			throw new BizException(BusinessErrorCode.USER_HAS_REGISTED.getId());
		}
		// 获取注册手机验证码key
		String registerMobileKey = CacheConstant.getSMSKey(id,MSConstant.SEND_MOBILE_QUICKLOGIN);
		// 生成6位短信随机验证码
		String verifyCode = SendSmsUtil.getVerifyCode();
		// 调用发送短信组件
		SendSmsUtil.sendSms(id, verifyCode);
		// 发送成功则保存手机验证码到redis中
		try {
			cacheService.set(registerMobileKey, 60, verifyCode);
		} catch (Exception e) {
			log.error("保存缓存错误");
			throw new BizException(BusinessErrorCode.SYSTEM_CACHE_ERROR.getId());
		}
	
	}
	
	/**
	 * 发送注册，登录，修改密码
	 */
	public void sendUserSmsCode(String id, String sendType) {
		
		id = id.trim();
		User user = userDao.getUser(id);
		// 发送注册短信，则用户需未注册
		if (MSConstant.SEND_MOBILE_REGISTER.equals(sendType)) {
			if (user!=null) {
				log.error("用户已经注册，手机号：" + id);
				throw new BizException(BusinessErrorCode.USER_HAS_REGISTED.getId());
			}
		} else {
			if (user==null) {
				log.error("用户未注册，手机号：" + id);
				throw new BizException(BusinessErrorCode.SMS_IS_NOT_REGISTER.getId());
			}
		}
		// 获取注册手机验证码key
		String registerMobileKey = CacheConstant.getSMSKey(id,sendType);
		// 生成6位短信随机验证码
		String verifyCode = SendSmsUtil.getVerifyCode();
		// 调用发送短信组件 TODO 先注释掉
		log.info("短信验证码：" + verifyCode);
		SendSmsUtil.sendSms(id, verifyCode);
		// 发送成功则保存手机验证码到redis中
		try {
			cacheService.set(registerMobileKey, 240, verifyCode);
		} catch (Exception e) {
			log.error("保存缓存错误");
			throw new BizException(BusinessErrorCode.SYSTEM_CACHE_ERROR.getId());
		}
	}
	
	/**
	 * 修改密码
	 */
	public void modifyUserPwd(RegisterForm registerForm) {
		// 手机号
		String id = registerForm.getId();
		// 获取验证码
		String verifyCode = registerForm.getVerifyCode();
		// 校验验证码
		String cacheKey = CacheConstant.getSMSKey(id, MSConstant.SEND_MOBILE_MODIFYPWD);
		String result = (String) cacheService.get(cacheKey);
		if (!verifyCode.equals(result)) {
			log.warn("修改登录密码手机验证码错误");
			throw  new BizException(BusinessErrorCode.MOBILE_VERIFYCODE_ERROR.getId());
		}
		// 修改密码
		User user = new User();
		user.setId(id);
		user.setPassword(registerForm.getPassword());
		userDao.updatePwd(user);
		log.info("修改密码成功，mobile:" + id);
	}

}
