package com.jkchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jebil Kuruvila
 */
@Component
public class LogoutHandlerClass extends SimpleUrlLogoutSuccessHandler {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.logout.
     * SimpleUrlLogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.Authentication)
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest arg0,
                                HttpServletResponse arg1, Authentication arg2)
            throws IOException, ServletException {
        this.simpMessagingTemplate.convertAndSend("/queue/loggedOutUser",
                arg2.getName());
        super.onLogoutSuccess(arg0, arg1, arg2);
    }
}
