package com.jkchat.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jkchat.models.User;
import com.jkchat.service.UserService;

@Component(value = "userValidator")
public class UserValidator implements Validator {
	@Autowired
	UserService userService;
	private static final Logger logger = Logger.getLogger(UserValidator.class);

	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object arg0, Errors arg1) {
		logger.debug("Inside validator method");
		ValidationUtils.rejectIfEmpty(arg1, "name", "name.empty");
		ValidationUtils.rejectIfEmpty(arg1, "password", "password.empty");
		User u = (User) arg0;
		if (null != userService.getUserDetails(u.getName())) {
			arg1.rejectValue("name", "already.exists");
		}
		if (u.getPassword().length() < 6) {
			arg1.rejectValue("password", "minimum.length");
		}
		logger.debug("end validator method");
	}

}
