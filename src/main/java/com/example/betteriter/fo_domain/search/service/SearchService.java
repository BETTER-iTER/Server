package com.example.betteriter.fo_domain.search.service;

import com.example.betteriter.fo_domain.search.dto.SearchLogRedis;
import com.example.betteriter.fo_domain.search.repository.SearchRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
    private final static Long RECENT_SEARCH_LOG_SIZE = 7L; // 최근 검색어 최대 갯수(7 개)
    private final static Long RECENT_SEARCH_LOG_EXPIRE = 604800000L; // 최근 검색어 유효 기간(7 일)
    private final static String KEY_SUFFIX = "RecentSearchLog"; // 최근 검색어 키

    private final SearchRepository searchRepository;

    private final UserService userService;
    private final RedisTemplate<String, SearchLogRedis> searchRecentLogRedisRedisTemplate;

    /* 최근 검색어 조회(7) */
    @Transactional(readOnly = true)
    public List<String> getRecentSearch() {
        Users users = this.userService.getCurrentUser();
        String key = searchLogKey(users);
        this.removeRecentSearchLog(key, users);

        return Objects.requireNonNull(this.searchRecentLogRedisRedisTemplate.opsForList()
                        .range(key, 0, RECENT_SEARCH_LOG_SIZE)).stream()
                .map(SearchLogRedis::getName)
                .collect(Collectors.toList());
    }


    /* 최근 검색어 추가 */
    @Transactional
    public void addRecentSearch(String name) {
        Users users = this.userService.getCurrentUser();
        String key = this.searchLogKey(users);
        this.removeRecentSearchLog(key, users); // 만료기간 지난 최근 검색어 삭제

        SearchLogRedis value = SearchLogRedis.builder()
                .name(name)
                .createdAt(LocalDateTime.now().toString())
                .build();

        if (this.checkRedisValueDuplication(key, name)) { // name 중복 여부 판단
            return;
        }
        Long size = this.searchRecentLogRedisRedisTemplate.opsForZSet().size(key);
        if (Objects.equals(size, RECENT_SEARCH_LOG_SIZE)) {
            this.searchRecentLogRedisRedisTemplate.opsForZSet().removeRange(key, 0, 1);
        }
        double score = System.currentTimeMillis() + RECENT_SEARCH_LOG_EXPIRE;
        this.searchRecentLogRedisRedisTemplate.opsForZSet().add(key, value, score);
    }

    /* 최근 검색어 삭제 */
    @Transactional
    public void deleteRecentSearch(String name) {
        Users users = this.userService.getCurrentUser();
        String key = this.searchLogKey(users);
        this.removeRecentSearchLog(key, users); // 만료기간 지난 최근 검색어 삭제

        Set<SearchLogRedis> allKeyValue
                = this.searchRecentLogRedisRedisTemplate.opsForZSet().range(key, 0, -1);  // 해당 key 에 대한 sorted set

        for (SearchLogRedis searchLogRedis : Objects.requireNonNull(allKeyValue)) {
            if (searchLogRedis.getName().equals(name)) {
                this.searchRecentLogRedisRedisTemplate.opsForZSet().remove(key, searchLogRedis);
            }
        }
    }

    private String searchLogKey(Users users) {
        return KEY_SUFFIX + users.getId();
    }

    private void removeRecentSearchLog(String key, Users users) {
        long currentTime = System.currentTimeMillis();
        this.searchRecentLogRedisRedisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTime); // 현재 시간보다 작은 score 가지는 value 모두 삭제
    }

    /**
     * - 최근 검색어 추가 name 중복 여부 판단
     **/
    private boolean checkRedisValueDuplication(String key, String name) {

        Set<SearchLogRedis> allKeyValue
                = this.searchRecentLogRedisRedisTemplate.opsForZSet().range(key, 0, -1);  // 해당 key 에 대한 sorted set

        if (Objects.requireNonNull(allKeyValue).isEmpty()) {
            return false;
        }
        for (SearchLogRedis searchLogRedis : Objects.requireNonNull(allKeyValue)) {
            if (searchLogRedis.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
