package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsConnectorImpl implements NewsConnector {
    private final NewsRepository newsRepository;

    /* 홈 화면에서 최신순 5개 조회 */
    @Override
    public List<ITNewsResponseDto> getTop5ITNews() {
        return this.newsRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(News::from)
                .collect(Collectors.toList());
    }
}
