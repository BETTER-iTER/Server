package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Job;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_IMAGE_NOT_FOUND;

@JsonPropertyOrder({"reviewDetail", "writerInfo", "reviewLikeInfo", "reviewCommentInfo", "relatedReviews"})
@Getter
@NoArgsConstructor
public class ReviewDetailResponse {
    @JsonProperty("reviewDetail")
    private GetReviewDetailResponseDto getReviewDetailResponseDto; // 리뷰 상세 데이터
    private GetUserResponseDto writerInfo; // 리뷰 작성자 장보
    @JsonProperty("relatedReviews")
    private List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto; // 연관 리뷰 데이터
    private ReviewLikeInfo reviewLikeInfo; // 리뷰 좋아요 데이터
    private ReviewCommentInfo reviewCommentInfo; // 리뷰 댓글 데이터

    @Builder
    public ReviewDetailResponse(GetReviewDetailResponseDto getReviewDetailResponseDto,
                                GetUserResponseDto writerInfo,
                                List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto,
                                ReviewLikeInfo reviewLikeInfo,
                                ReviewCommentInfo reviewCommentInfo
    ) {
        this.getReviewDetailResponseDto = getReviewDetailResponseDto;
        this.writerInfo = writerInfo;
        this.getRelatedReviewResponseDto = getRelatedReviewResponseDto;
        this.reviewLikeInfo = reviewLikeInfo;
        this.reviewCommentInfo = reviewCommentInfo;
    }

    public static ReviewDetailResponse of(Review review, List<Review> relatedReviews, Users currentUser) {
        GetReviewDetailResponseDto reviewDetail = GetReviewDetailResponseDto.from(review); // 리뷰 상세
        GetUserResponseDto writerInfo = GetUserResponseDto.from(review.getWriter()); // 리뷰 작성자 데이터
        List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto = GetRelatedReviewResponseDto.from(relatedReviews); // 연관 리뷰 데이터
        ReviewLikeInfo reviewLikeInfo = ReviewLikeInfo.from(review); // 리뷰 좋아요 데이터
        ReviewCommentInfo reviewCommentInfo = ReviewCommentInfo.from(review, currentUser); // 리뷰 댓글 데이터

        return ReviewDetailResponse.builder()
                .getReviewDetailResponseDto(reviewDetail)
                .writerInfo(writerInfo)
                .getRelatedReviewResponseDto(getRelatedReviewResponseDto)
                .reviewLikeInfo(reviewLikeInfo)
                .reviewCommentInfo(reviewCommentInfo)
                .build();
    }


    @Getter
    @NoArgsConstructor
    public static class GetRelatedReviewResponseDto {
        private Long id;
        private String productName;
        private String reviewImage;
        private String writerName;
        private boolean isExpert;


        @Builder
        public GetRelatedReviewResponseDto(Long id, String productName, String reviewImage,
                                           String writerName, boolean isExpert
        ) {
            this.id = id;
            this.productName = productName;
            this.reviewImage = reviewImage;
            this.writerName = writerName;
            this.isExpert = isExpert;
        }

        public static List<GetRelatedReviewResponseDto> from(List<Review> reviews) {
            return reviews.stream()
                    .map(r -> new GetRelatedReviewResponseDto(
                            r.getId(), r.getProductName(), getFirstImageWithReview(r), r.getWriter().getUsername(), r.getWriter().isExpert()))
                    .collect(Collectors.toList());
        }

