package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findFirst7ByCategoryOrderByCreatedAtDesc(Category category);

    List<Review> findFirst7ByWriterInOrderByCreatedAtDesc(List<Users> writers);

    // sum(count (r.reviewLiked),count(r.reviewScraped))
    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewScraped rs " +
            "LEFT JOIN r.reviewLiked rl " +
            "GROUP BY r.id " +
            "ORDER BY (COALESCE(COUNT(rs), 0) + COALESCE(COUNT(rl), 0)) DESC")
    List<Review> findTop7ReviewHavingMostScrapedAndLiked(Pageable pageable);

    @Query("select r from REVIEW r " +
            "LEFT JOIN r.reviewLiked rl " +
            "LEFT JOIN r.reviewScraped rs " +
            "WHERE r.category = :category " +
            "GROUP BY r.id " +
            "ORDER BY (COALESCE(COUNT(rl),0) + COALESCE(COUNT(rs),0)) DESC, " +
            "r.createdAt DESC")
    Slice<Review> findReviewByCategory(Category category, Pageable pageable);


    List<Review> findFirst20ByOrderByClickCountDescCreatedAtDesc();

    Slice<Review> findByProductNameOrderByCreatedAtDesc(String name, Pageable pageable);


    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewLiked rl " +
            "LEFT JOIN r.reviewScraped rs " +
            "WHERE rs.users = :user AND " +
            "   r.status = 'ACTIVE'" +
            "GROUP BY r.id " +
            "ORDER BY r.createdAt DESC")
    List<Review> findAllByTargetId(Long id);

    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewLiked rl " +
            "LEFT JOIN r.reviewScraped rs " +
            "WHERE rs.users = :user AND " +
            "   r.status != 'DELETED'" +
            "GROUP BY r.id " +
            "ORDER BY r.createdAt DESC")
    List<Review> findAllByUser(Long id);

    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewLiked rl " +
            "LEFT JOIN r.reviewScraped rs " +
            "WHERE rs.users = :user AND " +
            "   r.status = 'ACTIVE'" +
            "GROUP BY r.id " +
            "ORDER BY r.createdAt DESC")
    List<Review> findAllByReviewScrapedUser(Users user);

    @Query("SELECT r FROM REVIEW r " +
            "LEFT JOIN r.reviewLiked rl " +
            "LEFT JOIN r.reviewScraped rs " +
            "WHERE rl.users = :user AND " +
            "   r.status = 'ACTIVE'" +
            "GROUP BY r.id " +
            "ORDER BY r.createdAt DESC")
    List<Review> findAllByReviewLikedUser(Users user);


    /* 좋아요 수 많은 리뷰 조회 */
    Slice<Review> findByProductNameOrderByLikedCountDescCreatedAtDesc(String name, Pageable pageable);

    /* 스크랩 수 많은 리뷰 조회 */
    Slice<Review> findByProductNameOrderByScrapedCountDescCreatedAtDesc(String name, Pageable pageable);

}
