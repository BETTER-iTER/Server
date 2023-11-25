package com.example.betteriter.fo_domain.comment.repository;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.global.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentReadRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long reviewId);

    @Query( "select c " +
            "from COMMENT c " +
            "where c.review.id = :reviewId and " +
            "      c.status != 'INACTIAVE'" +
            "order by c.orderNum desc")
    List<Comment> findAllByReviewId(Long reviewId);

    @Query( "select c " +
            "from COMMENT c " +
            "where c.review.id = :reviewId and " +
            "      c.status = 'ACTIVE'" +
            "order by c.createdAt desc")
    List<Comment> findAllByReviewIdAndStatusOrderByCreatedAt(Long reviewId);
}
