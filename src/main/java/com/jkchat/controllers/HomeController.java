package com.jkchat.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jkchat.service.UserService;

/**
 * @author Jebil Kuruvila
 *
 */
@Controller
public class HomeController {
	private static final Logger LOGGER = Logger.getLogger(HomeController.class);
	@Autowired
	UserService userService;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@Autowired
	PersistentTokenRepository pTr;

	/**
	 * @param res
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletResponse res,
			HttpServletRequest req) throws IOException {
		LOGGER.debug("inside Home method");
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (sessionRegistry.getAllSessions(auth.getPrincipal(), false)
				.isEmpty()) {
			sessionRegistry.registerNewSession(req.getSession().getId(),
					auth.getPrincipal());
		}
		req.getSession().setAttribute("userName", auth.getName());
		List<String> list = userService.getAllOtherNames(auth.getName());
		model.setViewName("home");
		model.addObject("userList", list);
		model.addObject("me", auth.getName());
		LOGGER.debug("end of Home method");
		return model;
	}

	/**
	 * @param req
	 */
	@RequestMapping(value = "/forceSessionClose", method = RequestMethod.POST)
	public void forceLogout(HttpServletRequest req) {
		req.getSession().invalidate();
	}
}
