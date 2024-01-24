package com.example.betteriter.fo_domain.follow.controller;

import com.example.betteriter.fo_domain.follow.converter.FollowResponseConverter;
import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.follow.dto.FollowRequest;
import com.example.betteriter.fo_domain.follow.dto.FollowResponse;
import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "FollowController", description = "Follow API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    /**
     * 1. 팔로우
     * - /follow/following
     *
     * @param followingRequestDto : 팔로우 요청 DTO
     * @return followingResponseDto : 팔로우 응답 DTO
     */
    @PostMapping("/following")
    public ResponseDto<FollowResponse.FollowingDto> following(
           @RequestBody @Valid FollowRequest.FollowingDto followingRequestDto
    ) {
        Follow follow = followService.following(followingRequestDto);
        return ResponseDto.onSuccess(FollowResponseConverter.toFollowingDto(follow));
    }

    /**
     * 2. 언팔로우
     * - /follow/unfollowing
     *
     * @param unfollowingRequestDto : 언팔로우 요청 DTO
     * @return unfollowingResponseDto : 언팔로우 응답 DTO
     */
    @DeleteMapping("/unfollowing")
    public ResponseDto<FollowResponse.UnfollowingDto> unfollowing(
           @RequestBody @Valid FollowRequest.UnfollowingDto unfollowingRequestDto
    ) {
        followService.unfollowing(unfollowingRequestDto);
        return ResponseDto.onSuccess(FollowResponseConverter.toUnfollowingDto());
    }
}
