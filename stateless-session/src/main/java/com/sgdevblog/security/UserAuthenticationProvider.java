package com.sgdevblog.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service(value="userAuthenticationProvider")
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Override
	public boolean supports(Class<?> authentication) {
		return (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		PreAuthenticatedAuthenticationToken token = (PreAuthenticatedAuthenticationToken) authentication;

		UserSession session = (UserSession)token.getPrincipal();

		if (session != null && session.getUser() != null){
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(new UserAuthentication(session));
			return new UserAuthentication(session);
		}

		throw new BadCredentialsException("Unknown user name or password");
	}

}
