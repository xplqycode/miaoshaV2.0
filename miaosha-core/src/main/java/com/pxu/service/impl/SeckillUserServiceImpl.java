package com.pxu.service.impl;

import com.pxu.domain.SeckillUser;
import com.pxu.persistence.SeckillUserMapper;
import com.pxu.service.SeckillUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息表
 *
 * @author pxu31@qq.com
 * @date 2020/7/9 21:19
 */
@Slf4j
@Service
public class SeckillUserServiceImpl implements SeckillUserService {

    @Autowired
    SeckillUserMapper userMapper;

    @Override
    public int insertUserInfo(String passport, String passpord, long phoneNumber) {
        return userMapper.insert(new SeckillUser().setPassport(passport).setPassword(passpord).setPhoneNumber(phoneNumber));
    }
}
