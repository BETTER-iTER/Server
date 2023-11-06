package com.example.betteriter.bo_domain.news.controller;

import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 홈 화면에 조회되는 최신 IT 뉴스 관련 컨트롤러
 **/
@Slf4j
@RequestMapping("/news")
@RequiredArgsConstructor
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<Long> createITNews(
            @Valid @RequestBody CreateITNewsRequestDto request
    ) {
        return new ResponseEntity<>(
                this.newsService.createITNews(request), HttpStatus.CREATED);
    }
}
