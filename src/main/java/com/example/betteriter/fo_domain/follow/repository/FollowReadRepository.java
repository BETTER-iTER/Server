package com.example.betteriter.fo_domain.follow.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowReadRepository extends JpaRepository<Follow, Long> {

    Follow findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);
}
