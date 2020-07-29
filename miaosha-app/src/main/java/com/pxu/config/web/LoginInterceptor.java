package com.pxu.config.web;
import com.alibaba.fastjson.JSONObject;
import com.pxu.exception.RequestLimitException;
import com.pxu.redis.RedisStringCache;
import com.pxu.redis.constants.ExpireTimeConstant;
import com.pxu.util.IPAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登录拦截器
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

	public static final String REQ_COUNTS = "req_counts_";

	public static final String IP_BLOCKED = "ip_blocked_";

	@Autowired
	RedisStringCache stringCache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("我是拦截器");
		String remoteAddr = request.getRemoteAddr();
		Cookie[] cookies = request.getCookies();
		Map<String, String[]> parameterMap = request.getParameterMap();
		HttpSession session = request.getSession();
		log.info("add:{}", remoteAddr);
		log.info("parameterMap:{}", JSONObject.toJSONString(parameterMap));
		log.info("cookies:{}", JSONObject.toJSONString(cookies));
		log.info("session:{}", JSONObject.toJSONString(session));
		return true;
	}

	public void urlHandle(HttpServletRequest request, long limitTime,int limitCount) throws RequestLimitException {
		try {
			String ip = IPAddressUtil.getClientIpAddress(request);
			String url = request.getRequestURL().toString();
			String key = REQ_COUNTS + url + ip;
            String ipBlockKey = IP_BLOCKED + ip;

			//若在当日黑名单中，直接返回
            if(stringCache.exists(ipBlockKey)){
                log.info("ip: {} 已经被封禁", ip);
                throw new RequestLimitException("该ip秒杀超过限制次数");
            }

			int curCount = stringCache.getIntValue(key);
			if(curCount == -1){
				//设置当前时间开始 limitTime时间内限流
				stringCache.initIntValue(key, limitTime);
			}

			if(!stringCache.maybeExpired(key)){
				stringCache.increment(key,1);
			}else{
				if (curCount + 1 > limitCount){

                    //设置缓存ip黑名单标志位，失效时间设为1day，定时任务中，遍历前一天的dateNum，remove所有的标识位表示解封
                    stringCache.set(ipBlockKey, "1", ExpireTimeConstant.ONE_DAY_EXPIRETIME);
                    //超过限定次数了, 加入黑名单, 入库
					throw new RequestLimitException("该ip秒杀超过限制次数");
				}
			}
		} catch (RequestLimitException e) {
			throw e;
		} catch (Exception e) {
			log.error("发生异常: ", e);
		}
	}
}
