package com.example.betteriter.bo_domain.news.repository;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.betteriter.global.constant.RoleType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("최신순 뉴스 5개를 저장하고 조회한다.")
    void findTop5ByOrderByCreatedAtDesc() {
        // given
        Users user = Users.builder()
                .email("danaver12@daum.net")
                .password("password")
                .oauthId("123")
                .roleType(ROLE_ADMIN)
                .build();

        this.usersRepository.save(user);

        News news01 = News.builder()
                .writer(user)
                .title("title")
                .content("content")
                .imageUrl("imageUrl")
                .newsUrl("newsUrl")
                .build();

        News news02 = News.builder()
                .writer(user)
                .title("title")
                .content("content")
                .imageUrl("imageUrl")
                .newsUrl("newsUrl")
                .build();

        News news03 = News.builder()
                .writer(user)
                .title("title")
                .content("content")
                .imageUrl("imageUrl")
                .newsUrl("newsUrl")
                .build();

        this.newsRepository.saveAll(List.of(news01, news02, news03));
        // when
        System.out.println("====before find====");
        List<News> result = this.newsRepository.findTop5ByOrderByCreatedAtDesc();
        // then
        assertThat(result).hasSize(3);
        for (News news : result) {
            System.out.println("news = " + news);
        }
    }
}
