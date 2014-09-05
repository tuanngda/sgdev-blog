package com.sgdevblog.security;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import com.sgdevblog.constant.ParamName;
import com.sgdevblog.security.service.SessionCookieService;
import com.sgdevblog.security.service.UserSessionService;
import com.sgdevblog.security.util.SecurityUtil;

@Component(value="cookieSessionFilter")
public class CookieSessionFilter extends AbstractPreAuthenticatedProcessingFilter {

	private String SESSION_COOKIE_ATTRIBUTE = "session_attr";

	@Autowired
	private SessionCookieService sessionCookieService;

	@Autowired
	private UserSessionService userSessionService;

	@Value("${session.secret}")
	private String secret;

	@Autowired
	public CookieSessionFilter(@Qualifier(value="authenticationManager") AuthenticationManager authenticationManager) {
		setAuthenticationManager(authenticationManager);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		SecurityContext securityContext = extractSecurityContext(request);

		if (securityContext.getAuthentication()!=null && securityContext.getAuthentication().isAuthenticated()){
			UserAuthentication userAuthentication = (UserAuthentication) securityContext.getAuthentication();
			UserSession session = (UserSession) userAuthentication.getDetails();
			SecurityContextHolder.setContext(securityContext);
			return session;
		}

		return new UserSession();
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		String login = request.getParameter(ParamName.LOGIN);
		String password = request.getParameter(ParamName.PASSWORD);
		String siteCode = extractSiteCode(request);

		return new UserCredential(login, password, siteCode);
	}

	public String extractSiteCode(HttpServletRequest request) {
		//It suppose to be domain but for localhost testing, can take from request param
		return request.getParameter(ParamName.PASSWORD);
	}

	public SecurityContext extractSecurityContext(ServletRequest request) {
		if (request instanceof HttpServletRequest && request.getAttribute(SESSION_COOKIE_ATTRIBUTE) == null) {

			HttpServletRequest httpRequest = (HttpServletRequest) request;

			String path = httpRequest.getContextPath();
			if (path.indexOf("login")>=0 || path.indexOf("logout")>=0){
				return SecurityContextHolder.createEmptyContext();
			}

			Cookie[] cookies = httpRequest.getCookies();

			Cookie sessionCookie = null;
			Cookie signCookie = null;

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(SessionCookieService.SESSION_COOKIE_NAME)) {
						sessionCookie = cookie;
					}
					if (cookie.getName().equals(SessionCookieService.SIGN_COOKIE_NAME)) {
						signCookie = cookie;
					}
				}
			}

			if (sessionCookie != null && signCookie != null) {
				if (SecurityUtil.sign(sessionCookie.getValue(), secret).equals(signCookie.getValue())) {
					SessionCookieData sessionData = sessionCookieService.getSessionCookieData(sessionCookie);
					UserSession userSession = userSessionService.getUserSession(sessionData);
					request.setAttribute(SESSION_COOKIE_ATTRIBUTE, userSession);

					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					securityContext.setAuthentication(new UserAuthentication(userSession));
					return securityContext;
				}
			}
		}
		return SecurityContextHolder.createEmptyContext();
	}

}
