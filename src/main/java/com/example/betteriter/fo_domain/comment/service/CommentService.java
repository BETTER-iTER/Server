package com.example.betteriter.fo_domain.comment.service;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.comment.dto.CommentRequest;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.comment.repository.CommentRepository;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * - 댓글 관련 로직을 담고 있는 서비스
 * - 댓글 작성, 댓글 수정, 댓글 삭제 등
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * [댓글 작성]
     */
    @Transactional
    public Long createComment(CommentRequest.CreateCommentRequestDto commentCreateDto) {
        Review review = this.reviewService.findReviewById(commentCreateDto.getReview_id());
        User writer = this.userService.getCurrentUser();
        Comment comment = this.commentRepository.save(commentCreateDto.toEntity(review, writer));

        return comment.getId();
    }

    /**
     * [댓글 삭제]
     */
    @Transactional
    public Long deleteComment(CommentRequest.DeleteCommentRequestDto request) {
        Comment comment = this.commentRepository.findCommentById(request.getReview_id());
        this.commentRepository.delete(comment);

        return comment.getId();
    }

    /**
     * [댓글 조회]
     */
    public CommentResponse.ReadCommentDto readComment(Long reviewId) {
        List<Comment> commentList = this.commentRepository.findAllByReviewId(reviewId);

        return CommentResponse.ReadCommentDto.builder()
                .reviewId(reviewId)
                .commentList(commentList)
                .build();
    }
}
