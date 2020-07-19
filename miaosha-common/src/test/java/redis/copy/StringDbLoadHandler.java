package redis.copy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 8:58
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StringDbLoadHandler{
    private Callback<String> callback;
    private long timeoutSeconds;
}
