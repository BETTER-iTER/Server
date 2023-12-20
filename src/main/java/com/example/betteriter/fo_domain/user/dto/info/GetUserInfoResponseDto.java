package com.example.betteriter.fo_domain.user.dto.info;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * - 리뷰 등록시 보여줄 사용자 정보
 **/
@Getter
@NoArgsConstructor
public class GetUserInfoResponseDto {
    private String nickName;
    private Job job;
    private String profileImage;
    private boolean isExpert;

    @Builder
    private GetUserInfoResponseDto(String nickName, Job job,
                                   String profileImage, boolean isExpert) {
        this.nickName = nickName;
        this.job = job;
        this.profileImage = profileImage;
        this.isExpert = isExpert;
    }

    public static GetUserInfoResponseDto from(Users users) {
        UsersDetail usersDetail = users.getUsersDetail();
        return GetUserInfoResponseDto.builder()
                .nickName(usersDetail.getNickName())
                .job(usersDetail.getJob())
                .profileImage(usersDetail.getProfileImage())
                .isExpert(users.isExpert())
                .build();
    }
}
