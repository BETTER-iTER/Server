package com.example.betteriter.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String bearer;
    private String secret;
    private String accessHeader;
    private Long accessExpiration;
    private Long refreshExpiration;
    private String refreshHeader;
}
