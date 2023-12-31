package com.example.betteriter.fo_domain.comment.controller;


import com.example.betteriter.fo_domain.comment.dto.CommentRequest;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.comment.service.CommentService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Tag(name = "CommentControllers", description = "Comment API")
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
    @Operation(summary = "댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS_200", description = "댓글 작성 성공", content = @Content),
            @ApiResponse(responseCode = "REVIEW_NOT_EXIST_404", description = "리뷰가 존재하지 않음", content = @Content),
    })
    public ResponseDto<Long> createComment(
            @Valid @RequestBody CommentRequest.CreateCommentDto request
    ) {
        return ResponseDto.onSuccess(this.commentService.createComment(request));
    }

    /**
     * [댓글 삭제]
     * - /comment/delete
     * 1. 댓글 작성자만 수정 가능
     */
    @PostMapping("/delete")
    @Operation(summary = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS_200", description = "댓글 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "COMMENT_NOT_EXIST_404", description = "댓글이 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "USER_NOT_HAVE_COMMENT_401", description = "댓글 작성자가 아님", content = @Content),
    })
    public ResponseDto<CommentResponse.DeleteCommentDto> deleteComment(
            @Valid @RequestBody CommentRequest.DeleteCommentDto request
    ) {
        return ResponseDto.onSuccess(this.commentService.deleteComment(request));
    }
}
