package com.example.betteriter.fo_domain.comment.validation.validator;

import com.example.betteriter.fo_domain.comment.repository.CommentValidRepository;
import com.example.betteriter.fo_domain.comment.validation.annotation.UserHaveComment;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UserHaveCommentValidator implements ConstraintValidator<UserHaveComment, Long> {
    private final CommentValidRepository commentValidRepository;
    private final UserService userService;

    @Override
    public void initialize(UserHaveComment constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Long userId = this.userService.getCurrentUser().getId();
        boolean isHas = this.commentValidRepository.existsByIdAndUser_id(value, userId);

        if (!isHas) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.COMMENT_NOT_HAVE.getMessage())
                    .addConstraintViolation();
        }

        return isHas;
    }
}