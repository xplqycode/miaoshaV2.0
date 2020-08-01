package com.pxu.delayqueue.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 延迟任务
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Data
public class DelayQueueJob {

    /**
     * 延迟任务的唯一标识，用于检索任务
     */
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 任务类型（具体业务类型）
     */
    private String topic;

    /**
     * 任务的执行时间(seconds)
     */
    private long delayTime;

    /**
     * 任务的执行超时时间
     */
    private long ttlTime;

    /**
     * 任务具体的消息内容，用于处理具体业务逻辑用
     */
    private String message;

}
