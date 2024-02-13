package com.example.betteriter.fo_domain.review.domain;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.UpdateReviewRequestDto;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@DynamicUpdate
@Entity(name = "REVIEW")
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE iterdb.review SET review.status = 'DELETED' WHERE review.id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
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
    @Column(name = "compared_product_name", nullable = false)
    private String comparedProductName;
    @Column(name = "bought_at", nullable = false)
    private LocalDate boughtAt;
    @Column(name = "star_point", nullable = false)
    private double starPoint;
    @Column(name = "short_review", nullable = false)
    private String shortReview;
    @Column(name = "shown_cnt")
    private long shownCount; // 조회 수
    @Column(name = "click_cnt")
    private long clickCount; // 클릭 수
    @Column(name = "liked_cnt")
    private long likedCount; // 좋아요 수
    @Column(name = "scraped_cnt")
    private long scrapedCount; // 스크랩 수
    @Lob
    @Column(name = "good_point", nullable = false)
    private String goodPoint;
    @Lob
    @Column(name = "bad_point", nullable = false)
    private String badPoint;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // ACTIVE, DELETED, INACTIVE

    // --------------- Review 관련 엔티티 ---------------- //
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewScrap> reviewScraped = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewSpecData> specData = new ArrayList<>();
    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLiked = new ArrayList<>();
    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> reviewComment = new ArrayList<>();

    @Builder
    public Review(Long id, Users writer, Manufacturer manufacturer, long shownCount,
                  Category category, String productName, int price, int storeName, String comparedProductName,
                  LocalDate boughtAt, double starPoint, String shortReview, long clickCount, long likedCount,
                  long scrapedCount, String goodPoint, String badPoint, Status status

    ) {
        this.id = id;
        this.writer = writer;
        this.manufacturer = manufacturer;
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.storeName = storeName;
        this.comparedProductName = comparedProductName;
        this.boughtAt = boughtAt;
        this.starPoint = starPoint;
        this.shortReview = shortReview;
        this.shownCount = shownCount;
        this.clickCount = clickCount;
        this.likedCount = likedCount;
        this.scrapedCount = scrapedCount;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.status = status;
    }


    public ReviewResponseDto of(String firstImageUrl) {
        return ReviewResponseDto.builder().id(id).imageUrl(firstImageUrl).productName(productName).nickname(writer.getUsersDetail().getNickName()).profileImageUrl(writer.getUsersDetail().getProfileImage()).isExpert(writer.isExpert()).build();
    }

    public void resetClickCounts() {
        this.clickCount = 0L;
    }

    public void addReviewScrapedCount() {
        this.scrapedCount += 1;
    }

    public void addReviewLikedCount() {
        this.likedCount += 1;
    }

    public void minusReviewScrapedCount() {
        if (this.scrapedCount == 0) {
            return;
        }
        this.scrapedCount--;
    }

    public void minusReviewLikedCount() {
        if (this.likedCount == 0) {
            return;
        }
        this.likedCount--;
    }

    public void addClickCountsAndShownCounts() {
        this.clickCount++;
        this.shownCount++;
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

    public void setReviewSpecData(List<ReviewSpecData> reviewSpecData) {
        this.specData = reviewSpecData;
    }

    // 매주 월요일 자정 실행
    @Scheduled(cron = "0 0 0 ? * MON")
    public void resetClickCountsScheduler() {
        this.resetClickCounts();
    }

    public void updateReview(UpdateReviewRequestDto request, Manufacturer manufacturer) {
        this.category = request.getCategory() == null ? this.category : request.getCategory();
        this.productName = request.getProductName() == null ? this.productName : request.getProductName();
        this.boughtAt = request.getBoughtAt() == null ? this.boughtAt : request.getBoughtAt();
        this.manufacturer = manufacturer == null ? this.manufacturer : manufacturer;
        this.price = request.getPrice() == null ? this.price : request.getPrice();
        this.storeName = request.getStoreName() == null ? this.storeName : request.getStoreName();
        this.shortReview = request.getShortReview() == null ? this.shortReview : request.getShortReview();
        this.starPoint = request.getStarPoint() == null ? this.starPoint : request.getStarPoint();
        this.goodPoint = request.getGoodPoint() == null ? this.goodPoint : request.getGoodPoint();
        this.badPoint = request.getBadPoint() == null ? this.badPoint : request.getBadPoint();
    }
}
