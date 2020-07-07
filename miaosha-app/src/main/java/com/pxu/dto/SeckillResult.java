package com.pxu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装JSON返回的结果格式
 *
 * @auther TyCoding
 * @date 2018/10/8
 */
@Data
@AllArgsConstructor
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
