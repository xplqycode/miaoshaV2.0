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
 * 秒杀成功明细表(SeckillUser)表实体类
 *
 * @author makejava
 * @since 2020-07-09 21:02:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seckill_user")
public class SeckillUser {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //通行证
    private String passport;

    //密码
    private String password;

    //手机号码
    private Long phoneNumber;

    private Date createTime;

    private Date updateTime;
}