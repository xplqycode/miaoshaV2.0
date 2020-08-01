package com.pxu.delayqueue.core;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * 存放可以消费的jod, use list of redis
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
public class ReadyQueue {

    @Autowired


    /**
     * 添加jodid到准备队列
     * @param topic
     * @param delayQueueJodId
     */
    public static void pushToReadyQueue(String topic,long delayQueueJodId) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(topic);
        rBlockingQueue.offer(delayQueueJodId);
    }

    /**
     * 从准备队列中获取jodid
     * @param topic
     * @return
     */
    public static Long pollFormReadyQueue(String topic) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(topic);
        return rBlockingQueue.poll();
    }
}
