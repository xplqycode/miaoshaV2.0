package com.pxu.service.impl;

import com.pxu.WebApplication;
import com.pxu.redis.RedisStringCache;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pxu31@qq.com
 * @date 2020/7/30 14:43
 */
@Slf4j
@SpringBootTest(classes = WebApplication.class)
public class RedisTest {
    @Autowired
    SeckillProductService productService;
    //
    @Autowired
    RedisStringCache stringCache;

    @Test
    void testZset(){
        stringCache.zAdd("zset_test", "1001", 1);
        stringCache.zAdd("zset_test", "1002", 2);
        stringCache.zAdd("zset_test", "1003", 3);
        stringCache.zAdd("zset_test", "1004", 4);
        stringCache.zAdd("zset_test", "1005", 5);
        System.out.println(stringCache.getObjectIdKeyList("zset_test", 3));

    }
}
