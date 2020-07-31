package com.pxu.delayqueue;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Data
@AllArgsConstructor
public class ScoredSortedItem {

    /**
     * 任务的执行时间
     */
    private long delayTime;

    /**
     * 延迟任务的唯一标识
     */
    private long delayQueueJodId;
}
