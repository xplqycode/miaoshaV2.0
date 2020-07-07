package com.pxu.service;

import com.pxu.domain.SeckillProduct;

import java.util.List;

/**用于seckillCOntroller的一些服务支持
 * @author pxu31@qq.com
 * @date 2020/7/6 21:19
 */
public interface SeckillService {
    /**
     * 根据商品id查找商品
     *
     * @param id
     * @return
     */
    SeckillProduct getOneFromDb(long id);

    /**
     * 返回数据库所有商品信息
     * @return
     */
    List<SeckillProduct> getAllFromDb();

}