        private static String getFirstImageWithReview(Review review) {
            List<ReviewImage> reviewImages = review.getReviewImages();
            return reviewImages.stream()
                    .filter(ri -> ri.getOrderNum() == 0)
                    .findFirst()
                    .orElseThrow(() -> new ReviewHandler(_REVIEW_IMAGE_NOT_FOUND))
                    .getImgUrl();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GetUserResponseDto {
        private Long id;
        private String nickName;
        private Job job;
        private String profileImage;
        private boolean isExpert;

        @Builder
        public GetUserResponseDto(Long id, String nickName, Job job,
                                  String profileImage, boolean isExpert) {
            this.id = id;
            this.nickName = nickName;
            this.job = job;
            this.profileImage = profileImage;
            this.isExpert = isExpert;
        }

        public static ReviewDetailResponse.GetUserResponseDto from(Users writer) {
            return ReviewDetailResponse.GetUserResponseDto.builder()
                    .id(writer.getId())
                    .nickName(writer.getUsersDetail().getNickName())
                    .job(writer.getUsersDetail().getJob())
                    .isExpert(writer.isExpert())
                    .profileImage(writer.getUsersDetail().getProfileImage())
                    .build();
        }
    }

    /**
     * - 좋아요한 유저의 닉네임,프로필,직업
     * - 리뷰의 좋아요 총 갯수
     **/
    @Getter
    @NoArgsConstructor
    public static class ReviewLikeInfo {
        private List<GetUserResponseForLikeAndComment> reviewLikeUserInfo;
        private long reviewLikedCount;

        @Builder
        public ReviewLikeInfo(List<GetUserResponseForLikeAndComment> reviewLikeUserInfo, long reviewLikedCount) {
            this.reviewLikeUserInfo = reviewLikeUserInfo;
            this.reviewLikedCount = reviewLikedCount;
        }

        public static ReviewLikeInfo from(Review review) {
            return ReviewLikeInfo.builder()
                    .reviewLikeUserInfo(review.getReviewLiked()
                            .stream()
                            .map(r -> GetUserResponseForLikeAndComment.from(r.getUsers()))
                            .collect(Collectors.toList()))
                    .reviewLikedCount(review.getLikedCount())
                    .build();
        }
    }

    /**
     * - 댓글을 단 유저의 닉네임,프로필,직업 (ReviewCommentResponse)
     * - 댓글 내용, 댓글 작성일 (ReviewCommentResponse)
     * - 댓글 갯수 (ReviewCommentCount)
     **/
    @Getter
    @NoArgsConstructor
    public static class ReviewCommentInfo {
        @JsonProperty("reviewComments")
        private List<ReviewCommentResponse> reviewCommentResponses;
        private long reviewCommentCount;

        @Builder
        public ReviewCommentInfo(List<ReviewCommentResponse> reviewCommentResponses, long reviewCommentCount) {
            this.reviewCommentResponses = reviewCommentResponses;
            this.reviewCommentCount = reviewCommentCount;
        }

        public static ReviewCommentInfo from(Review review, Users currentUser) {
            return ReviewCommentInfo.builder()
                    .reviewCommentResponses(ReviewCommentResponse.from(review.getReviewComment(),currentUser))
                    .reviewCommentCount(review.getReviewComment().size())
                    .build();
        }

        @Getter
        @NoArgsConstructor
        public static class ReviewCommentResponse {
            private Long id;
            private GetUserResponseForLikeAndComment reviewCommentUserInfo;
            private String comment;
            private LocalDate commentCreatedAt;
            private boolean isMine; // 로그인한 유저의 댓글인지 여부

            @Builder
            public ReviewCommentResponse(Long id, GetUserResponseForLikeAndComment reviewCommentUserInfo,
                                         String comment, LocalDate commentCreatedAt, boolean isMine
            ) {
                this.id = id;
                this.reviewCommentUserInfo = reviewCommentUserInfo;
                this.comment = comment;
                this.commentCreatedAt = commentCreatedAt;
                this.isMine = isMine;
            }

            public static List<ReviewCommentResponse> from(List<Comment> comments, Users users) {
                return comments.stream()
                        .map(c -> ReviewCommentResponse.builder()
                                .id(c.getId())
                                .reviewCommentUserInfo(GetUserResponseForLikeAndComment.from(c.getUsers()))
                                .comment(c.getComment())
                                .commentCreatedAt(c.getCreatedAt().toLocalDate())
                                .isMine(c.getUsers().getId().equals(users.getId()))
                                .build())
                        .collect(Collectors.toList());
            }
        }
    }

    /* 리뷰 좋아요, 댓글 작성자를 위한 유저 응답 dto */
    @Getter
    @NoArgsConstructor
    public static class GetUserResponseForLikeAndComment {
        private Long id;
        private String nickName;
        private Job job;
        private String profileImage;

        @Builder
        public GetUserResponseForLikeAndComment(Long id, String nickName, Job job, String profileImage) {
            this.id = id;
            this.nickName = nickName;
            this.job = job;
            this.profileImage = profileImage;
        }

        public static GetUserResponseForLikeAndComment from(Users users) {
            return GetUserResponseForLikeAndComment.builder()
                    .id(users.getId())
                    .nickName(users.getUsersDetail().getNickName())
                    .job(users.getUsersDetail().getJob())
                    .profileImage(users.getUsersDetail().getProfileImage())
                    .build();
        }
    }
}