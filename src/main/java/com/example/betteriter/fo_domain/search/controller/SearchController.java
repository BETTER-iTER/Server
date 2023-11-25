package com.example.betteriter.fo_domain.search.controller;

import com.example.betteriter.fo_domain.search.service.SearchService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * - 검색 기능을 위한 API
 **/
@Tag(name = "SearchController", description = "Search API")
@Slf4j
@RequestMapping("/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    private final SearchService searchService;

    /**
     * - 최근 검색어 조회
     **/
    @GetMapping("/recent")
    public ResponseDto<List<String>> getRecentSearch() {
        return ResponseDto.onSuccess(this.searchService.getRecentSearch());
    }

    /**
     * - 최근 검색어 추가
     **/
    @PostMapping("/recent")
    public ResponseDto<Void> addRecentSearch(
            @RequestParam String search
    ) {
        this.searchService.addRecentSearch(search);
        return ResponseDto.onSuccess(null);
    }

    /**
     * - 최근 검색어 삭제
     **/
    @DeleteMapping("/recent")
    public ResponseDto<Void> deleteRecentSearch(
            @RequestParam String search
    ) {
        this.searchService.deleteRecentSearch(search);
        return ResponseDto.onSuccess(null);
    }
}
