package com.example.betteriter.global.config.web;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/* config.properties 패키지 내부에 모든 설정 클래스 위치 */
@Configuration
@EnableJpaAuditing
@ConfigurationPropertiesScan({"com.example.betteriter.global.config.properties"})
public class WebConfig {

}
