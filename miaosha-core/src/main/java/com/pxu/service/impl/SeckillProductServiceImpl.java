package com.pxu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pxu.constant.SeckillRedisConstant;
import com.pxu.domain.SeckillOrder;
import com.pxu.domain.SeckillProduct;
import com.pxu.persistence.SeckillOrderMapper;
import com.pxu.persistence.SeckillProductsMapper;
import com.pxu.redis.RedisStringCache;
import com.pxu.redis.constants.ExpireTimeConstant;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        String productInfoJson = stringCache.get(SeckillRedisConstant.getSeckillKey(id));
        if (StringUtils.isEmpty(productInfoJson)) {
            log.info("未查到redis");
            SeckillProduct seckillProductFromDb = productsMapper.selectById(id);
            if (seckillProductFromDb != null) {
                log.info("数据库查到了");
                stringCache.set(SeckillRedisConstant.getSeckillKey(id), JSONObject.toJSONString(seckillProductFromDb), ExpireTimeConstant.ONE_MINUTE_EXPIRETIME);
                log.info("放进去redis");
                return seckillProductFromDb;
            }
            return null;
        }

        SeckillProduct seckillProduct = null;
        try {
            log.info("redis查到了");
            seckillProduct = JSONObject.parseObject(productInfoJson, SeckillProduct.class);
        } catch (Exception e) {
            return null;
        }

        return seckillProduct;
    }

    @Override
    public int reduceNumber(long seckillId) {
        return productsMapper.reduceOneProduct(seckillId);
    }

    public void setAndGetProductInfo(long id) {
        SeckillProduct seckillProduct = productsMapper.selectById(id);
        stringCache.set(SeckillRedisConstant.getSeckillKey(id), JSONObject.toJSONString(seckillProduct), 10);
        String productInfoJson = stringCache.get(SeckillRedisConstant.getSeckillKey(id));
        SeckillProduct seckillProductFromRedis = JSONObject.parseObject(productInfoJson, SeckillProduct.class);
        log.info(productInfoJson + " " + JSONObject.toJSONString(seckillProductFromRedis));
    }
}
