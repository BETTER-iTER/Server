package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
}
