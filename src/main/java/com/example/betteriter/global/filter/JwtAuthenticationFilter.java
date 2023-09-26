package com.example.betteriter.global.filter;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.config.security.UserAuthentication;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.user.domain.User;
import com.example.betteriter.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * - JWT 기반의 인증 방식에서 핵심이 되는 인증 필터
 * # API 요청에 있어서 JWT 가 요청 헤더에 담겨서 올 때, 유효성 검증/인증 성공/인증 실패 처리
 * # 동시에 Refresh Token 이 같이 오는 경우, Access Token + Refresh Token 재발급
 **/
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // CORS pre-light 요청
        if (request.getMethod().equals("OPTIONS")) {
            return;
        }

        /**
         * - step 01 : 사용자 요청에서 Refresh Token 추출
         * -> Refresh Token 존재하는 경우는 Access Token(만료된) 재발급인 경우밖에 없음
         * -> 위의 경우 제외하고 모든 요청에서 Refresh Token == null
         **/
        String refreshToken = this.jwtUtil.extractRefreshToken(request)
                .filter(this.jwtUtil::validateToken)
                .orElse(null);

        // Access Token 재발급인 경우
        if (refreshToken != null) {
            reissueAccessTokenAndRefreshToken(response, refreshToken);
        }

        // 일반 API 요청인 경우
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

    }

    // Access Token + Refresh Token 재발급 메소드
    private void reissueAccessTokenAndRefreshToken(HttpServletResponse response, String refreshToken) {
        Long userId = Long.valueOf(this.redisUtil.getData(refreshToken));
        this.userRepository.findById(userId)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(userId);
                    String reIssuedAccessToken = this.jwtUtil.createAccessToken(String.valueOf(userId)).getTokenValue();
                    sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
                });
    }

    /**
     * - refresh token 재발급 하는 메소드
     * 1. 새로운 Refresh Token 발급
     * 2. 해당 Key 에 해당하는 Redis Value 업데이트
     **/
    private String reIssueRefreshToken(Long userId) {
        String reIssuedRefreshToken = jwtUtil.createRefreshToken().getTokenValue();
        this.redisUtil.setData(String.valueOf(userId), reIssuedRefreshToken);
        return reIssuedRefreshToken;
    }

    /**
     * - 재 발급한 refresh & access token 응답으로 보내는 메소드
     * 1. 상태 코드 설정
     * 2. 응답 헤더에 설정 (jwtProperties 에서 정보 가져옴)
     **/
    private void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(jwtProperties.getAccessHeader(), accessToken);
        response.setHeader(jwtProperties.getRefreshHeader(), refreshToken);
        log.info("Access Token, Refresh token 응답 헤더 설정 완료");
    }


    /**
     * - 일반 API 호출을 처리하는 메소드
     * 1. Authorization 헤더의 access token 검증
     * 2. accessToken 으로부터 UserId 가져와서 userRepository 조회
     * 3. Authentication 객체 생성 및 Security Context에 저장
     **/
    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        this.jwtUtil.extractAccessToken(request)
                .filter(StringUtils::hasText)
                .filter(this.jwtUtil::validateToken)
                .flatMap(this.jwtUtil::getUserIdFromToken)
                .flatMap(userId -> this.userRepository.findById(Long.valueOf(userId)))
                .ifPresent(this::saveAuthentication);

        // 다음 Security Filter 호출
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(User user) {
        UserAuthentication userAuthentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
