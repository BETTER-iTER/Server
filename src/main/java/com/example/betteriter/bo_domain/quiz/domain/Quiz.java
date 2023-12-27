package com.example.betteriter.bo_domain.quiz.domain;


import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "QUIZ")
public class Quiz extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private final QuizType type = QuizType.FOUR_CHOICE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quiestion", nullable = false, unique = true)
    private String question;
    @Column(name = "answer", nullable = false)
    private String answer;
    @Column(name = "options", nullable = false)
    private String options;
}
