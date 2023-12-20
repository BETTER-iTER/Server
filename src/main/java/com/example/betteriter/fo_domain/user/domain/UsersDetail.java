package com.example.betteriter.fo_domain.user.domain;

import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Job;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS_DETAIL")
public class UsersDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usr_nickname", unique = true, nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_job", nullable = false)
    private Job job;

    @Column(name = "usr_profile_img")
    private String profileImage;

    @Column(name = "usr_point")
    private int point; // 얻은 포인트

    @Column(name = "usr_review_cnt")
    private int reviewCount; // 작성한 리뷰 개수

    @Column(name = "usr_quiz_cnt")
    private int quizCount; // 맞춘 퀴즈 개수

    @Column(name = "usr_review_scraped")
    private int reviewScraped; // 작성한 리뷰 스크랩 총 횟수

    @Column(name = "usr_review_liked")
    private int reviewLiked; // 작성한 리뷰 좋아요 총 횟수

    @Builder
    private UsersDetail(String nickName, Job job, String profileImage,
                        int point, int reviewCount, int quizCount,
                        int reviewScraped, int reviewLiked) {
        this.nickName = nickName;
        this.job = job;
        this.profileImage = profileImage;
        this.point = point;
        this.reviewCount = reviewCount;
        this.quizCount = quizCount;
        this.reviewScraped = reviewScraped;
        this.reviewLiked = reviewLiked;
    }
}