package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;

import java.util.List;

public interface NewsConnector {
    List<ITNewsResponseDto> getTop5ITNews();
}
