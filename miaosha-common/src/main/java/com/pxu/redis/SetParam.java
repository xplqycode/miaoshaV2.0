package com.pxu.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础结构元素k v time
 *
 * @author pxu31@qq.com
 * @date 2020/7/6 8:57
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SetParam {

    private String key;

    private String value;

    private long timeoutSeconds;

}
