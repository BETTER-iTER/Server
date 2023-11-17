package com.example.betteriter.fo_domain.user.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS_DETAIL")
public class UsersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usr_nickname", unique = true)
    private String nickName;

    @Column(name = "usr_job")
    private int job;

    @Column(name = "usr_profile_img")
    private String profileImage;

    @Column(name = "usr_point")
    private int point; // 얻은 포인트

    @Column(name = "usr_review_cnt")
    private int reviewCount; // 작성한 리뷰 개수

    @Column(name = "usr_quiz_cnt")
    private int quizCount; // 맞춘 퀴즈 개수

    @Column(name = "usr_review_scraped")
    private int reviewScraped; // 작성한 리뷰 스크랩 횟수

    @Column(name = "usr_review_liked")
    private int reviewLiked; // 작성한 리뷰 좋아요 횟수
}
