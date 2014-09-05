package com.sgdevblog.security;

public class UserCredential {

	private String userName;

	private String password;

	private String siteCode;

	public UserCredential(String userName, String password, String siteCode) {
		super();
		this.userName = userName;
		this.password = password;
		this.siteCode = siteCode;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getSiteCode() {
		return siteCode;
	}

}
