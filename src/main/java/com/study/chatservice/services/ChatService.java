package com.study.chatservice.services;

import com.study.chatservice.entities.Chatroom;
import com.study.chatservice.entities.Member;
import com.study.chatservice.entities.MemberChatroomMapping;
import com.study.chatservice.entities.Message;
import com.study.chatservice.repositories.ChatroomRepository;
import com.study.chatservice.repositories.MemberChatroomRepository;
import com.study.chatservice.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {
    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomRepository memberChatroomRepository;
    private final MessageRepository messageRepository;

    public Chatroom createChatroom(Member member, String title){
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createAt(LocalDateTime.now())
                .build();

        chatroom= chatroomRepository.save(chatroom);

        MemberChatroomMapping memberChatroomMapping = chatroom.addMember(member);

        memberChatroomRepository.save(memberChatroomMapping);
        return chatroom;
    }

    public Boolean joinChatroom(Member member, Long newChatroomId , Long currentChatroomId) {
        if (currentChatroomId != null) {
            updateLastCheckedAt(member, currentChatroomId);
        }

        if (memberChatroomRepository.existsByMemberIdAndChatroomId(member.getId(), newChatroomId)) {
            log.info("이미 참여한 채팅방입니다");
            return false;
        }
        Chatroom chatroom = chatroomRepository.findById(newChatroomId).get();
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberChatroomRepository.save(memberChatroomMapping);
        return true;
    }

    private void updateLastCheckedAt(Member member, Long currentChatroomId) {
        MemberChatroomMapping memberChatroomMapping = memberChatroomRepository.findByMemberIdAndChatroomId(member.getId(), currentChatroomId);
        memberChatroomMapping.updateLastCheckedAt();
        memberChatroomRepository.save(memberChatroomMapping);
    }

    public Boolean leaveChatroom(Member member, Long chatroomId) {
        if (!memberChatroomRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)) {
            log.info("참여하지 않은 방입니다");
            return false;
        }
        memberChatroomRepository.deleteByMemberIdAndChatroomId(member.getId(), chatroomId);
        return true;
    }

    public List<Chatroom> getChatrommList(Member member) {
        List<MemberChatroomMapping> memberChatroomMappingList = memberChatroomRepository.findAll();
        return memberChatroomMappingList.stream()
                .map(memberChatroomMapping->{
                    Chatroom chatroom = memberChatroomMapping.getChatroom();
                    chatroom.setHasNewMessage(messageRepository.existsByChatroomIdAndCreatedAtAfter(chatroom.getId(), memberChatroomMapping.getLastCheckedAt()));
                    return chatroom;
                })
                .toList();
    }

    public Message saveMessage(Member member, String text, Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId).get();
        //.get()은 Optional<T> 객체에서 실제 값을 꺼내는 메서드입니다.
        Message message = Message.builder()
                .chatroom(chatroom)
                .text(text)
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessageList(Long chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }
}

