package com.jkchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author Jebil Kuruvila
 */
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.socket.config.annotation.
     * WebSocketMessageBrokerConfigurer#registerStompEndpoints(org.
     * springframework.web.socket.config.annotation.StompEndpointRegistry)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/recieveMessage").withSockJS();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.socket.config.annotation.
     * AbstractWebSocketMessageBrokerConfigurer#configureClientInboundChannel(
     * org.springframework.messaging.simp.config.ChannelRegistration)
     */
    @Override
    public void configureClientInboundChannel(
            ChannelRegistration registration) {
        registration.setInterceptors(new TopicSubscriptionInterceptor());
    }
}
