package com.example.betteriter.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@ConstructorBinding
@Getter @Setter
public class JwtProperties {
    private String secret;
    private String accessExpiration;
    private String refreshExpiration;
}
