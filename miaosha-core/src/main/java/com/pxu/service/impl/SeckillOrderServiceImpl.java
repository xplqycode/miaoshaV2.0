package com.pxu.service.impl;

import com.pxu.domain.SeckillOrder;
import com.pxu.persistence.SeckillOrderMapper;
import com.pxu.redis.RedisStringCache;
import com.pxu.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 主要用于订单表的数据处理
 *
 * @author pxu31@qq.com
 * @date 2020/7/9 21:18
 */
@Slf4j
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    SeckillOrderMapper orderMapper;

    @Autowired
    RedisStringCache stringCache;

    @Override
    public int insertSuccessKilled(long seckillId, String passport) {
        //插入提现记录表
        String curTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        String orderId = curTime + stringCache.initOrIncrement(curTime, 1, 2);
        int rtn = 0;
        try {
            rtn = orderMapper.insert(new SeckillOrder()
                    .setProductId(seckillId)
                    .setPassport(passport)
                    .setOrderId(orderId)
                    .setStatus(1)
                    .setCreateTime(new Date()));
        } catch (Exception e) {
            log.info("重复秒杀，product_id={}, passport={}", seckillId, passport);
        }
        return rtn;
    }

    @Override
    public List<SeckillOrder> getSeckillOrders(int offset, int limit) {
        return orderMapper.selectPage(new RowBounds(offset, limit), null);
    }

    @Override
    public SeckillOrder queryByIdAndPassport(long seckillId, String passport) {
        return orderMapper.selectOne(new SeckillOrder().setId(seckillId).setPassport(passport + ""));
    }
}
