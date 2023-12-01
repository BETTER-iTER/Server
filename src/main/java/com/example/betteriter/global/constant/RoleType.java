package com.example.betteriter.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 서비스 회원 역할
 **/
@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_USER("일반 유저"), // 일반 유저
    //    ROLE_EXPERTISE("전문가 유저"), // 전문가 유저
    ROLE_ADMIN("관리자 유저"); // 관리자 유저
    private final String roleName;
}
