package com.pxu.persistence;

import com.pxu.domain.SeckillProduct;
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
}
