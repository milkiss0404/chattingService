package com.study.chatservice.controllers;

import com.study.chatservice.dtos.ChatMessage;
import com.study.chatservice.entities.Message;
import com.study.chatservice.services.ChatService;
import com.study.chatservice.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messageTemplate;  //SimpMessagingTemplate는 Spring WebSocket(STOMP) 기반 실시간 메시징에서 서버가 클라이언트로 메시지를 푸시(Push) 하기 위해 사용하는 유틸리티입니다.

    //@DestinationVariable
    //클라이언트가 메시지를 보낼 때 사용한 경로의 변수 부분을 서버에서 꺼낼 때 사용합니다.

    //클라이언트가 /chat/{chatroomId}로 메시지를 보내면
    //@SendTO -> 메서드가 리턴한 메시지를 어디로 브로드캐스트할지 지정합니다.
    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handleMessage(@AuthenticationPrincipal Principal principal,@DestinationVariable Long chatroomId,
                                     @Payload Map<String ,String> payload) {
        log.info("{} sent {} in {}", principal.getName(),payload,chatroomId);
        CustomOAuth2User user = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        Message message = chatService.saveMessage(user.getMember(), payload.get("message"),chatroomId);
        messageTemplate.convertAndSend("/sub/chats/news", chatroomId);
        return new ChatMessage(principal.getName(), payload.get("message"));
    }

}
