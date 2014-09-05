package com.sgdevblog.security.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgdevblog.dao.SiteDAO;
import com.sgdevblog.dao.UserDAO;
import com.sgdevblog.model.User;
import com.sgdevblog.security.util.SecurityUtil;


@Service
@Transactional
public class AuthenticationService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDAO userDao;

	@Autowired
	SiteDAO siteDao;

	public User login(String siteCode, String login, String password) {
		boolean isPasswordValid = false;
		User user = userDao.getUser(siteCode, login);
		if(user!=null){
			try {
				isPasswordValid = user.getPassword().equals(SecurityUtil.hashPassword(password, user.getSalt()));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				logger.error("Error authenticating, " + login, e);
			}
		}

		if(user!=null && !isPasswordValid){
			user=null;
		}else if(user!=null && isPasswordValid){
			user.getUserRoles();
		}

		return user;
	}

}
