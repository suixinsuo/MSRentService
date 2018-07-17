package com.dpp.rent.service.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.dpp.rent.app.api.constant.BusinessErrorCode;
import com.dpp.rent.app.api.exception.BizException;


/**
 * className:SendSmsMessage.java
 * description: 发送短信message
 * date: 2018年6月24日
 * author:jpg
 */
public class SendSmsUtil {
	private static Log log = LogFactory.getLog(SendSmsUtil.class);
	// 发送短信url
	private static String SMS_URL = null;
	
	// 短信通道参数，account 和 token
	private static String ACCOUNT = null;
	private static String TOKEN = null;
	// 发送短信模板
	private static String CONTENT = null;
	
	static {
		SMS_URL = ResourceBundle.getBundle("config").getString("sms_url");
		ACCOUNT = ResourceBundle.getBundle("config").getString("sms_account");
		TOKEN = ResourceBundle.getBundle("config").getString("sms_token");
//		CONTENT = ResourceBundle.getBundle("config").getString("sms_content");
		try {
			CONTENT = new String(ResourceBundle.getBundle("config").getString("sms_content").getBytes("ISO-8859-1"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * description: 发送message
	 * retrun_type:void
	 * date: 2018年6月24日
	 * author:jpg
	 * @return 
	 */
	public static void sendSms(String mobile,String verifyCode) {
		String content = CONTENT.replace("verifyCode", verifyCode);
		// 组装发送短信url
		String url = new StringBuffer().append(SMS_URL)
						.append("?account=").append(ACCOUNT)
						.append("&token=").append(TOKEN)
						.append("&mobile=").append(mobile)
						.append("&content=").append(content)
						.toString();
		String response = HttpUtils.sendGet(url);
		// response 格式 "{\"Code\":\"0\",\"Message\":\"操作成功\",\"TaskId\":\"107487\"}";
		// 返回0代表发送成功
		JSONObject reqjson = new JSONObject(response);
		if (!"0".equals(reqjson.getString("Code"))) {
			log.error("发送短信失败，手机号：" + mobile);
			throw new BizException(BusinessErrorCode.SEND_SMS_ERROR.getId());
		}
	}
	
	/**
	 * description: 生成6位随机手机验证码
	 * retrun_type:String
	 * date: 2018年6月24日
	 * author:jpg
	 */
	public static String getVerifyCode() {
        String charValue = "";
        for (int i = 0; i < 6; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
	}
	
	public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
	
	public static void main(String[] args) {
//		String mobile = "15012924639";
//		String verifyCode = "123456";
//		System.out.println(SendSmsUtil.sendSms(mobile,verifyCode));
		System.out.println(getVerifyCode());
	}
}
