package com.pxu.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 21:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("seckill_order")
public class SeckillOrder {
    @TableId(value = "order_id", type = IdType.AUTO)
    private long orderId;

    private long userId;

    private long userPhone;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    public SeckillOrder(long seckillId, long userPhone) {
        this.orderId = seckillId;
        this.userPhone = userPhone;
    }
}
