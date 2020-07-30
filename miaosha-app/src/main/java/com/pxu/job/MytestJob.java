package com.pxu.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author pxu31@qq.com
 * @date 2020/7/30 9:05
 */
@Slf4j
@Component
public class MytestJob {
    @Scheduled(cron = "0/5 * * * * *")
    public void pxuTest() {
        log.info("我是自己使用的定时任务");
    }
}
