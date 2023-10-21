package com.example.betteriter.fo_domain.review.domain;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "REVIEW_IMAGE")
public class ReviewIamge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "review_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(name = "iamge_url", nullable = false)
    private String imgUrl;

    @Column(name = "order", nullable = false)
    private int order;
}
