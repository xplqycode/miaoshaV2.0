package com.pxu.delayqueue.core;

import com.pxu.delayqueue.entity.DelayQueueJob;
import com.pxu.delayqueue.utils.RedisDelayQueueUtil;
import com.pxu.redis.impl.RedisObjectCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 延迟任务池, 使用key-value，存放job的detail信息
 *
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Component
public class JobPool {


    @Autowired
    RedisObjectCache objectCache;

    /**
     * 查询job
     * @param delayQueueJobId
     * @return
     */
    public DelayQueueJob getDelayQueueJobById(String delayQueueJobId) {
        String key = RedisDelayQueueUtil.getJobPoolKey(delayQueueJobId);
        return objectCache.getObject(key, DelayQueueJob.class);
    }

    /**
     * 添加 job
     * @param delayQueueJob
     */
    public void addDelayQueueJod(DelayQueueJob delayQueueJob) {
        String key = RedisDelayQueueUtil.getJobPoolKey(delayQueueJob.getId());
        objectCache.setObject(key, delayQueueJob, delayQueueJob.getDelayTime());
    }

    /**
     * 删除 job
     *
     * @param delayQueueJodId
     */
    public void deleteDelayQueueJod(String delayQueueJodId) {
        String key = RedisDelayQueueUtil.getJobPoolKey(delayQueueJodId);
        objectCache.delete(key);
    }
}
