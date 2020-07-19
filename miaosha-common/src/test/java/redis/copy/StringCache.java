package redis.copy;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 8:57
 */
public interface StringCache {

    String get(String key, StringDbLoadHandler handler);

    void set(String key, String value, long timeoutSeconds);

    void setList(List<SetParam> paramList);

    void increment(String key, int i);

    Long initOrIncrement(String key, long i, long expireSeconds);

    void delete(String key);

    String get(String key);

    List<String> get(List<String> keyList);

    long lpush(String key, String value);

    void ltrim(String key, long start, long end);

    String rpop(String key);

    List<String> lrange(String key, long start, long stop);

    Set<String> reverseRange(String key, long start, long end);

    Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, long start, long end);

    boolean zAdd(String key, String value, double score);

    Long hicr(String key, String hashKey, long delta);

    Map<Object, Object> hgetAll(String key);

    void hPutAll(String key, Map<String, String> datas);

    /**
     * 分数从大到小，裁剪并保留前面排名的元素
     *
     * @param cacheKey
     * @param length
     */
    void zCutByScoreDesc(String cacheKey, int length);

    /**
     * @param cacheKey
     * @param item
     * @return 被成功移除的成员的数量
     */
    Long zrem(String cacheKey, String item);

    Long zCard(String cacheKey);

    Double zScore(String cacheKey, String itemKey);

    long lRemove(String key, long count, Object value);

    long incrementAndGet(String key, int i);

}
