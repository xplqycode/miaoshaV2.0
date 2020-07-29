package com.pxu.exception;

import lombok.NoArgsConstructor;

/**
 * @author pxu31@qq.com
 * @date 2020/7/29 16:47
 */
@NoArgsConstructor
public class RequestLimitException extends SeckillException {

    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException(String message, Throwable cause) {
        super(message, cause);
    }

}
