package com.example.betteriter.fo_domain.review.domain;


import com.example.betteriter.bo_domain.category.domain.Category;
import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "REVIEW")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "writer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JoinColumn(name = "made_by_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Manufacturer madeBy;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "bought_at", nullable = false)
    private Timestamp boughtAt;

    @Column(name = "star_point", nullable = false)
    private int starPoint;

    @Column(name = "shot_review", nullable = false)
    private String shotReview;

    @Column(name = "good_point", nullable = false)
    private String goodPoint;

    @Column(name = "bad_point", nullable = false)
    private String badPoint;
}
