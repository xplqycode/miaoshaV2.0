package com.pxu.delayqueue.core;

import com.pxu.delayqueue.entity.DelayQueueJob;
import com.pxu.delayqueue.entity.ScoredSortedItem;
import com.pxu.delayqueue.utils.RedisDelayQueueUtil;
import com.pxu.redis.impl.RedisZsetCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * delay-queue,
 * add item into zSet，value is unique id，score is delayTime
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Slf4j
@Component
public class DelayQueueZset {

    @Autowired
    RedisZsetCache zsetCache;

    /**
     * get first elem of zset from high to low
     */
    public ScoredSortedItem getFirstElem() {
        String key = RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY;
        String zsetFirstValue = zsetCache.getZsetFirstValue(key);
        if(zsetFirstValue == null){
            return null;
        }
        Double zsetScore = zsetCache.getZsetScore(key, zsetFirstValue);
        return new ScoredSortedItem(zsetFirstValue, zsetScore);
    }

    /**
     * 添加延迟任务到延迟队列, 加到zset中
     * @param delayQueueJob
     */
    public void offer(DelayQueueJob delayQueueJob) {
        if(delayQueueJob == null) {
            return;
        }
        String key = RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY;
        zsetCache.zAdd(key, delayQueueJob.getId(), delayQueueJob.getDelayTime());
    }

    /**
     * 删除延迟队列任务-从zset中删除
     * @param delayQueueJodId
     */
    public void delete(String delayQueueJodId) {
        zsetCache.removeValue(RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY, delayQueueJodId);
    }

}
