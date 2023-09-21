package com.example.betteriter.user.repository;

import com.example.betteriter.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
