package com.pxu.delayqueue.core;


import com.pxu.redis.impl.RedisListCache;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 存放可以消费的jod, use list of redis
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */

public class ReadyQueue {

    @Autowired
    RedisListCache listCache;

    /**
     * 添加jodid到准备队列
     * @param topic
     * @param delayQueueJodId
     */
    public void pushToReadyQueue(String topic, String delayQueueJodId) {
        listCache.rightPush(topic, delayQueueJodId);
    }

    /**
     * 从准备队列中获取jodid
     * @param topic
     * @return
     */
    public String pollFormReadyQueue(String topic) {
        return listCache.leftPop(topic);
    }
}
