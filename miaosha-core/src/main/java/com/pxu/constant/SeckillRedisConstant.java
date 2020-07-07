package com.pxu.constant;

import lombok.Data;

/**
 * seckill的一些key
 * @author pxu31@qq.com
 * @date 2020/7/7 21:18
 */
@Data
public class SeckillRedisConstant {

    private static final String BASE_KEY = "seckill_key:";

    public static final String getSeckillKey(long id) {
        return BASE_KEY + id;
    }

}
