package com.pxu.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis处理的初始类，主要放一些公共方法
 *
 * @author pxu31@qq.com
 * @date 2020/7/6 9:03
 */
@Slf4j
@Service
public abstract class AbstractRedisCache {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    /**
     *查看key是否失效或即将失效
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

    /**
     * 查询剩余失效时间
     * @param key
     * @return
     */
    public Long getExpired(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 设置失效时间
     * @param key
     * @param timeoutSeconds
     * @return
     */
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

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对key进行设置
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    public void set(String key, String value, long timeoutSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存异常 key {} {} ", key, value, e);
        }
    }

    /**
     * 删除某一key
     * @param key
     */
    public void delete(String key) {
            redisTemplate.expire(key, 1L, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量删除一些key
     * @param keyList
     */
    public void delete(List<String> keyList) {
        try {
            redisTemplate.delete(keyList);
        } catch (Exception e) {
            log.error("缓存异常", e);
        }
    }

}
