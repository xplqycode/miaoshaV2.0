package com.pxu.redis.impl;

import com.pxu.redis.HashCache;
import com.pxu.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 8:46
 */
@Component
public class RedisHashCache extends AbstractRedisCache implements HashCache {

    @Override
    public boolean setHash(String key, String hashKey, Object value, long timeExpire) {
        boolean rtn = redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        redisTemplate.expire(hashKey, timeExpire, TimeUnit.SECONDS);
        return rtn;
    }

    @Override
    public Map<Object, Object> getHashMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
}
