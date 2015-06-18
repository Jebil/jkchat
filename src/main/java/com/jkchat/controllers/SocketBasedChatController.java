package com.jkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.jkchat.models.ChatMessage;
import com.jkchat.models.MessageDTO;
import com.jkchat.models.UserMessages;
import com.jkchat.service.UserService;

@Controller
public class SocketBasedChatController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	UserService userService;

	@MessageMapping("/recieveMessage")
	public void chatHandler(@Payload MessageDTO mdto) {
		UserMessages um = new UserMessages();
		ChatMessage cm = new ChatMessage();
		cm.setFrom(mdto.getFrom());
		cm.setMessage(mdto.getMessage());
		um.setuName(mdto.getTo());
		um.setCm(cm);
		userService.saveMessagesToDB(um);
		userService.putMessage(mdto.getTo().toLowerCase(), cm);
		this.simpMessagingTemplate.convertAndSend("/queue/" + mdto.getTo(),
				mdto);
	}
}
