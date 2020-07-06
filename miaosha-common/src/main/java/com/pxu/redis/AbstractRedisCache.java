package com.pxu.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 9:03
 */
@Slf4j
@Service
public abstract class AbstractRedisCache {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    protected CacheExceptionHandler cacheExceptionHandler;

    /**
     * 注意：！！！！！！！！！！！永久key会返回 false
     * @param key
     * @return
     */
    public boolean maybeExpired(String key) {
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire == null || expire < 1;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getExpired(String key) {
        return redisTemplate.getExpire(key);
    }

    public boolean expired(String key, long timeoutSeconds) {
        try {
            Boolean expire = redisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
            if (expire != null) {
                return expire;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    public void set(String key, String value, long timeoutSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存异常 key {} {} ", key, value, e);
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.expire(key, 1L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("缓存异常", e);
            cacheExceptionHandler.handle(e, key);
        }
    }

    public void delete(List<String> keyList) {
        try {
            redisTemplate.delete(keyList);
        } catch (Exception e) {
            log.error("缓存异常", e);
            cacheExceptionHandler.handle(e, keyList.toArray(new String[]{}));
        }
    }

    public boolean hincr(String key, String hashKey, long count) {
        if (StringUtils.isEmpty(hashKey) || maybeExpired(key)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().increment(key, hashKey, count);
            return true;
        } catch (Exception e) {
            log.error("getObject error", e);
            return false;
        }
    }

    public boolean hset(String key, String hashKey, String value) {
        if (StringUtils.isEmpty(hashKey) || maybeExpired(key)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("getObject error", e);
            return false;
        }
    }

}
