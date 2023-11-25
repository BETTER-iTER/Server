package com.example.betteriter.fo_domain.comment.validation.validator;

import com.example.betteriter.fo_domain.comment.repository.CommentValidRepository;
import com.example.betteriter.fo_domain.comment.validation.annotation.ExistComment;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ExistCommentValidator implements ConstraintValidator<ExistComment, Long> {

    private final CommentValidRepository commentValidRepository;

    @Override
    public void initialize(ExistComment constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isExist = this.commentValidRepository.existsById(value);

        if (!isExist) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.COMMENT_NOT_EXIST.getMessage())
                    .addConstraintViolation();
        }

        return isExist;
    }
}
