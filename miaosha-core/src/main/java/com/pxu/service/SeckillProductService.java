package com.pxu.service;

import com.pxu.domain.SeckillProduct;
import org.springframework.stereotype.Service;

import java.util.List;

/**处理数据库数据
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:09
 */
public interface SeckillProductService {

    List<SeckillProduct> getAll();

    SeckillProduct findById(long id);

    /**
     * 记录购买订单
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(long seckillId, long userPhone);
}
