package com.pxu.redis.impl;

import com.alibaba.fastjson.JSONObject;
import com.pxu.redis.ObjectCache;
import com.pxu.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 8:49
 */
@Component
public class RedisObjectCache extends AbstractRedisCache implements ObjectCache {
    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        T rtn;
        try {
             rtn = JSONObject.parseObject(value, clazz);
        } catch (Exception e) {
            return null;
        }
        return rtn;
    }

    @Override
    public void setObject(String key, Object value, long timeoutSeconds) {
        if(value == null){
            return;
        }
        String stringValue = JSONObject.toJSONString(value);
        redisTemplate.opsForValue().set(key, stringValue, timeoutSeconds);
    }
}
