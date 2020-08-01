package com.pxu.delayqueue.core;

import com.pxu.delayqueue.entity.DelayQueueJob;
import com.pxu.delayqueue.entity.ScoredSortedItem;
import com.pxu.delayqueue.utils.RedisDelayQueueUtil;
import com.pxu.redis.impl.RedisZsetCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * delay-queue,
 * add item into zSet，value is unique id，score is delayTime
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Slf4j
public class DelayQueueZset {

    @Autowired
    RedisZsetCache zsetCache;

    /**
     * get first elem of zset from high to low
     */
    private ScoredSortedItem getFirstElem() {
        String key = RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY;
        String zsetFirstValue = zsetCache.getZsetFirstValue(key);
        if(zsetFirstValue == null){
            return null;
        }
        Double zsetScore = zsetCache.getZsetScore(key, zsetFirstValue);
        return new ScoredSortedItem(zsetFirstValue, zsetScore);
    }

    /**
     * 添加延迟任务到延迟队列
     * @param delayQueueJob
     */
    public void push(DelayQueueJob delayQueueJob) {
        String key = RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY;
        zsetCache.zAdd(key, delayQueueJob.getId(), delayQueueJob.getDelayTime());
    }

    /**
     * 获取准备好的延迟任务, 放到就绪队列中，并且删除zset中的key
     */
    public void popReadyJob() {
        ScoredSortedItem firstElem = getFirstElem();
        if(firstElem != null && firstElem.getDelayTime() < System.currentTimeMillis()){
            //第一个任务到期了，将其放入就绪队列中



            //删除zset中key
            zsetCache.removeValue(RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY, firstElem.getDelayQueueJodId());

        }
    }

    /**
     * 删除延迟队列任务-从zset中删除
     * @param delayQueueJodId
     */
    public void delete(String delayQueueJodId) {
        zsetCache.removeValue(RedisDelayQueueUtil.DELAY_QUEUE_ZSET_KEY, delayQueueJodId);
    }

    /**
     *
     * @param delayQueueJodId
     */
    public void finish(long delayQueueJodId) {
        DelayQueueJob delayQueueJod = JobPool.getDelayQueueJobById(delayQueueJodId);
        if (delayQueueJod == null) {
            return;
        }
        JobPool.deleteDelayQueueJod(delayQueueJodId);
        ScoredSortedItem item = new ScoredSortedItem(delayQueueJod.getId(), delayQueueJod.getDelayTime());
        DelayBucket.deleteFormBucket(getFirstElem(delayQueueJod.getId()),item);
    }

    /**
     * 查询delay job
     * @param delayQueueJodId
     * @return
     */
    public DelayQueueJob get(long delayQueueJodId) {
        return JobPool.getDelayQueueJobById(delayQueueJodId);
    }
}
