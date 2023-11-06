package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
