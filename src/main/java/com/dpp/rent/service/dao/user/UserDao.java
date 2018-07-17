package com.dpp.rent.service.dao.user;

import java.util.List;

import com.dpp.rent.app.api.domain.User;

public interface UserDao {
	
	/**
	 * description: 根据手机号查询用户
	 * retrun_type:User
	 * date: 2018年6月24日
	 * author:jpg
	 */
	public User getUser(String id);
	
	/**
	 * description: 新增用户
	 * retrun_type:void
	 * date: 2018年6月24日
	 * author:jpg
	 */
	public void saveUser(User user);
	
	/**
	 * description: 修改密码
	 * retrun_type:void
	 * date: 2018年6月30日
	 * author:jpg
	 * @param user 
	 */
	public void updatePwd(User user);
	
	/**
	 * description: 查询所有用户
	 * retrun_type:List<User>
	 * date: 2018年7月11日
	 * author:jpg
	 */
	public List<User> getAllUser();
	
	/**
	 * 获取最大4位推荐生成码
	 */
	String getMaxLinkCode();
	
	/**
	 * description: 动态条件查询用户
	 * retrun_type:User
	 * date: 2018年7月13日
	 * author:jpg
	 */
	List<User> getUserByParam(User user);
}
