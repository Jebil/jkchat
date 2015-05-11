package com.jkchat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jkchat.models.ChatMessage;
import com.jkchat.service.UserService;

@RestController
public class WebServicesController {
	@Autowired
	UserService userService;

	@RequestMapping(value = "getOnlineUsers", produces = "application/json")
	public List<String> getOnlineUsers() {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		return userService.getOnlineNames(myName);

	}

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

	@RequestMapping(value = "getMessages", produces = "application/json")
	public List<ChatMessage> getMessages() {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		List<ChatMessage> list = userService.getMessages(myName.toLowerCase(),
				myName);
		return list;
	}

	@RequestMapping(value = "loadPreviousMessages", produces = "application/json")
	public List<ChatMessage> loadPreviousMessages(
			@RequestParam(value = "from") String from) {
		String myName = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		return userService.getMessagesFromDB(myName.toLowerCase(), from.trim()
				.toLowerCase());
	}
}
