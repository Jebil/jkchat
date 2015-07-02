package com.jkchat.authentication;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.jkchat.models.User;
import com.jkchat.service.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	UserService userService;

	private static final Logger logger = Logger
			.getLogger(CustomAuthenticationProvider.class);

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		logger.debug("Inside authenticate function.");
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		User user = userService.getUserDetails(name);
		if (null == user || !(BCrypt.checkpw(password, user.getPassword()))) {
			throw new BadCredentialsException("Bad Credentials");
		}
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(name,
				password, grantedAuths);
		logger.debug("end of authenticate function.");
		return auth;
	}

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
