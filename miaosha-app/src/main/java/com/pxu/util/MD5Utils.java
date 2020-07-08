package com.pxu.util;

import org.springframework.util.DigestUtils;

/**
 * @author pxu31@qq.com
 * @date 2020/7/8 9:18
 */
public class MD5Utils {
    // md5盐值字符串，用于混淆MD5
    private static final String SLAT = "skdfjksjdf7787%^%^%^FSKJFK*(&&%^%&^8DF8^%^^*7hFJDHFJ";

    /**
     * 生成md5
     *
     * md5本质上就是对任意一个字符串产生特定长度的编码,不可逆,即使知道结果,用户也不知道之前字符串.
     * 如果只对seckinnId作md5,用户可以根据特定的算法跑出来特定的数值.
     * 那么拼接之后,用户不知道拼接规则和盐值,是很难猜测出来的
     * 加上盐值字符串slat
     * @param seckillId
     * @return
     */
    public static String getMD5(long seckillId) {
        String base = seckillId + "/" + SLAT;
        //spring工具类,将string转为二进制类型
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
