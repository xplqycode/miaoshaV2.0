package com.pxu.service;

import org.springframework.stereotype.Service;

/**
 * @author pxu31@qq.com
 * @date 2020/7/9 21:18
 */
@Service
public interface SeckillUserService {
    /**
     * 插入用户信息
     * @param passport
     * @param passpord
     * @param phoneNumber
     * @return
     */
    int insertUserInfo(String passport, String passpord, long phoneNumber);
}
