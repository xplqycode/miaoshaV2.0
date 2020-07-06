package com.pxu.redis.exceptionhandler;


import com.pxu.redis.CacheExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangmaocheng on 2018/4/20.
 */
@Slf4j
@Component
public class DeleteCacheExceptionHandler implements CacheExceptionHandler, Runnable {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private LinkedBlockingQueue<String> keyQueue = new LinkedBlockingQueue<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        executorService.scheduleAtFixedRate(this, 30, 1, TimeUnit.SECONDS);
    }

    @Override
    public void handle(Exception e, String... keys) {
        log.error("缓存异常", e);
        if (keys == null) {
            return;
        }
        try {
            for (String key : keys) {
                if (StringUtils.isNotBlank(key)) {
                    // 如果队列满了，return false
                    keyQueue.offer(key);
                }
            }
        } catch (Exception e1) {
        }
    }

    @Override
    public void run() {
        // 如果队列满了，返回null
        String key = null;
        while ((key = keyQueue.poll()) != null) {
            log.info("删除key:{}...", key);
            try {
                redisTemplate.expire(key, 1, TimeUnit.SECONDS);
            } catch (Exception e) {
                keyQueue.offer(key);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
