package com.pxu.redis;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 8:37
 */
public interface ObjectCache {
    /**
     * 获取指定对象，通过Alibaba.json来解析
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObject(String key, Class<T> clazz);

    /**
     * 设置一个对象，将对象json化后存到redis中
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    void setObject(String key, Object value, long timeoutSeconds);

}
