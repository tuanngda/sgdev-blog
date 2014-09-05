package com.sgdevblog.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.sgdevblog.model.User;
import com.sgdevblog.security.UserAuthentication;
import com.sgdevblog.security.UserSession;
import com.sgdevblog.security.annotation.SessionUpdate;
import com.sgdevblog.security.service.AuthenticationService;
import com.sgdevblog.security.service.SessionCookieService;


@Controller
@RequestMapping("/auth")
public class AuthenticationController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationService authService;

	@Autowired
	private SessionCookieService sessionCookieService;

	/**
	 * Temporary created to simulate site selection(multi-tenancy). To be remove once multi-tenancy is implemented.
	 * @param login
	 * @param password
	 * @param site
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "login")
	@SessionUpdate
	public String login(@RequestParam(value="login") String login, @RequestParam(value="password") String password, @RequestParam(value="siteCode") String site) throws IOException{

		if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)){
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Missing login and password");
		}

		logger.info("login user(" + login + "), siteCode(" + site + ").");

		User user = authService.login(site, login, password);
		if(user!=null){
			SecurityContext securityContext = SecurityContextHolder.getContext();
			UserSession userSession = new UserSession();
			userSession.setSite(user.getSite());
			userSession.setUser(user);
			securityContext.setAuthentication(new UserAuthentication(userSession));
		}else{
			throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Invalid login or password");
		}

		return "index";
	}

	/**
	 * This endpoint checks that the user is authenticated before redirecting to the index page
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "")
	public String landing(HttpServletRequest request, HttpServletResponse response){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		if (authentication instanceof UserAuthentication && authentication.isAuthenticated()){
			logger.info("User(" + securityContext.getAuthentication().getPrincipal() + ") is logged in.");
			return "index";
		}

		logger.info("User(" + securityContext.getAuthentication().getPrincipal() + ") is redirected to login.");
		return "login";
	}

	/**
	 * This endpoint will return a login form view
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = {"logout"})
	@SessionUpdate
	public String logout(HttpServletRequest request, HttpServletResponse response){
		SecurityContext securityContext = SecurityContextHolder.getContext();

		logger.info("Logging out user(" + securityContext.getAuthentication().getPrincipal()  + ").");

		securityContext.setAuthentication(new UserAuthentication(null));

		return "login";
	}
}
