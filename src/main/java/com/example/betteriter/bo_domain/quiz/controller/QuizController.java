package com.example.betteriter.bo_domain.quiz.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "QuizController", description = "Quiz API")
@Slf4j
@RequestMapping("/quiz")
@RequiredArgsConstructor
@RestController
public class QuizController {
}
