package com.example.betteriter.global;

import com.example.betteriter.infra.s3.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;

@DisplayName("S3Config 설정 클래스는")
@ContextConfiguration(classes = {S3Config.class, ConfigTest.PropertyPlaceholderConfig.class})
public class ConfigTest {

    @Autowired
    private S3Config s3Config;

    @TestConfiguration
    static class PropertyPlaceholderConfig {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Nested
    @DisplayName("각 필드 값을")
    class Get_s3_config_values {

        private String accessKey;
        private String accessSecret;

        @BeforeEach
        void setUp() {
            accessKey = s3Config.getAccessKey();
            accessSecret = s3Config.getAccessSecret();
        }

        @Test
        @DisplayName("정상적으로 가져온다.")
        void With_successful() {
            // then
            System.out.println("accessKey = " + accessKey);
            System.out.println("accessSecret = " + accessSecret);
        }
    }
}
