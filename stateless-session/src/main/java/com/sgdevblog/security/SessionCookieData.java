package com.sgdevblog.security;

import java.util.Date;


public class SessionCookieData {

	private Integer userId;

	private Integer siteId;

	private Date timeStamp;

	public SessionCookieData() {
		this(0,0);
	}

	public SessionCookieData(Integer userId, Integer siteId) {
		super();
		this.userId = userId;
		this.siteId = siteId;
		this.timeStamp = new Date();
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}
}
