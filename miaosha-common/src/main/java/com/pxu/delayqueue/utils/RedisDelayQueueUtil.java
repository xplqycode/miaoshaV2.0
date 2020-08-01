package com.pxu.delayqueue.utils;

/**
 * @author pxu31@qq.com
 * @date 2020/8/1 10:48
 */
public class RedisDelayQueueUtil {

    private static final String BASE_KEY = "delay_queue:";

    private static final String DELAY_QUEUE_JOB_POOL = BASE_KEY + "job_pool";

    public static final String DELAY_QUEUE_ZSET_KEY = BASE_KEY + "zset";


    public static final String DELAY_QUEUE_LIST_KEY = BASE_KEY + "list";


    /**
     * key of job-pool
     * @param delayQueueJobId
     * @return
     */
    public static String getJobPoolKey(String delayQueueJobId) {
        return DELAY_QUEUE_JOB_POOL.concat(delayQueueJobId);
    }
//
//    /**
//     * value of zset
//     * @param delayQueueJodId
//     * @return
//     */
//    public static String getZsetKey(long delayQueueJodId) {
//        return DELAY_QUEUE_ZSET_VALUE_KEY_PREFIX.concat(String.valueOf(delayQueueJodId));
//    }
}
