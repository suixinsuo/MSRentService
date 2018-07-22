package com.dpp.rent.service.service.huanxin;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dpp.rent.app.api.constant.CacheConstant;
import com.dpp.rent.app.api.service.huanxin.HuanXinService;
import com.dpp.rent.app.api.util.CacheService;
import com.dpp.rent.app.api.util.StringCommonUtil;
import com.dpp.rent.service.util.HuaxXinUtil;

/**
 * className:HuanXinServiceImpl.java
 * description: 环信用户业务处理
 * date: 2018年7月19日
 * author:jpg
 */
@Service
public class HuanXinServiceImpl implements HuanXinService{
	
	@Autowired
	private CacheService cacheService;
	
    // 注册用户url
	private static String ADD_USER_URL = null;
	// 重置密码url
	private static String RET_PWD_URL = null;
	// 获取授权code
	private static String GET_TOKEN_URL = null;
	private static String CLIENT_ID = null;
	private static String CLIENT_SECRET = null;
	static {
		ADD_USER_URL = ResourceBundle.getBundle("config").getString("huanxin_adduser_url");
		RET_PWD_URL = ResourceBundle.getBundle("config").getString("huanxin_resetpwd_url");
		GET_TOKEN_URL = ResourceBundle.getBundle("config").getString("huanxin_gettoken_url");
		CLIENT_ID = ResourceBundle.getBundle("config").getString("client_id");
		CLIENT_SECRET = ResourceBundle.getBundle("config").getString("client_secret");
	}
	

	@Override
	public void addUser(String userId,String password) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", userId);
		map.put("password",StringCommonUtil.reverseStr(password));
		HuaxXinUtil.postJson(ADD_USER_URL, map);
	}
	
	@Override
	public void reSetPwd(String id, String password) {
		// TODO 有bug需要做
		Map<String,String> map = new HashMap<String, String>();
		map.put("newpassword",StringCommonUtil.reverseStr(password));
		String url = RET_PWD_URL.replace("{userName}", id);
		HuaxXinUtil.postJson(url, map);
	}
	
	@Override
	public void deleteUser(String userId) {
	}

	@Override
	public String getToken() {
		// 缓存中没有则生成
		String cachekey = CacheConstant.getHxtokenKey();
		String acceToken = (String) cacheService.get(cachekey);
		if (StringUtils.isBlank(acceToken)) {
			// 没有则生成
			Map<String,String> map = new HashMap<String, String>();
			map.put("grant_type","client_credentials");
			map.put("client_id",CLIENT_ID);
			map.put("client_secret",CLIENT_SECRET);
			JSONObject jSONObject =  HuaxXinUtil.postJson(GET_TOKEN_URL, map);
			String token = (String) jSONObject.get("access_token");
			int expiress = (int) jSONObject.get("expires_in");
			cacheService.set(cachekey, expiress, token);
			return token;
		}
		return acceToken;
	}
	
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
		/*map.put("newpassword","12345678");
		String url = "https://a1.easemob.com/1126180717146444/365test/users/admin1221/password";*/
		map.put("grant_type","client_credentials");
		map.put("client_id","YXA6rpiWQIlzEei8HSULBxprPA");
		map.put("client_secret","YXA6LLq8RWcwMBHAoMh-nX9msMArCMc");
		String url ="https://a1.easemob.com/1126180717146444/365test/token";
		JSONObject jSONObject =  HuaxXinUtil.postJson(url, map);
		String token = (String) jSONObject.get("access_token");
		
		// token YWMt4jBWIIzDEeicFXcXwpgEiAAAAAAAAAAAAAAAAAAAAAGumJZAiXMR6LwdJQsHGms8AgMAAAFkvA17rQBPGgCcHastcOu1nOTnpNWaoGwVWQ13JS5QdOl04z2g9uccTQ
		System.out.println(token);
	}
}	
