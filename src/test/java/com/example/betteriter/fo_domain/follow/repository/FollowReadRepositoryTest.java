package com.example.betteriter.fo_domain.follow.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class FollowReadRepositoryTest {

    @Autowired
    private FollowReadRepository followReadRepository;

    @Autowired
    private FollowWriteRepository followWriteRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("팔로워와 팔로위 정보가 주어질떄 Follow 엔티티 존재 여부를 확인한다.")
    void checkFollowerAndFolloweeTest01() {
        // given

        // 팔로워
        Users follower = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        // 팔로위
        Users followee = Users.builder()
                .email("whole34@naver.com")
                .roleType(ROLE_USER)
                .build();

        this.usersRepository.saveAll(List.of(followee, follower));

        this.followWriteRepository.save(Follow.builder().
                follower(follower)
                .followee(followee).build());
        // when
        boolean result = this.followReadRepository.existsByFollowerAndFollowee(follower, followee);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("팔로워와 팔로위 정보가 주어질때 Follow 엔티티 존재 여부를 확인한다.")
    void checkFollowerAndFolloweeTest02(){
        // given
        // 팔로워
        Users follower = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        // 팔로위
        Users followee = Users.builder()
                .email("whole34@naver.com")
                .roleType(ROLE_USER)
                .build();

        this.usersRepository.saveAll(List.of(followee, follower));

        this.followWriteRepository.save(Follow.builder()
                .follower(follower)
                .followee(follower)
                .build());
        // when
        boolean result = this.followReadRepository.existsByFollowerAndFollowee(follower,followee);

        // then
        assertThat(result).isFalse();
    }
}