package com.pxu.exception;

import lombok.NoArgsConstructor;

/**
 * 秒杀相关业务异常
 * 
 * @author 徐鹏
 */
@NoArgsConstructor
public class SeckillException extends RuntimeException {

	public SeckillException(String message) {
		super(message);
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

}
