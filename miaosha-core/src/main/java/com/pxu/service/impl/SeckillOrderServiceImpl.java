package com.pxu.service.impl;

import com.pxu.domain.SeckillOrder;
import com.pxu.persistence.SeckillOrderMapper;
import com.pxu.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/7/9 21:18
 */
@Slf4j
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    SeckillOrderMapper orderMapper;

    @Override
    public int insertSuccessKilled(long seckillId, String passport) {
        return orderMapper.insert(new SeckillOrder()
                .setProductId(seckillId)
                .setPassport(passport));
    }

    @Override
    public List<SeckillOrder> getSeckillOrders(int offset, int limit) {
        return orderMapper.selectPage(new RowBounds(offset, limit), null);
    }
}
