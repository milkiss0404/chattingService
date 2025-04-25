package com.study.chatservice.services;

import com.study.chatservice.entities.Chatroom;
import com.study.chatservice.entities.Member;
import com.study.chatservice.entities.MemberChatroomMapping;
import com.study.chatservice.repositories.ChatroomRepository;
import com.study.chatservice.repositories.MemberChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomRepository memberChatroomRepository;

    public Chatroom createChatroom(Member member, String title){
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createAt(LocalDateTime.now())
                .build();

        chatroom= chatroomRepository.save(chatroom);

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberChatroomRepository.save(memberChatroomMapping);
        return chatroom;
    }

    public Boolean joinChatroom(Member member, Long chatroomId) {
        if (memberChatroomRepository.existsByMemberIdandChatroomId(member.getId(), chatroomId)) {
            log.info("이미 참여한 채팅방입니다");
            return false;
        }
        Chatroom chatroom = chatroomRepository.findById(chatroomId).get();
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberChatroomRepository.save(memberChatroomMapping);
        return true;
    }

    public Boolean leaveChatroom(Member member, Long chatroomId) {
        if (!memberChatroomRepository.existsByMemberIdandChatroomId(member.getId(), chatroomId)) {
            log.info("참여하지 않은 방입니다");
            return false;
        }
        memberChatroomRepository.delteByMemberIdAndChatroomId(member.getId(), chatroomId);
        return true;
    }

    public List<Chatroom> getChatrommList(Member member) {
        List<MemberChatroomMapping> memberChatroomMappingList = memberChatroomRepository.findAllByMemberId(member.getId());
        return memberChatroomMappingList.stream()
                .map(MemberChatroomMapping::getChatroom)
                .toList();
    }
}

