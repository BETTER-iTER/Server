package com.example.betteriter.fo_domain.review.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "REVIEW_IMAGE")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "review_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(name = "iamge_url", nullable = false)
    private String imgUrl;

    @Column(name = "order_num", nullable = false)
    private int orderNum;

    @Builder
    private ReviewImage(String imgUrl, int orderNum) {
        this.imgUrl = imgUrl;
        this.orderNum = orderNum;
    }

    public static ReviewImage createReviewImage(String imgUrl, int orderNum) {
        return ReviewImage.builder()
                .imgUrl(imgUrl)
                .orderNum(orderNum)
                .build();
    }
}
