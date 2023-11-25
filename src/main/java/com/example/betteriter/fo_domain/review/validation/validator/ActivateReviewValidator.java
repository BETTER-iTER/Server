package com.example.betteriter.fo_domain.review.validation.validator;

import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.validation.annotation.ActivatedReview;
import com.example.betteriter.fo_domain.review.validation.annotation.ExistReview;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ActivateReviewValidator implements ConstraintValidator<ActivatedReview, Long> {

    final private ReviewRepository reviewRepository;

    @Override
    public void initialize(ActivatedReview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isExistReview = this.reviewRepository.existsById(value);

        if(!isExistReview) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.REVIEW_NOT_ACTIVATE.toString())
                    .addConstraintViolation();
        }

        return isExistReview;
    }
}
