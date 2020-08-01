package com.pxu.redis.impl;

import com.pxu.redis.ZsetCache;
import com.pxu.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void removeValue(String key, String value) {
         redisTemplate.opsForZSet().remove(key, value);
    }

    @Override
    public String getZsetFirstValue(String key){
        Set<String> set = redisTemplate.opsForZSet().reverseRange(key, 0, 1);
        if(set == null || set.size() == 0){
            return null;
        }
        return new ArrayList<>(set).get(0);
    }

    @Override
    public Double getZsetScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }
}
