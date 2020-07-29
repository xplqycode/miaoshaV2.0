package com.pxu.service;

import com.pxu.domain.SeckillProduct;

import java.util.List;

/**处理数据库数据
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:09
 */
public interface SeckillProductService {

    /**
     * 查询所有商品信息
     * @return
     */
    List<SeckillProduct> getAll();

    /**
     * 通过id来查询商品信息结合缓存查询
     * @param id
     * @return
     */
    SeckillProduct findById(long id);

    /**
     * 减少订单数量
     * @param seckillId
     * @return
     */
    int reduceNumber(long seckillId);

    /**
     * 分页查询方法开始秒杀时间在十五分钟以内的热点商品信息
     * @param limit
     * @param id
     * @return
     */
    List<SeckillProduct> hotListFromDb(int limit, long id);
}
