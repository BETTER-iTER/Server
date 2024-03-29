package com.example.betteriter.global.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String bearer;
    private String secret;
    private String accessHeader;
    private Long accessExpiration;
    private Long refreshExpiration;
    private String refreshHeader;
}
