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

import java.util.Date;

/**
 * @author pxu31@qq.com
 * @date 2020/7/29 16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seckill_black_list")
public class SeckillBlackList {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //用户账号
    @TableField(value = "passport")
    private String ip;

    //秒杀的商品id
    private Long productId;

    //状态标示：-1=无效，0=成功
    private Integer status;

    //该ip被列入黑名单的时间
    private Integer dateNum;

    private Date createTime;

    private Date updateTime;
}
