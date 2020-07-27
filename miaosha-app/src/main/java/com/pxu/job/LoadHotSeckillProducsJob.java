package com.pxu.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pxu31@qq.com
 * @date 2020/7/12 9:34
 */
@Slf4j
public class LoadHotSeckillProducsJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        //执行定时加载hot商品信息到缓存中
        //缓存预热，固定每小时遍历一次，将秒杀开始时间在五分钟之内的数据放到缓存中
    }
}
