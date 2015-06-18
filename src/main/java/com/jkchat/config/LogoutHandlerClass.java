package com.jkchat.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.jkchat.service.UserService;

@Component
public class LogoutHandlerClass extends SimpleUrlLogoutSuccessHandler {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private UserService userService;

	@Override
	public void onLogoutSuccess(HttpServletRequest arg0,
			HttpServletResponse arg1, Authentication arg2) throws IOException,
			ServletException {
		this.simpMessagingTemplate.convertAndSend("/queue/loggedOutUser",
				arg2.getName());
		super.onLogoutSuccess(arg0, arg1, arg2);
	}
}
