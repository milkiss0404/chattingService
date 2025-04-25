package com.study.chatservice.entities;

import com.study.chatservice.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Id
    private Long id;

    private String email;
    private String nickName;
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;
//    private LocalDate birthDay;
    private String role;

    @OneToMany(mappedBy = "member")
    private Set<MemberChatroomMapping> memberChatroomMappingSet;

}
