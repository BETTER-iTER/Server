package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("회원 이메일을 통해 회원 엔티티 정보를 가져온다.")
    void findUserByUserEmail() {
        // given
        Users user = Users.builder()
                .id(1L)
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();
        System.out.println("==1==");
        this.usersRepository.save(user);
        System.out.println("==2==");
        // when
        System.out.println("==3==");
        Optional<Users> result01 = this.usersRepository.findByEmail("danaver12@daum.net");
        System.out.println("==4==");
        Optional<Users> result02 = this.usersRepository.findByEmail("danaver12@daum.net");
        // then
    }
}
