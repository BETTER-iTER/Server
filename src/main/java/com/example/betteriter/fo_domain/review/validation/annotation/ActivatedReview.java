package com.example.betteriter.fo_domain.review.validation.annotation;

import com.example.betteriter.fo_domain.review.validation.validator.ActivateReviewValidator;
import com.example.betteriter.fo_domain.review.validation.validator.ExistReviewValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActivateReviewValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivatedReview {
    String message() default "삭제되었거나 비공개된 리뷰입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
