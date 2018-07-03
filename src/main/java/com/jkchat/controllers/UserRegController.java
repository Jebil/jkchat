package com.jkchat.controllers;

import com.jkchat.models.User;
import com.jkchat.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jebil Kuruvila
 */
@Controller
public class UserRegController {
    private static final Logger logger = Logger
            .getLogger(UserRegController.class);
    @Autowired
    UserService userService;
    @Autowired
    @Qualifier("userValidator")
    private Validator validator;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * @param binder
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * @param res
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getRegisterView(HttpServletResponse res)
            throws IOException {
        logger.debug("inside register method");
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            res.sendRedirect("admin");
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("register");
        model.addObject("userModel", new User());
        logger.debug("end of register method");
        return model;
    }

    /**
     * @param userModel
     * @param result
     * @param req
     * @param model
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("userModel") User userModel,
                           BindingResult result, HttpServletRequest req, Model model) {
        validator.validate(userModel, result);
        if (result.hasErrors()) {
            return "register";
        }
        if (userService.addUser(userModel)) {
            this.simpMessagingTemplate.convertAndSend("/queue/users",
                    userModel.getName());
            return "redirect:login";
        } else {
            return "redirect:register?error";
        }
    }
}
