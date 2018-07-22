package com.dpp.rent.service.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.dpp.rent.app.api.constant.BusinessErrorCode;
import com.dpp.rent.app.api.exception.BizException;
import com.dpp.rent.service.service.user.UserBuyHouseServiceImpl;
import com.google.gson.Gson;

/**
 * className:HuaxXinUtil.java
 * description: 发送环信请求
 * date: 2018年7月21日
 * author:jpg
 */
public class HuaxXinUtil {
	
	private static Log log = LogFactory.getLog(HuaxXinUtil.class);

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

	public static JSONObject postJson(String url,Map<String,String> map){
		String json = new Gson().toJson(map);
        // 设置参数
//        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//        }
        // 编码
//        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        // 取得HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
		httpPost.setHeader("Content-Type", "application/json");

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
