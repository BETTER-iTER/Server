package com.example.betteriter.fo_domain.review.validation.validator;

import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.validation.annotation.ExistReview;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ExistReviewValidator implements ConstraintValidator<ExistReview, Long> {

    final private ReviewRepository reviewRepository;

    @Override
    public void initialize(ExistReview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isExistReview = this.reviewRepository.existsById(value);

        if(!isExistReview) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.REVIEW_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isExistReview;
    }
}
