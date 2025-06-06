package com.study.chatservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chats"); //클라이언트가 WebSocket 연결을 열 때 접속할 엔드포인트를 등록하는 부분
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
        //클라이언트가 서버로 메시지를 보낼 때 사용하는 prefix. 예: /pub/chat/message
        //클라이언트가 서버로부터 메시지를 구독할 때 사용하는 prefix. 예: /sub/chat/room1
    }
}
