package com.study.chatservice.repositories;

import com.study.chatservice.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChatroomRepository extends JpaRepository<MemberChatroomMapping,Long> {
    boolean existsByMemberIdandChatroomId(Long id, Long chatroomId);

    void delteByMemberIdAndChatroomId(Long id, Long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long id);
}
