package com.sgdevblog.security;

import com.sgdevblog.model.Site;
import com.sgdevblog.model.User;

public class UserSession {

	private User user;

	private Site site;

	public SessionCookieData generateSessionCookieData(){

		return new SessionCookieData(user.getId(), site.getId());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
}
