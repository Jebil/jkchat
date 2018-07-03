package com.jkchat.validator;

import com.jkchat.models.User;
import com.jkchat.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Jebil Kuruvila
 */
@Component(value = "userValidator")
public class UserValidator implements Validator {
    private static final Logger logger = Logger.getLogger(UserValidator.class);
    @Autowired
    UserService userService;

    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     * org.springframework.validation.Errors)
     */
    public void validate(Object arg0, Errors arg1) {
        logger.debug("Inside validator method");
        ValidationUtils.rejectIfEmptyOrWhitespace(arg1, "name", "name.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(arg1, "password",
                "password.empty");
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
