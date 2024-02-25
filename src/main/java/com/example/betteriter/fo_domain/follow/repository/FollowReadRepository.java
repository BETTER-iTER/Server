package com.example.betteriter.fo_domain.follow.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowReadRepository extends JpaRepository<Follow, Long> {

    Follow findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    List<Follow> findByFolloweeIdOrderByCreatedAt(Long followeeId, Pageable pageable); // 팔로위(나를 팔로우하는 사람) 목록 조회

    List<Follow> findByFollowerIdOrderByCreatedAt(Long followerId, Pageable pageable); // 팔로워(내가 팔로우하는 사람) 목록 조회

    boolean existsByFollowerAndFollowee(Users follower, Users followee);

    Integer countFollowByFolloweeId(Long followeeId);

    Integer countFollowByFollowerId(Long followerId);
}
