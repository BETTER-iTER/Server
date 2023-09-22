package com.example.betteriter.global.util;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.user.dto.oauth.ServiceToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    // access token 생성
    public ServiceToken createAccessToken(String payload) {
        String token = createToken(payload, this.jwtProperties.getAccessExpiration());
        return getToken(token, this.jwtProperties.getAccessExpiration());
    }


    // refresh token 생성
    public ServiceToken createRefreshToken() {
        String token = createToken(UUID.randomUUID().toString(), this.jwtProperties.getRefreshExpiration());
        return getToken(token, this.jwtProperties.getRefreshExpiration());
    }

    // 실제 token 생성 로직
    private String createToken(String payload, Long accessExpiration) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date accessTokenExpiresIn = new Date(new Date().getTime() + this.jwtProperties.getAccessExpiration());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
                .compact();
    }

    private ServiceToken getToken(String token, Long expiration) {
        return ServiceToken.builder()
                .tokenValue(token)
                .expiredTime(expiration)
                .build();
    }
}
