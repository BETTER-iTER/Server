package com.example.betteriter.fo_domain.search.service;

import com.example.betteriter.fo_domain.search.dto.SearchLogRedis;
import com.example.betteriter.fo_domain.search.repository.SearchRepository;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
    private final static Long RECENT_SEARCH_LOG_SIZE = 7L;
    private final static String KEY_SUFFIX = "SearchLog";

    private final SearchRepository searchRepository;

    private final UserService userService;
    private final RedisTemplate<String, SearchLogRedis> searchRecentLogRedisRedisTemplate;

    /* 최근 검색어 조회(7) */
    @Transactional(readOnly = true)
    public List<String> getRecentSearch() {
        User user = this.userService.getCurrentUser();
        String key = searchLogKey(user);

        return Objects.requireNonNull(this.searchRecentLogRedisRedisTemplate.opsForList()
                        .range(key, 0, RECENT_SEARCH_LOG_SIZE)).stream()
                .map(SearchLogRedis::getName)
                .collect(Collectors.toList());
    }


    /* 최근 검색어 추가 */
    @Transactional
    public void addRecentSearch(String name) {
        User user = this.userService.getCurrentUser();
        String key = searchLogKey(user);

        SearchLogRedis value = SearchLogRedis.builder()
                .name(name)
                .createdAt(LocalDateTime.now().toString())
                .build();

        Long size = this.searchRecentLogRedisRedisTemplate.opsForList().size(key);
        if (Objects.equals(size, RECENT_SEARCH_LOG_SIZE)) {
            this.searchRecentLogRedisRedisTemplate.opsForList().rightPop(key);
        }

        this.searchRecentLogRedisRedisTemplate.opsForList().leftPush(key, value);
    }

    private String searchLogKey(User user) {
        return KEY_SUFFIX + user.getId();
    }
}
