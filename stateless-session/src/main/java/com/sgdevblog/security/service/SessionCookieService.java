package com.sgdevblog.security.service;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sgdevblog.security.SessionCookieData;
import com.sgdevblog.security.util.SecurityUtil;

@Service
public class SessionCookieService {

	private Gson gson = new Gson();

	public static String SESSION_COOKIE_NAME = "session";
	public static String SIGN_COOKIE_NAME = "session_sign";

	@Value("${session.timeout.minute:30}")
	private int sessionTimeOut = 30;

	@Value("${session.secret}")
	private String secret;

	public Cookie generateSessionCookie(SessionCookieData cookieData, String domain){

		Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, gson.toJson(cookieData));
		sessionCookie.setPath("/");

		if (domain != null){
			sessionCookie.setDomain(domain);
		}

		return sessionCookie;
	}

	public SessionCookieData getSessionCookieData(Cookie sessionCookie){
		String sessionValue = sessionCookie.getValue();
		SessionCookieData sessionData = gson.fromJson(sessionValue, SessionCookieData.class);

		if ((System.currentTimeMillis() - sessionData.getTimeStamp().getTime()) > sessionTimeOut * 60000){
			return null;
		}

		return sessionData;
	}

	public Cookie generateSignCookie(Cookie sessionCookie){
		String sign = SecurityUtil.sign(sessionCookie.getValue(), secret);
		Cookie signCookie = new Cookie(SIGN_COOKIE_NAME, sign);

		if (sessionCookie.getDomain()!=null){
			signCookie.setDomain(sessionCookie.getDomain());
		}

		if (sessionCookie.getPath()!=null){
			signCookie.setPath(sessionCookie.getPath());
		}

		return signCookie;
	}
}
