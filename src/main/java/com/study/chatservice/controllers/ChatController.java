package com.study.chatservice.controllers;

import com.study.chatservice.entities.Chatroom;
import com.study.chatservice.services.ChatService;
import com.study.chatservice.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/chats")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private ChatService chatService;

    @PostMapping
    public Chatroom createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestParam String title) {
        return chatService.createChatroom(user.getMember(), title);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId) {
        return chatService.joinChatroom(user.getMember(), chatroomId);
    }
    @DeleteMapping("{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId){
        return chatService.leaveChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<Chatroom> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {
        return chatService.getChatrommList(user.getMember());
    }

}
