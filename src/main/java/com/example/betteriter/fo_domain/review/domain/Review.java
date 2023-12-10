package com.example.betteriter.fo_domain.review.domain;


import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // ACTIVE, DELETED

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
