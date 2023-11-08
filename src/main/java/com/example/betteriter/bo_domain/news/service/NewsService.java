package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.repository.NewsRepository;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsService {
    private final UserService userService;
    private final NewsRepository newsRepository;

    @Transactional
    public Long createITNews(CreateITNewsRequestDto request) {
        return this.newsRepository.save(request.toEntity(this.userService.getCurrentUser())).getId();
    }

    @Transactional(readOnly = true)
    public List<ITNewsResponseDto> getItNews() {
        return this.newsRepository.findTop5ByOrderByUpdatedAtDesc().stream()
                .map(News::from)
                .collect(Collectors.toList());
    }
}
