package com.example.betteriter.fo_domain.comment.controller;


import com.example.betteriter.fo_domain.comment.dto.CommentRequest;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.comment.service.CommentService;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.betteriter.global.error.exception.ErrorCode._METHOD_ARGUMENT_ERROR;


@Tag(name = "CommentController", description = "Comment API")
@Slf4j
@RequestMapping("/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    /**
     * [댓글 작성]
     * - /comment/create
     * 1. 1 댑스 이상 불가
     */
    @PostMapping("/create")
    public ResponseDto<Long> createComment(
            @Valid @RequestBody CommentRequest.CreateCommentRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ResponseDto.onSuccess(this.commentService.createComment(request));
    }

    /**
     * [댓글 삭제]
     * - /comment/delete
     * 1. 댓글 작성자만 수정 가능
     */
    @PostMapping("/delete")
    public ResponseDto<Long> deleteComment(
            @Valid @RequestBody CommentRequest.DeleteCommentRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ResponseDto.onSuccess(this.commentService.deleteComment(request));
    }

    /**
     * [댓글 조회]
     * - /comment/read/{review_id}
     */
    @GetMapping("/read/{review_id}")
    public ResponseDto<CommentResponse.ReadCommentDto> readComment(
            @PathVariable Long review_id
    ) {
        return ResponseDto.onSuccess(this.commentService.readComment(review_id));
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new ReviewHandler(_METHOD_ARGUMENT_ERROR);
        }
    }

}
