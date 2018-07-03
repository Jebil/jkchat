package com.jkchat.authentication;

import com.jkchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jebil Kuruvila
 */
@Component
public class AuthenticationSuccessHandlerImpl
        implements AuthenticationSuccessHandler {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserService userService;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.Authentication)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        this.simpMessagingTemplate.convertAndSend("/queue/onlineUsers",
                authentication.getName());
        response.sendRedirect("home");
    }

}
