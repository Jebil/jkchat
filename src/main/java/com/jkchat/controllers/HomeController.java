package com.jkchat.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jkchat.service.UserService;

@Controller
public class HomeController {
	private static final Logger LOGGER = Logger.getLogger(HomeController.class);
	@Autowired
	UserService userService;

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletResponse res) throws IOException {
		LOGGER.debug("inside Home method");
		ModelAndView model = new ModelAndView();
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		List<String> list = userService.getAllOtherNames(myName);
		model.setViewName("home");
		model.addObject("userList", list);
		model.addObject("me", myName);
		LOGGER.debug("end of Home method");
		return model;
	}
}
