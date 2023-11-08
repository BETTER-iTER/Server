package com.example.betteriter.bo_domain.news.controller;

import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.dto.UpdateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.service.NewsService;
import com.example.betteriter.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequestMapping("/news")
@RequiredArgsConstructor
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseDto<Long> createITNews(
            @Valid @RequestBody CreateITNewsRequestDto request
    ) {
        return ResponseDto.onSuccess(this.newsService.createITNews(request));
    }

    @GetMapping
    public ResponseDto<List<ITNewsResponseDto>> getITNews() {
        return ResponseDto.onSuccess(this.newsService.getITNews());
    }

    @PutMapping("/{id}")
    public ResponseDto<Void> updateITNews(
            @Valid @RequestBody UpdateITNewsRequestDto request,
            @PathVariable Long id) {
        this.newsService.updateITNews(id, request);
        return ResponseDto.onSuccess();
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Void> deleteITNews(@PathVariable Long id) {
        this.newsService.deleteITNews(id);
        return ResponseDto.onSuccess();
    }

}
