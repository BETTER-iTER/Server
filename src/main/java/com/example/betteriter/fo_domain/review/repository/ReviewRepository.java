package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.global.constant.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findFirst7ByCategoryOrderByCreatedAtDesc(Category category);

    List<Review> findReviewByWriter(User user);

    List<Review> findFirst7ByWriterInOrderByCreatedAtDesc(List<User> writers);

    // sum(count (r.reviewLiked),count(r.reviewScraped))
    @Query("select r from REVIEW r")
    List<Review> findReviewHavingMostScrapedAndLiked();
}
