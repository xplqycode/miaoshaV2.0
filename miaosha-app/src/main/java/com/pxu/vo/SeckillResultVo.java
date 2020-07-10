package com.pxu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装JSON返回的结果格式,用于ajax交互
 *
 * @auther TyCoding
 * @date 2018/10/8
 */
@Data
@AllArgsConstructor
public class SeckillResultVo<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillResultVo(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResultVo(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
