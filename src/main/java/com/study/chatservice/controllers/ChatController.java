package com.study.chatservice.controllers;

import com.study.chatservice.dtos.ChatMessage;
import com.study.chatservice.dtos.ChatroomDto;
import com.study.chatservice.entities.Chatroom;
import com.study.chatservice.entities.Message;
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
    private final ChatService chatService;

    @PostMapping
    public ChatroomDto createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestParam String title) {
        Chatroom chatroom = chatService.createChatroom(user.getMember(), title);
        return ChatroomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId, @RequestParam(required = false) Long currentChatroomId) {
        return chatService.joinChatroom(user.getMember(), chatroomId,currentChatroomId);
    }
    @DeleteMapping("{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId){
        return chatService.leaveChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<ChatroomDto> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {
        List<Chatroom> chatrommList = chatService.getChatrommList(user.getMember());
        return chatrommList.stream()
                .map(ChatroomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessageList(@PathVariable Long chatroomId) {
        List<Message> messageList = chatService.getMessageList(chatroomId);
        return messageList.stream()
                .map(message
                        -> new ChatMessage(message.getMember().getNickName(), message.getText())).toList();
    }

}
