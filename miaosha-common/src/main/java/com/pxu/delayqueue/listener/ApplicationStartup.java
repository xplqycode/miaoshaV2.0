package com.pxu.delayqueue.listener;

import com.pxu.delayqueue.core.ScanZset2ReadyQueue;
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
            executorService.execute(new ScanZset2ReadyQueue());
    }
}
