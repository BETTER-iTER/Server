package com.example.betteriter.fo_domain.follow.service;

import com.example.betteriter.fo_domain.follow.converter.FollowConverter;
import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.follow.dto.FollowRequest;
import com.example.betteriter.fo_domain.follow.exception.FollowHandler;
import com.example.betteriter.fo_domain.follow.repository.FollowReadRepository;
import com.example.betteriter.fo_domain.follow.repository.FollowWriteRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {

    private final UserService userService;
    private final FollowWriteRepository followWriteRepository;
    private final FollowReadRepository followReadRepository;

    @Transactional
    public Follow following(FollowRequest.FollowingDto followingRequestDto) {
        Users user = userService.getCurrentUser();
        Users targetUser = userService.getUserById(followingRequestDto.getTargetId());
        Follow follow = FollowConverter.toFollowing(user, targetUser);

        return followWriteRepository.save(follow);
    }

    @Transactional
    public void unfollowing(FollowRequest.UnfollowingDto unfollowingRequestDto) {
        Users user = userService.getCurrentUser();
        Users targetUser = userService.getUserByEmail(unfollowingRequestDto.getEmail());

        Follow follow = findFollowData(user, targetUser);

        followWriteRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<Users> getFollowerList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Follow> followerList = followReadRepository.findByFollowerId(user.getId(), pageable);

        return followerList.stream()
                .map(Follow::getFollowee)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Users> getFolloweeList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Follow> followeeList = followReadRepository.findByFolloweeId(user.getId(), pageable);

        return followeeList.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    public boolean isFollow(Users follower, Users followee) {
        return this.followReadRepository.existsByFollowerAndFollowee(follower,followee);
    }

    private Follow findFollowData(Users user, Users targetUser) {
        Follow follow = followReadRepository.findByFolloweeIdAndFollowerId(user.getId(), targetUser.getId());
        if (follow == null) throw new FollowHandler(ErrorStatus._FOLLOW_NOT_FOUND);

        return follow;
    }

    public Integer getFollowerCount(Users user) {
        return followReadRepository.countFollowByFolloweeId(user.getId());
    }

    public Integer getFolloweeCount(Users user) {
        return followReadRepository.countFollowByFollowerId(user.getId());
    }
}