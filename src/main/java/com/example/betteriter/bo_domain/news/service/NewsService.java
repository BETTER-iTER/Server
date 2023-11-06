package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.repository.NewsRepository;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsService {
    private final UserService userService;
    private final NewsRepository newsRepository;

    public Long createITNews(CreateITNewsRequestDto request) {
        return this.newsRepository.save(
                        request.toEntity(this.userService.getCurrentUser()))
                .getId();
    }
}
