package com.pxu.redis;

import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/7/31 21:49
 */
public interface ListCache {
    /**
     * 头进
     * @param key
     * @param value
     * @return
     */
    long rightPush(String key, String value);

    /**
     * 尾出
     * @param key
     * @return
     */
    String leftPop(String key);

    /**
     * 查询所有元素
     * @param key
     * @return
     */
    List<String> getAll(String key);
}
