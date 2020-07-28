package com.pxu.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author pxu31@qq.com
 * @date 2020/7/12 9:34
 */
@Slf4j
public class LoadHotSeckillProducsJob implements SimpleJob {

    @Autowired


    @Override
    public void execute(ShardingContext shardingContext) {

    }
}
