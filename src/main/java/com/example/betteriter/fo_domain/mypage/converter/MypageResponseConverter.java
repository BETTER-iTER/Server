package com.example.betteriter.fo_domain.mypage.converter;

import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.GetReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.service.ReviewConnector;
import com.example.betteriter.fo_domain.user.domain.Users;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MypageResponseConverter {

    private final ReviewConnector reviewConnector;

    public static MypageResponse.FollowerListDto toFollowerListDto(List<Users> followerList, Integer totalCount) {
        List<MypageResponse.FollowerDto> followerDtoList = new ArrayList<>();

        followerList.forEach(f -> {
            MypageResponse.FollowerDto followerDto = MypageResponse.FollowerDto.builder()
                    .id(f.getId())
                    .email(f.getEmail())
                    .profileImage(f.getUsersDetail().getProfileImage())
                    .nickname(f.getUsersDetail().getNickName())
                    .build();
            followerDtoList.add(followerDto);
        });

        return MypageResponse.FollowerListDto.builder()
                .totalCount(totalCount)
                .followerList(followerDtoList)
                .build();
    }

    public static MypageResponse.UserProfileDto toUserProfileDto(
            Users user, Integer followerCount, Integer followingCount
    ) {
        return MypageResponse.UserProfileDto.builder()
                .profileImage(user.getUsersDetail().getProfileImage())
                .nickname(user.getUsersDetail().getNickName())
                .job(user.getUsersDetail().getJob())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    public static MypageResponse.PointDetailDto toPointDetailDto(
            Users user, Integer totalLikeCount, Integer totalScrapCount
    ) {
        Integer totalPoint = user.getReviews().size() * 20 +
                totalLikeCount * 7 +
                totalScrapCount * 10;

        return MypageResponse.PointDetailDto.builder()
                .id(user.getId())
                .totalPoint(totalPoint)
                .totalReviewCount(user.getReviews().size())
                .totalLikeCount(totalLikeCount)
                .totalScrapCount(totalScrapCount)
                .build();
    }

    public ReviewResponse toReviewResponse(Slice<Review> reviews) {
        List<GetReviewResponseDto> getReviewResponseDtos = reviews.getContent()
                .stream()
                .map(review -> GetReviewResponseDto.of(review,
                        this.checkCurrentUserIsScrapReview(review),
                        this.checkCurrentUserIsLikeReview(review))
                ).collect(Collectors.toList());

        return new ReviewResponse(getReviewResponseDtos, reviews.hasNext(), !reviews.isEmpty());
    }

    public MypageResponse.ReviewListDto toReviewListDto(Page<Review> reviewList) {
        List<MypageResponse.ReviewDto> myReviewList = reviewList.stream()
                .map(MypageResponseConverter::getReviewDto)
                .collect(Collectors.toList());

        MypageResponse.PageDto pageInfo = getReviewPageInfo(reviewList);

	    return MypageResponse.ReviewListDto.builder()
                .reviewList(myReviewList)
                .pageInfo(pageInfo)
                .build();
    }

    private static MypageResponse.PageDto getReviewPageInfo(Page<Review> reviewList) {
        return MypageResponse.PageDto.builder()
                .page(reviewList.getNumber())
                .size(reviewList.getSize())
                .totalPage(reviewList.getTotalPages())
                .totalCount(reviewList.getTotalElements())
                .hasNext(reviewList.hasNext())
                .hasPrev(reviewList.hasPrevious())
                .build();
    }

    private static MypageResponse.ReviewDto getReviewDto(Review r) {
        return MypageResponse.ReviewDto.builder()
                .reviewId(r.getId())
                .title(r.getProductName())
                .thumbnailImage((!r.getReviewImages().isEmpty()) ?
                        r.getReviewImages().get(0).getImgUrl() : null)
                .writerId(r.getWriter().getId())
                .writerJob(r.getWriter().getUsersDetail().getJob())
                .writerNickname(r.getWriter().getUsersDetail().getNickName())
                .profileImage(r.getWriter().getUsersDetail().getProfileImage())
                .likeCount((long)r.getReviewLiked().size())
                .scrapCount((long)r.getReviewScraped().size())
                .isLike(r.getReviewLiked().stream().anyMatch(rl -> rl.getUsers().getId().equals(r.getWriter().getId())))
                .isScrap(r.getReviewScraped()
                        .stream()
                        .anyMatch(rs -> rs.getUsers().getId().equals(r.getWriter().getId())))
                .build();
    }

    private boolean checkCurrentUserIsScrapReview(Review review) {
        return this.reviewConnector.existsReviewScrapByReviewAndUsers(review);
    }

    private boolean checkCurrentUserIsLikeReview(Review review) {
        return this.reviewConnector.existsReviewLikeByReviewAndUsers(review);
    }
}
