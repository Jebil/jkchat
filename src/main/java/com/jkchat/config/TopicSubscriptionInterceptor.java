package com.jkchat.config;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class TopicSubscriptionInterceptor extends ChannelInterceptorAdapter {
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())
				&& headerAccessor.getUser() != null) {
			Principal userPrincipal = headerAccessor.getUser();
			String[] dest = headerAccessor.getDestination().split("/");
			if (dest[1].equals("messageQueue")) {
				if (!(userPrincipal.getName().equals(dest[2]))) {
					throw new IllegalArgumentException(
							"No permission for this topic");
				}
			}
		}
		return message;
	}
}
