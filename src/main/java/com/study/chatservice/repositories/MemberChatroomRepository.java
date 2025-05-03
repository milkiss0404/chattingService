package com.study.chatservice.repositories;

import com.study.chatservice.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChatroomRepository extends JpaRepository<MemberChatroomMapping,Long> {
    boolean existsByMemberIdAndChatroomId(Long id, Long chatroomId);

    void deleteByMemberIdAndChatroomId(Long id, Long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long id);

    MemberChatroomMapping findByMemberIdAndChatroomId(Long id, Long currentChatroomId);
}
