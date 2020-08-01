package com.pxu.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Lists;
import com.pxu.constant.SeckillRedisConstant;
import com.pxu.domain.SeckillProduct;
import com.pxu.redis.impl.RedisStringKeyCache;
import com.pxu.redis.constants.ExpireTimeConstant;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 定时任务
 * 缓存预热，提前将秒杀开始时间15分钟以内的商品放到redis中
 * @author pxu31@qq.com
 * @date 2020/7/12 9:34
 */
@Slf4j
public class LoadHotSeckillProducsJob implements SimpleJob {

    @Autowired
    SeckillProductService seckillProductService;

    @Autowired
    RedisStringKeyCache stringCache;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("****开始缓存预热");
        int limit = 100;
        long id = -1;
        List<SeckillProduct> hotProductList = Lists.newArrayList();
        List<SeckillProduct> hotProductSubList;

        //分页查询出
        do {
            hotProductSubList = seckillProductService.hotListFromDb(limit, id);
            if (CollectionUtils.isEmpty(hotProductSubList)) {
                break;
            }
            hotProductList.addAll(hotProductSubList);
            id = hotProductSubList.get(hotProductSubList.size() - 1).getSeckillId();
        } while (hotProductSubList.size() == limit);

        if (hotProductList.size() > 0) {
            for (SeckillProduct product : hotProductList) {
                long seckillId = product.getSeckillId();
                String key = SeckillRedisConstant.getSeckillKey(seckillId);
                stringCache.set(key, JSONObject.toJSONString(product), ExpireTimeConstant.ONE_MINUTE_EXPIRETIME);
            }
        }


        log.info("热点商品信息为：{}", JSONObject.toJSONString(hotProductList));
        log.info("****预热结束，共添加{}条热点商品到redis中", hotProductList.size());
    }
}
