package com.pxu.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 21:56
 */
@Mapper
public interface BaseMapper<T> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {
    // 这里可以放一些公共的方法
    //...
}
