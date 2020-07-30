package com.pxu.redis;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 8:57
 */
public interface StringCache {

    /**
     * 查询结果，查询不到就调用查询数据库的handler查询
     * @param key
     * @param handler
     * @return
     */
    String get(String key, StringDbLoadHandler handler);

    /**
     * 设置值
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    void set(String key, String value, long timeoutSeconds);

    /**
     * 批量设置
     * @param paramList
     */
    void setList(List<SetParam> paramList);

    void increment(String key, int i);

    Long initOrIncrement(String key, long i, long expireSeconds);

    void delete(String key);

    String get(String key);

    int getIntValue(String key);

    void initIntValue(String key, long expireSeconds);

    /**
     * ????? todo
     * @param keyList
     * @return
     */
    List<String> get(List<String> keyList);

    /**
     * 往sortedSet中添加元素，或者修改score，如果元素已经存在的话,则为修改其分数
     * @param key
     * @param value
     * @param score
     * @return
     */
    boolean zAdd(String key, String value, double score);

    List<String> getObjectIdKeyList(String key, long size);

}
