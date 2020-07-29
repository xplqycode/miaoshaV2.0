package com.pxu.config.web;

import com.pxu.config.web.resp.RespCodeEnum;
import com.pxu.config.web.resp.RespDto;
import com.pxu.exception.RequestLimitException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author pxu31@qq.com
 * @date 2020/7/29 20:12
 */
@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(value =RuntimeException.class)
    @ResponseBody
    public Object exceptionHandler(Exception e){
        if(e instanceof RequestLimitException){
            return new RespDto(RespCodeEnum.IP_OUTOF_LIMIT);
        }
        return null;
    }
}
