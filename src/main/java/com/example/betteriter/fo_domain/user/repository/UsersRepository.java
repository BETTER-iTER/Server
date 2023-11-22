package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String oauthId);

    Optional<Users> findByEmail(String email);
}
