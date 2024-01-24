package com.example.betteriter.fo_domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.repository.custom.CustomReviewRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Category;

import io.lettuce.core.dynamic.annotation.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    List<Review> findFirst7ByCategoryOrderByCreatedAtDesc(Category category);

    /* 특정 회원이 팔로우 하는 팔로잉들이 작성한 모든 리뷰 최신순으로 7개 조회 */
    @Query("SELECT r from REVIEW r WHERE r.writer " +
            "IN (SELECT u FROM USERS u WHERE u.id " +
            "IN (SELECT f.followee from FOLLOW f where f.follower = :users))" +
            "ORDER BY r.createdAt DESC ")
    List<Review> findFirst7WrittenByFollowingCreatedAtDesc(@Param("users") Users users, Pageable pageable);

    // sum(count (r.reviewLiked),count(r.reviewScraped))
    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewScraped rs " +
            "LEFT JOIN r.reviewLiked rl " +
            "GROUP BY r.id " +
            "ORDER BY (COALESCE(COUNT(rs), 0) + COALESCE(COUNT(rl), 0)) DESC")
    List<Review> findTop7ReviewHavingMostScrapedAndLiked(Pageable pageable);

    @Query("SELECT r from REVIEW r WHERE r.category =:category " +
            "ORDER BY (r.scrapedCount + r.likedCount) DESC, r.createdAt DESC")
    Slice<Review> findReviewByCategoryOrderByScrapedCountAndLikedCount(@Param("category") Category category, Pageable pageable);


    List<Review> findFirst20ByOrderByClickCountDescCreatedAtDesc();

    Slice<Review> findByProductNameOrderByCreatedAtDesc(String name, Pageable pageable);


    @Query("SELECT r FROM REVIEW r " +
            "WHERE r.writer = :user " +
            "ORDER BY r.id DESC")
    List<Review> findAllByTargetUser(@Param("user") Users user);

    @Query("SELECT r FROM REVIEW r WHERE r.writer = :user ORDER BY r.createdAt desc ")
    Page<Review> findAllByUser(@Param("user") Users user, Pageable pageable);

    @Query("SELECT r FROM REVIEW r LEFT JOIN r.reviewScraped rs WHERE rs.users = :user ORDER BY r.id DESC")
    Page<Review> findAllByReviewScrapedUser(@Param("user") Users user, Pageable pageable);

    @Query("select rl.review from REVIEW_LIKE  rl where rl.users = :user order by rl.createdAt desc")
    Page<Review> findAllByReviewLikedUser(@Param("user") Users user, Pageable pageable);

    /* 좋아요 수 많은 리뷰 조회 */
    Slice<Review> findByProductNameOrderByLikedCountDescCreatedAtDesc(String name, Pageable pageable);

    /* 스크랩 수 많은 리뷰 조회 */
    Slice<Review> findByProductNameOrderByScrapedCountDescCreatedAtDesc(String name, Pageable pageable);

    /* 동일한 제품명 리뷰 조회 (좋아요 + 스크랩 수 많은 순) */
    @Query(value = "select * from review where product_name = :productName " +
            "order by (scraped_cnt + liked_cnt) DESC, created_at DESC LIMIT 4", nativeQuery = true)
    List<Review> findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc(@Param("productName") String productName);

    /* 같은 스펙을 가지는 리뷰 조회 (좋아요 + 스크랩 수 많은 순) */
    @Query("SELECT r from REVIEW r JOIN r.specData rsd JOIN rsd.specData sd " +
            "WHERE rsd.id = :reviewSpecDataId " +
            "AND sd.data IN (SELECT sd.data FROM ReviewSpecData rsd JOIN rsd.specData sd WHERE rsd.id = :reviewSpecDataId) " +
            "ORDER BY (r.likedCount + r.scrapedCount) DESC")
    List<Review> findRelatedReviewsByReviewSpecData(@Param("reviewSpecData") Long reviewSpecData, Pageable pageable);

    /* 리뷰의 작성자 팔로워가 많은 순으로 리뷰 조회 */
    @Query("SELECT r from REVIEW r JOIN r.writer w LEFT JOIN FOLLOW f " +
            "ON f.followee = w WHERE r.productName = :productName " +
            "GROUP BY r ORDER BY COUNT(f.id) DESC ,r.createdAt DESC")
    Slice<Review> findByProductNameOrderByMostWriterFollower(@Param("productName") String productName, Pageable pageable);

    /* 동일 카테고리 중 좋아요 + 스크랩 많은 순 리뷰 조회 */
    @Query(value = "SELECT * FROM review " +
            "WHERE category = :category " +
            "ORDER BY (scraped_cnt + liked_cnt) DESC, created_at DESC", nativeQuery = true)
    List<Review> findByProductNameOrderByScrapedCountAndLikedCountDescCreatedAtDesc(@Param("category") Category category);

    @Query("SELECT COUNT(r) FROM REVIEW r WHERE r.writer = :user")
    Integer countByWriter(Users user);

    @Query("SELECT COALESCE(COUNT(r), 0) FROM REVIEW r " +
            "LEFT JOIN REVIEW_SCRAP rs on r.id = rs.id " +
            "WHERE rs.users = :user")
    Integer countByMyScrap(Users user);

    @Query("SELECT COALESCE(COUNT(rl), 0) FROM REVIEW r " +
            "LEFT JOIN REVIEW_LIKE rl on r.id = rl.id " +
            "WHERE r.writer = :user")
    Integer countByReviewLiked(Users user);

    @Query("SELECT COALESCE(COUNT(rs), 0) FROM REVIEW r " +
            "LEFT JOIN REVIEW_SCRAP rs on r.id = rs.id " +
            "WHERE r.writer = :user")
    Integer countByReviewScraped(Users user);
}
