package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.fo_domain.user.domain.UsersWithdrawReason;
import com.example.betteriter.fo_domain.user.dto.info.GetUserInfoResponseDto;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import com.example.betteriter.fo_domain.user.repository.UsersWithdrawReasonRepository;
import com.example.betteriter.global.constant.Job;
import com.example.betteriter.global.constant.RoleType;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.global.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersWithdrawReasonRepository usersWithdrawReasonRepository;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private SecurityUtil securityUtil;

    @Test
    @DisplayName("회원 로그아웃을 성공한다.")
    void userLogoutTest() {
        // given
        Users user = Users.builder()
                .id(1L)
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .build();

        given(securityUtil.getCurrentUserEmail()).willReturn("danaver12@daum.net");
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user));
        willDoNothing().given(redisUtil).deleteData(anyString());

        // when
        Long result = userService.logout();
        // then
        assertThat(result).isEqualTo(1L);
        verify(usersRepository, times(1)).findByEmail(anyString());
        verify(redisUtil, times(1)).deleteData(anyString());
        verify(securityUtil, times(1)).getCurrentUserEmail();
    }

    @Test
    @DisplayName("회원 정보를 가져온다.")
    void getUserInfoTest() {
        // given
        Users user = Users.builder()
                .id(1L)
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .usersDetail(UsersDetail.builder()
                        .job(Job.DESIGNER)
                        .profileImage("profileImage")
                        .build())
                .build();

        given(securityUtil.getCurrentUserEmail()).willReturn("danaver12@daum.net");
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user));
        // when
        GetUserInfoResponseDto result = this.userService.getUserInfo();
        // then
        assertThat(result.isExpert()).isEqualTo(true);
        verify(securityUtil, times(1)).getCurrentUserEmail();
        verify(usersRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("회원 탈퇴를 한다.")
    void withdrawUserTest() {

        // given
        Users user = Users.builder()
                .id(1L)
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .usersDetail(UsersDetail.builder()
                        .job(Job.DESIGNER)
                        .profileImage("profileImage")
                        .build())
                .build();

        given(securityUtil.getCurrentUserEmail()).willReturn("danaver12@daum.net");
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user));

        willDoNothing().given(redisUtil).deleteData(anyString());

        given(usersWithdrawReasonRepository.save(any()))
                .willReturn(UsersWithdrawReason.builder()
                        .id(1L)
                        .reason(1)
                        .build());

        willDoNothing().given(usersRepository).delete(any());
        // when
        this.userService.withdraw("1");
        // then
        verify(usersWithdrawReasonRepository, times(1)).save(any());
    }
}
