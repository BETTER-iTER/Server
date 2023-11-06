package com.example.betteriter.fo_domain.user.dto;

import lombok.Getter;


/**
 * 서비스 회원 역할
 **/
@Getter
public enum RoleType {
    ROLE_USER, // 일반 유저
    ROLE_EXPERTISE, // 전문가 유저
    ROLE_ADMIN // 관리자 유저
}
