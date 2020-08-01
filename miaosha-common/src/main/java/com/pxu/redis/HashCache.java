package com.pxu.redis;

import java.util.Map;

/**
 * 操作hash对象的方法
 *
 * @author pxu31@qq.com
 * @date 2020/7/31 21:23
 */
public interface HashCache {
    boolean setHash(String key, String hashKey, Object value, long timeExpire);

    Map<Object, Object> getHashMap(String key);

    Object getHashValue(String key, String hashKey);
}
