package com.pxu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pxu.WebApplication;
import com.pxu.domain.SeckillProduct;
import com.pxu.persistence.SeckillOrderMapper;
import com.pxu.persistence.SeckillProductsMapper;
import com.pxu.persistence.SeckillUserMapper;
import com.pxu.redis.impl.RedisStringKeyCache;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:13
 */
@Slf4j
@SpringBootTest(classes = WebApplication.class)
public class SeckillProductServiceImplTest {

    @Autowired
    SeckillProductService productService;
//
    @Autowired
RedisStringKeyCache stringCache;
//    @Test
//    void list() {
//        System.out.println(productService);
//        List<SeckillProduct> list = productService.getAll();
//        if(list == null){
//            System.out.println("null");
//        }
//        System.out.println(JSONObject.toJSONString(list));
//    }
//
//    @Test
//    void testRedis(){
//        stringCache.set("testKey", "ceshi", 10);
//        System.out.println(stringCache.get("testKey"));
//    }
//
//    @Test
//    void testRedisString2Obj(){
////        productService.setAndGetProductInfo(1000);
//    }
//
//    @Test
//    void testRedisString2Obj2(){
//        System.out.println(JSONObject.toJSONString(productService.findById(1000)));
//    }
//
//    @Test
//    void testLog(){
//        log.info("日志");
//        log.error("ceshi");
//    }
    @Autowired
    SeckillOrderMapper orderMapper;
    @Autowired
    SeckillProductsMapper productsMapper;
    @Autowired
    SeckillUserMapper userMapper;

    @Test
    void testMapper(){
//        List<SeckillProduct> seckillProducts = productsMapper.selectList(null);
//        System.out.println(JSONObject.toJSONString(seckillProducts));
//        Integer insert = orderMapper.insert(new SeckillOrder().setPassport("pxu31@qq.com").setProductId(1123L).setOrderId("sadasd").setStatus(1).setCreateTime(new Date()));
//        System.out.println(insert);
        productsMapper.reduceOneProduct(1000);
    }

    @Test
    void testRedisInitOrIncr(){
        stringCache.delete("foo");
        stringCache.set("foo", "asd", 10);
        System.out.println(stringCache.get("foo"));
        System.out.println(stringCache.initOrIncrement("pxytest2", 1L, 100));
        System.out.println(stringCache.initOrIncrement("pxytest2", 1L, 100));
        System.out.println(stringCache.initOrIncrement("pxytest2", 1L, 100));
        System.out.println(stringCache.initOrIncrement("pxytest2", 1L, 1));
        String pxutest = stringCache.get("pxutest2");
        System.out.println(stringCache.getExpired("pxutest2"));
        System.out.println(stringCache.getExpired("pxuasdadtest2"));
        System.out.println(stringCache.getExpired("foo"));


        stringCache.set("xptest", "0", 100);
        stringCache.increment("xptest", 2);
        System.out.println(stringCache.getIntValue("xptest"));

    }

    @Test
    void testFindByRedisAndDb(){
        System.out.println(productService.findById(1000L));
    }

    @Test
    void hotList(){
        List<SeckillProduct> seckillProducts = productService.hotListFromDb(2, -1);
        log.info(JSONObject.toJSONString(seckillProducts));
    }

    @Test
    void testRedis(){
    }
}