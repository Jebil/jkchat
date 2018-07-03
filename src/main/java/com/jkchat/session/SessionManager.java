package com.jkchat.session;

import com.jkchat.models.User;

import javax.servlet.http.HttpServletRequest;

public class SessionManager {
    public static User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    public static void setLastIp(HttpServletRequest request, String ipAddress) {
        request.getSession().setAttribute("lastIp", ipAddress);
    }

    public static String getLastIp(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("lastIp");
    }
}
