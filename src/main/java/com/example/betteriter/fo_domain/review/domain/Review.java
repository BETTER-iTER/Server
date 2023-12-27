package com.example.betteriter.fo_domain.review.domain;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'") // ACTIVE 상태인 REVIEW 만 조회
@Entity(name = "REVIEW")
public class Review extends BaseEntity {
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewScrap> reviewScraped = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ReviewSpecData> specData = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
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
    @Column(name = "price")
    private int price;
    @Column(name = "store_name", nullable = false)
    private int storeName;
    @Column(name = "bought_at", nullable = false)
    private LocalDate boughtAt;
    @Column(name = "star_point", nullable = false)
    private double starPoint;
    @Column(name = "short_review", nullable = false)
    private String shortReview;
    @Column(name = "click_cnt")
    private long clickCount; // 클릭 수
    @Column(name = "liked_cnt")
    private long likedCount; // 좋아요 수
    @Column(name = "scraped_cnt")
    private long scrapedCount; // 스크랩 수
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
    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();
    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLiked = new ArrayList<>();
    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> reviewComment = new ArrayList<>();

    @Builder
    private Review(Long id, Users writer, Manufacturer manufacturer, Category category,
                   String productName, int storeName, LocalDate boughtAt, int price,
                   double starPoint, String shortReview, String goodPoint, Status status,
                   String badPoint, long clickCount, long likedCount, long scrapedCount
    ) {
        this.id = id;
        this.writer = writer;
        this.manufacturer = manufacturer;
        this.category = category;
        this.productName = productName;
        this.storeName = storeName;
        this.boughtAt = boughtAt;
        this.price = price;
        this.starPoint = starPoint;
        this.shortReview = shortReview;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.clickCount = clickCount;
        this.likedCount = likedCount;
        this.scrapedCount = scrapedCount;
        this.status = status;
    }

    public ReviewResponseDto of(String firstImageUrl) {
        return ReviewResponseDto.builder().id(id).imageUrl(firstImageUrl).productName(productName).nickname(writer.getUsersDetail().getNickName()).profileImageUrl(writer.getUsersDetail().getProfileImage()).isExpert(writer.isExpert()).build();
    }

    // review 클릭 수를 초기화하는 메소드
    public void resetClickCounts() {
        this.clickCount = 0L;
    }

    public void countReviewScrapedCount() {
        this.scrapedCount += 1;
    }

    public void countReviewLikedCount() {
        this.likedCount += 1;
    }

    // TODO : 제거
    public void setReviewImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
    }

    // TODO : 제거
    public void setReviewLikes(List<ReviewLike> reviewLikes) {
        this.reviewLiked = reviewLikes;
    }

    // TODO : 제거
    public void setReviewsComment(List<Comment> comments) {
        this.reviewComment = comments;
    }

    // 매주 월요일 자정 실행
    @Scheduled(cron = "0 0 0 ? * MON")
    public void resetClickCountsScheduler() {
        this.resetClickCounts();
    }
}