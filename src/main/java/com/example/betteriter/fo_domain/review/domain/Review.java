package com.example.betteriter.fo_domain.review.domain;


import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @JoinColumn(name = "made_by_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Manufacturer madeBy;

    @Enumerated(EnumType.STRING)
    private Category category; // 리뷰 카테고리

    @Column(name = "product_name", nullable = false)
    private String productName;

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

    // --------------- Review 관련 엔티티 ---------------- //
    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages;

    @OneToMany(mappedBy = "review")
    private List<ReviewScrap> reviewScraped;

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLiked;

    public ReviewResponseDto of(String firstImageUrl) {
        return ReviewResponseDto.builder()
                .id(id)
                .imageUrl(firstImageUrl)
                .productName(productName)
                .nickname(writer.getUsersDetail().getNickName())
                .profileImageUrl(writer.getUsersDetail().getProfileImage())
                .isExpert(writer.isExpert())
                .build();
    }
}
