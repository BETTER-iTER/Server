package com.example.betteriter.fo_domain.review.domain;


import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "REVIEW")
public class Review extends BaseEntity {
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ReviewSpecData> specData;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "writer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users writer;
    @JoinColumn(name = "manufacturer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Manufacturer manufacturer;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "store_name", nullable = false)
    private int storeName;
    @Column(name = "bought_at", nullable = false)
    private LocalDate boughtAt;
    @Column(name = "star_point", nullable = false)
    private int starPoint;
    @Column(name = "short_review", nullable = false)
    private String shortReview;
    @Lob // 최대 500 자
    @Column(name = "good_point", nullable = false)
    private String goodPoint;
    @Lob // 최대 500 자
    @Column(name = "bad_point", nullable = false)
    private String badPoint;
    // --------------- Review 관련 엔티티 ---------------- //
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewScrap> reviewScraped;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLiked;

    @Builder
    private Review(Long id, Users writer, Manufacturer manufacturer, Category category,
                   String productName, int amount, int storeName, LocalDate boughtAt,
                   int starPoint, String shortReview, String goodPoint, String badPoint,
                   List<ReviewImage> reviewImages, List<ReviewScrap> reviewScraped, List<ReviewLike> reviewLiked
    ) {
        this.id = id;
        this.writer = writer;
        this.manufacturer = manufacturer;
        this.category = category;
        this.productName = productName;
        this.amount = amount;
        this.storeName = storeName;
        this.boughtAt = boughtAt;
        this.starPoint = starPoint;
        this.shortReview = shortReview;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.reviewImages = reviewImages;
        this.reviewScraped = reviewScraped;
        this.reviewLiked = reviewLiked;
    }

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

    public void setReviewImages(List<ReviewImage> reviewImages) {
        this.reviewImages = reviewImages;
    }
}
