package com.pxu.delayqueue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 用于往zset中添加元素
 * @author pxu31@qq.com
 * @date 2020/7/31 20:17
 */
@Data
@AllArgsConstructor
public class ScoredSortedItem {

    /**
     * 延迟任务的唯一标识-value
     */
    private String delayQueueJodId;

    /**
     * 任务的执行时间, 放时间戳-score
     */
    private double delayTime;

    public static void main(String[] args) {
        double cur = System.currentTimeMillis();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        System.out.println(cur < now.getTime());
    }

}
