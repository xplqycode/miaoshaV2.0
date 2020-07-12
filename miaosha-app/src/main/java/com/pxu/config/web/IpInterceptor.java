package com.pxu.config.web;

import com.pxu.redis.RedisStringCache;
import com.pxu.redis.constants.ExpireTimeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**拦截ip地址次数
 * @author pxu31@qq.com
 * @date 2020/7/10 14:01
 */
public class IpInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisStringCache stringCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        stringCache.initOrIncrement(remoteAddr, 1, ExpireTimeConstant.FIVE_MINUTE_EXPIRETIME);
        String acessTimes = stringCache.get(remoteAddr);
        int number = 0;
        try {
            number = Integer.parseInt(acessTimes);
        } catch (NumberFormatException e) {
            return true;
        }
        return number <= 10;
    }
}
