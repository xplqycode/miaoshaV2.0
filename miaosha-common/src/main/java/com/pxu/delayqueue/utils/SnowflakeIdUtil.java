package com.pxu.delayqueue.utils;

/**
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
public class SnowflakeIdUtil {

    private static SnowflakeIdWorker snowflakeIdWorker;

    public SnowflakeIdUtil(long workerId, long dataCenterId){
        snowflakeIdWorker = new SnowflakeIdWorker(workerId,dataCenterId);
    }

    public String nextId(){
        return String.valueOf(snowflakeIdWorker.nextId());
    }

    public static void main(String[] args) {
        SnowflakeIdUtil snowflakeIdUtil = new SnowflakeIdUtil(5, 6);
        for (int i = 0; i < 20; i++) {

            System.out.println(snowflakeIdUtil.nextId());
        }
    }
}
