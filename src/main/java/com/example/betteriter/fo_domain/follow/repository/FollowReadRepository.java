package com.example.betteriter.fo_domain.follow.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowReadRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(
            @Param("follower") Users follower,
            @Param("followee") Users followee);

    List<Follow> findByFolloweeIdOrderByCreatedAt(Long followeeId, Pageable pageable); // 팔로위(나를 팔로우하는 사람) 목록 조회

    List<Follow> findByFollowerIdOrderByCreatedAt(Long followerId, Pageable pageable); // 팔로워(내가 팔로우하는 사람) 목록 조회

    boolean existsByFollowerAndFollowee(Users follower, Users followee);

    Integer countFollowByFolloweeId(Long followeeId);

    Integer countFollowByFollowerId(Long followerId);
}
