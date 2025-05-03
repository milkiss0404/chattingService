package com.study.chatservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Chatroom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Id
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chatroom")
    private Set<MemberChatroomMapping> memberChatroomMappingSet ;

    private LocalDateTime createAt;

    //Transient 는 엔티티 클래스에서 특정 필드를 DB에 매핑하지 않도록 지정할 때 사용합니다.
    //즉, 해당 필드는 DB에 저장되지 않으며, 영속성 컨텍스트에 포함되지 않습니다.
    //이 필드는 비즈니스 로직에서만 사용하고, DB 에는 필요 없는 데이터를 담을 때 유용합니다.
    @Transient
    Boolean hasNewMessage;

    public void setHasNewMessage(boolean hasNewMessage) {
        this.hasNewMessage = hasNewMessage;
    }
    //멤버를 더해줄건데 채팅룸이없으면 채팅룸을 만들어주고
    //매핑 객체를 만들어줘서 채팅룸 set 에 넣어줌
    // 근데 매번 저렇게 매핑객체를 만들어서넣어주면 전에있던 객체랑 같은 채팅방일떄  아, 맵이니까 맵 객체로 하나씩 넣어주는거구나
    public MemberChatroomMapping addMember(Member member) {
        if (this.getMemberChatroomMappingSet() == null) {
            this.memberChatroomMappingSet = new HashSet<>();
        }
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();
        this.memberChatroomMappingSet.add(memberChatroomMapping);
        return memberChatroomMapping;
    }
}
