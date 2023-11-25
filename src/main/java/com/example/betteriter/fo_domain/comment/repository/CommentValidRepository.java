package com.example.betteriter.fo_domain.comment.repository;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentValidRepository extends JpaRepository<Comment, Long> {

    boolean existsByIdAndUser_id(Long value, Long user_id);
}
