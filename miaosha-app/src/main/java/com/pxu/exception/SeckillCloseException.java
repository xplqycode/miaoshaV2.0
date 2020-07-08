package com.pxu.exception;

/**
 * 秒杀关闭异常
 * 秒杀完毕就不应该执行秒杀了
 *
 * @author 徐鹏
 */
public class SeckillCloseException extends SeckillException {

	public SeckillCloseException(String message) {
		super(message);
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

}
