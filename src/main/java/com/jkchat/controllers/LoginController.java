package com.jkchat.controllers;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jebil Kuruvila
 */
@Controller
public class LoginController {

    private static final Logger logger = Logger
            .getLogger(LoginController.class);

    /**
     * @param res
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletResponse res) throws IOException {
        logger.debug("inside login method");
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            res.sendRedirect("home");
        }
        model.setViewName("login");
        logger.debug("end of login method");
        return model;
    }
}
