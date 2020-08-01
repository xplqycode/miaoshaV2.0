package com.pxu.config.web;

import com.pxu.exception.RequestLimitException;
import com.pxu.redis.impl.RedisStringKeyCache;
import com.pxu.redis.constants.ExpireTimeConstant;
import com.pxu.util.IPAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理ip防刷
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public static final String REQ_COUNTS = "req_counts_";

    public static final String IP_BLOCKED = "ip_blocked_";

    @Autowired
    RedisStringKeyCache stringCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            urlHandle(request, ExpireTimeConstant.TEN_SECOND_EXPIRETIME, 5);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public void urlHandle(HttpServletRequest request, long limitTime, int limitCount) throws Exception {
        try {
            String ip = IPAddressUtil.getClientIpAddress(request);
            String url = request.getRequestURL().toString();
            String ipCountsKey = REQ_COUNTS + url + ip;
            String ipBlockKey = IP_BLOCKED + ip;

            //若在当日黑名单中，直接返回
            if (stringCache.exists(ipBlockKey)) {
                log.info("ip: {} 已经被封禁", ip);
                throw new RequestLimitException("该ip秒杀超过限制次数");
            }

            int curCount = stringCache.getIntValue(ipCountsKey);
            if (curCount == -1) {
                //初始化限流的ipCountsKey
                //，limitTime时间内限流
                log.info("初始化待限流的key，失效时间为{}", limitTime);
                stringCache.initIntValue(ipCountsKey, limitTime);
            }

            //防止即将过期时候
            if (!stringCache.maybeExpired(ipCountsKey)) {
                stringCache.increment(ipCountsKey, 1);
                log.info("加一次之后为{}", curCount + 1);
            }

            if (curCount + 1 > limitCount) {
                //设置缓存ip黑名单标志位，失效时间设为1day，定时任务中，遍历前一天的dateNum，remove所有的标识位表示解封
                log.info("ip：{}为异常秒杀ip，设置异常缓存表示位", ip);
                stringCache.set(ipBlockKey, "1", ExpireTimeConstant.ONE_DAY_EXPIRETIME);

                //超过限定次数了, 加入黑名单, 入库，定时任务遍历来解封ip
                //或者通过直接设置当天时间剩余s数为失效时间来禁某用户ip
                //todo

                throw new RequestLimitException("该ip秒杀超过限制次数");
            }
        }catch (Exception e){
            throw e;
        }

    }
}
