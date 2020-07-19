package redis.copy;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 8:59
 */
public interface CacheExceptionHandler {
    void handle(Exception e, String... keys);
}
