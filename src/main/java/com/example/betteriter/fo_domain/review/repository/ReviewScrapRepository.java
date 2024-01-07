package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewScrapRepository extends JpaRepository<ReviewScrap, Long> {
    Optional<ReviewScrap> findByReviewAndUsers(Review review, Users users);
}
