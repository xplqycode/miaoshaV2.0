package com.pxu.persistence;

import com.pxu.domain.SeckillProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:01
 */

/**
 * 注解使用的的是Responsitory 而不是mapper
 */
@Repository
public interface SeckillProductsMapper extends BaseMapper<SeckillProduct> {

    /**
     * 减少库存
     * @param id
     * @return
     */
    @Update("update seckill_product set number = number - 1 where id = #{id}")
    int reduceOneProduct(@Param("id") long id);

}
