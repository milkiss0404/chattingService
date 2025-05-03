package com.study.chatservice.repositories;

import com.study.chatservice.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByChatroomId(Long chatroomId);

    boolean existsByChatroomIdAndCreatedAtAfter(Long id, LocalDateTime lastCheckedAt);
}
