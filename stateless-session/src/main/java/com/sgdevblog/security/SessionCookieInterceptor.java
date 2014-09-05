package com.sgdevblog.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sgdevblog.security.annotation.SessionUpdate;
import com.sgdevblog.security.service.SessionCookieService;


@Component
public class SessionCookieInterceptor extends HandlerInterceptorAdapter {

	@Value("${domain.default}")
	private String defaultDomain;

	@Autowired
	private SessionCookieService sessionCookieService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			SessionUpdate sessionUpdateAnnotation = handlerMethod.getMethod().getAnnotation(SessionUpdate.class);

			if (sessionUpdateAnnotation == null){
				SecurityContext context = SecurityContextHolder.getContext();
				if (context.getAuthentication() instanceof UserAuthentication && response instanceof HttpServletResponse){
					UserAuthentication userAuthentication = (UserAuthentication)context.getAuthentication();
					UserSession session = (UserSession) userAuthentication.getDetails();
					persistSessionCookie(response, session);
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			SessionUpdate sessionUpdateAnnotation = handlerMethod.getMethod().getAnnotation(SessionUpdate.class);

			if (sessionUpdateAnnotation != null){
				SecurityContext context = SecurityContextHolder.getContext();
				if (context.getAuthentication() instanceof UserAuthentication && response instanceof HttpServletResponse){
					UserAuthentication userAuthentication = (UserAuthentication)context.getAuthentication();
					UserSession session = (UserSession) userAuthentication.getDetails();
					persistSessionCookie(response, session);
				}
			}
		}
	}

	public void persistSessionCookie(HttpServletResponse response, UserSession session) {
		SessionCookieData sessionCookieData = (session!=null) ? session.generateSessionCookieData() : new SessionCookieData(0, 0);

		String domain = defaultDomain;

		Cookie sessionCookie = sessionCookieService.generateSessionCookie(sessionCookieData, domain);
		Cookie signCookie = sessionCookieService.generateSignCookie(sessionCookie);

		if (response instanceof HttpServletResponse){
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.addCookie(sessionCookie);
			httpResponse.addCookie(signCookie);
		}
	}

}
