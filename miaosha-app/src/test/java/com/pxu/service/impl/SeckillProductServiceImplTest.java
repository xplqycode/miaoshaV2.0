package com.pxu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pxu.WebApplication;
import com.pxu.domain.SeckillProduct;
import com.pxu.redis.RedisStringCache;
import com.pxu.service.SeckillProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:13
 */
@SpringBootTest(classes = WebApplication.class)
public class SeckillProductServiceImplTest {

    @Autowired
    SeckillProductServiceImpl productService;

    @Autowired
    RedisStringCache stringCache;
    @Test
    void list() {
        System.out.println(productService);
        List<SeckillProduct> list = productService.getAll();
        if(list == null){
            System.out.println("null");
        }
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    void testRedis(){
        stringCache.set("testKey", "ceshi", 10);
        System.out.println(stringCache.get("testKey"));
    }

    @Test
    void testRedisString2Obj(){
        productService.setAndGetProductInfo(1000);
    }

    @Test
    void testRedisString2Obj2(){
        System.out.println(JSONObject.toJSONString(productService.findById(1000)));
    }
}