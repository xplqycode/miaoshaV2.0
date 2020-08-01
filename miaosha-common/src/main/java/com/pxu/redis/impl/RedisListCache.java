package com.pxu.redis.impl;

import com.pxu.redis.ListCache;
import com.pxu.redis.base.AbstractRedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 14:48
 */
@Component
public class RedisListCache extends AbstractRedisCache implements ListCache {

    @Override
    public long rightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public String leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public List<String> getAll(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return redisTemplate.opsForList().range(key, 0, size - 1);
    }


}
