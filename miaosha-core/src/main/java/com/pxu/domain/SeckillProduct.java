package com.pxu.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀库存表(SeckillProduct)表实体类
 *
 * @author makejava
 * @since 2020-07-09 21:02:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seckill_product")
public class SeckillProduct {
    //商品id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //商品名称
    private String name;

    //库存数量
    private Integer number;

    //秒杀开启时间
    private Date startTime;

    //秒杀结束时间
    private Date endTime;

    //创建时间
    private Date createTime;

    //创建时间
    private Date updateTime;

    //版本号
    private Integer version;
}