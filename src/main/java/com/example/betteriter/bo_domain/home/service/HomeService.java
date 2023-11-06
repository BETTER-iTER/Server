package com.example.betteriter.bo_domain.home.service;

import com.example.betteriter.bo_domain.home.dto.GetHomeResponseDto;
import com.example.betteriter.bo_domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final NewsService newsService;
    
    @Transactional(readOnly = true)
    public GetHomeResponseDto getHome() {
        return null;
    }
}
