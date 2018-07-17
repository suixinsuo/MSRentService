package com.dpp.rent.service.util;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dpp.rent.app.api.util.CacheService;

@Service
public class CacheServiceImpl implements CacheService{
	 // 从容器中获得JedisClient对象  
    @SuppressWarnings("rawtypes")
	private static RedisTemplate redisTemplate =  (RedisTemplate) SpringContextUtils.getBeanById("redisTemplate");  
    
	@SuppressWarnings("unchecked")
	public void set(String key, int time, String value) {
		redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);  
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
