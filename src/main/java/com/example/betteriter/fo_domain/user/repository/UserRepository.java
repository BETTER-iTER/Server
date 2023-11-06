package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);

    Optional<User> findByEmail(String email);
}
