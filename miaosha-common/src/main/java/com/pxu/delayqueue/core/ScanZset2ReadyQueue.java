package com.pxu.delayqueue.core;

import com.pxu.delayqueue.entity.DelayQueueJob;
import com.pxu.delayqueue.entity.ScoredSortedItem;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 扫描zset中的任务，放到redyQueue中
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Slf4j
public class ScanZset2ReadyQueue implements Runnable {

    private String delayBucketKey;

    public ScanZset2ReadyQueue(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
    }

    public String getDelayBucketKey() {
        return delayBucketKey;
    }

    public void setDelayBucketKey(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
    }

    @Override
    public void run() {
        while (true) {
//            try {
//                //获取zset中的第一个元素
//                ScoredSortedItem item = DelayBucket.getFromBucket(this.delayBucketKey);
//                //没有任务
//                if (item == null) {
//                    sleep();
//                    continue;
//                }
//                //延迟时间没到
//                if (item.getDelayTime() > System.currentTimeMillis()) {
//                    sleep();
//                    continue;
//                }
//
//                DelayQueueJob delayQueueJod = JobPool.getDelayQueueJobById(item.getDelayQueueJodId());
//                //延迟任务元数据不存在
//                if (delayQueueJod == null) {
//                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
//                    continue;
//                }
//
//                //再次确认延时时间是否到了
//                if (delayQueueJod.getDelayTime() > System.currentTimeMillis()) {
//                    //删除旧的
//                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
//                    //重新计算延迟时间
//                    DelayBucket.addToBucket(this.delayBucketKey,new ScoredSortedItem(delayQueueJod.getId(),delayQueueJod.getDelayTime()));
//                } else {
//                    ReadyQueue.pushToReadyQueue(delayQueueJod.getTopic(),delayQueueJod.getId());
//                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
//                }
//
//            }catch (Exception e) {
//                log.error("扫描delaybucket出错：",e);
//            }


        }
    }

    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1L);
        }catch (InterruptedException e){
            log.error("",e);
        }
    }
}
