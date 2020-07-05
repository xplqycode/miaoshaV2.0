package com.pxu.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 22:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("seckill_product")
public class SeckillProduct {

    @TableId(value = "id", type = IdType.AUTO)
    private long seckillId;

    private String name;

    private Integer number;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer version;

}
