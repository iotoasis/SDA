package com.pineone.icbms.sda.comm.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info(
				"======================================          START         ======================================");
		log.info(" Request URI \t:  " + request.getRequestURI());

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info(
				"======================================           END          ======================================\n");
	}
}