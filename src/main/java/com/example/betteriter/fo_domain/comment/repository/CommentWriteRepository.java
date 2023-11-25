package com.example.betteriter.fo_domain.comment.repository;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentWriteRepository extends JpaRepository<Comment, Long> {

    // 명시적 댓글 삭제 기능
    @Modifying
    @Query("update COMMENT c " +
            "set c.status = 'DELETED'" +
            "where c.id = :commentId")
    int explicitDeleteById(Long commentId);
}
