package com.example.betteriter.bo_domain.news.controller;

import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.dto.UpdateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.exception.NewsHandler;
import com.example.betteriter.bo_domain.news.service.NewsService;
import com.example.betteriter.global.common.response.ApiResponse;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Tag(name = "NewsController", description = "News API")
@Slf4j
@RequestMapping("/news")
@RequiredArgsConstructor
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ApiResponse<Long> createITNews(
            @Valid @RequestBody CreateITNewsRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ApiResponse.onSuccess(this.newsService.createITNews(request));
    }

    @GetMapping
    public ApiResponse<List<ITNewsResponseDto>> getITNews() {
        return ApiResponse.onSuccess(this.newsService.getITNews());
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateITNews(
            @Valid @RequestBody UpdateITNewsRequestDto request,
            @PathVariable Long id,
            BindingResult bindingResult) {
        this.checkRequestValidation(bindingResult);
        this.newsService.updateITNews(id, request);
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteITNews(@PathVariable Long id) {
        this.newsService.deleteITNews(id);
        return ApiResponse.onSuccess(null);
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new NewsHandler(ErrorStatus._METHOD_ARGUMENT_ERROR);
        }
    }
}
