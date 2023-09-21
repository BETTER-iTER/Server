package com.example.betteriter.user.domain;

import com.example.betteriter.user.dto.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usr_oauth_id", unique = true)
    private String oauthId;

    @Column(name = "usr_email", nullable = false, unique = true)
    private String email;

    @Column(name = "usr_pwd", unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role")
    private RoleType role;

    // 기타 회원 정보
    @Column(name = "usr_nickname", unique = true)
    private String nickName;

    @Column(name = "usr_age")
    private int age;

    @Column(name = "usr_job")
    private int job;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Integer> interests = new ArrayList<>();
}
