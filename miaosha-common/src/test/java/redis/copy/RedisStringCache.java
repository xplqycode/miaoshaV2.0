package redis.copy;

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
     * 直接封装好的一个方法，
     * @param key
     * @param handler
     * @return
     */
    @Override
    public String get(String key, StringDbLoadHandler handler) {
        try {
            String result = redisTemplate.opsForValue().get(key);
            if (result != null) {
                return result;
            } else {
                result = handler.getCallback().excute();
                if (result != null) {
                    redisTemplate.opsForValue().set(key, result, handler.getTimeoutSeconds(), TimeUnit.SECONDS);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("缓存异常 key {}", key, e);
            return handler.getCallback().excute();
        }
    }

    @Override
    public void set(String key, String value, long timeoutSeconds) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存异常 key {} {} ", key, value, e);
            cacheExceptionHandler.handle(e, key);
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
            redisTemplate.opsForValue().increment(key, i);
        } catch (Exception e) {
            log.error("缓存异常", e);
            cacheExceptionHandler.handle(e, key);
        }
    }

    @Override
    public long incrementAndGet(String key, int i) {
        return redisTemplate.opsForValue().increment(key, i);
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
            cacheExceptionHandler.handle(e, key);
        }
        return null;
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
    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void ltrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public String rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    @Override
    public Set<String> reverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    @Override
    public boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long hicr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public void hPutAll(String key, Map<String, String> datas) {
        redisTemplate.opsForHash().putAll(key, datas);
    }

    @Override
    public void zCutByScoreDesc(String cacheKey, int length) {
        int maxLength = length;
        long existsLength = redisTemplate.opsForZSet().zCard(cacheKey);
        if (existsLength <= maxLength) {
            return;
        }
        //按照从小到大排名删除前面几个（分数低的）
        redisTemplate.opsForZSet().removeRange(cacheKey, 0, existsLength - maxLength - 1);
    }

    @Override
    public Long zrem(String cacheKey, String item) {
        return redisTemplate.opsForZSet().remove(cacheKey, item);
    }

    @Override
    public Long zCard(String cacheKey) {
        return redisTemplate.opsForZSet().zCard(cacheKey);
    }

    @Override
    public Double zScore(String cacheKey, String itemKey) {
        return redisTemplate.opsForZSet().score(cacheKey, itemKey);
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }
}
