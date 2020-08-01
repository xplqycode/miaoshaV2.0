package com.pxu.threadpool;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * @author xupeng3@corp.netease.com
 * @date 2020/6/19 9:49
 */
@Configuration
public class ThreadPool {

    @Bean
    public  ExecutorService getExecutor() {
        return new ThreadPoolExecutor(8, 8, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactoryPxu("秒杀项目线程池_"));
    }


}
