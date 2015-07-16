package com.jkchat.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jkchat.models.ChatMessage;
import com.jkchat.models.ServerLocation;
import com.jkchat.service.ServerLocationService;
import com.jkchat.service.UserService;
import com.jkchat.session.SessionManager;

/**
 * @author Jebil Kuruvila
 *
 */
@RestController
public class WebServicesController {
	@Autowired
	UserService userService;
	@Autowired
	ServerLocationService serverLocationService;

	/**
	 * @return
	 */
	@RequestMapping(value = "getOnlineUsers", produces = "application/json")
	public List<String> getOnlineUsers() {
		return userService.getOnlineNames();

	}

	/**
	 * @param to
	 * @param from
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "sendMessage", produces = "application/json")
	public String sendMessage(@RequestParam(value = "to") String to,
			@RequestParam(value = "from") String from,
			@RequestParam(value = "message") String msg) {
		ChatMessage cm = new ChatMessage();
		cm.setFrom(from.trim().toLowerCase());
		cm.setMessage(msg);
		userService.putMessage(to.toLowerCase(), cm);
		return "success";
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "getMessages", produces = "application/json")
	public List<ChatMessage> getMessages() {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		List<ChatMessage> list = userService.getMessages(myName.toLowerCase(),
				myName);
		return list;
	}

	/**
	 * @param from
	 * @return
	 */
	@RequestMapping(value = "loadPreviousMessages", produces = "application/json")
	public List<ChatMessage> loadPreviousMessages(
			@RequestParam(value = "from") String from) {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		List<ChatMessage> list = userService.getMessagesFromDB(
				myName.toLowerCase().trim(), from.trim().toLowerCase());
		Collections.reverse(list);
		return list;
	}

	@RequestMapping(value = "getLocationByIP", produces = "application/json")
	public ServerLocation getLocationByIP(@RequestParam String ipAddress,
			HttpServletRequest request) {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		ServerLocation loc = serverLocationService.getLocation(ipAddress);
		if (!(SessionManager.getLastIp(request).equals(ipAddress))) {
			userService.setLastLocationByName(myName, loc);
		}
		return loc;
	}
}
