package com.pxu.service.impl;

import com.pxu.domain.SeckillProduct;
import com.pxu.service.SeckillProductService;
import com.pxu.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 21:20
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    SeckillProductService productService;

    @Override
    public SeckillProduct getOneFromDb(long id) {
        return productService.getOne(id);
    }

    @Override
    public List<SeckillProduct> getAllFromDb() {
        return productService.getAll();
    }
}
