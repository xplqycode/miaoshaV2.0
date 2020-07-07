package com.pxu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pxu.domain.SeckillProduct;
import com.pxu.persistence.BaseMapper;
import com.pxu.persistence.SeckillProductsMapper;
import com.pxu.service.SeckillProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:10
 */
@Service
public class SeckillProductServiceImpl implements SeckillProductService {

    @Autowired
    SeckillProductsMapper productsMapper;

    @Override
    public List<SeckillProduct> getAll() {
        return productsMapper.selectList(null);
    }

    @Override
    public SeckillProduct getOne(long id) {
        return productsMapper.selectById(id);
    }
}
