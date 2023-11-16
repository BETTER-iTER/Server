package com.example.betteriter.fo_domain.comment.controller;

import com.example.betteriter.fo_domain.comment.service.CommentService;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.betteriter.global.error.exception.ErrorCode._METHOD_ARGUMENT_ERROR;

@Slf4j
@RequestMapping("/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    /**
     * [댓글 작성]
     * - /comment
     * 1. 1 댑스 이상 불가
     */
    @PostMapping("/create")
    public ResponseDto<Long> createComment(
            @Valid @RequestBody CommentResponse.CreateCommentRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ResponseDto.onSuccess(this.commentService.createComment(request));
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new ReviewHandler(_METHOD_ARGUMENT_ERROR);
        }
    }

}
