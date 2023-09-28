package com.example.betteriter.global.filter;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.config.security.UserAuthentication;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.user.domain.User;
import com.example.betteriter.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;


/**
 * - JWT 기반의 인증 방식에서 핵심이 되는 인증 필터
 * # API 요청에 있어서 JWT 가 요청 헤더에 담겨서 올 때, 유효성 검증/인증 성공/인증 실패 처리
 * # 동시에 Refresh Token 이 같이 오는 경우, Access Token + Refresh Token 재발급
 * <p>
 * << AuthenticationException >>
 * 1. 토큰의 유효성이 만료되었거나 유효하지 않은 경우 : BadCredentialsException
 * 2. 사용자 정보가 없는 경우 : UsernameNotFoundException
 * 3. 토큰이 없는 경우 : AuthenticationCredentialsNotFoundException
 **/
@Component
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
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            // 이후 현재 필터 진행 방지
            return;
        }
        /*
          -  사용자 요청에서 Refresh Token 추출
          -> Refresh Token 존재하는 경우는 Access Token(만료된) 재발급인 경우밖에 없음
          -> 위의 경우 제외하고 모든 요청에서 Refresh Token == null
        */
        String refreshToken = this.jwtUtil.extractRefreshToken(request);

        // Case 01) Access Token 재발급인 경우(Authorization Header Access Token 유효성 x)
        if (refreshToken != null) {
            try {
                String accessToken = this.jwtUtil.extractAccessToken(request)
                        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("There is no Access Token"));
                reissueAccessTokenAndRefreshToken(response, accessToken, refreshToken);
            } catch (AuthenticationException exception) {
                log.info("JwtAuthentication UnauthorizedUserException!");
                request.setAttribute("UnauthorizedUserException", exception);
            }
        }

        // Case 02) 일반 API 요청인 경우
        else {
            checkAccessTokenAndAuthentication(request, response, filterChain);
            log.info("API 요청 예외 없이 정상 처리");
        }

        log.info("다음 필터로 넘어가기전");
        log.info("hh");
        // Authentication Exception 없이 정상 인증처리 된 경우
        filterChain.doFilter(request, response);
    }

    // Case 01) Access Token + Refresh Token 재발급 메소드
    private void reissueAccessTokenAndRefreshToken(HttpServletResponse response,
                                                   String accessToken,
                                                   String refreshToken) throws AuthenticationException {

        log.info("reissueAccessTokenAndRefreshToken() called!");
        // 요청으로 받은 Refresh Token 의 유효성 검증 및 서버 Refresh Token 과 비교

        try {
            if (validateRefreshToken(refreshToken) || isRefreshTokenMatch(refreshToken, accessToken)) {
                String newAccessToken = this.jwtUtil.createAccessToken(this.jwtUtil.getUserIdFromToken(accessToken));
                String newRefreshToken = this.reIssueRefreshToken(this.jwtUtil.getUserIdFromToken(accessToken));
                sendAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
            }
        } catch (IllegalArgumentException | JwtException exception) {
            throw new BadCredentialsException("Authentication Error Occurred");
        }
    }

    /**
     * - 재 발급한 refresh & access token 응답으로 보내는 메소드
     * 1. 상태 코드 설정
     * 2. 응답 헤더에 설정 (jwtProperties 에서 정보 가져옴)
     **/
    private void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(SC_OK);
        response.setHeader(jwtProperties.getAccessHeader(), accessToken);
        response.setHeader(jwtProperties.getRefreshHeader(), refreshToken);
        log.info("Access Token, Refresh token 응답 헤더 설정 완료");
    }


    private boolean isRefreshTokenMatch(String refreshToken, String accessToken) {
        String userId = this.jwtUtil.getUserIdFromToken(accessToken);
        String storedRefreshToken = this.redisUtil.getData(userId);
        return refreshToken.equals(storedRefreshToken);
    }

    private boolean validateRefreshToken(String refreshToken) {
        return this.jwtUtil.validateToken(refreshToken);
    }

    /**
     * - refresh token 재발급 하는 메소드
     * 1. 새로운 Refresh Token 발급
     * 2. 해당 Key 에 해당하는 Redis Value 업데이트
     **/
    private String reIssueRefreshToken(String userId) {
        this.redisUtil.deleteData(userId); // 기존 refresh token 삭제
        String reIssuedRefreshToken = jwtUtil.createRefreshToken();
        this.redisUtil.setData(userId, reIssuedRefreshToken); // refresh token 저장
        return reIssuedRefreshToken;
    }

    /**
     * - 일반 API 호출을 처리하는 메소드
     * 1. Authorization 헤더의 access token 검증
     * 2. accessToken 으로부터 UserId 가져와서 userRepository 조회
     * 3. Authentication 객체 생성 및 Security Context에 저장
     **/
    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   FilterChain filterChain) {

        log.info("checkAccessTokenAndAuthentication() called!");
        try {
            String accessToken = this.jwtUtil.extractAccessToken(request)
                    .orElseThrow(() -> new JwtException("jwtException"));

            User user = this.userRepository.findById(Long.valueOf(this.jwtUtil.getUserIdFromToken(accessToken)))
                    .orElseThrow(() -> new UsernameNotFoundException("usernameNotFoundException"));

            // SecurityContext 에 인증된 Authentication 저장
            UserAuthentication authentication = new UserAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IllegalArgumentException | JwtException | UsernameNotFoundException exception) {
            request.setAttribute("UnauthorizedUserException", exception);
        }
    }
}