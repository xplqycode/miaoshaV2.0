package com.pxu.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 8:57
 */
@Slf4j
@Component
public class RedisStringCache extends AbstractRedisCache implements StringCache {

    /**
     * 直接封装好的一个方法，处理缓存和数据库的读取逻辑
     * @param key
     * @param handler
     * @return
     */
    @Override
    public String get(String key, StringDbLoadHandler handler) {
        try {
            String result = redisTemplate.opsForValue().get(key);
            if (result != null) {
                log.info("缓存查到了");
                return result;
            } else {
                result = handler.getCallback().excute();
                if (result != null) {
                    log.info("db查到了设置缓存");
                    redisTemplate.opsForValue().set(key, result, handler.getTimeoutSeconds(), TimeUnit.SECONDS);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("缓存异常 key {}", key, e);
            return handler.getCallback().excute();
        }
    }

    /**
     * 设置一个kv
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    @Override
    public void set(String key, String value, long timeoutSeconds) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存异常 key {} {} ", key, value, e);
        }
    }

    @Override
    public void setList(List<SetParam> paramList) {
        try {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                StringRedisConnection conn = (StringRedisConnection) connection;
                for (SetParam param : paramList) {
                    conn.set(param.getKey(), param.getValue());
                    conn.expire(param.getKey(), param.getTimeoutSeconds());                }
                return null;
            });
        } catch (Exception e) {
            log.error("setList error", e);
        }
    }

    @Override
    public void increment(String key, int i) {
        if (i == 0 || maybeExpired(key)) {
            return;
        }
        try {
            //Increment an integer value stored as string value under {@code key} by {@code delta}
            redisTemplate.opsForValue().increment(key, i);
        } catch (Exception e) {
            log.error("缓存异常", e);
        }
    }

    @Override
    public Long initOrIncrement(String key, long i, long expireSeconds) {
        long value = redisTemplate.opsForValue().increment(key, i);
        redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public void delete(String key) {
        super.delete(key);
    }

    @Override
    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("缓存异常 key {} ", key, e);
        }
        return null;
    }

    @Override
    public int getIntValue(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            return Integer.valueOf(value == null ? "-1" : value);
        } catch (Exception e) {
            log.error("缓存异常 key {} ", key, e);
        }
        return -1;
    }

    @Override
    public void initIntValue(String key, long expireSeconds) {
        redisTemplate.opsForValue().set(key, "0", expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public List<String> get(List<String> keyList) {
        List<Object> valueList = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            StringRedisConnection conn = (StringRedisConnection) connection;
            for (String key : keyList) {
                conn.get(key);
            }
            return null;
        });
        return valueList.stream().map(value -> (String) value).collect(Collectors.toList());
    }

    @Override
    public boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }
}
