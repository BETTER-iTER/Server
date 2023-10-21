package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
