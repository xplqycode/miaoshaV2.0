package com.pxu.config.web.resp;

/**
 * @author pxu31@qq.com
 * @date 2020/7/29 20:19
 */
public enum RespCodeEnum {

    IP_OUTOF_LIMIT(10002, "当日IP限流");

    private int code;
    private String message;

    RespCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static void main(String[] args) {
        System.out.println(IP_OUTOF_LIMIT.getMessage());
    }
}
