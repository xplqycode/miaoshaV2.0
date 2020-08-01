package com.pxu.delayqueue.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ExecutorService executorService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        ExecutorService executorService = Executors.newFixedThreadPool((int)DelayQueueZset.DELAY_BUCKET_NUM);
//        for (int i = 0; i < DelayQueueZset.DELAY_BUCKET_NUM; i++) {
//            executorService.execute(new ScanZset2ReadyQueue(DelayQueueZset.DELAY_BUCKET_KEY_PREFIX+i));
//        }
    }
}
