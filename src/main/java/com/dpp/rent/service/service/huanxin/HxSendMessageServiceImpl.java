package com.dpp.rent.service.service.huanxin;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dpp.rent.app.api.constant.BusinessErrorCode;
import com.dpp.rent.app.api.domain.User;
import com.dpp.rent.app.api.domain.UserBuyHouse;
import com.dpp.rent.app.api.exception.BizException;
import com.dpp.rent.app.api.model.request.huanxin.HuanxinMsg;
import com.dpp.rent.app.api.model.request.huanxin.Msg;
import com.dpp.rent.app.api.service.huanxin.HuanXinService;
import com.dpp.rent.app.api.service.huanxin.HxSendMessageService;
import com.dpp.rent.service.dao.user.UserBuyHouseDao;
import com.dpp.rent.service.dao.user.UserDao;
import com.dpp.rent.service.util.HuaxXinUtil;
import com.google.gson.Gson;

/**
 * className:HxSendMessageServiceImpl.java
 * description: 发送环信消息
 * date: 2018年7月21日
 * author:jpg
 */
@Service
public class HxSendMessageServiceImpl implements HxSendMessageService{
	private static Log log = LogFactory.getLog(HxSendMessageServiceImpl.class);
	
	private static String SEND_MSG_URL = null;
	static {
		SEND_MSG_URL = ResourceBundle.getBundle("config").getString("huanxin_send_msg_url");
	}
	
	@Autowired
	private HuanXinService huanXinService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserBuyHouseDao userBuyHouseDao;
	
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
	
    
    /**
     * description: 信息员用户推送订单消息
     * retrun_type:void
     * date: 2018年7月22日
     * author:jpg
     */
	@Override
	public void sendMsg(String id,String houseId) {
		// id为用户发布房源的用户，houseId为发布的房源id
		String[] array = {"admin", "admin1", "admin2","15012924639"};
		String message = "hello from rest";
		// 信息员用户录入的订单，如果是经纪人绑定的小区，则推送给他们，15分钟后推送给商圈所有的经纪人。
		// 1.查询出该用户对应的上级，若没有上级，先将
		User user = userDao.getUser(id);
		if ("0".equals(user.getType())) {
			// 游客发布消息
			userBuyHouseDao.getMaxHouseId();
			
			
		}
		
		if ("2".equals(user.getType())) {
			// 信息员发布消息
		}
		
		HuanxinMsg huanxinMsg = new HuanxinMsg(array,message);	
		String json = new Gson().toJson(huanxinMsg);
		String token = huanXinService.getToken();
		postJson(SEND_MSG_URL,json,token);
	}
	
	public static void main(String[] args) {
		String url = "https://a1.easemob.com/1126180717146444/365test/messages";
		Map<String,String> map = new HashMap<String, String>();
		
		String[] array = {"admin", "admin1", "admin2","15012924639"};
		String message = "hello from rest";
		HuanxinMsg huanxinMsg = new HuanxinMsg(array,message);
		String json = new Gson().toJson(huanxinMsg);
//		postJson(SEND_MSG_URL,json,);
//		huanXinService.getToken();
//		YWMt4jBWIIzDEeicFXcXwpgEiAAAAAAAAAAAAAAAAAAAAAGumJZAiXMR6LwdJQsHGms8AgMAAAFkvA17rQBPGgCcHastcOu1nOTnpNWaoGwVWQ13JS5QdOl04z2g9uccTQ
	}
	
	public static JSONObject postJson(String url,String json,String token){
        // 取得HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer "+token);
//		httpPost.setHeader("Content-Type", "application/json");

        // 参数放入Entity
        httpPost.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 执行post请求
            response = httpclient.execute(httpPost);
            // 得到entity
            HttpEntity entity = response.getEntity();
            // 得到字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
        log.error("操作环信用户"+result);
		System.out.println(result);
        // 将返回的转换为json对象
		JSONObject jsonObject = JSONObject.parseObject(result);
		if (jsonObject.getString("error")!=null) {
			log.error("操作环信用户"+result);
			System.out.println(result);
			throw new BizException(BusinessErrorCode.OPERATE_HUANXINUSER_ERROR.getId());
		}
		return jsonObject;
    }


}
