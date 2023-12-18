package com.example.betteriter.fo_domain.comment.validation.annotation;

import com.example.betteriter.fo_domain.comment.validation.validator.ExistCommentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistCommentValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistComment {

    String message() default "존재하지 않는 댓글입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
