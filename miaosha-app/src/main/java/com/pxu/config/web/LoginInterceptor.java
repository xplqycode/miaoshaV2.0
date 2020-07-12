package com.pxu.config.web;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录拦截器
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

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

}
