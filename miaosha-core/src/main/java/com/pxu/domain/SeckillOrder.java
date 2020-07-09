package com.pxu.domain;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 秒杀系统订单表(SeckillOrder)表实体类
 *
 * @author makejava
 * @since 2020-07-09 21:01:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seckill_order")
public class SeckillOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //订单id
    private String orderId;

    //用户账号
    @TableField(value = "passport")
    private String passport;

    //秒杀的商品id
    private Long productId;

    //状态标示：-1=无效，0=成功
    private Integer status;

    private Date createTime;

    private Date updateTime;

}