package com.example.betteriter.fo_domain.comment.validation.annotation;

import com.example.betteriter.fo_domain.comment.validation.validator.ExistCommentValidator;
import com.example.betteriter.fo_domain.comment.validation.validator.UserHaveCommentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserHaveCommentValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserHaveComment {

    String message() default "댓글을 작성한 유저가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
