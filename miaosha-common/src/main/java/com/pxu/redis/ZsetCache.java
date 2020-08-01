package com.pxu.redis;

import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 8:33
 */
public interface ZsetCache {
    /**
     * 往sortedSet中添加元素，或者修改score，如果元素已经存在的话,则为修改其分数
     * @param key
     * @param value
     * @param score
     * @return
     */
    boolean zAdd(String key, String value, double score);

    void removeValue(String key, String value);

    /**
     * get first elem of zset from high to low
     * @param key
     * @return
     */
    String getZsetFirstValue(String key);

    Double getZsetScore(String key, String value);

    List<String> getObjectIdKeyList(String key, long size);

}
