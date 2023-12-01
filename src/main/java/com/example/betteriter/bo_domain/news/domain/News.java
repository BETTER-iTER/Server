package com.example.betteriter.bo_domain.news.domain;


import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.dto.UpdateITNewsRequestDto;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "NEWS")
public class News extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "writer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users writer;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "news_url")
    private String newsUrl;

    @Builder
    private News(Long id, Users writer, String title, String content,
                 String imageUrl, String newsUrl
    ) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
    }

    public ITNewsResponseDto from() {
        return ITNewsResponseDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .newsUrl(newsUrl)
                .build();
    }

    public void update(UpdateITNewsRequestDto request, Users newWriter) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.imageUrl = request.getImageUrl();
        this.newsUrl = request.getNewsUrl();
        this.writer = newWriter;
    }
}
