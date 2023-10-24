package com.example.betteriter.bo_domain.news.repository;

import com.example.betteriter.bo_domain.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

}
