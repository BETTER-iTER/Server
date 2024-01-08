package com.example.betteriter.fo_domain.review.repository.custom;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.global.constant.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomReviewRepository {

    Slice<Review> findReviewsBySearch(String name, String sort, Pageable pageable, Category category, Boolean expert);
}
