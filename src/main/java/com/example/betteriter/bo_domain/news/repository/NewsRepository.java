package com.example.betteriter.bo_domain.news.repository;

import com.example.betteriter.bo_domain.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // UpdatedAt 기준 최신 5개 조회
    List<News> findTop5ByOrderByUpdatedAtDesc();

}
