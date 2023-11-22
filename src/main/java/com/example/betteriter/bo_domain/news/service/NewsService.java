package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.bo_domain.news.dto.CreateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.dto.UpdateITNewsRequestDto;
import com.example.betteriter.bo_domain.news.exception.NewsHandler;
import com.example.betteriter.bo_domain.news.repository.NewsRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.error.exception.ErrorCode;
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

    /* ITNews 생성 */
    @Transactional
    public Long createITNews(CreateITNewsRequestDto request) {
        return this.newsRepository.save(request.toEntity(this.userService.getCurrentUser())).getId();
    }

    /* ITNews 조회 */
    @Transactional(readOnly = true)
    public List<ITNewsResponseDto> getITNews() {
        return this.newsRepository.findITNewsOrderByCreatedAt();
    }

    /* ITNews 삭제 */
    @Transactional
    public void deleteITNews(Long id) {
        this.newsRepository.delete(this.getNews(id));
    }


    /* ITNews 수정 */
    @Transactional
    public void updateITNews(Long id, UpdateITNewsRequestDto request) {
        Users currentUsers = this.userService.getCurrentUser();
        this.getNews(id).update(request, currentUsers);
    }


    public List<ITNewsResponseDto> getItNewsForHome() {
        return this.newsRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(News::from)
                .collect(Collectors.toList());
    }

    private News getNews(Long id) {
        return this.newsRepository.findById(id)
                .orElseThrow(() -> new NewsHandler(ErrorCode._NEWS_NOT_FOUND));
    }
}
