package com.pxu.redis.impl;

import com.pxu.redis.ZsetCache;
import com.pxu.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 8:42
 */
@Component
public class RedisZsetCache extends AbstractRedisCache implements ZsetCache {
    @Override
    public boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zSetRemove(String key, String value) {
         redisTemplate.opsForZSet().remove(key, value);
    }
}
