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

    List<Review> findReviewByWriter(Users users);

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
            "LEFT JOIN r.specData sd " +
            "WHERE r.category = :category " +
            "GROUP BY r.id " +
            "ORDER BY (COALESCE(COUNT(rl),0) + COALESCE(COUNT(rs),0)) DESC, " +
            "r.createdAt DESC")
    Slice<Review> findReviewByCategory(Category category, Pageable pageable);
}
