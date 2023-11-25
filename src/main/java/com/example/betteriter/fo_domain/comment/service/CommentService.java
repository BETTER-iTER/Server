package com.example.betteriter.fo_domain.comment.service;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.comment.converter.CommentResponseConverter;
import com.example.betteriter.fo_domain.comment.converter.CommentConverter;
import com.example.betteriter.fo_domain.comment.dto.CommentRequest;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.comment.repository.CommentReadRepository;
import com.example.betteriter.fo_domain.comment.repository.CommentWriteRepository;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * - 댓글 관련 로직을 담고 있는 서비스
 * - 댓글 작성, 댓글 수정, 댓글 삭제 등
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentReadRepository commentReadRepository;
    private final CommentWriteRepository commentWriteRepository;

    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * [댓글 작성]
     * TODO: 이후 대댓글 기능 작성시 order_num과 group_id 관련 로직 추가 해야함
     */
    @Transactional
    public Long createComment(CommentRequest.CreateCommentDto request) {
        Review review = this.reviewService.findReviewById(request.getReview_id());
        User writer = this.userService.getCurrentUser();

        Comment comment = CommentConverter.toComment(
                request.getComment(),
                review, writer
        );

        comment = this.commentReadRepository.save(comment);
        return comment.getId();
    }

    /**
     * [댓글 삭제]
     */
    @Transactional
    public CommentResponse.DeleteCommentDto deleteComment(CommentRequest.DeleteCommentDto request) {
        int result = this.commentWriteRepository.explicitDeleteById(request.getComment_id());
        return CommentResponseConverter.toDeleteCommentResponse();
    }

    /**
     * [댓글 조회]
     */
    public List<Comment> readComment(Long reviewId) {
        return this.commentReadRepository.findAllByReviewIdAndStatusOrderByCreatedAt(reviewId);
    }
}
