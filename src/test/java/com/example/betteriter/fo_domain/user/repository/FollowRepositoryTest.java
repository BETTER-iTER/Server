package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("Follow 저장 테스트를 진행한다.")
    void saveFollowTest() {
        // given
        Users users01 = Users.builder()
                .email("danaver12@daum.net")
                .roleType(RoleType.ROLE_USER)
                .build();

        Users users02 = Users.builder()
                .email("whole34@naver.com")
                .roleType(RoleType.ROLE_USER)
                .build();

        this.usersRepository.saveAll(List.of(users01, users02));

        // users01 -> users02
        Follow follow = Follow.builder()
                .follower(users01)
                .followee(users02)
                .build();
        // when
        this.followRepository.save(follow);

        // then
        List<Follow> response = this.followRepository.findAll();
        Follow savedFollow = response.get(0);

        List<Users> savedUsers = this.usersRepository.findAll();

    }

}