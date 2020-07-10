package com.pxu.service;

import com.pxu.domain.SeckillOrder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/7/9 21:18
 */
public interface SeckillOrderService {
    /**
     * 插入用户购买订单
     * @param seckillId
     * @param passport
     * @return
     */
    int insertSuccessKilled(long seckillId, String passport);

    /**
     * 分页查询订单数量
     * @param offset
     * @param limit
     * @return
     */
    List<SeckillOrder> getSeckillOrders(int offset, int limit);

    /**
     * 查询订单
     * @param seckillId
     * @param passport
     * @return
     */
    SeckillOrder queryByIdAndPassport(long seckillId, String passport);
}
