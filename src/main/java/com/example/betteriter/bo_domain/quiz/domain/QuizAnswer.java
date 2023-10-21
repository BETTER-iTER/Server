package com.example.betteriter.bo_domain.quiz.domain;


import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "QUIZ_ANSWER")
public class QuizAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "quiz_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "is_right", nullable = false)
    private Boolean isRight;
}
