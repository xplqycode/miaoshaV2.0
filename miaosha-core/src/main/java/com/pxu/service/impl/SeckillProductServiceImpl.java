package com.pxu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pxu.constant.DateTimeConstants;
import com.pxu.constant.SeckillRedisConstant;
import com.pxu.domain.SeckillProduct;
import com.pxu.persistence.SeckillProductsMapper;
import com.pxu.redis.RedisStringCache;
import com.pxu.redis.StringDbLoadHandler;
import com.pxu.redis.constants.ExpireTimeConstant;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 主要用于商品表的信息处理
 *
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:10
 */
@Slf4j
@Service
public class SeckillProductServiceImpl implements SeckillProductService {

    @Autowired
    SeckillProductsMapper productsMapper;


    @Autowired
    RedisStringCache stringCache;

    @Override
    public List<SeckillProduct> getAll() {
        return productsMapper.selectList(null);
    }

    @Override
    public SeckillProduct findById(long id) {
        String key = SeckillRedisConstant.getSeckillKey(id);
        String productJson = stringCache.get(key, new StringDbLoadHandler(() -> {
            SeckillProduct productFromDb = productsMapper.selectById(id);
            if (productFromDb == null) {
                return null;
            }
            stringCache.set(key, JSONObject.toJSONString(productFromDb), ExpireTimeConstant.ONE_MINUTE_EXPIRETIME);
            return JSONObject.toJSONString(productFromDb);
        }, ExpireTimeConstant.ONE_MINUTE_EXPIRETIME));
        return JSONObject.parseObject(productJson, SeckillProduct.class);
    }

    @Override
    public int reduceNumber(long seckillId) {
        return productsMapper.reduceOneProduct(seckillId);
    }

    @Override
    public List<SeckillProduct> hotListFromDb(int limit, long id) {
        Date now = new Date();
        //十五分钟以后
        Date end = new Date(now.getTime() + DateTimeConstants.FIVE_MINUTE_MILLISECOND);

        String timeStart = DateFormatUtils.format(now, DateTimeConstants.DEFAULT_FORMAT);
        String timeEnd = DateFormatUtils.format(end, DateTimeConstants.DEFAULT_FORMAT);

        //now <start_time && start_time - 15 < now, 即end > start_time
        Wrapper<SeckillProduct> wrapper = new EntityWrapper<SeckillProduct>()
                .between("start_time", timeStart, timeEnd)
                .orderBy("id desc limit " + limit);
        if (id != -1) {
            wrapper.lt("id", id);
        }
        return productsMapper.selectList(wrapper);
    }

    public void setAndGetProductInfo(long id) {
        SeckillProduct seckillProduct = productsMapper.selectById(id);
        stringCache.set(SeckillRedisConstant.getSeckillKey(id), JSONObject.toJSONString(seckillProduct), 10);
        String productInfoJson = stringCache.get(SeckillRedisConstant.getSeckillKey(id));
        SeckillProduct seckillProductFromRedis = JSONObject.parseObject(productInfoJson, SeckillProduct.class);
        log.info(productInfoJson + " " + JSONObject.toJSONString(seckillProductFromRedis));
    }
}
