package com.example.betteriter.fo_domain.follow.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowReadRepository extends JpaRepository<Follow, Long> {

    Follow findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);

    List<Follow> findByFolloweeId(Long followeeId); // 팔로위(나를 팔로우하는 사람) 목록 조회

    List<Follow> findByFollowerId(Long followerId); // 팔로워(내가 팔로우하는 사람) 목록 조회
}
