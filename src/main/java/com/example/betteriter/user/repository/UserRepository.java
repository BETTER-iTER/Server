package com.example.betteriter.user.repository;

import com.example.betteriter.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);

    Optional<User> findByEmail(String email);

    int countByNickName(String nickname);
}
