package com.example.betteriter.bo_domain.news.repository;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // UpdatedAt 기준 최신 5개 조회
    List<News> findTop5ByOrderByCreatedAtDesc();

    @Query("select new com.example.betteriter.bo_domain.news.dto" +
            ".ITNewsResponseDto(n.id,n.title,n.content,n.imageUrl,n.newsUrl) " +
            "from NEWS n order by n.createdAt")
    List<ITNewsResponseDto> findITNewsOrderByCreatedAt();
}
