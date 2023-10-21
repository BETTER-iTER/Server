package com.example.betteriter.bo_domain.quiz.repository;

import com.example.betteriter.bo_domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
