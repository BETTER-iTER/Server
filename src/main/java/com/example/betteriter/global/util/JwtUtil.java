package com.example.betteriter.global.util;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * - jwt 관련된 모든 작업을 위한 util
 * - payload : 회원 id
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;

    // HttpServletRequest 부터 Access Token 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(this.jwtProperties.getAccessHeader()))
                .filter(StringUtils::hasText)
                .filter(accessToken -> accessToken.startsWith(jwtProperties.getBearer()))
                .map(accessToken -> accessToken.replace(jwtProperties.getBearer(), ""));
    }

    // HttpServletRequest 부터 Refresh Token 추출
    public String extractRefreshToken(HttpServletRequest request) {
        return request.getHeader(this.jwtProperties.getRefreshHeader());
    }

    // access token 생성
    public String createAccessToken(String payload) {
        return createToken(payload, this.jwtProperties.getAccessExpiration());
    }


    // refresh token 생성
    public String createRefreshToken() {
        return createToken(UUID.randomUUID().toString(), this.jwtProperties.getRefreshExpiration());

    }


    // access token 으로부터 회원 아이디 추출
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception exception) {
            log.error("Access Token is not valid");
        }
        return null;
    }

    // kakao oauth 로그인 & 일반 로그인 시 jwt 응답 생성
    public UserServiceTokenResponseDto getServiceToken(User user) {
        String accessToken = this.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = this.createRefreshToken();

        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(this.jwtProperties.getAccessExpiration() / 1000);
        this.redisUtil.setData(String.valueOf(user.getId()), refreshToken);
        return UserServiceTokenResponseDto.builder()
                .accessToken(this.jwtProperties.getBearer() + " " + accessToken)
                .refreshToken(refreshToken)
                .expiredTime(expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isExisted(user.getNickName() != null)
                .build();
    }

    // token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.jwtProperties.getSecret()).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            log.warn("만료된 jwt 입니다.");
        } catch (UnsupportedJwtException exception) {
            log.warn("지원되지 않는 jwt 입니다.");
        } catch (IllegalArgumentException exception) {
            log.warn("jwt 에 오류가 존재합니다.");
        }
        return false;
    }

    // 실제 token 생성 로직
    private String createToken(String payload, Long tokenExpiration) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date tokenExpiresIn = new Date(new Date().getTime() + tokenExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
                .compact();
    }
}


