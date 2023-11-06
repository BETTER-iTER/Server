package com.example.betteriter.bo_domain.home.service;

import com.example.betteriter.bo_domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final NewsService newsService;

//    @Transactional(readOnly = true)
//    public GetHomeResponseDto getHome() {
//        return null;
//    }
}
