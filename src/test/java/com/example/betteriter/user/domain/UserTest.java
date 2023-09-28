package com.example.betteriter.user.domain;


import com.example.betteriter.user.dto.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    @DisplayName("user test")
    public void userTest() {
        User user = User.builder()
                .id(1L)
                .nickName("닉네임")
                .email("danaver12@daum.net")
                .password("1234")
                .role(RoleType.ROLE_USER)
                .build();

        System.out.println(user.getRole().name());
        assertThat(user.getRole().name()).isEqualTo("ROLE_USER");
    }
}
