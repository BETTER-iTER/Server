package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select ri.imgUrl from REVIEW_IMAGE ri where ri.review = :review and ri.orderNum = 0")
    String findFirstImageWithReview(@Param("review") Review review);
}
