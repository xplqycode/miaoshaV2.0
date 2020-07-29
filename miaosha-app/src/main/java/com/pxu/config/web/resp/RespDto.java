package com.pxu.config.web.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 全局响应结构
 * @author pxu31@qq.com
 * @date 2020/7/29 20:27
 */
@Data
@AllArgsConstructor
public class RespDto {

    public int code;

    public String data;

    private Date timestamp;

    public RespDto(RespCodeEnum respCodeEnum){
        this.code = respCodeEnum.getCode();
        this.data = respCodeEnum.getMessage();
        this.timestamp = new Date();
    }

}
