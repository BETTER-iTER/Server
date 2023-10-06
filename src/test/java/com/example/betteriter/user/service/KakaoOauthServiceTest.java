package com.example.betteriter.user.service;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.filter.JwtAuthenticationEntryPoint;
import com.example.betteriter.global.filter.JwtAuthenticationFilter;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.user.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestController.class,
    excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        JwtAuthenticationFilter.class,
                        JwtAuthenticationEntryPoint.class
                })
        })
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "betteritem.store/docs/api", uriPort = 80)
class KakaoOauthServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void kakaoOauthLogin() {
    }
}