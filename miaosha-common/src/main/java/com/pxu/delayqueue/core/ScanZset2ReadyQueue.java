package com.pxu.delayqueue.core;

import com.pxu.delayqueue.entity.DelayQueueJob;
import com.pxu.delayqueue.entity.ScoredSortedItem;
import com.pxu.delayqueue.utils.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 扫描zset中的任务，放到redyQueue中
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Slf4j
public class ScanZset2ReadyQueue implements Runnable {

    @Autowired
    DelayQueueZset delayQueueZset;

    @Autowired ReadyQueue readyQueue;

    @Override
    public void run() {
        while (true) {
            try {
                //获取zset中第一个elem
                ScoredSortedItem firstElem = delayQueueZset.getFirstElem();
                if(firstElem == null) {
                    sleep();
                    continue;
                }
                double delayTime = firstElem.getDelayTime();
                if(delayTime < System.currentTimeMillis()){
                    //删除zset中的key
                    delayQueueZset.delete(firstElem.getDelayQueueJodId());

                    //该任务到期了，加入到任务队列中
                    readyQueue.pushToReadyQueue(RedisDelayQueueUtil.DELAY_QUEUE_LIST_KEY, firstElem.getDelayQueueJodId());
                }
            } catch (Exception e) {
                log.error("此次扫描失败", e);
            }
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
